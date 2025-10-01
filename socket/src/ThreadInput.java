import java.io.DataInputStream;


class ThreadInput implements Runnable {
    private DataInputStream entrada;
    
    public ThreadInput(DataInputStream entrada) {
        this.entrada = entrada;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                String linha = entrada.readUTF();
                if (linha.isEmpty()) {
                    System.out.println("x: Conexão encerrada!");
                    break;
                }
                
                System.out.println(linha);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}