package Resposta;

public class ArquivoRecebido implements Resposta{
    
    @Override
    public TipoResposta getTipo() {
        return TipoResposta.ARQUIVO_RECEBIDO;
    }
    
}
