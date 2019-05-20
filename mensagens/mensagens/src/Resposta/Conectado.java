package Resposta;

public class Conectado implements Resposta{
    
    @Override
    public TipoResposta getTipo() {
        return TipoResposta.CONECTADO;
    }
    
}
