package Requisicao;

public class SolicitarArquivo implements Requisicao{

    private String nome;
    
    public SolicitarArquivo(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
    
    @Override
    public TipoRequisicao getTipo() {
        return TipoRequisicao.SOLICITAR_ARQUIVO;
    }
    
}
