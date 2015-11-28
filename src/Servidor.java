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
	
	
	public static String getArquivo(String arquivo) throws FileNotFoundException{
		
			 BufferedReader in = new BufferedReader(new FileReader(arquivo));
			 String str;
			 StringBuffer buf = new StringBuffer();
			 try {
				while (in.ready()) {
				  str = in.readLine();
				  buf.append(str);
				 }
				 in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			 
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

            System.out.println("ConexÃ£o estabelecida (" + cliente + ")");
            in = new DataInputStream(cliente.getInputStream());
            out = new DataOutputStream(cliente.getOutputStream());
            
            //String Sucesso Na Requisição
            String sucesso = "HTTP/1.0 200 OK ";
           
            //	String que o servidor recebe dos clientes
            String recebe = "";

            do {
                // Ler os dados do Cliente
                recebe = in.readUTF();
                // Printa no console a requisição
                System.out.print(recebe + " -- ");

                // Divide a requisição
                String[] parametros = recebe.split("/");

                // Valida os parametros.
                if (parametros.length == 3 && parametros[0].equals("GET")) {
                    
                	//Requisição HTTP INDEX
                    if (parametros[1].equals("HTTP")) {
                    	
                        	out.writeUTF(sucesso + getArquivo("index.htm"));
                    //Outros tipos de Arquivos    	
                    }else{
                    	
                    	String[] outroArquivo = parametros[0].split(" ");
                    	
                    	
                    	if(outroArquivo[0].equals("teste.htm")){
                    		
                    		out.writeUTF(sucesso + getArquivo("teste.htm"));
                    		
                    	}else{
                    		
                    		out.writeUTF(sucesso + getArquivo("sobre.htm") );
                    	}
                    }
                 //FormatoIncorreto   
                }else{
                	
                	out.writeUTF("400 BAD REQUEST");
                }
                
            } while (recebe.length() > 0);

            cliente.close();
            System.out.println("Conexão encerrada.");
            
        }catch (FileNotFoundException e){    
        	
        	try {
				out.writeUTF("404 HTTP Not Found");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
			}

        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
