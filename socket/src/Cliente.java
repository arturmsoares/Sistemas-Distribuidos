import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {
    
    public static void main(String[] args) throws Exception {
        Socket conexao = new Socket("localhost", 2001);
        try (ObjectOutputStream saida = new ObjectOutputStream(conexao.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(conexao.getInputStream());
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {
            
            while (true) {
                System.out.print("> Digite o cpf: ");
                String cpf = teclado.readLine();
                System.out.print("> Digite o valor: ");
                Double valor = new Double(teclado.readLine());
                Pedido pedido = new Pedido(cpf, valor);
                saida.writeObject(pedido);
                String resposta = (String) entrada.readObject();
                System.out.println(resposta);
            }
        }
    }
}

