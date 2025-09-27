import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Barbearia {

    private final int NUM_CADEIRAS;
    private int cadeirasOcupadas = 0;

    private boolean barbeiroOcupado = false;

    private final ReentrantLock lock = new ReentrantLock();

    // Conditions são usadas para coordenação entre threads
    private final Condition clienteNaCadeira = lock.newCondition();

    private final Condition barbeiroDisponivel = lock.newCondition();

    public Barbearia(int numCadeiras) {
        this.NUM_CADEIRAS = numCadeiras;
    }

    public void chegarCliente(String nomeCliente) {
        lock.lock();
        try {
            if (cadeirasOcupadas < NUM_CADEIRAS) {

                if (cadeirasOcupadas == 0 && !barbeiroOcupado) {
                    System.out.println(nomeCliente + ": Chegou e acordou o barbeiro.");
                    barbeiroDisponivel.signal();
                }

                cadeirasOcupadas++;
                System.out.println(nomeCliente + ": Sentou na cadeira. " +
                        "Cadeiras ocupadas: " + cadeirasOcupadas + " | Cadeiras livres: "
                        + (NUM_CADEIRAS - cadeirasOcupadas));

                clienteNaCadeira.await();

                System.out.println(nomeCliente + ": Foi atendido e saiu da barbearia.");

                barbeiroDisponivel.signal();
            } else {
                System.out.println(nomeCliente + ": Barbearia estava lotada e foi embora.");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void atenderCliente() {
        lock.lock();
        try {
            while (cadeirasOcupadas == 0) {
                System.out.println("Barbeiro: Como não há clientes, foi dormir.");
                barbeiroOcupado = false;
                barbeiroDisponivel.await();
            }

            barbeiroOcupado = true;

            cadeirasOcupadas--;
            System.out.println("Barbeiro: Chamou o cliente para a cadeira de corte." +
                    " Cadeiras ocupadas: " + cadeirasOcupadas + " | Cadeiras livres: "
                    + (NUM_CADEIRAS - cadeirasOcupadas));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }

        // Simula o tempo de corte de cabelo
        try {
            System.out.println("Barbeiro: Cortando cabelo...");
            Thread.sleep(3000);
            System.out.println("Barbeiro: Corte finalizado!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        lock.lock();
        try {
            clienteNaCadeira.signal();

            barbeiroDisponivel.await();

        } catch (InterruptedException e) {
            //quando a thread é interrompida, o status de interrupção é limpo
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

}
