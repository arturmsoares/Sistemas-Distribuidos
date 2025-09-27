import java.util.Random;

public class Consumidor implements Runnable {
   private Random random = new Random();
    private Buffer buffer;

    public Consumidor(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run(){
        while (true){
            try {
                buffer.consumir();
                Thread.sleep(random.nextInt(500));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    
}
