package Mensagem;

public class ListarArquivos extends Mensagem{
    
    private String arquivos[];
    
    @Override
    public TipoMensagem getTipo() {
        return TipoMensagem.LISTAR_ARQUIVOS;
    }
    
    public String[] getArquivos() {
        return this.arquivos;
    }
    
}
