/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gspd.ispd.motor.carga;

import NumerosAleatorios.GeracaoNumAleatorios;
import gspd.ispd.motor.filas.RedeDeFilas;
import gspd.ispd.motor.filas.Tarefa;
import gspd.ispd.motor.filas.servidores.CS_Processamento;
import java.util.ArrayList;
import java.util.List;

/**
 * Descreve como gerar tarefas na forma randomica
 * @author denison_usuario
 */
public class CargaRandom extends GerarCarga {

    private int numeroTarefas;
    private int minComputacao;
    private int maxComputacao;
    private int AverageComputacao;
    private double ProbabilityComputacao;
    private int minComunicacao;
    private int maxComunicacao;
    private int AverageComunicacao;
    private double ProbabilityComunicacao;
    private int timeOfArrival;

    public CargaRandom(int numeroTarefas, int minComputacao, int maxComputacao, int AverageComputacao, double ProbabilityComputacao, int minComunicacao, int maxComunicacao, int AverageComunicacao, double ProbabilityComunicacao, int timeToArrival) {
        this.numeroTarefas = numeroTarefas;
        this.minComputacao = minComputacao;
        this.maxComputacao = maxComputacao;
        this.AverageComputacao = AverageComputacao;
        this.ProbabilityComputacao = ProbabilityComputacao;
        this.minComunicacao = minComunicacao;
        this.maxComunicacao = maxComunicacao;
        this.AverageComunicacao = AverageComunicacao;
        this.ProbabilityComunicacao = ProbabilityComunicacao;
        this.timeOfArrival = timeToArrival;
    }

    @Override
    public List<Tarefa> toTarefaList(RedeDeFilas rdf) {
        List<Tarefa> tarefas = new ArrayList<Tarefa>();
        int identificador = 0;
        int quantidadePorMestre = this.getNumeroTarefas() / rdf.getMestres().size();
        int resto = this.getNumeroTarefas() % rdf.getMestres().size();
        GeracaoNumAleatorios gerador = new GeracaoNumAleatorios((int)System.currentTimeMillis());
        for (CS_Processamento mestre : rdf.getMestres()) {
            for (int i = 0; i < quantidadePorMestre; i++) {
                Tarefa tarefa = new Tarefa(
                        identificador,
                        mestre.getProprietario(),
                        "application1",
                        mestre,
                        gerador.twoStageUniform(minComunicacao, AverageComunicacao, maxComunicacao, ProbabilityComunicacao),
                        0.0009765625 /*arquivo recebimento*/,
                        gerador.twoStageUniform(minComputacao, AverageComputacao, maxComputacao, ProbabilityComputacao),
                        gerador.exponencial(timeOfArrival)/*tempo de criação*/);
                tarefas.add(tarefa);
                identificador++;
            }
        }
        for (int i = 0; i < resto; i++) {
            Tarefa tarefa = new Tarefa(
                    identificador,
                    rdf.getMestres().get(0).getProprietario(),
                    "application1",
                    rdf.getMestres().get(0),
                    gerador.twoStageUniform(minComunicacao, AverageComunicacao, maxComunicacao, ProbabilityComunicacao),
                    0.0009765625 /*arquivo recebimento 1 kbit*/,
                    gerador.twoStageUniform(minComputacao, AverageComputacao, maxComputacao, ProbabilityComputacao),
                    gerador.exponencial(timeOfArrival)/*tempo de criação*/);
            tarefas.add(tarefa);
            identificador++;
        }
        return tarefas;
    }

    @Override
    public String toString() {
        return String.format("%d %d %d %f\n%d %d %d %f\n%d %d %d",
                this.minComputacao, this.AverageComputacao, this.maxComputacao, this.ProbabilityComputacao,
                this.minComunicacao, this.maxComunicacao, this.AverageComunicacao, this.ProbabilityComunicacao,
                0, this.timeOfArrival, this.numeroTarefas);
    }

    public static GerarCarga newGerarCarga(String entrada) {
        CargaRandom newObj;
        String aux = entrada.replace("\n", " ");
        String[] valores = aux.split(" ");
        int minComputacao = Integer.parseInt(valores[0]);
        int AverageComputacao = Integer.parseInt(valores[1]);
        int maxComputacao = Integer.parseInt(valores[2]);
        double ProbabilityComputacao = Double.parseDouble(valores[3]);
        int minComunicacao = Integer.parseInt(valores[4]);
        int AverageComunicacao = Integer.parseInt(valores[5]);
        int maxComunicacao = Integer.parseInt(valores[6]);
        double ProbabilityComunicacao = Double.parseDouble(valores[7]);
        //não usado --> valores[8]
        int timeOfArrival = Integer.parseInt(valores[9]);
        int numeroTarefas = Integer.parseInt(valores[10]);
        newObj = new CargaRandom(numeroTarefas,
                minComputacao, maxComputacao, AverageComputacao, ProbabilityComputacao,
                minComunicacao, maxComunicacao, AverageComunicacao, ProbabilityComunicacao,
                timeOfArrival);
        return newObj;
    }

    @Override
    public int getTipo() {
        return GerarCarga.RANDOM;
    }

    //Gets e sets
    public Integer getAverageComputacao() {
        return AverageComputacao;
    }

    public void setAverageComputacao(int AverageComputacao) {
        this.AverageComputacao = AverageComputacao;
    }

    public Integer getAverageComunicacao() {
        return AverageComunicacao;
    }

    public void setAverageComunicacao(int AverageComunicacao) {
        this.AverageComunicacao = AverageComunicacao;
    }

    public Double getProbabilityComputacao() {
        return ProbabilityComputacao;
    }

    public void setProbabilityComputacao(double ProbabilityComputacao) {
        this.ProbabilityComputacao = ProbabilityComputacao;
    }

    public Double getProbabilityComunicacao() {
        return ProbabilityComunicacao;
    }

    public void setProbabilityComunicacao(double ProbabilityComunicacao) {
        this.ProbabilityComunicacao = ProbabilityComunicacao;
    }

    public Integer getMaxComputacao() {
        return maxComputacao;
    }

    public void setMaxComputacao(int maxComputacao) {
        this.maxComputacao = maxComputacao;
    }

    public Integer getMaxComunicacao() {
        return maxComunicacao;
    }

    public void setMaxComunicacao(int maxComunicacao) {
        this.maxComunicacao = maxComunicacao;
    }

    public Integer getMinComputacao() {
        return minComputacao;
    }

    public void setMinComputacao(int minComputacao) {
        this.minComputacao = minComputacao;
    }

    public Integer getMinComunicacao() {
        return minComunicacao;
    }

    public void setMinComunicacao(int minComunicacao) {
        this.minComunicacao = minComunicacao;
    }

    public Integer getNumeroTarefas() {
        return numeroTarefas;
    }

    public void setNumeroTarefas(int numeroTarefas) {
        this.numeroTarefas = numeroTarefas;
    }

    public Integer getTimeToArrival() {
        return timeOfArrival;
    }

    public void setTimeToArrival(int timeToArrival) {
        this.timeOfArrival = timeToArrival;
    }
}
