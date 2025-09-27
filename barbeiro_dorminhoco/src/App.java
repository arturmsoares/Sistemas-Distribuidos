import java.util.concurrent.TimeUnit;


public class App {
    public static void main(String[] args) throws InterruptedException {
        Barbearia barbearia = new Barbearia(3);

        Thread barbeiroThread = new Thread(new Barbeiro(barbearia));
        barbeiroThread.start();

        for (int i = 0; i < 10; i++) {
            Thread clienteThread = new Thread(new Cliente(barbearia), "Cliente-" + (i + 1));
            clienteThread.start();

            // Clientes chegam a cada 1 segundo
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
