package Resposta;

public class ArquivoErroAoSalvar implements Resposta{
    
    private String mensagem;

    public ArquivoErroAoSalvar(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
    
    @Override
    public TipoResposta getTipo() {
        return TipoResposta.ERRO_AO_SALVAR;
    }
    
}
