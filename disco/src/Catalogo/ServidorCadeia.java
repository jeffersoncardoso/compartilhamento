package Catalogo;

import Conexao.ConexaoServidor;
import java.io.IOException;

public class ServidorCadeia {
    private final ConexaoServidor conexao;

    public ServidorCadeia(ConexaoServidor conexao) throws IOException {
        this.conexao = conexao;
    }
    
}
