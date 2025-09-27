public class Leitor implements Runnable {
    private CadernoCompartilhado caderno;

    public Leitor(CadernoCompartilhado caderno) {
        this.caderno = caderno;
    }

    @Override
    public void run() {
        try {
            String conteudo = caderno.ler();
            System.out.println(Thread.currentThread().getName() + 
                " - Leu: " + conteudo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
