import java.util.LinkedList;
import java.util.Queue;

public class Buffer {
    
    //Queue é uma interface que representa uma fila de elementos. LinkedList é uma implementação concreta dessa interface.
    private Queue<Integer> fila = new LinkedList<>();
    private static final int LIMITE = 5;

    public synchronized void produzir(int item) throws InterruptedException {
        while (fila.size() == LIMITE) {
            System.out.println("Buffer cheio, produtor aguardando o consumidor...");
            wait();
        }
        fila.add(item);
        System.out.println("Produziu: " + item);
        notifyAll();
    }

    public synchronized void consumir() throws InterruptedException {
        while (fila.isEmpty()) {
            System.out.println("Buffer vazio, consumidor aguardando o produtor...");
            wait();
        }
        int item = fila.poll();
        System.out.println("Consumiu: " + item);
        
        if(fila.size() == LIMITE - 1) {
            notifyAll();
        }
    }
}


//synchronized: Garante que apenas uma thread pode acessar o método por vez, prevenindo condições de corrida.
//wait(): Faz a thread atual esperar até que outra thread chame notify() ou notifyAll