package Servidor;

import Comunicacao.Mensagem;
import Conexao.ConexaoServidor;
import java.io.IOException;

public class ServidorCadeia {
    private final ConexaoServidor conexao;

    public ServidorCadeia(ConexaoServidor conexao) throws IOException {
        this.conexao = conexao;
    }
    
    public String getNome() {
        return conexao.getNome();
    }
    
    public Mensagem receber() throws IOException {
        return conexao.receber();
    }
    
    public void enviar(Mensagem mensagem) throws IOException {
        if(!conexao.estaOcupado())
            conexao.enviar(mensagem);
    }
    
    public void encerrar() {
        conexao.encerrar();
    }
    
}
