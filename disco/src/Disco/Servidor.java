package Disco; 

import Estrutura.Arquivo;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.*;

public class Servidor {    
    ServerSocket socketServidor;
    Socket s;
    public ArrayList<Usuario> usuarios = new ArrayList();
    
    public Servidor(int porta) {
        try {
            this.socketServidor = new ServerSocket(porta);
            Saida.escrever("Servidor iniciado na porta %s", porta);
        } catch (IOException ex) {
            throw new RuntimeException("Ocorreu um erro: " + ex.getMessage());
        }
    }

    public Socket esperarCliente() throws IOException
    {
        return socketServidor.accept();
    }
    
    public void iniciarBarramentos() {
        try {
            new AguardarConexoes(this).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void adicionar(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    boolean estaLigado() {
        return true;
    }
    
    private String getDiretorio() {
        return System.getProperty("user.dir") + "/anexos/";
    }
    
    public void salvarArquivo(Arquivo arquivo) throws IOException {
        Path diretorio = Paths.get(getDiretorio() + arquivo.getNome());
        
        Files.write(diretorio, arquivo.getConteudo());
    }
    
    public Arquivo buscarArquivo(String nome) throws IOException {
        Path diretorio = Paths.get(getDiretorio() + nome);
        
        return new Arquivo(nome, Files.readAllBytes(diretorio));
    }

    String[] listarArquivos() {
        ArrayList<String> nomesArquivo = new ArrayList();
        
        File[] arquivos = new File(getDiretorio()).listFiles();
        
        
        for (File arquivo : arquivos) {
            if (arquivo.isFile()) {
                nomesArquivo.add(arquivo.getName());
            }
        }
  
        return Arrays.copyOf(nomesArquivo.toArray(), nomesArquivo.size(), String[].class);
    }
}
