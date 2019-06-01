package Conexao;

import Comunicacao.Mensagem;
import GUI.Saida;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConexaoServidor extends Conexao{

    private boolean ocupado = false;
    
    public ConexaoServidor(Socket socket, ObjectOutputStream enviar, ObjectInputStream receber) throws IOException {
        super(socket, enviar, receber);
    }

    public ConexaoServidor(Socket socket) throws IOException {
        super(socket);
    }
    
    public String getNome() {
        return String.valueOf(socket.getLocalPort());
    }    
    
    public ConexaoServidor iniciar() throws IOException {
        try {
            QualTipoConexao pergunta = (QualTipoConexao)receber.readObject();
            enviar.writeObject(new ConectarServidor());
            enviar.flush();
        } catch (ClassNotFoundException ex) {
            Saida.escrever("Erro na conexão com o servidor");
        }
        
        return this;
    }
    
    public void enviar(Mensagem object) throws IOException {
        ocupado = true;
        
        this.enviar.writeObject(object);
        this.enviar.flush();
        
        ocupado = false;
    }
    
    public Mensagem receber() throws IOException{
        try {
            ocupado = true;
            Mensagem object = (Mensagem)this.receber.readObject();
            ocupado = false;
            return object;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Erro fatal");
        }
    }
    
    public boolean estaOcupado() {
        return ocupado;
    }

    public Mensagem enviar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
