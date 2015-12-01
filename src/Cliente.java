import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

/**
 * Created by paulomenezes on 17/11/15.
 */
public class Cliente {
    public static int indexOf(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.start() : -1;
    }

    public static void main(String[] arg) {
        Socket s = null;

        try {
            System.out.println("Conectando...");

            s = new Socket("localhost", 6789);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String pagina = "";

            do {
                pagina = JOptionPane.showInputDialog(null, "Digite sua requisição");

                out.writeUTF(pagina);
                out.flush();

                String recebe = in.readUTF();
                String sucesso = "HTTP/1.0 200 OK";
                
                if (recebe.contains(sucesso)) {
                    String resultado = recebe.substring(sucesso.length());

                    resultado = resultado.replaceAll("<htm>", "<html>");
                    resultado = resultado.replaceAll("</htm>", "</html>");

                    resultado = resultado.replaceAll("<neg>", "<b>");
                    resultado = resultado.replaceAll("</neg>", "</b>");

                    resultado = resultado.replaceAll("<ita>", "<i>");
                    resultado = resultado.replaceAll("</ita>", "</i>");

                    resultado = resultado.replaceAll("<sub>", "<u>");
                    resultado = resultado.replaceAll("</sub>", "</u>");

                    int index = indexOf(Pattern.compile("(<tamanho.*?>)"), resultado);
                    if (index > 0) {
                        resultado = resultado.replaceAll("</tamanho>", "</font>");
                        String tamanho = resultado.substring(index, index + resultado.substring(index).indexOf(">") + 1);
                        resultado = resultado.replaceAll(tamanho, tamanho.split(" ")[0].replace("tamanho", "font") + " size=\"" + tamanho.split(" ")[1].replace(">", "") + "\">");
                    }

                    System.out.println(resultado);

                    JOptionPane.showMessageDialog(null, resultado);
                } else {
                    JOptionPane.showMessageDialog(null, recebe);
                }
                
            } while (pagina.length() > 0);
            
        } catch(Exception e) {
        	
            e.printStackTrace();
            System.out.println("Erro: " + e.getMessage());
            
        } finally {
        	
            try {
                if(s != null)
                    s.close();
            } catch(Exception e2){

            }
        }

        System.out.println("Conexão encerrada");

        try {
            System.in.read();
        } catch(Exception e3) {

        }
    }
}
