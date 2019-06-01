package Conexao;

import GUI.Saida;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Conexao{
    protected Socket socket;
    protected ObjectInputStream receber;
    protected ObjectOutputStream enviar;

    public Conexao(Socket socket) throws IOException {
        this.socket = socket;
        enviar = new ObjectOutputStream(socket.getOutputStream());
        receber = new ObjectInputStream(socket.getInputStream());
        enviar.flush();
    }
    
    public Conexao(Socket socket, ObjectOutputStream enviar, ObjectInputStream receber) throws IOException {
        this.socket = socket;
        this.enviar = enviar;
        this.receber = receber;
    }
    
    public Conexao definir() throws IOException, ClassNotFoundException {
        this.enviar.writeObject(new QualTipoConexao());
        this.enviar.flush();
        QualTipoConexao tipo = (QualTipoConexao)this.receber.readObject();
        
        if(tipo instanceof ConectarCliente) {
            return new ConexaoCliente(socket, enviar, receber);
        } else {
            return new ConexaoServidor(socket, enviar, receber);
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
