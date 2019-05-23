package Estrutura;

import java.io.Serializable;

public class Arquivo implements Serializable{
    
    private final String nome;
    private final byte[] conteudo;

    public Arquivo(String nome, byte[] conteudo) {
        this.nome = nome;
        this.conteudo = conteudo;
    }

    public String getNome() {
        return nome;
    }

    public byte[] getConteudo() {
        return conteudo;
    }

    public String getExtensao() {
        int index = this.nome.lastIndexOf('.');
        String extensao = this.nome.substring(index + 1);
        
        return extensao;
    }
    
}
