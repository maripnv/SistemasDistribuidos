package lab2;
/**
 * Laboratorio 2 de Sistemas Distribuidos
 * 
 * Autor: Lucio A. Rocha
 * Estudantes: Mariana Pedroso Naves 2320720
 *             Andrei Fernandes Zani 2367831
 * Ultima atualizacao: 20/10/2024
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Cliente {
    
    private static Socket socket;
    private static DataInputStream entrada;
    private static DataOutputStream saida;
    
    private int porta=1025;
    public void iniciar(){
    	System.out.println("Cliente iniciado na porta: "+porta);
    	
    	try {
            
            socket = new Socket("127.0.0.1", porta);
            entrada = new DataInputStream(socket.getInputStream());
            saida = new DataOutputStream(socket.getOutputStream());
            
            //Recebe do usuario algum valor
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Digite o metodo(read ou write): ");
            String metodo = br.readLine();
            String argumento = "";
            if(!metodo.equals("read")){
                System.out.println("Digite o argumento: ");
                argumento = br.readLine();
                
            }
            // Envia uma mensagem ao servidor no formato JSON
            saida.writeUTF("{\n" + "\"method\":" + "\"" + metodo + "\"" + ",\n" + "\"args\":"  + "[" + "\"" + argumento + "\"" +"]" + "\n}\n");          
            
            try{
            // Lê a resposta do servidor e armazena na variável 'resultado'
            String resultado = entrada.readUTF();
            
            //Mostra o resultado 
            System.out.println(resultado);
            }catch(Exception e){
                e.printStackTrace();
            }
            socket.close();
            
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new Cliente().iniciar();
    }
    
}
