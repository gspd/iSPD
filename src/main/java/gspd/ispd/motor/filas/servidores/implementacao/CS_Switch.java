/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gspd.ispd.motor.filas.servidores.implementacao;

import gspd.ispd.motor.EventoFuturo;
import gspd.ispd.motor.Simulation;
import gspd.ispd.motor.filas.Mensagem;
import gspd.ispd.motor.filas.Tarefa;
import gspd.ispd.motor.filas.servidores.CS_Comunicacao;
import gspd.ispd.motor.filas.servidores.CentroServico;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author denison_usuario
 */
public class CS_Switch extends CS_Comunicacao implements Vertice {

    private List<CentroServico> conexoesEntrada;
    private List<CentroServico> conexoesSaida;
    private List<Tarefa> filaPacotes;
    private List<Mensagem> filaMensagens;
    private boolean linkDisponivel;
    private boolean linkDisponivelMensagem;
    private double tempoTransmitirMensagem;

    public CS_Switch(String id, double LarguraBanda, double Ocupacao, double Latencia) {
        super(id, LarguraBanda, Ocupacao, Latencia);
        this.conexoesEntrada = new ArrayList<CentroServico>();
        this.conexoesSaida = new ArrayList<CentroServico>();
        this.linkDisponivel = true;
        this.filaPacotes = new ArrayList<Tarefa>();
        this.filaMensagens = new ArrayList<Mensagem>();
        this.tempoTransmitirMensagem = 0;
        this.linkDisponivelMensagem = true;
    }

    public void addConexoesEntrada(CentroServico conexao) {
        this.conexoesEntrada.add(conexao);
    }

    public void addConexoesSaida(CentroServico conexao) {
        this.conexoesSaida.add(conexao);
    }

    @Override
    public void chegadaDeCliente(Simulation simulacao, Tarefa cliente) {
        cliente.iniciarEsperaComunicacao(simulacao.getTime(this));
        if (linkDisponivel) {
            //indica que recurso está ocupado
            linkDisponivel = false;
            //cria evento para iniciar o atendimento imediatamente
            EventoFuturo novoEvt = new EventoFuturo(
                    simulacao.getTime(this),
                    EventoFuturo.ATENDIMENTO,
                    this,
                    cliente);
            simulacao.addEventoFuturo(novoEvt);
        } else {
            filaPacotes.add(cliente);
        }
    }

    @Override
    public void atendimento(Simulation simulacao, Tarefa cliente) {
        cliente.finalizarEsperaComunicacao(simulacao.getTime(this));
        cliente.iniciarAtendimentoComunicacao(simulacao.getTime(this));
        //Gera evento para atender proximo cliente da lista
        EventoFuturo evtFut = new EventoFuturo(
                simulacao.getTime(this) + tempoTransmitir(cliente.getTamComunicacao()),
                EventoFuturo.SAIDA,
                this, cliente);
        //Event adicionado a lista de evntos futuros
        simulacao.addEventoFuturo(evtFut);
    }

    @Override
    public void saidaDeCliente(Simulation simulacao, Tarefa cliente) {
        //Incrementa o número de Mbits transmitido por este link
        this.getMetrica().incMbitsTransmitidos(cliente.getTamComunicacao());
        //Incrementa o tempo de transmissão
        double tempoTrans = this.tempoTransmitir(cliente.getTamComunicacao());
        this.getMetrica().incSegundosDeTransmissao(tempoTrans);
        //Incrementa o tempo de transmissão no pacote
        cliente.finalizarAtendimentoComunicacao(simulacao.getTime(this));
        //Gera evento para chegada da tarefa no proximo servidor
        EventoFuturo evtFut = new EventoFuturo(
                simulacao.getTime(this),
                EventoFuturo.CHEGADA,
                cliente.getCaminho().remove(0), cliente);
        //Event adicionado a lista de evntos futuros
        simulacao.addEventoFuturo(evtFut);
        if (filaPacotes.isEmpty()) {
            //Indica que está livre
            this.linkDisponivel = true;
        } else {
            //Gera evento para atender proximo cliente da lista
            Tarefa proxCliente = filaPacotes.remove(0);
            evtFut = new EventoFuturo(
                    simulacao.getTime(this),
                    EventoFuturo.ATENDIMENTO,
                    this, proxCliente);
            //Event adicionado a lista de evntos futuros
            simulacao.addEventoFuturo(evtFut);
        }
    }

    @Override
    public void requisicao(Simulation simulacao, Mensagem cliente, int tipo) {
        if (tipo == EventoFuturo.SAIDA_MENSAGEM) {
            tempoTransmitirMensagem += tempoTransmitir(cliente.getTamComunicacao());
            //Incrementa o número de Mbits transmitido por este link
            this.getMetrica().incMbitsTransmitidos(cliente.getTamComunicacao());
            //Incrementa o tempo de transmissão
            double tempoTrans = this.tempoTransmitir(cliente.getTamComunicacao());
            this.getMetrica().incSegundosDeTransmissao(tempoTrans);
            //Gera evento para chegada da mensagem no proximo servidor
            EventoFuturo evtFut = new EventoFuturo(
                    simulacao.getTime(this) + tempoTrans,
                    EventoFuturo.MENSAGEM,
                    cliente.getCaminho().remove(0), cliente);
            //Event adicionado a lista de evntos futuros
            simulacao.addEventoFuturo(evtFut);
            if (!filaMensagens.isEmpty()) {
                //Gera evento para chegada da mensagem no proximo servidor
                evtFut = new EventoFuturo(
                        simulacao.getTime(this) + tempoTrans,
                        EventoFuturo.SAIDA_MENSAGEM,
                        this, filaMensagens.remove(0));
                //Event adicionado a lista de evntos futuros
                simulacao.addEventoFuturo(evtFut);
            }else{
                linkDisponivelMensagem = true;
            }
        } else if(linkDisponivelMensagem){
            linkDisponivelMensagem = false;
                //Gera evento para chegada da mensagem no proximo servidor
                EventoFuturo evtFut = new EventoFuturo(
                        simulacao.getTime(this),
                        EventoFuturo.SAIDA_MENSAGEM,
                        this, cliente);
                //Event adicionado a lista de evntos futuros
                simulacao.addEventoFuturo(evtFut);
        }else{
            filaMensagens.add(cliente);
        }
    }

    @Override
    public List<CentroServico> getConexoesSaida() {
        return this.conexoesSaida;
    }

    @Override
    public void addConexoesEntrada(CS_Link conexao) {
        this.conexoesSaida.add(conexao);
    }

    @Override
    public void addConexoesSaida(CS_Link conexao) {
        this.conexoesSaida.add(conexao);
    }
    
    @Override
    public Integer getCargaTarefas() {
        if (linkDisponivel && linkDisponivelMensagem) {
            return 0;
        } else {
            return (filaMensagens.size() + filaPacotes.size()) + 1;
        }
    }
}
