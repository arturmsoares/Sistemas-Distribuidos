import java.util.Random;

public class Produtor implements Runnable{

    private Buffer buffer;
    private Random random = new Random();

    public Produtor(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run(){
        while(true){
            int item = random.nextInt(3000);
            try {
                System.out.println("Produtor produziu " + item);
                buffer.produzir(item);
                System.out.println("Produtor entregou " + item +" no buffer");
                Thread.sleep(random.nextInt(500));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    
}
