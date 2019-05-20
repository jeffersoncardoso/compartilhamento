package Requisicao;

public class ListarArquivos implements Requisicao{
    
    private String arquivos[];
    
    @Override
    public TipoRequisicao getTipo() {
        return TipoRequisicao.LISTAR_ARQUIVOS;
    }
    
    public String[] getArquivos() {
        return this.arquivos;
    }
    
}
