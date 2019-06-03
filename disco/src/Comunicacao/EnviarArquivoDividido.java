package Comunicacao;

import GUI.Saida;
import Servidor.Servidor;
import java.io.IOException;
import Estrutura.Arquivo;
import java.util.ArrayList;
import java.util.Arrays;

public class EnviarArquivoDividido extends Mensagem{
    
    private final String origem;
    private final ArrayList<String> servidores = new ArrayList();
    private final ArrayList<byte[]> divisao = new ArrayList();
    private final String nome;
    private int parteAtual = 0;
    
    
    public EnviarArquivoDividido(Servidor origem, Arquivo arquivo, int partes) {
        this.origem = origem.getNome();
        this.nome = arquivo.getNome();
        dividir(arquivo, partes);
    }
    
    private void dividir(Arquivo arquivo, int partes) {
        byte[] conteudo = arquivo.getConteudo();
        int tamanho = conteudo.length;
        
        int tamanhoParte = (int)Math.ceil(tamanho / partes);
        int inicio = 0;
        int fim = tamanhoParte;
        boolean dividindo = true;
        
        while(dividindo) {
            divisao.add(Arrays.copyOfRange(conteudo, inicio, fim));
        
            inicio = fim;
            fim = inicio + tamanhoParte;
            
            if(fim > tamanho) {
                fim = tamanho;
                dividindo = false;
            }
        }
    }
    
    @Override
    public void executar(Servidor servidor) throws IOException {
        if(!servidor.getNome().equals(origem) || (servidor.getNome().equals(origem) && parteAtual == 0)) {
            
            servidor.salvarArquivo(new Arquivo(nome, buscarParteAtual()), parteAtual);
            Saida.escrever("Salvando parte %s do arquivo %s em %s", parteAtual + 1, nome, servidor.getNome());
            
            passouPor(servidor);
            
            if(servidor.possuiCadeia())
                servidor.encaminhar(this);
        }else{
            passouPor(servidor);
        }
    }
    
    private byte[] buscarParteAtual() {
        byte[] conteudo;
        try {
            conteudo = divisao.get(parteAtual);
        } catch(IndexOutOfBoundsException ex) {
            conteudo = new byte[0];
        }
        
        return conteudo;
    }
    
    public void passouPor(Servidor servidor) {
        servidores.add(servidor.getNome());
        parteAtual++;
    }
}
