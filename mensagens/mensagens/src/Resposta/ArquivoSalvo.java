package Resposta;

import Estrutura.Arquivo;

public class ArquivoSalvo implements Resposta{
    
    private final Arquivo arquivo;

    public ArquivoSalvo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }

    public Arquivo getArquivo() {
        return arquivo;
    }
    
    @Override
    public TipoResposta getTipo() {
        return TipoResposta.ARQUIVO_SALVO;
    }
    
}
