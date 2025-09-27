import java.util.concurrent.locks.StampedLock;

public class CadernoCompartilhado {
    private final StampedLock lock = new StampedLock();
    private String conteudo;

    public CadernoCompartilhado(String conteudoInicial) {
        this.conteudo = conteudoInicial;
    }

    public String ler() throws InterruptedException {
        // LEITURA OTIMISTA - característica principal do problema
        long stamp = lock.tryOptimisticRead();
        String conteudoLido = this.conteudo;
        
        System.out.println(Thread.currentThread().getName() + 
            " - Tentando leitura OTIMISTA (stamp: " + stamp + ")");

        Thread.sleep(50);

        if (!lock.validate(stamp)) {
            System.out.println(Thread.currentThread().getName() + 
                " - LEITURA OTIMISTA FALHOU! Usando fallback para ReadLock...");
            
            // FALLBACK 
            stamp = lock.readLock();
            try {
                conteudoLido = this.conteudo;
                System.out.println(Thread.currentThread().getName() + 
                    " - Leitura com ReadLock realizada");
            } finally {
                lock.unlockRead(stamp);
            }
        } else {
            System.out.println(Thread.currentThread().getName() + 
                " - Leitura Otimista bem-sucedida!");
        }
        
        return conteudoLido;
    }

    public void escrever(String novoConteudo) throws InterruptedException {
        long stamp = lock.writeLock();
        try {
            System.out.println(Thread.currentThread().getName() + 
                " - WriteLock adquirido - ESCREVENDO...");
            
            Thread.sleep(100);
            
            this.conteudo = novoConteudo;
            System.out.println(Thread.currentThread().getName() + 
                " - Escreveu: " + novoConteudo);
                
        } finally {
            System.out.println(Thread.currentThread().getName() + 
                " - WriteLock liberado");
            lock.unlockWrite(stamp);
        }
    }
}
