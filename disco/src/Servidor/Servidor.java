package Servidor;

import Comunicacao.EnviarArquivoDividido;
import Comunicacao.Mensagem;
import Comunicacao.PercorrerCaminho;
import Comunicacao.ReceberArquivoDividido;
import Conexao.ConexaoServidor;
import GUI.Saida;
import Conexao.Usuario;
import Estrutura.Arquivo;
import GUI.SaidaCliente;
import Resposta.ArquivoSalvo;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.*;

public final class Servidor {
    ServerSocket socketServidor;
    public HashMap<Long, Usuario> usuarios = new HashMap();
    
    private ServidorCadeia anterior;
    private ServidorCadeia proximo;
    private PercorrerCaminho caminho;
    private boolean conectado;
    
    public Servidor(int porta) {
        try {
            this.socketServidor = new ServerSocket(porta);
            this.conectado = true;
            
            Saida.escrever("Servidor iniciado na porta %s", porta);
     
        } catch (IOException ex) {
            throw new RuntimeException("Ocorreu um erro: " + ex.getMessage());
        }
    }
    
    public void desconectar() throws IOException {
        conectado = false;
        
        for (Long id : usuarios.keySet()) {
            usuarios.get(id).sair();
        }
        
        if(anterior instanceof ServidorCadeia) anterior.encerrar();
        if(proximo instanceof ServidorCadeia) proximo.encerrar();
        
        anterior = null;
        proximo = null;
        usuarios = new HashMap();
        
        socketServidor.close();
    }
    
    public void conectarAnterior(ServidorCadeia servidor) {
        Saida.escrever("O servidor %s se conectou", servidor.getNome());
        anterior = servidor;
    }
    
    public void conectarProximo(String endereco) throws IOException {
        Socket socket = new Socket("localhost", Integer.parseInt(endereco));
        proximo = new ServidorCadeia(new ConexaoServidor(socket).iniciar());
        Saida.escrever("Conectado ao servidor %s", socket.getPort());
    }    
    
    public String getEndereco() {
        return this.socketServidor.getInetAddress().toString();
    }

    public String getNome() {
        return String.valueOf(this.socketServidor.getLocalPort());
    }
    
    public Socket esperarCliente() throws IOException
    {
        return socketServidor.accept();
    }
    
    public void iniciarBarramentos() {
        try {
            new AguardarConexoes(this).start();
            new ReceberMensagens(this).start();
            new EnviarMensagens(this).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void adicionar(Usuario usuario) {
        this.usuarios.put(usuario.getId(), usuario);
    }

    boolean estaLigado() {
        return conectado;
    }
    
    private String getDiretorioBase() {
        String local = System.getProperty("user.dir") + "/anexos/" + getNome() + "/";
        
        File diretorio = new File(local);
        
        if(!diretorio.exists()){
            diretorio.mkdirs();
        }
        
        return local;
    }
    
    public void salvarArquivo(Arquivo arquivo, int numero) throws IOException {
        Path diretorio = Paths.get(getDiretorioBase() + arquivo.getNome() + "_" + numero);
        
        Files.write(diretorio, arquivo.getConteudo());
    }
    
    public void dividirArquivo(Arquivo arquivo) throws IOException {
        int numeroPartes = (proximo instanceof ServidorCadeia && possuiCadeia()) ? this.caminho.getTotal() : 1;
        
        SaidaCliente.escrever("Salvar %s (%s bytes - %s parte(s))", arquivo.getNome(), arquivo.getConteudo().length, numeroPartes);
        
        EnviarArquivoDividido arquivoPartes = new EnviarArquivoDividido(this, arquivo, numeroPartes);
        arquivoPartes.executar(this);
    }
    
    public void solicitarArquivo(Usuario solicitante, String nome) throws IOException {
        Arquivo arquivo = buscarParteArquivo(nome);
        Saida.escrever("Buscando parte %s do arquivo %s", arquivo.getParte() + 1, nome);
        
        if(possuiCadeia())
            encaminhar(new ReceberArquivoDividido(this, solicitante, nome, arquivo));
        else
            devolverArquivoCompleto(solicitante.getId(), arquivo);
    }
    public Arquivo buscarParteArquivo(String nome) throws IOException {
        File diretorio = new File(getDiretorioBase());
        File arquivosDiretorio[] = diretorio.listFiles();
        
        Arquivo arquivoEncontrado = null;
        for(File arquivo:arquivosDiretorio){
            if(arquivo.getName().contains(nome + "_")){
                arquivoEncontrado = new Arquivo(arquivo.getName(), Files.readAllBytes(Paths.get(arquivo.getAbsolutePath())));
            }
        }
        
        return arquivoEncontrado;
    }
    
    public void devolverArquivoCompleto(long solicitante, Arquivo arquivo) {
        SaidaCliente.escrever("Devolvendo arquivo %s completo", arquivo.getNome());
        
        Usuario usuario = usuarios.get(solicitante);
        usuario.devolverArquivoSolicitado(new ArquivoSalvo(arquivo));
    }

    public String[] listarArquivos() {
        ArrayList<String> nomesArquivo = new ArrayList();
        
        File[] arquivos = new File(getDiretorioBase()).listFiles();
        
        if(arquivos == null) {
            arquivos = new File[0];
        }
        
        for (File arquivo : arquivos) {
            if (arquivo.isFile()) {
                int indexNome = arquivo.getName().lastIndexOf('_');
                if(indexNome != -1) {
                    String nome = arquivo.getName().substring(0, indexNome);
                    nomesArquivo.add(nome);
                }
            }
        }
  
        return Arrays.copyOf(nomesArquivo.toArray(), nomesArquivo.size(), String[].class);
    }
    
    public boolean possuiCadeia() {
        if(anterior == null || proximo == null)
            return false;
        
        return anterior instanceof ServidorCadeia && proximo instanceof ServidorCadeia;
    }

    public ServidorCadeia getAnterior() {
        return anterior;
    }

    public ServidorCadeia getProximo() {
        return proximo;
    }
    
    public Mensagem receber() throws IOException {
        return anterior.receber();
    } 
    
    public void encaminhar(Mensagem mensagem) throws IOException {
        proximo.enviar(mensagem);
    }

    public void setCaminho(PercorrerCaminho caminho) {
        this.caminho = caminho;
    }
}
