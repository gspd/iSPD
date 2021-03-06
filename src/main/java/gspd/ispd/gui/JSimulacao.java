package gspd.ispd.gui;

import gspd.ispd.arquivo.xml.IconicoXML;
import gspd.ispd.motor.SimulationProgress;
import gspd.ispd.motor.Simulation;
import gspd.ispd.motor.SequentialSimulation;
import gspd.ispd.motor.SimulacaoSequencialCloud;
import gspd.ispd.motor.filas.RedeDeFilas;
import gspd.ispd.motor.filas.RedeDeFilasCloud;
import gspd.ispd.motor.filas.Tarefa;
import gspd.ispd.motor.metricas.Metricas;
import java.awt.Color;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;

import gspd.ispd.motor.workload.WorkloadGenerator;
import org.w3c.dom.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Realiza faz chamada ao motor de simulação e apresenta os passos realizados e
 * porcentagem da simulação concluida.
 *
 * @author denison_usuario
 */
public class JSimulacao extends javax.swing.JDialog implements Runnable {

    private SimpleAttributeSet configuraCor = new SimpleAttributeSet();// configura a cor do texto que é exibido no painel de notificações
    private Thread threadSim;// thread principal que realiza a simulação
    private RedeDeFilas redeDeFilas;// rede de filas (caso de GRID)
    private RedeDeFilasCloud redeDeFilasCloud;// rede de fulas (caso de CLOUD)
    private List<Tarefa> tarefas;//lista de tarefas a serem executadas
    private String modeloTexto;
    private Document modelo;//XML do modelo (?)
    private ResourceBundle palavras;
    private double porcentagem = 0; //% da execução da simulação
    private SimulationProgress progrSim;
    private int tipoModelo; //define se o modelo simulado é de grid, cloud iaas ou cloud paas (de acordo com {@link EscolherClasse})

