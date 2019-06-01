package Comunicacao;

import Servidor.Servidor;
import java.io.IOException;
import java.io.Serializable;

abstract public class Mensagem implements Serializable{
    
    protected String destino;
    
    abstract public void executar(Servidor servidor) throws IOException;
    
    public boolean ehParaMim(Servidor servidor) {
        return servidor.getNome().equals(destino);
    }

    public String getDestino() {
        return destino;
    }
}
