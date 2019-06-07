/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gspd.ispd.escalonadorCloud;

import gspd.ispd.motor.Simulation;
import gspd.ispd.motor.filas.Tarefa;
import gspd.ispd.motor.filas.servidores.CS_Processamento;

/**
 * Interface que possui métodos implementados penas em um nó Mestre,
 * os métodos desta interface são utilizados pelos escalonadores
 * @author denison
 */
public interface MestreCloud {
    //Tipos de escalonamentos
    public static final int ENQUANTO_HOUVER_TAREFAS = 1;
    public static final int QUANDO_RECEBE_RESULTADO = 2;
    public static final int AMBOS = 3;
    //Métodos que geram eventos
    public void enviarTarefa(Tarefa tarefa);
    public void processarTarefa(Tarefa tarefa);
    public void executarEscalonamento();
    public void enviarMensagem(Tarefa tarefa, CS_Processamento escravo, int tipo);
    public void atualizar(CS_Processamento escravo);
    
    //Get e Set
    public void liberarEscalonador();
    public void setSimulacao(Simulation simulacao);
    public int getTipoEscalonamento();
    public void setTipoEscalonamento(int tipo);

    public Tarefa criarCopia(Tarefa get);
    public Simulation getSimulacao();
}