    /**
     * Creates new form AguardaSimulacao
     */
    public JSimulacao(java.awt.Frame parent, boolean modal, Document modelo, String modeloTexto, ResourceBundle plavras, int tipoModelo) {
        super(parent, modal);
        this.palavras = plavras;
        this.tipoModelo = tipoModelo;
        this.progrSim = new SimulationProgress() {
            @Override
            public void incProgresso(int n) {
                porcentagem += n;
                int value = (int) porcentagem;
                jProgressBar.setValue(value);
            }

            @Override
            public void print(String text, Color cor) {
                javax.swing.text.Document doc = jTextPaneNotificacao.getDocument();
                try {
                    if (cor != null) {
                        StyleConstants.setForeground(configuraCor, cor);
                    } else {
                        StyleConstants.setForeground(configuraCor, Color.black);
                    }
                    if (palavras.containsKey(text)) {
                        doc.insertString(doc.getLength(), palavras.getString(text), configuraCor);
                    } else {
                        doc.insertString(doc.getLength(), text, configuraCor);
                    }
                } catch (BadLocationException ex) {
                    Logger.getLogger(JSimulacao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        initComponents();
        this.modelo = modelo;
        this.modeloTexto = modeloTexto;
        this.tarefas = null;
        this.redeDeFilas = null;
        this.redeDeFilasCloud = null;
        // antes de fechar a janela, verifica se há uma simulação em execução e a termina
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (threadSim != null) {
                    //threadSim.stop();
                    threadSim = null;
                }
                dispose();
            }
        });
    }

    public int getTipoModelo() {
        return tipoModelo;
    }

    public void setTipoModelo(int tipoModelo) {
        this.tipoModelo = tipoModelo;
    }

    public void setRedeDeFilas(RedeDeFilas redeDeFilas) {
        this.redeDeFilas = redeDeFilas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void iniciarSimulacao() {
        threadSim = new Thread(this);
        threadSim.start();
    }

    public void setMaxProgresso(int n) {
        jProgressBar.setMaximum(n);
    }

    public void setProgresso(int n) {
        this.porcentagem = n;
        jProgressBar.setValue(n);
    }

    public void incProgresso(double n) {
        this.porcentagem += n;
        int value = (int) porcentagem;
        jProgressBar.setValue(value);
    }

    // execução principal que é realizada pela 'threadSim'
    @Override
    public void run() {
        progrSim.println("Simulation Initiated.");
        try {
            //0%
            //Verifica se foi construido modelo na area de desenho
            progrSim.validarInicioSimulacao(modelo);//[5%] --> 5%
            //Constrói e verifica modelos icônicos e simuláveis
            progrSim.analisarModelos(modeloTexto);//[20%] --> 25%
            //criar grade
            progrSim.print("Mounting network queue.");
            progrSim.print(" -> ");
            if (tipoModelo == EscolherClasse.GRID) {
                this.redeDeFilas = IconicoXML.newRedeDeFilas(modelo);
                incProgresso(10);//[10%] --> 35%
                progrSim.println("OK", Color.green);
                //criar tarefas
                progrSim.print("Creating tasks.");
                progrSim.print(" -> ");
                // this.tarefas = IconicoXML.newGerarCarga(modelo).toTarefaList(redeDeFilas);
                this.tarefas = IconicoXML.getWorkloadGenerator(modelo).generateTaskList();
                incProgresso(10);//[10%] --> 45%
                progrSim.println("OK", Color.green);
                //Verifica recursos do modelo e define roteamento
                Simulation sim = new SequentialSimulation(progrSim, redeDeFilas, tarefas);//[10%] --> 55 %
                //Realiza asimulação
                progrSim.println("Simulating.");
                //recebe instante de tempo em milissegundos ao iniciar a simulação
                double t1 = System.currentTimeMillis();

                sim.simular();//[30%] --> 85%

                //Recebe instnte de tempo em milissegundos ao fim da execução da simulação
                double t2 = System.currentTimeMillis();
                //Calcula tempo de simulação em segundos
                double tempototal = (t2 - t1) / 1000;
                //Obter Resultados
                Metricas metrica = sim.getMetricas();
                //[5%] --> 90%
                //Apresentar resultados
                progrSim.print("Showing results.");
                progrSim.print(" -> ");
                JResultados janelaResultados = new JResultados(null, metrica, redeDeFilas, tarefas);
                incProgresso(10);//[10%] --> 100%
                progrSim.println("OK", Color.green);
                progrSim.println("Simulation Execution Time = " + tempototal + "seconds");
                janelaResultados.setLocationRelativeTo(this);
                janelaResultados.setVisible(true);

            } else if (tipoModelo == EscolherClasse.IAAS) {
                this.redeDeFilasCloud = IconicoXML.newRedeDeFilasCloud(modelo);
                incProgresso(10);//[10%] --> 35%
                progrSim.println("OK", Color.green);
                //criar tarefas
                progrSim.print("Creating tasks.");
                progrSim.print(" -> ");
                // this.tarefas = IconicoXML.newGerarCarga(modelo).toTarefaList(redeDeFilasCloud);
                WorkloadGenerator generator = IconicoXML.getWorkloadGenerator(modelo);
                generator.setQueueNetwork(redeDeFilasCloud);
                this.tarefas = generator.generateTaskList();
                incProgresso(10);//[10%] --> 45%
                progrSim.println("OK", Color.green);
                //Verifica recursos do modelo e define roteamento
                Simulation sim = new SimulacaoSequencialCloud(progrSim, redeDeFilasCloud, tarefas);//[10%] --> 55 %
                //Realiza asimulação
                progrSim.println("Simulating.");
                //recebe instante de tempo em milissegundos ao iniciar a simulação
                double t1 = System.currentTimeMillis();

                sim.simular();//[30%] --> 85%

                //Recebe instnte de tempo em milissegundos ao fim da execução da simulação
                double t2 = System.currentTimeMillis();
                //Calcula tempo de simulação em segundos
                double tempototal = (t2 - t1) / 1000;
                //Obter Resultados
                Metricas metrica = sim.getMetricasCloud();
                //[5%] --> 90%
                //Apresentar resultados
                progrSim.print("Showing results.");
                progrSim.print(" -> ");
                JResultadosCloud janelaResultados = new JResultadosCloud(null, metrica, redeDeFilasCloud, tarefas);
                incProgresso(10);//[10%] --> 100%
                progrSim.println("OK", Color.green);
                progrSim.println("Simulation Execution Time = " + tempototal + "seconds");
                janelaResultados.setLocationRelativeTo(this);
                janelaResultados.setVisible(true);
            }
        } catch (IllegalArgumentException erro) {

            Logger.getLogger(JSimulacao.class.getName()).log(Level.SEVERE, null, erro);
            progrSim.println(erro.getMessage(), Color.red);
            progrSim.print("Simulation Aborted", Color.red);
            progrSim.println("!", Color.red);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar = new javax.swing.JProgressBar();
        jButtonCancelar = new javax.swing.JButton();
        jScrollPane = new javax.swing.JScrollPane();
        jTextPaneNotificacao = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(palavras.getString("Running Simulation")); // NOI18N

        jProgressBar.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);

        jButtonCancelar.setText(palavras.getString("Cancel")); // NOI18N
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jTextPaneNotificacao.setEditable(false);
        jTextPaneNotificacao.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jScrollPane.setViewportView(jTextPaneNotificacao);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonCancelar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(jScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(jProgressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        // TODO add your handling code here:
        if (this.threadSim != null) {
            //this.threadSim.stop();
            this.threadSim = null;
        }
        this.dispose();
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTextPane jTextPaneNotificacao;
    // End of variables declaration//GEN-END:variables
}
