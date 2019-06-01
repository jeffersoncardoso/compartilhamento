package GUI;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SaidaCliente{
    private static JTextArea textArea;
    private static JScrollPane painel;
    
    public static void setSaida(JTextArea textArea, JScrollPane painel) {
        SaidaCliente.textArea = textArea;
        SaidaCliente.painel = painel;
    }
    
    public static void escrever(String texto) {
        
        Date dataHoraAtual = new Date();
        String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);
        
        SaidaCliente.textArea.append(String.format("[%s] - %s \n", hora, texto));
        SaidaCliente.textArea.setCaretPosition(SaidaCliente.textArea.getDocument().getLength());
    }
    
    public static void escrever(String formato, Object... args) {
        SaidaCliente.escrever(String.format(formato, args));
    }
}
