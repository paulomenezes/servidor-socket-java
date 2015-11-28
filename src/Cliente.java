import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * Created by paulomenezes on 17/11/15.
 */
public class Cliente {
    public static void main(String[] arg) {
        Socket s = null;

        try {
        	
        	System.out.println("<html><b>Guilherme melo</b></html>");
        	
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
                
                System.out.println(recebe);
                
                if (recebe.contains(sucesso)) {
                    String resultado = recebe.substring(sucesso.length() + 1);

                    resultado = resultado.replaceAll("\n", "<br />");
                    resultado = "<html>" + resultado + "</html>";

                    resultado = resultado.replaceAll("<neg>", "<b>");
                    resultado = resultado.replaceAll("</neg>", "</b>");

                    resultado = resultado.replaceAll("<ita>", "<i>");
                    resultado = resultado.replaceAll("</ita>", "</i>");

                    resultado = resultado.replaceAll("<sub>", "<u>");
                    resultado = resultado.replaceAll("</sub>", "</u>");

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
