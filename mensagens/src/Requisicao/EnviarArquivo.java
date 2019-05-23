package Requisicao;

import Estrutura.Arquivo;

public class EnviarArquivo implements Requisicao{

    private final Arquivo arquivo;
    
    public EnviarArquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }

    public Arquivo getArquivo() {
        return arquivo;
    }

    @Override
    public TipoRequisicao getTipo() {
        return TipoRequisicao.ENVIAR_ARQUIVO;
    }
}
