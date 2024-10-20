package lab2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;


public class Servidor {

	private static Socket socket;
	private static ServerSocket server;

	private static DataInputStream entrada;
	private static DataOutputStream saida;

	private int porta = 1025;
        
        public final static Path path = Paths.get("C:\\Users\\marip\\OneDrive\\Documentos\\NetBeansProjects\\Lab2\\src\\lab2\\fortune-br.txt");
        private final String formatError = """
                                           {
                                           "result": "false"
                                           }
                                           """;
	private int NUM_FORTUNES = 0;
        
        
        public int countFortunes() throws FileNotFoundException {
                int lineCount = 0;
                InputStream is = new BufferedInputStream(new FileInputStream(
                        path.toString()));
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                    String line = "";
                    while (!(line == null)) {

                        if (line.equals("%")) {
                            lineCount++;
                        }

                        line = br.readLine();

                    }// fim while
                } catch (IOException e) {
                    System.out.println("SHOW: Excecao na leitura do arquivo.");
                }
                return lineCount;
            }
 
        public void parser(HashMap<Integer, String> hm) throws FileNotFoundException {

            InputStream is = new BufferedInputStream(new FileInputStream(path.toString()));
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                int lineCount = 0;
                String line = "";
                while (!(line == null)) {
                    if (line.equals("%"))
                        lineCount++;
                    line = br.readLine();
                    StringBuffer fortune = new StringBuffer();
                    while (!(line == null) && !line.equals("%")) {
                        fortune.append(line + "\n");
                        line = br.readLine();
                    }
                    hm.put(lineCount, fortune.toString());
                   
                }// fim while
            } catch (IOException e) {
                System.out.println("SHOW: Excecao na leitura do arquivo.");
            }
        }
        
       //Metodo para ler a fortune no servidor
        public void read(HashMap<Integer, String> hm) throws IOException {
                int randomInt = (int)(Math.random() * NUM_FORTUNES);
                //Mostra a fortuna aleatória
                saida.writeUTF("{\n" + "\"result\":" + "\"" + hm.get(randomInt).trim()+ "\"" + "\n}\n");
        }

        // Método para escrever uma fortune no servidor
        public void write(BufferedReader br, String args, HashMap<Integer, String> hm) {
             try (BufferedWriter escrita = new BufferedWriter(new FileWriter(path.toString(), true))){
                        //Adiciona uma nova Fortune no HashMap com a prox chave
                        int novaChave = hm.size() + 1;
                        if(args.length() != 0){
                            hm.put(novaChave, args);
                        //Escreve a nova fortune no arq com "%" separando
                            escrita.write(args);
                            escrita.newLine();
                            escrita.write("%");
                            escrita.newLine();
                            saida.writeUTF("{\n" + "\"result\":" + "\"" + args.trim() + "\"" + "\n}\n"); 
                        }else{
                            saida.writeUTF(formatError);
                        }        
                    } catch (IOException e){
                        //Notificação que a fortune não foi adicionada
                        System.out.println("Erro ao escrever no arquivo.");
                        e.printStackTrace();
                    }			
		}
         
        // Método que retorna uma substring dos valores encontrados no JSON, dado um índice inicial
        public String valoresCampos(String json, int indexInicial ){
            Boolean valorEncontrado = false;
            int i = indexInicial;
            while(valorEncontrado == false){
                if(json.charAt(i+1) == '\"'){
                    valorEncontrado = true;
                }
                i++;
            }
            int indexFinal = i;
            return json.substring(indexInicial, indexFinal);
        }
        
        
	public void iniciar() {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Servidor iniciado na porta: " + porta);
		try {

			// Criar porta de recepcao
			server = new ServerSocket(porta);
			socket = server.accept();  //Processo fica bloqueado, ah espera de conexoes

			// Criar os fluxos de entrada e saida
			entrada = new DataInputStream(socket.getInputStream());
			saida = new DataOutputStream(socket.getOutputStream());
                        
                        // Chama um método para contar a quantidade de "fortunes" disponíveis
                        NUM_FORTUNES = countFortunes();
                        // Cria um HashMap para armazenar as "fortunes" e preenche o HashMap através do método 'parser'
                        HashMap hm = new HashMap<Integer, String>();
                        parser(hm);
			//Recebe uma mensagem do cliente, no formato JSON, e a armazena na string 'json'
			String json = entrada.readUTF();
                        
                        System.out.println(json);
                        
                        // Definindo campos específicos que serão procurados no JSON (metodo e args)
                        String metodoCampo = "\"method\"";
                        String argsCampo = "\"args\"";
                        String args = "";
                        
                        // Localiza o índice inicial e final do campo "method" e "args" no JSON
                        int indexInicialMetodo = json.indexOf(metodoCampo);
                        int indexFinalMetodo = indexInicialMetodo + metodoCampo.length() + 2;
                        int indexInicialArgs = json.indexOf(argsCampo);
                        int indexFinalArgs = indexInicialArgs + argsCampo.length()+ 3;   
                        
                        // Extrai o valor do campo "method" do JSON
                        String metodo = valoresCampos(json, indexFinalMetodo);
                        if(metodo.equals("write")){
                            args = valoresCampos(json, indexFinalArgs);
                        }
                        // Se o método for "read", chama o método 'read' passando o HashMap
                        if(metodo.equals("read")){
                            read(hm);
                        // Se o método for "write", chama o método 'write' passando o BufferedReader, os argumentos e o HashMap
                        }else if(metodo.equals("write")){
                            write(br, args, hm);
                        }else{
                            saida.writeUTF(formatError);
                        }
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		new Servidor().iniciar();

	}

}
