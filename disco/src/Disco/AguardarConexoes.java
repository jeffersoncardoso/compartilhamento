package Disco;

import java.io.IOException;
import java.net.Socket;

public class AguardarConexoes extends Thread{
    
    private Servidor servidor;

    public AguardarConexoes(Servidor servidor) {
        this.servidor = servidor;
    }
    
    @Override
    public void run() {
        while(this.servidor.estaLigado()){
            try {
                Socket socket = this.servidor.esperarCliente();

                Usuario novoUsuario = new Usuario(new Conexao(socket), this.servidor);
                servidor.adicionar(novoUsuario);
                
                novoUsuario.start();
            } catch (IOException ex) {
                ex.getStackTrace();
                Saida.escrever("Erro na conexão: " + ex.getMessage());
            }
        }
    }
}
