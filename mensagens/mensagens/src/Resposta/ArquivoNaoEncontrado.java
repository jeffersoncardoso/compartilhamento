package Resposta;

public class ArquivoNaoEncontrado implements Resposta{

    @Override
    public TipoResposta getTipo() {
        return TipoResposta.ARQUIVO_NAO_ENCONTRADO;
    }
    
}
