/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gspd.ispd.gui.iconico.grade;

import gspd.ispd.gui.iconico.Vertice;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 *
 * @author denison
 */
public class Cluster extends Vertice implements ItemGrade {

    private IdentificadorItemGrade id;
    private HashSet<ItemGrade> conexoesEntrada;
    private HashSet<ItemGrade> conexoesSaida;
    private Double banda;
    private Double latencia;
    private String algoritmo;
    private Double poderComputacional;
    private Integer nucleosProcessador;
    private Integer numeroEscravos;
    private Boolean mestre;
    private Double memoriaRAM;
    private Double discoRigido;
    private boolean configurado;
    private String proprietario;
    private Double costperprocessing;
    private Double costpermemory; 
    private Double costperdisk;
    private String VMMallocpolicy;

    public Cluster(Integer x, Integer y, int idLocal, int idGlobal) {
        super(x, y);
        this.id = new IdentificadorItemGrade(idLocal, idGlobal, "cluster" + idGlobal);
        this.algoritmo = "---";
        this.proprietario = "user1";
        this.numeroEscravos = 0;
        this.poderComputacional = 0.0;
        this.nucleosProcessador = 1;
        this.banda = 0.0;
        this.latencia = 0.0;
        this.memoriaRAM = 0.0;
        this.discoRigido = 0.0;
        this.mestre = true;
        this.costperprocessing=0.0;
        this.costpermemory = 0.0;
        this.costperdisk = 0.0;
        this.VMMallocpolicy = "---";
        this.conexoesEntrada = new HashSet<ItemGrade>();
        this.conexoesSaida = new HashSet<ItemGrade>();
    }

    @Override
    public IdentificadorItemGrade getId() {
        return this.id;
    }

    @Override
    public Set<ItemGrade> getConexoesEntrada() {
        return conexoesEntrada;
    }

    @Override
    public Set<ItemGrade> getConexoesSaida() {
        return conexoesSaida;
    }

    @Override
    public String toString() {
        return "id: " + getId().getIdGlobal() + " " + getId().getNome();
    }

    @Override
    public String getAtributos(ResourceBundle palavras) {
        String texto = palavras.getString("Local ID:") + " " + this.getId().getIdLocal()
                + "<br>" + palavras.getString("Global ID:") + " " + this.getId().getIdGlobal()
                + "<br>" + palavras.getString("Label") + ": " + this.getId().getNome()
                + "<br>" + palavras.getString("X-coordinate:") + " " + this.getX()
                + "<br>" + palavras.getString("Y-coordinate:") + " " + this.getY()
                + "<br>" + palavras.getString("Number of slaves") + ": " + getNumeroEscravos()
                + "<br>" + palavras.getString("Computing power") + ": " + getPoderComputacional()
                + "<br>" + palavras.getString("Bandwidth") + ": " + getBanda()
                + "<br>" + palavras.getString("Latency") + ": " + getLatencia()
                + "<br>" + palavras.getString("Scheduling algorithm") + ": " + getAlgoritmo();
        return texto;
    }

    /**
     *
     * @param posicaoMouseX the value of X position
     * @param posicaoMouseY the value of Y position
     * @param idGlobal the value of idGlobal
     * @param idLocal the value of idLocal
     */
    @Override
    public Cluster criarCopia(int posicaoMouseX, int posicaoMouseY, int idGlobal, int idLocal) {
        Cluster temp = new Cluster(posicaoMouseX, posicaoMouseY, idGlobal, idLocal);
        temp.algoritmo = this.algoritmo;
        temp.poderComputacional = this.poderComputacional;
        temp.mestre = this.mestre;
        temp.proprietario = this.proprietario;
        temp.banda = this.banda;
        temp.latencia = this.latencia;
        temp.numeroEscravos = this.numeroEscravos;
        temp.verificaConfiguracao();
        return temp;
    }

    @Override
    public boolean isConfigurado() {
        return configurado;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(DesenhoGrade.ICLUSTER, getX() - 15, getY() - 15, null);
        if (isConfigurado()) {
            g.drawImage(DesenhoGrade.IVERDE, getX() + 15, getY() + 15, null);
        } else {
            g.drawImage(DesenhoGrade.IVERMELHO, getX() + 15, getY() + 15, null);
        }

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(getId().getIdGlobal()), getX(), getY() + 30);
        // Se o icone estiver ativo, desenhamos uma margem nele.
        if (isSelected()) {
            g.setColor(Color.RED);
            g.drawRect(getX() - 19, getY() - 17, 37, 34);
        }
    }

    @Override
    public boolean contains(int x, int y) {
        if (x < getX() + 17 && x > getX() - 17) {
            if (y < getY() + 17 && y > getY() - 17) {
                return true;
            }
        }
        return false;
    }

    public Boolean isMestre() {
        return mestre;
    }

    public void setMestre(Boolean mestre) {
        this.mestre = mestre;
        verificaConfiguracao();
    }

    public Double getBanda() {
        return banda;
    }

    public void setBanda(Double banda) {
        this.banda = banda;
        verificaConfiguracao();
    }

    public Double getLatencia() {
        return latencia;
    }

    public void setLatencia(Double latencia) {
        this.latencia = latencia;
        verificaConfiguracao();
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
        verificaConfiguracao();
    }
    
    public Double getPoderComputacional() {
        return poderComputacional;
    }

    public void setPoderComputacional(Double poderComputacional) {
        this.poderComputacional = poderComputacional;
        verificaConfiguracao();
    }

    public Integer getNumeroEscravos() {
        return numeroEscravos;

    }

    public void setNumeroEscravos(Integer numeroEscravos) {
        this.numeroEscravos = numeroEscravos;
        verificaConfiguracao();
    }

    public Integer getNucleosProcessador() {
        return nucleosProcessador;
    }

    public void setNucleosProcessador(Integer nucleosProcessador) {
        this.nucleosProcessador = nucleosProcessador;
    }

    public Double getMemoriaRAM() {
        return memoriaRAM;
    }

    public void setMemoriaRAM(Double memoriaRAM) {
        this.memoriaRAM = memoriaRAM;
    }

    public Double getDiscoRigido() {
        return discoRigido;
    }

    public void setDiscoRigido(Double discoRigido) {
        this.discoRigido = discoRigido;
    }
    
    public Double getCostperprocessing() {
        return costperprocessing;
    }


    public void setCostperprocessing(Double costperprocessing) {
        this.costperprocessing = costperprocessing;
    }
    
    public Double getCostpermemory() {
        return costpermemory;
    }

    public void setCostpermemory(Double costpermemory) {
        this.costpermemory = costpermemory;
    }
    
    public Double getCostperdisk() {
        return costperdisk;
    }

    public void setCostperdisk(Double costperdisk) {
        this.costperdisk = costperdisk;
    }
    
    public String getVMMallocpolicy() {
        return VMMallocpolicy;
    }

    public void setVMMallocpolicy(String VMMallocpolicy) {
        this.VMMallocpolicy = VMMallocpolicy;
    }

    private void verificaConfiguracao() {
        if (banda > 0 && latencia > 0 && poderComputacional > 0 && numeroEscravos > 0) {
            configurado = true;
            if (mestre && algoritmo.equals("---")) {
                configurado = false;
            }
        } else {
            configurado = false;
        }
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String string) {
        this.proprietario = string;
    }
}
