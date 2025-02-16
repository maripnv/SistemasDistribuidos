package br.edu.utfpr.trabalho_sd.ejb;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Título do Arquivo: ReservaService.java
 * 
 * Descrição: A interface ReservaService define os métodos para reservar,
 * cancelar e listar reservas de livros. Esta interface será usada para 
 * comunicação via RMI, permitindo que as operações sejam executadas remotamente.
 * 
 * Autora: Mariana Pedroso Naves
 * RA: 2320720
 * Data: 10/02/2025
 */

// A interface ReservaService estende Remote para possibilitar chamadas remotas
public interface ReservaService extends Remote {
    // Método para reservar um livro
    boolean reservarLivro(int idLivro, int idUsuario) throws RemoteException; 
    // Método para cancelar a reserva
    boolean cancelarReserva(int idLivro, int idUsuario) throws RemoteException;
    // Método para listar todas as reservas. 
    List<String> listarReservas() throws RemoteException; 
}
