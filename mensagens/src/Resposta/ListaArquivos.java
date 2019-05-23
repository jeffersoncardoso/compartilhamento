package Resposta;

public class ListaArquivos implements Resposta{
    
    protected String[] arquivos;

    public ListaArquivos(String[] arquivos) {
        this.arquivos = arquivos;
    }

    public String[] getArquivos() {
        return arquivos;
    }
    
    @Override
    public TipoResposta getTipo() {
        return TipoResposta.LISTA_ARQUIVOS;
    }
    
}
