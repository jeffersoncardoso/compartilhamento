package Comunicacao;

import GUI.Saida;
import Servidor.Servidor;
import java.io.IOException;
import java.util.ArrayList;

public final class PercorrerCaminho extends Mensagem{
    
    private final ArrayList<String> servidores = new ArrayList();
    
    public PercorrerCaminho(Servidor inicio) {
        destino = inicio.getNome();
        passouPor(inicio);
    }
    
    public void passouPor(Servidor servidor) {
        servidores.add(servidor.getNome());
    }

    public int getTotal() {
        return servidores.size();
    }

    public String getCaminho() {
        String caminho = "";
        for (String servidor : servidores) {
            caminho += String.format("%s -> ", servidor);
        }
        
        caminho += destino;
        
        return caminho;
    }

    @Override
    public void executar(Servidor servidor) throws IOException{
        if(ehParaMim(servidor)) {
            servidor.setCaminho(this);
        } else {
            passouPor(servidor);
            servidor.encaminhar(this);
        } 
    }
}
