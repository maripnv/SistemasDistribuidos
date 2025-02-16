package br.edu.utfpr.trabalho_sd.jsf;

import br.edu.utfpr.trabalho_sd.ejb.Reserva;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Título do Arquivo: ConsultarLivro.java
 * JSF
 * Descrição: A classe ConsultarLivro declara os livros cadastrados e
 * listar cada um deles, mostrando também a sua disponibilidade.
 * 
 * 
 * Autora: Mariana Pedroso Naves
 * RA: 2320720
 * Data: 12/02/2025
 */

@Named("consultarLivro") // Para que possa ser injetado corretamente
@ApplicationScoped // Mantém os livros cadastrados ao longo da aplicação
public class ConsultarLivro {

    // Map de livros, onde a chave é o ID do livro e o valor é o objeto Livro
    private final Map<Integer, Livro> livros = new HashMap<>();
    
    @Inject
    private Reserva reserva; // Injeção da dependência Reserva para verificar o status de reserva de cada livro

    public ConsultarLivro() {
        // Livros cadastrados no sistema
        livros.put(1, new Livro(1, "Romeu E Julieta", "William Shakespeare"));
        livros.put(2, new Livro(2, "Dom Casmurro", "Machado de Assis"));
        livros.put(3, new Livro(3, "1984", "George Orwell"));
    }

    // Retorna todos os livros com status de reserva
    public List<String> listarTodosOsLivros() {
        return livros.values().stream()
                .map(livro -> {
                    String status = reserva.isReservado(livro.getIdLivro()) ? "Reservado" : "Disponível";
                    return "ID: " + livro.getIdLivro() + " | Título: " + livro.getTitulo() + " | Status: " + status;
                })
                .collect(Collectors.toList());
    }

    // Retorna o HashMap de livros
    public Map<Integer, Livro> getLivros() {
        return livros;
    }

    // Verifica se o livro está reservado
    public boolean isLivroReservado(int idLivro) {
        return reserva.isReservado(idLivro);
    }
}
