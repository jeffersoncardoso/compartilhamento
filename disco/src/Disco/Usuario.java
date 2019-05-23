package Disco;

import Conexao.ConexaoCliente;
import Estrutura.Arquivo;
import Requisicao.Requisicao;
import Requisicao.EnviarArquivo;
import Requisicao.ListarArquivos;
import Requisicao.SolicitarArquivo;
import Resposta.ArquivoRecebido;
import Resposta.ArquivoErroAoSalvar;
import Resposta.ArquivoSalvo;
import Resposta.ArquivoNaoEncontrado;
import Resposta.ListaArquivos;
import Resposta.Resposta;
import java.io.IOException;

public class Usuario extends Thread{
    private boolean conectado = true;
    private final ConexaoCliente conexao;
    private final Servidor servidor;

    public Usuario(ConexaoCliente conexao, Servidor servidor) {
        this.conexao = conexao;
        this.servidor = servidor;
        
        Saida.escrever("Se conectou");
    }
    
    private void enviarArquivo(EnviarArquivo requisicao) {
        Saida.escrever("Solicitado upload do arquivo: %s", requisicao.getArquivo().getNome());
        try {
            servidor.salvarArquivo(requisicao.getArquivo());
        } catch (IOException ex) {
            ex.printStackTrace();
            Saida.escrever("Erro ao salvar o arquivo: %s", requisicao.getArquivo().getNome());
            conexao.enviar(new ArquivoErroAoSalvar(ex.getMessage()));
            
            return;
        }
        
        conexao.enviar(new ArquivoRecebido());
    }
    
    private void listarArquivos(ListarArquivos requisicao) {
        Saida.escrever("Solicitado lista de arquivos");
        String[] arquivos = servidor.listarArquivos();
        conexao.enviar(new ListaArquivos(arquivos));
    }
    
    private void solicitarArquivo(SolicitarArquivo requisicao) {
        Saida.escrever("Solicitado arquivo");
        Resposta resposta;
        try {
            Arquivo arquivo = servidor.buscarArquivo(requisicao.getNome());
            resposta = new ArquivoSalvo(arquivo);
        } catch (IOException ex) {
            ex.printStackTrace();
            resposta = new ArquivoNaoEncontrado();
        }
        
        conexao.enviar(resposta);
    }

    @Override
    public void run() {
        Saida.escrever("Usuário conectado");
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
                
            } catch (Exception e) {
                e.printStackTrace();
                Saida.escrever("Ocorreu um erro ao receber a resposta do cliente");
                sair();
            }
        }
    }
    
    public void sair() {
        conectado = false;
        conexao.encerrar();
    }
    
}
