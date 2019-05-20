package Disco;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Requisicao.Requisicao;
import Resposta.Resposta;

public class Conexao extends Thread{
    private final Socket socket;
    private final ObjectInputStream receber;
    private final ObjectOutputStream enviar;

    public Conexao(Socket socket) throws IOException {
        this.socket = socket;
        enviar = new ObjectOutputStream(socket.getOutputStream());
        receber = new ObjectInputStream(socket.getInputStream());
        enviar.flush();
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
