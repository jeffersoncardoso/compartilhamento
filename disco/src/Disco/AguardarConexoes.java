package Disco;

import Conexao.ConexaoCliente;
import Conexao.ConexaoServidor;
import Conexao.ConexaoFabrica;
import java.io.IOException;
import java.net.Socket;
import Requisicao.Requisicao;
import Requisicao.TipoRequisicao;
import Conexao.Conexao;

public class AguardarConexoes extends Thread{
    
    private Servidor servidor;

    public AguardarConexoes(Servidor servidor) {
        this.servidor = servidor;
    }
    
    @Override
    public void run() {
        while(servidor.estaLigado()){
            try {
                Socket socket = this.servidor.esperarCliente();
                
                ConexaoFabrica fabrica = new ConexaoFabrica(servidor, socket);
                fabrica.start();
                
            } catch (IOException ex) {
                ex.getStackTrace();
                Saida.escrever("Erro na conexão: " + ex.getMessage());
            }
        }
    }
}
