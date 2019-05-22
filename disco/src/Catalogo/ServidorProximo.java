package Catalogo;

import Disco.Conexao;
import java.io.IOException;
import java.net.Socket;

public class ServidorProximo {
    private final Conexao conexao;

    public ServidorProximo(int porta) throws IOException {
        Socket socket = new Socket("localhost", porta);
        
        conexao = new Conexao(socket);
    }
    
    
}
