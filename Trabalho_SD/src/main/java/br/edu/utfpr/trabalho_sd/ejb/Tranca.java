    package br.edu.utfpr.trabalho_sd.ejb;

import jakarta.ejb.Singleton;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Título do Arquivo: Tranca.java
 * 
 * Descrição: Classe responsável pelo mecanismo de tranca para controle de 
 * concorrência, controle de reservas dos livros. Garante que um usuário pode 
 * reservar apenas um livro por vez e gerencia a fila de espera.
 * 
 * 
 * Autora: Mariana Pedroso Naves
 * RA: 2320720
 * Data: 10/02/2025
 */

@Singleton // EJB Singleton: mantém apenas uma instância dessa classe em toda a aplicação
public class Tranca {
    // Mapa que associa ID do livro ao ID do usuário que reservou
    private final Map<Integer, Integer> reservas = new HashMap<>(); // Livro ID → Usuário ID
    // Mapa que associa ID do usuário ao ID do livro que ele reservou
    private final Map<Integer, Integer> usuarioReservas = new HashMap<>(); // Usuário ID → Livro ID
    // Mapa que mantém filas de espera para cada livro (Lista de IDs de usuários esperando)
    private final Map<Integer, Queue<Integer>> filaEspera = new HashMap<>(); // Livro ID → Fila de espera (usuários)

    // Método sincronizado que tenta reservar um livro para um usuário.
    // Se o livro já estiver reservado, o usuário entra na fila de espera.
    public synchronized boolean reservarLivro(int idLivro, int idUsuario) {
        // Verifica se o usuário já tem um livro reservado
        if (usuarioReservas.containsKey(idUsuario)) {
            System.out.println("ERRO: Usuario " + idUsuario + " ja possui um livro reservado.");
            return false;
        }

        // Se o livro já está reservado, adiciona o usuário à fila de espera
        if (reservas.containsKey(idLivro)) {
            filaEspera.putIfAbsent(idLivro, new LinkedList<>()); // Garante que a fila existe
            filaEspera.get(idLivro).offer(idUsuario);
            System.out.println("FILA: Usuario " + idUsuario + " entrou na fila de espera para o livro " + idLivro);
            return false;
        }

        // Reserva o livro
        reservas.put(idLivro, idUsuario);
        usuarioReservas.put(idUsuario, idLivro);
        System.out.println("RESERVA: Livro " + idLivro + " reservado pelo usuario " + idUsuario);
        return true;
    }

    public synchronized boolean cancelarReserva(int idLivro, int idUsuario) {
        // Verifica se o usuário tem esse livro reservado
        if (!reservas.containsKey(idLivro) || !reservas.get(idLivro).equals(idUsuario)) {
            System.out.println("ERRO: Livro nao reservado ou apenas o usuario que reservou pode cancelar.");
            return false;
        }

        // Remove a reserva do usuário
        reservas.remove(idLivro);
        usuarioReservas.remove(idUsuario);
        System.out.println("CANCELAMENTO: Reserva do livro " + idLivro + " cancelada pelo usuario " + idUsuario);

        // Se houver fila de espera, atribuir a reserva ao próximo usuário elegível
        Queue<Integer> fila = filaEspera.get(idLivro);
        while (fila != null && !fila.isEmpty()) {
            int proximoUsuario = fila.poll(); // Pega o primeiro da fila
            
            // Verifica se o usuário ainda é elegível para reserva
            if (!usuarioReservas.containsKey(proximoUsuario)) {
                reservas.put(idLivro, proximoUsuario);
                usuarioReservas.put(proximoUsuario, idLivro);
                System.out.println("LIBERADO: Livro " + idLivro + " agora reservado para o usuario " + proximoUsuario);
                return true;
            } else {
                System.out.println("REMOVIDO: Usuario " + proximoUsuario + " foi retirado da fila de espera pois ja reservou outro livro.");
            }
        }
        return true;
    }
    
  
    // Método sincronizado para verificar se um livro está reservado.
    public synchronized boolean isReservado(int idLivro) {
        return reservas.containsKey(idLivro);
    }
    
    // Método sincronizado para verificar se um usuário já possui uma reserva ativa.
    public synchronized boolean usuarioPossuiReserva(int idUsuario) {
        return usuarioReservas.containsKey(idUsuario);
    }
}
