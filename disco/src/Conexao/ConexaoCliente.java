package Conexao;

import Disco.Saida;
import Requisicao.Requisicao;
import Resposta.Resposta;
import java.io.IOException;
import java.net.Socket;

public class ConexaoCliente extends Conexao{
    
    public ConexaoCliente(Socket socket) throws IOException {
        super(socket);
    }
    
    public void enviar(Resposta object) {
        try {
            this.enviar.writeObject(object);
            this.enviar.flush();
        } catch (IOException ex) {
            Saida.escrever("Ocorreu um erro: " + ex.getMessage());
        }
    }
    
    public Requisicao receber() throws IOException{
        try {
            Requisicao object = (Requisicao)this.receber.readObject();
            return object;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Erro fatal");
        }
    }
    
}
