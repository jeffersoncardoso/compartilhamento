package Conexao;

import Catalogo.ServidorCadeia;
import Disco.Saida;
import Disco.Servidor;
import Disco.Usuario;
import java.io.IOException;
import java.net.Socket;

public class ConexaoFabrica extends Thread{
    
    private Servidor servidor;
    private Socket socket;

    public ConexaoFabrica(Servidor servidor, Socket socket) {
        this.servidor = servidor;
        this.socket = socket;
    }
    
    private QualTipoConexao perguntarTipoConexao() throws IOException, ClassNotFoundException {
        return new ConectarServidor();
    }
    
    private void conectarServidor() throws IOException {
        ServidorCadeia anterior = new ServidorCadeia(new ConexaoServidor(socket));
        servidor.conectarServidorAnterior(anterior);
    }
    
    private void conectarCliente() throws IOException {
        Conexao conexao = new ConexaoCliente(socket);
        Usuario usuario = new Usuario((ConexaoCliente)conexao, this.servidor);
        servidor.adicionar(usuario);

        usuario.start();
    }
    
    @Override
    public void run() {
        try {
            
            QualTipoConexao tipo = perguntarTipoConexao();

            if(tipo instanceof ConectarCliente) {
                conectarCliente();
            } else if(tipo instanceof ConectarServidor) {
                conectarServidor();
            }
        
        } catch (IOException ex) {
            ex.getStackTrace();
            Saida.escrever("Erro na conexão: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Saida.escrever("Erro na conexão: " + ex.getMessage());
        }
    }
}
