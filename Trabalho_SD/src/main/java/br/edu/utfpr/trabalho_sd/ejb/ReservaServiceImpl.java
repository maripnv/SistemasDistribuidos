package br.edu.utfpr.trabalho_sd.ejb;

import br.edu.utfpr.trabalho_sd.jsf.ConsultarLivro;
import br.edu.utfpr.trabalho_sd.jsf.Livro;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Título do Arquivo: ReservaServiceImpl.java
 * 
 * Descrição: Classe que implementa a interface ReservaService, permitindo 
 * operações remotas de reserva de livros. Esta classe é um EJB Stateless, 
 *não mantém estado entre chamadas. Também é um serviço RMI, permitindo a 
 * reserva de livros.
 * 
 * Autora: Mariana Pedroso Naves
 * RA: 2320720
 * Data: 12/02/2025
 */

@Stateless
@LocalBean
public class ReservaServiceImpl extends UnicastRemoteObject implements ReservaService {

    @Inject
    private ConsultarLivro consultarLivro;

    @Inject
    private Reserva reserva;

    // Construtor
    public ReservaServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean reservarLivro(int idLivro, int idUsuario) throws RemoteException {
        // Verificando se o livro existe
        Livro livro = consultarLivro.getLivros().get(idLivro);
        if (livro == null) {
            return false; // Livro não encontrado
        }

        // Verificando se o livro está reservado
        if (reserva.isReservado(idLivro)) {
            return false; // Livro reservado
        }

        // Realizando a reserva
        return reserva.reservarLivro(idLivro, idUsuario);
    }

    @Override
    public boolean cancelarReserva(int idLivro, int idUsuario) throws RemoteException {
        // Verificando se o livro foi reservado
        if (!reserva.isReservado(idLivro)) {
            return false; // Livro não está reservado
        }

        // Cancelando a reserva
        return reserva.cancelarReserva(idLivro, idUsuario);
    }

    @Override
    public List<String> listarReservas() throws RemoteException {
        // Listando todos os livros com status de reserva
        /**
         *  entrySet(): retorna um conjunto contendo todas as entradas do mapa (chave e o valor).
         *  stream():  cria um Stream a partir de uma lista.
         *  Stream: sequência de elementos que permite operações funcionais
         *  .map: percorre cada entrada (entry) e transforma os dados em um texto formatado.
         *  entry ->: cria uma string formatada a partir dos elementos (chave + valor)
         *  .collect: junta todas as strings formatadas em uma lista. Coleta o resultado em uma lista.
         */
        return consultarLivro.getLivros().entrySet().stream()
                .map(entry -> {
                    String status = reserva.isReservado(entry.getKey()) ? "Reservado" : "Disponível";
                    return "ID: " + entry.getKey() + " | Título: " + entry.getValue().getTitulo() + " | Status: " + status;
                })
                .collect(Collectors.toList());
    }

    // Inicializando o servidor RMI:
    public static void main(String[] args) {
        try {
            // Criando o registro RMI e vinculando o serviço
            Registry registry = LocateRegistry.createRegistry(1099);
            // ReservaServiceImpl - pode ser acessado remotamente
            registry.rebind("ReservaService", new ReservaServiceImpl());
            System.out.println("Servidor RMI iniciado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
