import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by paulomenezes on 17/11/15.
 */
public class Servidor {
    public static void main(String[] arg) {
        ServerSocket s;

        // Páginas
        HashMap<String, String> paginas = new HashMap<>();
        paginas.put("index.htm", "Essa é o <neg>index.htm</neg>\n\n<ita>Itálico</ita>\n<neg>Negrito</neg>\n<sub>Sublinhado</sub>");
        paginas.put("contato.htm", "Essa é o <tam 20>contato.htm</tam>");
        paginas.put("sobre.htm", "Essa é o sobre.htm");
        paginas.put("error.htm", "Essa é o error.htm");

        try {
            int p = 6789;
            s = new ServerSocket(p);
            System.out.println("Servidor iniciado na porta " + p);
            Socket cliente = s.accept();

            System.out.println("Conexão estabelecida (" + cliente + ")");
            DataInputStream in = new DataInputStream(cliente.getInputStream());
            DataOutputStream out = new DataOutputStream(cliente.getOutputStream());

            // String que o servidor recebe dos clientes
            String recebe = "";

            do {
                // Ler os dados do Cliente
                recebe = in.readUTF();
                // Printa no console a requisição
                System.out.print(recebe + " -- ");

                // Divide a requsição
                String[] parametros = recebe.split(" ");

                String saida = "";
                boolean escever = false;

                // Válida os parâmetros. Se começou com GET
                if (parametros.length == 3 && parametros[0].equals("GET")) {
                    // Se terminar com HTTP/1.0
                    if (parametros[2].equals("HTTP/1.0")) {
                        String sucesso = "HTTP/1.0 200 Success\n";
                        // Se a página existe no dicionário
                        if (parametros[1].startsWith("/") && paginas.containsKey(parametros[1].substring(1))) {
                            saida = sucesso;
                            out.writeUTF(sucesso + paginas.get(parametros[1].substring(1)));

                            escever = true;
                        } else {
                            // Se for igual a '/', mostra o index.htm
                            if (parametros[1].equals("/") && paginas.containsKey("index.htm")) {
                                saida = sucesso;
                                out.writeUTF(sucesso + paginas.get("index.htm"));

                                escever = true;
                            } else {
                                saida = "HTTP/1.0 404 Not found";
                            }
                        }
                    } else {
                        saida = "HTTP/1.0 400 Bad request";
                    }
                } else {
                    saida = "HTTP/1.0 400 Bad request";
                }

                if (!escever)
                    out.writeUTF(saida);

                System.out.print(saida);
                System.out.println();
            } while (recebe.length() > 0);

            cliente.close();
            System.out.println("Conexão encerrada.");

        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
