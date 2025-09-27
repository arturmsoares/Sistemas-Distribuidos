public class App {
    public static void main(String[] args) throws Exception {
        Corrida schumacher = new Corrida("Schumacher");
        Corrida senna = new Corrida("Senna");
        Corrida barrichello = new Corrida("Barrichello");
        Thread thread = new Thread(schumacher);
        Thread thread1 = new Thread(senna);
        Thread thread2 = new Thread(barrichello);
        thread.start();
        thread1.start();
        thread2.start();
        thread.join();
        thread1.join();
        thread2.join();
        System.out.println("Acabou a corrida");
    }
}

//O método start() inicia a execução paralela das threads. 
//Cada thread executará seu método run() simultaneamente, não sequencialmente.
//O método join() faz com que a thread principal (main) espere que todas as outras threads terminem antes de continuar
//Isso garante que Todos os pilotos terminem a corrida e Só depois seja exibido "Acabou a corrida"