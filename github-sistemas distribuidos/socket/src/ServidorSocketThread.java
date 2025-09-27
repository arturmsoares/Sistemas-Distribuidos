import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.xml.crypto.Data;

public class ServidorSocketThread implements Runnable {

    private final Socket socketClient;

    private Observador observador;

    private DataOutputStream saida;

    public ServidorSocketThread(Socket socketClient, Observador observador) {
        this.socketClient = socketClient;
        this.observador = observador;
    }

    public DataOutputStream getSaida() {
        return saida;
    }

    @Override
    public void run() {
        try {
            this.saida = new DataOutputStream(socketClient.getOutputStream());
            DataInputStream entrada = new DataInputStream(socketClient.getInputStream());

            String linha;
            while ((linha = entrada.readUTF()) != null && !linha.trim().isEmpty()) {
                saida.writeUTF("Servidor leu :" + linha);
                if (linha.contains("<todos>")) {
                    observador.enviarMensagem(linha);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
