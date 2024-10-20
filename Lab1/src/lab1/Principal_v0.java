package lab1;

/**
 * Lab1: Leitura de Base de Dados Nao-Distribuida
 * 
 * Autor: Lucio A. Rocha
 * Estudantes: Mariana Pedroso Naves (RA:2320720) e Andrei Fernandes Zani (RA: 2367831)
 * Ultima atualizacao: 20/10/2024
 * 
 * Referencias: 
 * https://docs.oracle.com/javase/tutorial/essential/io
 * 
 */

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Principal_v0 {

	public final static Path path = Paths			
			.get("C:\\Users\\marip\\OneDrive\\Documentos\\NetBeansProjects\\Lab1\\src\\lab1\\fortune-br.txt");
	private int NUM_FORTUNES = 0;

	public class FileReader {

		public int countFortunes() throws FileNotFoundException {

			int lineCount = 0;

			InputStream is = new BufferedInputStream(new FileInputStream(
					path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

				String line = "";
				while (!(line == null)) {

					if (line.equals("%"))
						lineCount++;

					line = br.readLine();

				}// fim while

				System.out.println(lineCount);
			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
			return lineCount;
		}

		public void parser(HashMap<Integer, String> hm)
				throws FileNotFoundException {

			InputStream is = new BufferedInputStream(new FileInputStream(
					path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

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
						// System.out.print(lineCount + ".");
					}

					hm.put(lineCount, fortune.toString());
					System.out.println(fortune.toString());

					System.out.println(lineCount);
				}// fim while

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
		}

		public void read(HashMap<Integer, String> hm)
				throws FileNotFoundException {
                    
                    //Seta o range de número aleatório para ser um inteiro de 0 ao numero total de fortunes
                    int randomInt = (int)(Math.random() * NUM_FORTUNES);
                    
                    //Mostra a fortuna aleatória
                    System.out.println("Fortune numero "+ randomInt + ": " + hm.get(randomInt));
                    
		}

		public void write(HashMap<Integer, String> hm)
				throws FileNotFoundException {
                    
                    //Abrir o arquivo para escrita (sem sobrescrever)
                    try (BufferedWriter escrita = new BufferedWriter(new FileWriter(path.toString(), true))){
                    
                        //Escrevendo uma nova "fortune"
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("Digite a nova fortune: ");
                        String novaFortune = scanner.nextLine();
                        
                        //Adiciona uma nova Fortune no HashMap com a prox chave
                        int novaChave = hm.size() + 1;
                        hm.put(novaChave, novaFortune);
                        
                        //Escreve a nova "fortune" no arq com "%" separando
                        escrita.write(novaFortune);
                        escrita.newLine();
                        escrita.write("%");
                        escrita.newLine();
                        
                        //Notificação que a fortune foi adicionada com sucesso 
                        System.out.println("Nova fortune adiciona com sucesso!");
                    } catch (IOException e){
                        //Notificação que a fortune não foi adicionada
                        System.out.println("Erro ao escrever no arquivo.");
                        e.printStackTrace();
                    }			
		}
	}

	public void iniciar() {

		FileReader fr = new FileReader();
		try {
			NUM_FORTUNES = fr.countFortunes();
			HashMap hm = new HashMap<Integer, String>();
			fr.parser(hm);
			fr.read(hm);
			fr.write(hm);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Principal_v0().iniciar();
	}

}
