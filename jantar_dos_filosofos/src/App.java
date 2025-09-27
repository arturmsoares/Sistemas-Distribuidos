public class App {
    public static void main(String[] args) throws Exception {
        Garfo garfo = new Garfo();
        for (int i = 0; i < 5; i++) {
            Filosofo filosofo = new Filosofo(i, garfo);
            new Thread(filosofo).start();
        }
    }
}
