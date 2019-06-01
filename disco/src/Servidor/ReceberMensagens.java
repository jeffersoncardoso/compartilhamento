package Servidor;

import Servidor.Servidor;
import Comunicacao.Mensagem;
import GUI.Saida;
import java.io.EOFException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ReceberMensagens extends Thread{
    private final Servidor servidor;

    public ReceberMensagens(Servidor servidor) {
        this.servidor = servidor;
    }
    
    @Override
    public void run() {
        Saida.escrever("Recebendo mensagens");
        while(servidor.estaLigado()) {            
            try {
                if(!servidor.possuiCadeia()){
                    Thread.sleep(10);
                    continue;
                }
                
                Mensagem mensagem = servidor.receber();
                mensagem.executar(servidor);
                
                Thread.sleep(10);
                
            } catch (EOFException e) {
                break;
            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                
                Saida.escrever("Erro no recebimento de mensagem: %s", exceptionAsString);
                Saida.escrever(e.getMessage());
                
                try { Thread.sleep(10); } catch (InterruptedException ex) { }
            }
        }
    }
}
