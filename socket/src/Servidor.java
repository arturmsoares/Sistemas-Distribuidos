import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor implements Observador {

    private List<ServidorSocketThread> conexoes = new ArrayList<>();
    
    public static void main(String[] args) throws Exception {
        Servidor servidor = new Servidor();
        try (ServerSocket serverSocket = new ServerSocket(2001, 10000)) {
            System.out.println("Servidor iniciado.Aguardando conexões...");
            while (true) {
                Socket conexao = serverSocket.accept();
                System.out.println("Conexão estabelecida!");
                ServidorSocketThread thread = new ServidorSocketThread(conexao, servidor);
                servidor.conexoes.add(thread);
                new Thread(thread).start();
            }
        }

    }

    @Override
    public void enviarMensagem(String mensagem) throws Exception {
        for (ServidorSocketThread thread : conexoes) {
            thread.getSaida().writeUTF(mensagem);
        }
    }
}

// DataInputStream é um fluxo de entrada que permite ler dados primitivos de uma
// maneira portátil.
// DataOutputStream é um fluxo de saída que permite escrever dados primitivos de
// uma maneira portátil

// ambos são úteis para comunicação entre cliente e servidor, onde dados
// estruturados precisam ser transmitidos de forma eficiente e consistente.
// nesse caso, são essenciais para enviar e receber mensagens entre o cliente e
// o servidor usando o formato UTF.

