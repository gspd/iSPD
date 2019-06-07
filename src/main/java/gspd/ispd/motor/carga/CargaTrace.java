/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gspd.ispd.motor.carga;

import NumerosAleatorios.GeracaoNumAleatorios;
import gspd.ispd.arquivo.interpretador.cargas.Interpretador;
import gspd.ispd.motor.filas.RedeDeFilas;
import gspd.ispd.motor.filas.Tarefa;
import gspd.ispd.motor.filas.servidores.CS_Processamento;
import gspd.ispd.motor.filas.servidores.implementacao.CS_Maquina;
import gspd.ispd.motor.filas.servidores.implementacao.CS_Mestre;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diogo Tavares
 */
public class CargaTrace extends GerarCarga {

    private String tipo, caminho;
    private File file;
    private int num_tasks;

    public CargaTrace(File file, int num_tasks, String tipo) {
        this.file = file;
        this.caminho = file.getAbsolutePath();
        Interpretador interpret = new Interpretador(caminho);
        this.num_tasks = num_tasks;
        this.tipo = tipo;
    }

    private double MediaCapProcGrade(List<CS_Maquina> maquinas) {
        double media = 0;
        int i = 0;
        for (CS_Maquina cS_Maquina : maquinas) {
            media += cS_Maquina.getPoderComputacional();
            i++;
        }
        media = media / i;
        return media;
    }

    @Override
    public List<Tarefa> toTarefaList(RedeDeFilas rdf) {
        List<Tarefa> tarefas = new ArrayList<Tarefa>();
        List<String> users = new ArrayList<String>();
        List<Double> pcomp = new ArrayList<Double>();
        int quantidadePorMestre = this.num_tasks / rdf.getMestres().size();
        int resto = this.num_tasks % rdf.getMestres().size();
        GeracaoNumAleatorios gerador = new GeracaoNumAleatorios((int) System.currentTimeMillis());
        double mediaCap = MediaCapProcGrade(rdf.getMaquinas());
        try {
            BufferedReader in = new BufferedReader(new FileReader(caminho));
            String aux;
            int j = 0;
            //escapa o cabeçalho
            while (in.ready() && j < 5) {
                in.readLine();
                j++;
            }
            if (tipo.equals("SWF") || tipo.equals("GWF")) {
                for (CS_Processamento mestre : rdf.getMestres()) {
                    for (int i = 0; i < quantidadePorMestre; i++) {
                        aux = in.readLine();
                        String[] campos = aux.split("\"");
                        if (rdf.getUsuarios().contains(campos[11]) == false && users.contains(campos[11]) == false) {
                            users.add(campos[11]);
                            pcomp.add(0.0);
                        }
                        Tarefa tarefa = new Tarefa(
                                (int) Integer.parseInt(campos[1]),
                                campos[11],
                                "application1",
                                mestre,
                                gerador.twoStageUniform(200, 5000, 25000, 0.5),
                                0.0009765625 /*arquivo recebimento*/,
                                (double) (Double.parseDouble(campos[7]) * mediaCap),
                                Double.parseDouble(campos[3])/*tempo de criação*/);
                        tarefas.add(tarefa);
                        if (campos[5].contains("0") || campos[5].contains("5")) {
                            tarefa.setLocalProcessamento(mestre);
                            tarefa.cancelar(0);
                        }
                    }
                }
                for (int i = 0; i < resto; i++) {
                    aux = in.readLine();
                    String[] campos = aux.split("\"");
                    if (rdf.getUsuarios().contains(campos[11]) == false && users.contains(campos[11]) == false) {
                        users.add(campos[11]);
                        pcomp.add(0.0);
                    }
                    Tarefa tarefa = new Tarefa(
                            (int) Integer.parseInt(campos[1]),
                            campos[11],
                            "application1",
                            rdf.getMestres().get(0),
                            gerador.twoStageUniform(200, 5000, 25000, 0.5),
                            0.0009765625 /*arquivo recebimento*/,
                            (double) (Double.parseDouble(campos[7]) * mediaCap),
                            Double.parseDouble(campos[3])/*tempo de criação*/);
                    tarefas.add(tarefa);
                    if (campos[5].contains("0") || campos[5].contains("5")) {
                        tarefa.setLocalProcessamento(rdf.getMestres().get(0));
                        tarefa.cancelar(0);
                    }
                }
                for (CS_Processamento mestre : rdf.getMestres()) {
                    CS_Mestre mestreaux = (CS_Mestre) mestre;
                    mestreaux.getEscalonador().getMetricaUsuarios().addAllUsuarios(users, pcomp);
                }
                rdf.getUsuarios().addAll(users);
                return tarefas;

            } else if (tipo.equals("iSPD")) {
                for (CS_Processamento mestre : rdf.getMestres()) {
                    for (int i = 0; i < quantidadePorMestre; i++) {
                        aux = in.readLine();
                        String[] campos = aux.split("\"");
                        if (rdf.getUsuarios().contains(campos[11]) == false && users.contains(campos[11]) == false) {
                            users.add(campos[11]);
                            pcomp.add(0.0);
                        }
                        Tarefa tarefa = new Tarefa(
                                (int) Integer.parseInt(campos[1]),
                                campos[11],
                                "application1",
                                mestre,
                                (double) Double.parseDouble(campos[9]),
                                0.0009765625 /*arquivo recebimento*/,
                                (double) Double.parseDouble(campos[7]),
                                Double.parseDouble(campos[3])/*tempo de criação*/);
                        tarefas.add(tarefa);
                    }

                }
                for (int i = 0; i < resto; i++) {
                    aux = in.readLine();
                    String[] campos = aux.split("\"");
                    if (rdf.getUsuarios().contains(campos[11]) == false && users.contains(campos[11]) == false) {
                        users.add(campos[11]);
                        pcomp.add(0.0);
                    }
                    Tarefa tarefa = new Tarefa(
                            (int) Integer.parseInt(campos[1]),
                            campos[11],
                            "application1",
                            rdf.getMestres().get(0),
                            (double) Double.parseDouble(campos[9]),
                            0.0009765625 /*arquivo recebimento*/,
                            (double) Double.parseDouble(campos[7]),
                            Double.parseDouble(campos[3])/*tempo de criação*/);
                    tarefas.add(tarefa);
                }
                for (CS_Processamento mestre : rdf.getMestres()) {
                    CS_Mestre mestreaux = (CS_Mestre) mestre;
                    mestreaux.getEscalonador().getMetricaUsuarios().addAllUsuarios(users, pcomp);
                }
                rdf.getUsuarios().addAll(users);
                return tarefas;

            }

        } catch (IOException ex) {
            Logger.getLogger(CargaTrace.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    public File getFile() {
        return file;
    }
    
    public String getTraceType() {
        return tipo;
    }
    
    public Integer getNumberTasks() {
        return num_tasks;
    }

    @Override
    public String toString() {
        return file.getAbsolutePath();

    }

    @Override
    public int getTipo() {
        return GerarCarga.TRACE;
    }
}