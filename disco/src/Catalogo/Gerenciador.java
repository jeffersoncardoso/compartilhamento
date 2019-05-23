package Catalogo;

import Conexao.ConexaoServidor;
import Disco.Servidor;
import Exceptions.ServidorJaConectadoException;
import Exceptions.ServidorNaoEncontradoException;
import java.io.IOException;
import java.net.Socket;

public class Gerenciador {
    private Servidor servidor;
    private ServidorCadeia anterior;
    private ServidorCadeia proximo;

    public Gerenciador() {
    }
    
    public Servidor iniciarServidor(String nome, int porta) {        
        servidor = new Servidor(nome, porta);
        
        return servidor;
    }
    
    public boolean conectar(String endereco) throws ServidorJaConectadoException, ServidorNaoEncontradoException {
        try {
            Socket socket = new Socket("localhost", Integer.parseInt(endereco));
            
            servidor.conectarProximoServidor(new ServidorCadeia(new ConexaoServidor(socket)));
            
            return true;
        } catch (IOException ex) {
            throw new ServidorNaoEncontradoException(String.format("Erro ao conectar no endereço %s", endereco));
        }
    }
}
