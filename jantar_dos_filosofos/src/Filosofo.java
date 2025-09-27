import java.util.Random;

public class Filosofo implements Runnable {
    
    private Integer id;
    private Garfo garfo;
    private Random random = new Random();

    public Filosofo(Integer id, Garfo garfo) {
        this.id = id;
        this.garfo = garfo;
    }
    @Override
    public void run() {
        while(true){

            try{
            System.out.println("O Filosofo " + id + " quer pegar o garfo.");
            garfo.pegar(id);
            System.out.println("O Filosofo " + id + " pegou os garfos.");
            Thread.sleep(random.nextInt(50000));
            System.out.println("O Filosofo " + id + " quer devolver os garfos.");
            garfo.devolver(id);
            System.out.println("O Filosofo " + id + " devolveu os garfos.");
            Thread.sleep(random.nextInt(50000));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
