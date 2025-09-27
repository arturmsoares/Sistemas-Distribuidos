public class Escritor implements Runnable {
    private CadernoCompartilhado caderno;

    public Escritor(CadernoCompartilhado caderno) {
        this.caderno = caderno;
    }

    @Override
    public void run() {
        try {
            String novoConteudo = "Texto do " + Thread.currentThread().getName() + 
                " (timestamp: " + System.currentTimeMillis() + ")";
            caderno.escrever(novoConteudo);
            System.out.println();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
