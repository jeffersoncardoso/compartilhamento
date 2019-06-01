package Cliente;

import Estrutura.Arquivo;
import Requisicao.EnviarArquivo;
import Requisicao.ListarArquivos;
import Requisicao.SolicitarArquivo;
import Resposta.Resposta;
import Resposta.ArquivoRecebido;
import Resposta.ArquivoSalvo;
import Resposta.ListaArquivos;
import Resposta.TipoResposta;
import Util.Saida;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;

public class Disco {
    private String nome;
    private Conexao conexao;
    private String[] arquivos = {};

    public Disco(String endereco, Integer porta) throws IOException {
        Socket socket = new Socket(endereco, porta);
        conexao = new Conexao(socket);
        
        Saida.escrever("Conectado");
        
        listarArquivos();
    }
    
    public void desconectar() {
        conexao.encerrar();
        Saida.escrever("Desconectado");
    }
    
    public void enviarArquivo(File arquivo) throws IOException {
        Arquivo arquivoEnviado = new Arquivo(arquivo.getName(), Files.readAllBytes(arquivo.toPath()));
        
        conexao.enviar(new EnviarArquivo(arquivoEnviado));
        Resposta resposta = (Resposta)conexao.receber();
        
        switch(resposta.getTipo()) {
            case ERRO_AO_SALVAR: 
                Saida.escrever("Erro ao salvar o arquivo:");
                break;
            case ARQUIVO_RECEBIDO:
                Saida.escrever("Arquivo salvo com sucesso");
                break;
        }
        
        listarArquivos();
    }
    
    public void fazerDownload(String chave) throws IOException {
        conexao.enviar(new SolicitarArquivo(chave));
        Resposta resposta = conexao.receber();
        
        if(resposta.getTipo() == TipoResposta.ARQUIVO_SALVO) {
            ArquivoSalvo arquivoSalvo = (ArquivoSalvo)resposta;
            File temp = File.createTempFile(arquivoSalvo.getArquivo().getNome(), "." + arquivoSalvo.getArquivo().getExtensao());
            temp.deleteOnExit();
            
            FileOutputStream stream = new FileOutputStream(temp);
            stream.write(arquivoSalvo.getArquivo().getConteudo());
            stream.close();
            
            Desktop dt = Desktop.getDesktop();
            try { dt.open(temp); } 
            catch (IOException ex) { ex.getStackTrace(); }
        }
    }
    
    public String[] getArquivos() {
        return arquivos;
    }
    
    private void listarArquivos() throws IOException {
        conexao.enviar(new ListarArquivos());
        ListaArquivos lista = (ListaArquivos)conexao.receber();
        arquivos = lista.getArquivos();
    }
}
