package Disco;

import Conexao.ConexaoFabrica;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VerificarCadeia extends Thread{
    
    private Servidor servidor;

    public VerificarCadeia(Servidor servidor) {
        this.servidor = servidor;
    }
    
    @Override
    public void run() {
        Saida.escrever("Verificando cadeia");
        while(servidor.estaLigado()){
            try {
                if(servidor.possuiCadeia()) {
                    
                }
                
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(VerificarCadeia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
