package Comunicacao;

import Servidor.Servidor;
import Conexao.Cliente;
import Estrutura.Arquivo;
import GUI.Saida;
import java.io.IOException;
import java.util.HashMap;

public class ReceberArquivoDividido extends Mensagem{
    
    private final String origem;
    private final long solicitante;
    private final String nomeOriginal;
    private HashMap<Integer, Arquivo> arquivos = new HashMap();

    public ReceberArquivoDividido(Servidor origem, Cliente usuario, String nomeOriginal, Arquivo parte) {
        this.origem = origem.getNome();
        this.solicitante = usuario.getId();
        this.nomeOriginal = nomeOriginal;
        arquivos.put(parte.getParte(), parte);
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
            arquivos.put(proximaParte.getParte(), proximaParte);
            
            servidor.encaminhar(this);
        } else {
            servidor.devolverArquivoCompleto(solicitante, unirPartes());
        }
    }
    
    private Arquivo unirPartes() {
        Arquivo primeiro = arquivos.get(0);
        for (int i = 1; i < arquivos.size(); i++) {
            primeiro = primeiro.concatenar(arquivos.get(i));
        }
        
        return new Arquivo(nomeOriginal, primeiro.getConteudo());
    }
    
}
