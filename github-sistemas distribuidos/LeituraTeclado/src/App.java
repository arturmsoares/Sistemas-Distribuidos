
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        Buffer buffer = new Buffer();
        SalvamentoAutomatico salvamento = new SalvamentoAutomatico(buffer);
        List<Observador> observadores = Arrays.asList(salvamento);
        LeituraTeclado leitura = new LeituraTeclado(buffer, observadores);
        Thread t1 = new Thread(leitura);
        t1.start();
        Thread t2 = new Thread(salvamento);
        t2.start();
        System.out.println("tchau main");
    }
}
