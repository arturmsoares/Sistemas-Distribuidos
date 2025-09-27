public class Cliente implements Runnable {

    private final Barbearia barbearia;

    public Cliente(Barbearia barbearia) {
        this.barbearia = barbearia;
    }


    @Override
    public void run() {
        String nomeCliente = Thread.currentThread().getName();

        barbearia.chegarCliente(nomeCliente);
    }
}
