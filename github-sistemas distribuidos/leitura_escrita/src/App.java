public class App {
    public static void main(String[] args) throws InterruptedException {
        
        CadernoCompartilhado caderno = new CadernoCompartilhado("Conteúdo Inicial");

        // Cenário 1: Leituras concorrentes (devem ser otimistas)
        System.out.println("CENÁRIO 1: Múltiplas leituras simultâneas");
        for (int i = 0; i < 3; i++) {
            new Thread(new Leitor(caderno), "Leitor-" + (i + 1)).start();
        }
        
        Thread.sleep(200); // Aguarda leitores iniciarem
        
        // Cenário 2: Escrita durante leituras (deve causar fallback)
        System.out.println("\nCENÁRIO 2: Escrita interferindo nas leituras");
        new Thread(new Escritor(caderno), "Escritor-1").start();
        
        // Mais leitores após a escrita iniciar
        Thread.sleep(50);
        for (int i = 3; i < 6; i++) {
            new Thread(new Leitor(caderno), "Leitor-" + (i + 1)).start();
        }
        
        Thread.sleep(300);
        
        // Cenário 3: Múltiplas escritas
        System.out.println("\nCENÁRIO 3: Múltiplas escritas concorrentes");
        for (int i = 2; i <= 3; i++) {
            new Thread(new Escritor(caderno), "Escritor-" + i).start();
        }
    }
}
