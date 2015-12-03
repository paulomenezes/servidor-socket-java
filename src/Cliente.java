import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Criado por Guilherme Melo e Paulo Menezes
 * Projeto de Redes - Jeisa
 */
public class Cliente {
    private static final String OK = "HTTP/1.0 200 OK";
    private static final String NOTFOUND = "HTTP/1.0 404 NOT FOUND";
    private static final String BADREQUEST = "HTTP/1.0 400 BAD REQUEST";

    public static int indexOf(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.start() : -1;
    }

    public static String getColor(String name) {
        HashMap<String, String> cores = new HashMap<>();
        cores.put("branco", "white");
        cores.put("preto", "black");
        cores.put("vermelho", "red");
        cores.put("azul", "blue");
        cores.put("amarelo", "yellow");
        cores.put("verde", "green");
        cores.put("laranja", "orange");
        cores.put("roxo", "purple");
        cores.put("rosa", "pink");
        cores.put("cinza", "gray");
        cores.put("prata", "silver");
        cores.put("marrom", "brown");
        cores.put("dourado", "gold ");
        cores.put("verde limÃ£o", "lime");
        cores.put("violeta", "violet");
        cores.put("vinho", "magenta");

        return cores.containsKey(name) ? cores.get(name) : "black";
    }

    public static void main(String[] arg) {
        Socket s = null;

        try {
            System.out.println("Conectando...");

            s = new Socket("localhost", 6789); //->COLOCAR ENDERECO IP DO SERVIDOR AQUI
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String pagina;

            do {
                pagina = JOptionPane.showInputDialog(null, "Digite sua requisicao");

                if (pagina != null && pagina.length() > 0) {

                    out.writeUTF(pagina);
                    out.flush();

                    String recebe = in.readUTF();

                    if (recebe.contains(OK)) {
                        String resultado = recebe.substring(OK.length());

                        resultado = resultado.replaceAll("<htm>", "<html>");
                        resultado = resultado.replaceAll("</htm>", "</html>");

                        resultado = resultado.replaceAll("<neg>", "<b>");
                        resultado = resultado.replaceAll("</neg>", "</b>");

                        resultado = resultado.replaceAll("<ita>", "<i>");
                        resultado = resultado.replaceAll("</ita>", "</i>");

                        resultado = resultado.replaceAll("<sub>", "<u>");
                        resultado = resultado.replaceAll("</sub>", "</u>");

                        resultado = resultado.replaceAll("<pequeno>", "<small>");
                        resultado = resultado.replaceAll("</pequeno>", "</small>");

                        resultado = resultado.replaceAll("<subscrito>", "<sub>");
                        resultado = resultado.replaceAll("</subscrito>", "</sub>");

                        resultado = resultado.replaceAll("<superscrito>", "<sup>");
                        resultado = resultado.replaceAll("</superscrito>", "</sup>");

                        int index = indexOf(Pattern.compile("(<tamanho.*?>)"), resultado);
                        while (index > 0) {
                            resultado = resultado.replaceAll("</tamanho>", "</font>");
                            String tamanho = resultado.substring(index, index + resultado.substring(index).indexOf(">") + 1);
                            resultado = resultado.replaceAll(tamanho, tamanho.split(" ")[0].replace("tamanho", "font") + " size=\"" + tamanho.split(" ")[1].replace(">", "") + "\">");

                            index = indexOf(Pattern.compile("(<tamanho.*?>)"), resultado);
                        }

                        int index2 = indexOf(Pattern.compile("(<cor.*?>)"), resultado);
                        while (index2 > 0) {
                            resultado = resultado.replaceAll("</cor>", "</font>");
                            String tamanho = resultado.substring(index2, index2 + resultado.substring(index2).indexOf(">") + 1);
                            resultado = resultado.replaceAll(tamanho, tamanho.split(" ")[0].replace("cor", "font") + " color=\"" + (tamanho.split(" ")[1].replace(">", "").startsWith("#") ? tamanho.split(" ")[1].replace(">", "") : getColor(tamanho.split(" ")[1].replace(">", ""))) + "\">");

                            index2 = indexOf(Pattern.compile("(<cor.*?>)"), resultado);
                        }

                        JOptionPane.showMessageDialog(null, resultado);
                    } else {
                        switch (recebe) {
                            case NOTFOUND:
                                JOptionPane.showMessageDialog(null, "Desculpe, mas a página solicitada não foi encontrada.");
                                break;
                            case BADREQUEST:
                                JOptionPane.showMessageDialog(null, "Desculpe, mas houve um erro na requisição.");
                                break;
                            default:
                                JOptionPane.showMessageDialog(null, "Erro desconhecido.");
                                break;
                        }
                    }
                }
            } while (pagina != null && pagina.length() > 0);
            
        } catch(Exception e) {
        	
            e.printStackTrace();
            System.out.println("Erro: " + e.getMessage());
            
        } finally {
            try {
                if (s != null) s.close();
            } catch(Exception e2){
                System.out.println("Erro interno.");
            }
        }

        System.out.println("Conexão encerrada");
    }
}
