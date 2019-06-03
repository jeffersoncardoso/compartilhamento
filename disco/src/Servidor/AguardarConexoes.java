package Servidor;

import Servidor.Servidor;
import Servidor.ServidorCadeia;
import Conexao.Conexao;
import Conexao.ConexaoCliente;
import Conexao.ConexaoServidor;
import GUI.Saida;
import Conexao.Cliente;
import java.io.IOException;
import java.net.Socket;

public class AguardarConexoes extends Thread{
    
    private final Servidor servidor;

    public AguardarConexoes(Servidor servidor) {
        this.servidor = servidor;
    }
    
    private void conectar(ConexaoCliente conexao) {
        Cliente usuario = new Cliente(conexao, this.servidor);
        servidor.adicionar(usuario);
        
        usuario.start();
    }
    
    private void conectar(ConexaoServidor conexao) throws IOException {
        ServidorCadeia anterior = new ServidorCadeia(conexao);
        servidor.conectarAnterior(anterior);
    }
    
    @Override
    public void run() {
        Saida.escrever("Aguardando conexões");
        
        while(servidor.estaLigado()){
            try {
                Socket socket = this.servidor.esperarCliente();
                
                Conexao conexao = new Conexao(socket).definir();
                
                if(conexao instanceof ConexaoCliente)
                    conectar((ConexaoCliente)conexao);
                
                if(conexao instanceof ConexaoServidor)
                    conectar((ConexaoServidor)conexao);
                
            } catch (IOException ex) {
                ex.getStackTrace();
            } catch (ClassNotFoundException ex) {
                Saida.escrever("Erro na mensagem: " + ex.getMessage());
            }
        }
    }
}
