import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServidorSocketThread implements Runnable {

    private final Socket socketClient;

    public ServidorSocketThread(Socket socketClient) {
        this.socketClient = socketClient;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream saida = new ObjectOutputStream(socketClient.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(socketClient.getInputStream())) {

            Pedido pedido = (Pedido) entrada.readObject();

            System.out.println("Recebi o pedido: " + pedido);
            saida.writeObject("Objeto recebido");

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
