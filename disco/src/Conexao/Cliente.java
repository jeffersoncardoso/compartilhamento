package Conexao;

import Servidor.Servidor;
import GUI.SaidaCliente;
import Requisicao.Requisicao;
import Requisicao.EnviarArquivo;
import Requisicao.ListarArquivos;
import Requisicao.SolicitarArquivo;
import Resposta.ArquivoRecebido;
import Resposta.ArquivoErroAoSalvar;
import Resposta.ArquivoNaoEncontrado;
import Resposta.ListaArquivos;
import Resposta.Resposta;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketException;

public class Cliente extends Thread{
    private boolean conectado = true;
    private final ConexaoCliente conexao;
    private final Servidor servidor;

    public Cliente(ConexaoCliente conexao, Servidor servidor) {
        this.conexao = conexao;
        this.servidor = servidor;
    }
    
    private void enviarArquivo(EnviarArquivo requisicao) {
        try {
            servidor.dividirArquivo(requisicao.getArquivo());
        } catch (IOException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            SaidaCliente.escrever("Erro ao salvar o arquivo: %s (%s)", requisicao.getArquivo().getNome(), exceptionAsString);
            conexao.enviar(new ArquivoErroAoSalvar(ex.getMessage()));
            
            return;
        }
        
        conexao.enviar(new ArquivoRecebido());
    }
    
    private void listarArquivos(ListarArquivos requisicao) {
        String[] arquivos = servidor.listarArquivos();
        conexao.enviar(new ListaArquivos(arquivos));
    }
    
    
    public void devolverArquivoSolicitado(Resposta resposta) {
        conexao.enviar(resposta);
    }
    private void solicitarArquivo(SolicitarArquivo requisicao) {
        SaidaCliente.escrever("Solicitado arquivo %s", requisicao.getNome());
        try {
            servidor.solicitarArquivo(this, requisicao.getNome());
        } catch (IOException ex) {
            ex.printStackTrace();
            conexao.enviar(new ArquivoNaoEncontrado());
        }
    }

    @Override
    public void run() {
        SaidaCliente.escrever("Usuário %s conectado", getId());
        
        while(conectado) {
            try {
                Requisicao requisicao = conexao.receber();
                
                switch(requisicao.getTipo()) {
                    case ENVIAR_ARQUIVO: 
                        enviarArquivo((EnviarArquivo)requisicao);
                        
                        break;
                        
                    case LISTAR_ARQUIVOS:
                        listarArquivos((ListarArquivos)requisicao);
                        
                        break;
                        
                    case SOLICITAR_ARQUIVO:
                        solicitarArquivo((SolicitarArquivo)requisicao);
                        
                        break;
                }
                
            } catch (EOFException|SocketException e) {
                sair();
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                SaidaCliente.escrever("Ocorreu um erro ao receber a resposta: %s", exceptionAsString);
                sair();
            }
        }
    }
    
    public void sair() {
        SaidaCliente.escrever("Usuário %s se desconectou", getId());
        conectado = false;
        conexao.encerrar();
    }
    
}
