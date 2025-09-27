
import java.util.List;
import java.util.Scanner;

public class LeituraTeclado implements Runnable {

    private List<Observador> observadores;

    private Buffer buffer;

    public LeituraTeclado(Buffer buffer,List<Observador> observadores) {
        this.buffer = buffer;
        this.observadores = observadores;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String texto = scanner.nextLine();
            buffer.addConteudoBuffer(texto+"\n");
            for (Observador observador: observadores) {
                observador.notificaMudanca();
            }
        }
    }

}
