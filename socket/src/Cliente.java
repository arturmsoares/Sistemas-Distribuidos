import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) throws Exception {
        Socket conexao = new Socket("127.0.0.1", 2001);
        try (
                DataInputStream entrada = new DataInputStream(conexao.getInputStream());
                DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
                BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {

            while (true) {
                System.out.print("> ");
                String linha = teclado.readLine();
                saida.writeUTF(linha);

                linha = entrada.readUTF();
                if (linha.isEmpty()) {
                    System.out.println("x: Conexão encerrada!");
                    break;
                }

                System.out.println(linha);
            }
        }
    }

}

