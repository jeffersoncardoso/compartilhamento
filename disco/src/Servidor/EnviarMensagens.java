package Servidor;

import Servidor.Servidor;
import Comunicacao.PercorrerCaminho;
import GUI.Saida;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketException;

public class EnviarMensagens extends Thread{
    private final Servidor servidor;

    public EnviarMensagens(Servidor servidor) {
        this.servidor = servidor;
    }
    
    @Override
    public void run() {
        Saida.escrever("Enviando mensagens");
        while(servidor.estaLigado()) {
            try {
                if(servidor.possuiCadeia()) {
                    servidor.encaminhar(new PercorrerCaminho(servidor));
                }
            } catch(SocketException ex){
                break;
            } catch (IOException ex) {
                ex.printStackTrace();
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                
                Saida.escrever("Erro no recebimento de mensagem: %s", exceptionAsString);
                Saida.escrever(ex.getMessage());
            }
            
            try { Thread.sleep(1000); } catch (InterruptedException ex) { }
        }
    }
}
