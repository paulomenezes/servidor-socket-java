import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by paulomenezes on 17/11/15.
 */
public class Servidor {
	
	public static String getArquivo(String arquivo) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(arquivo));
        String str;
        StringBuffer buf = new StringBuffer();

        while (in.ready()) {
            str = in.readLine();
            buf.append(str);
        }

        in.close();

        return buf.toString();
	}
	
    public static void main(String[] arg) {
        DataInputStream in;
        DataOutputStream out = null;

        ServerSocket s;
        Socket cliente;

        try {
            int p = 6789;
            s = new ServerSocket(p);
            System.out.println("Servidor iniciado na porta " + p);
            cliente = s.accept();

            System.out.println("Conexão estabelecida (" + cliente + ")");
            in = new DataInputStream(cliente.getInputStream());
            out = new DataOutputStream(cliente.getOutputStream());

            //String Sucesso Na Requisição
            String sucesso = "HTTP/1.0 200 OK";

            //	String que o servidor recebe dos clientes
            String recebe = "";

            do {
                // Ler os dados do Cliente
                recebe = in.readUTF();

                if (recebe.length() > 0) {
                    // Printa no console a requisição
                    System.out.print(recebe + " -- ");

                    // Divide a requisição
                    String[] parametros = recebe.split(" ");

                    // Valida os parametros.
                    if (parametros.length == 3 && parametros[0].equals("GET")) {
                        // Requisição HTTP INDEX
                        if (parametros[2].equals("HTTP/1.0")) {
                            try {
                                if (parametros[1].equals("/")) {
                                    out.writeUTF(sucesso + getArquivo("index.htm"));
                                } else {
                                    out.writeUTF(sucesso + getArquivo(parametros[1].substring(1)));
                                }
                                System.out.print(sucesso);
                            } catch (IOException e) {
                                out.writeUTF("404 HTTP Not Found");
                                System.out.print("404 HTTP Not Found");
                            }
                        } else {
                            out.writeUTF("400 BAD REQUEST");
                            System.out.print("400 BAD REQUEST");
                        }
                    } else {
                        out.writeUTF("400 BAD REQUEST");
                        System.out.print("400 BAD REQUEST");
                    }
                }

                System.out.println();
            } while (recebe.length() > 0);

            cliente.close();
            System.out.println("Conexão encerrada.");
        } catch (Exception e) {

        }
    }
}
