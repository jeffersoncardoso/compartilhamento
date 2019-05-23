package Conexao;

import Comunicacao.Mensagem;
import Disco.Saida;
import Requisicao.Requisicao;
import Resposta.Resposta;
import java.io.IOException;
import java.net.Socket;

public class ConexaoServidor extends Conexao{

    public ConexaoServidor(Socket socket) throws IOException {
        super(socket);
    }
    
    public void enviar(Mensagem object) {
        try {
            this.enviar.writeObject(object);
            this.enviar.flush();
        } catch (IOException ex) {
            Saida.escrever("Ocorreu um erro: " + ex.getMessage());
        }
    }
    
    public Mensagem receber() throws IOException{
        try {
            Mensagem object = (Mensagem)this.receber.readObject();
            return object;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Erro fatal");
        }
    }
}
