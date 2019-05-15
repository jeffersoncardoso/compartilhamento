package Cliente;

import Mensagem.Mensagem;
import Mensagem.EnviarArquivo;
import Mensagem.SolicitarArquivo;
import Mensagem.ListarArquivos;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;

public class Disco {
    private String nome;
    private final Conexao conexao;

    public Disco(String endereco, Integer porta) throws IOException {
        Socket socket = new Socket(endereco, porta);
        conexao = new Conexao(socket);
    }
    
    public void enviarArquivo(File arquivo) throws IOException {
        Arquivo arquivoEnviado = new Arquivo(arquivo.getName(), Files.readAllBytes(arquivo.toPath()));
        EnviarArquivo mensagem = new EnviarArquivo(arquivoEnviado);
        
        conexao.enviar(mensagem);
    }
    
    public Mensagem fazerDownload(String chave) throws IOException {
        conexao.enviar(new SolicitarArquivo(chave));
        
        return conexao.receber();
    }
    
    public void listarArquivos() {
        conexao.enviar(new ListarArquivos());
    }
}
