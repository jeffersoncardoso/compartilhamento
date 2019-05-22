package Catalogo;

import Disco.Servidor;
import Exceptions.ServidorJaConectadoException;
import Exceptions.ServidorNaoEncontradoException;
import java.io.IOException;

public class Gerenciador {
    private Servidor servidor;
    private ServidorAnterior anterior;
    private ServidorProximo proximo;

    public Gerenciador() {
    }
    
    public Servidor iniciarServidor(String nome, int porta) {        
        servidor = new Servidor(nome, porta);
        
        return servidor;
    }
    
    public boolean conectar(String endereco) throws ServidorJaConectadoException, ServidorNaoEncontradoException {
        try {
            if(proximo instanceof ServidorProximo)
                throw new ServidorJaConectadoException(String.format("Servidor já está conectado"));
            
            proximo = new ServidorProximo(Integer.parseInt(endereco));
            
            return true;
        } catch (IOException ex) {
            throw new ServidorNaoEncontradoException(String.format("Erro ao conectar no endereço %s", endereco));
        }
    }
}
