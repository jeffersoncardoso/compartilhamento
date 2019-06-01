package Comunicacao;

import Servidor.Servidor;
import Conexao.Usuario;
import Estrutura.Arquivo;
import GUI.Saida;
import java.io.IOException;
import java.util.ArrayList;

public class ReceberArquivoDividido extends Mensagem{
    
    private final String origem;
    private final long solicitante;
    private final String nomeOriginal;
    private ArrayList<Arquivo> arquivos = new ArrayList();

    public ReceberArquivoDividido(Servidor origem, Usuario usuario, String nomeOriginal, Arquivo primeiraParte) {
        this.origem = origem.getNome();
        this.solicitante = usuario.getId();
        this.nomeOriginal = nomeOriginal;
        arquivos.add(0, primeiraParte);
    }
    
    @Override
    public void executar(Servidor servidor) throws IOException {
        if(!origem.equals(servidor.getNome())) {
            Arquivo proximaParte = servidor.buscarParteArquivo(nomeOriginal);
            
            if(proximaParte == null) { //Não encontrou o arquivo nesse nó, manda para o próximo
                servidor.encaminhar(this);
                return;
            }
            
            Saida.escrever("Buscando parte %s do arquivo %s", proximaParte.getParte() + 1, nomeOriginal);
            arquivos.add(proximaParte.getParte(), proximaParte);
            
            servidor.encaminhar(this);
        } else {
            servidor.devolverArquivoCompleto(solicitante, unirPartes());
        }
    }
    
    private Arquivo unirPartes() {
        Arquivo primeiro = arquivos.get(0);
        primeiro = primeiro.concatenar(arquivos.get(1));
        
        return new Arquivo(nomeOriginal, primeiro.getConteudo());
    }
    
}
