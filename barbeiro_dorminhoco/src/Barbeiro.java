
public class Barbeiro implements Runnable  {


    private final Barbearia barbearia;
    

    public Barbeiro(Barbearia barbearia) {
        this.barbearia = barbearia;
    }


    @Override
    public void run() {
        // Loop infinito: Só para quando a aplicação é encerrada ou a thread é interrompida
        while(true) {
            barbearia.atenderCliente();
        }
    }
}
