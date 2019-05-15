package Mensagem;

public class SolicitarArquivo extends Mensagem{

    private String chave;
    
    public SolicitarArquivo(String chave) {
        this.chave = chave;
    }
    
    @Override
    public TipoMensagem getTipo() {
        return TipoMensagem.SOLICITAR_ARQUIVO;
    }
    
}
