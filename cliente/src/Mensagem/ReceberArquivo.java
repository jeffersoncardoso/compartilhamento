package Mensagem;

public class ReceberArquivo extends Mensagem{

    @Override
    public TipoMensagem getTipo() {
        return TipoMensagem.RECEBER_ARQUIVO;
    }
    
}
