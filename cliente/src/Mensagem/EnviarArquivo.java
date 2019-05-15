package Mensagem;

import Cliente.Arquivo;

public class EnviarArquivo extends Mensagem{

    private Arquivo arquivo;
    
    public EnviarArquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }

    public Arquivo getArquivo() {
        return arquivo;
    }

    @Override
    public TipoMensagem getTipo() {
        return TipoMensagem.ENVIAR_ARQUIVO;
    }
}
