package Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextArea;


public class Saida {
    private static JTextArea textArea;
    
    public static void setSaida(JTextArea textArea) {
        Saida.textArea = textArea;
    }
    
    public static void escrever(String texto) {
        Date dataHoraAtual = new Date();
        String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);
        
        Saida.textArea.append(String.format("[%s] - %s \n", hora, texto));
        Saida.textArea.setCaretPosition(Saida.textArea.getDocument().getLength());
    }
    
    public static void escrever(String formato, Object... args) {
        Saida.escrever(String.format(formato, args));
    }
    
    
}