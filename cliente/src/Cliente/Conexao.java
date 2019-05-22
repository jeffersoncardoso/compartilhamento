package Cliente;

import Util.Saida;
import Requisicao.Requisicao;
import Resposta.Resposta;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Conexao extends Thread{
    private final Socket socket;
    private final ObjectInputStream receber;
    private final ObjectOutputStream enviar;

    public Conexao(Socket socket) throws IOException {
        this.socket = socket;
        enviar = new ObjectOutputStream(socket.getOutputStream());
        receber = new ObjectInputStream(socket.getInputStream());
    }
    
    public void enviar(Requisicao mensagem) {
        try {
            this.enviar.writeObject(mensagem);
            this.enviar.flush();
        } catch (IOException ex) {
            Saida.escrever("Ocorreu um erro: " + ex.getMessage());
        }
    }
    
    public Resposta receber() throws IOException{
        try {
            Resposta object = (Resposta)this.receber.readObject();
            return object;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Erro na mensagem");
        }
    }
    
    public void encerrar(){
        try {
            this.receber.close();
            this.enviar.close();
            this.socket.close();
        } catch (IOException ex) {
            Saida.escrever("Ocorreu um erro: " + ex.getMessage());
        }
    }
}