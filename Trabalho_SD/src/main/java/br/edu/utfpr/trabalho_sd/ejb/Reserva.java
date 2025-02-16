package br.edu.utfpr.trabalho_sd.ejb;

import jakarta.ejb.Singleton;
import jakarta.inject.Inject; 

/**
 * Título do Arquivo: Reserva.java
 * 
 * Descrição: A classe Reserva é responsável por: Realizar a reserva de um livro;
 * Cancelar a reserva de um livro e verificar se o livro foi reservado. Além 
 * disso, essa classe interage com a classe Tranca para gerenciar o bloqueio 
 * das reservas.
 * 
 * Autora: Mariana Pedroso Naves
 * RA: 2320720
 * Data: 10/02/2025
 */

@Singleton // Garante uma única instância durante o ciclo de vida da aplicação
public class Reserva {

    @Inject // Injeção de dependências
    private Tranca trancaReservas; 

    // Método para reservar um livro
    public synchronized boolean reservarLivro(int idLivro, int idLeitor) {
        trancaReservas.reservarLivro(idLivro, idLeitor);
        return true;
    }

    // Método para cancelar uma reserva
    public synchronized boolean cancelarReserva(int idLivro, int idLeitor) {
        trancaReservas.cancelarReserva(idLivro, idLeitor);
        return true;
    }

    // Verifica se o livro está reservado
    public synchronized boolean isReservado(int idLivro) {
        return trancaReservas.isReservado(idLivro);
    }
}
