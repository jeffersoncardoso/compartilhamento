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
    
    public Arquivo concatenar(Arquivo arquivo) {
        byte[] primeiraParte = conteudo;
        byte[] proximaParte = arquivo.getConteudo();
        byte[] combinado = new byte[primeiraParte.length + proximaParte.length];

        for (int i = 0; i < combinado.length; ++i)
        {
            combinado[i] = i < primeiraParte.length ? primeiraParte[i] : proximaParte[i - primeiraParte.length];
        }
        
        return new Arquivo(nome, combinado);
    }

    public int getParte() {
        int indexParte = this.nome.lastIndexOf("_");
        
        return Integer.parseInt(this.nome.substring(indexParte + 1));
    }
    
}
