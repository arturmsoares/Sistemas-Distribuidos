import java.util.Random;

public class Corrida implements Runnable{

    private Random randon = new Random();

    private String nome;

    public Corrida(String nome) {
        this.nome = nome;
    }

    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println("Piloto " +nome+" completou "+i+" voltas");
            try{
                Thread.sleep(randon.nextInt(2001));
            }catch(InterruptedException e){
            }
        }
        System.out.println("Piloto " +nome+" terminou a corrida");
    }
    
}

//Runnable para criar tarefas que podem ser executadas em threads separadas. 
//Ao implementar Runnable, a classe deve fornecer o método run().
//O método run() define o que cada thread vai executar
//Thread.sleep(randon.nextInt(2001)) faz a thread "dormir" por um tempo aleatório (0-2000ms), simulando que cada piloto tem velocidades diferentes
