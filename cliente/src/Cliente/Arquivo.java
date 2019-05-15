package Cliente;

public class Arquivo {
    
    private String nome;
    private String extensao;
    private byte[] conteudo;

    public Arquivo(String nome, byte[] conteudo) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public byte[] getConteudo() {
        return conteudo;
    }
    
}
