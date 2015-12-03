import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Criado por Guilherme Melo e Paulo Menezes
 * Projeto de Redes - Jeisa
 */
public class Servidor {
    private static final String OK = "HTTP/1.0 200 OK";
    private static final String NOTFOUND = "HTTP/1.0 404 NOT FOUND";
    private static final String BADREQUEST = "HTTP/1.0 400 BAD REQUEST";

    public static String getArquivo(String arquivo) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(arquivo));
        String str;
        StringBuilder buf = new StringBuilder();

        while (in.ready()) {
            str = in.readLine();
            buf.append(str);
        }

        in.close();

        return buf.toString();
	}
	
    public static void main(String[] arg) {
        DataInputStream in;
        DataOutputStream out;

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

            //	String que o servidor recebe dos clientes
            String recebe;

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
                        // RequisiÃ§Ã£o HTTP INDEX
                        if (parametros[2].equals("HTTP/1.0")) {
                            try {
                                if (parametros[1].equals("/")) {
                                    out.writeUTF(OK + getArquivo("index.htm"));
                                } else {
                                    out.writeUTF(OK + getArquivo(parametros[1].substring(1)));
                                }
                                System.out.print(OK);
                            } catch (IOException e) {
                                out.writeUTF(NOTFOUND);
                                System.out.print(NOTFOUND);
                            }
                        } else {
                            out.writeUTF(BADREQUEST);
                            System.out.print(BADREQUEST);
                        }
                    } else {
                        out.writeUTF(BADREQUEST);
                        System.out.print(BADREQUEST);
                    }
                }

                System.out.println();
            } while (recebe.length() > 0);

            cliente.close();
            System.out.println("Conexão encerrada.");
        } catch (Exception e) {
            System.out.println("Erro interno.");
        }
    }
}
