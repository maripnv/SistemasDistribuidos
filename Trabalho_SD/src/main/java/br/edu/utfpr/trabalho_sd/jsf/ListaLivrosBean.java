package br.edu.utfpr.trabalho_sd.jsf;

import br.edu.utfpr.trabalho_sd.ejb.Reserva;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Título do Arquivo: ListaLivrosBean.java
 * 
 * Descrição: A classe ListaLivrosBean fornece uma lista de livros com o status 
 * atualizado de "Reservado" ou "Disponível". A classe interage com as classes 
 * ConsultarLivro e Reserva para obter informações sobre os livros.
 * 
 * Autora: Mariana Pedroso Naves
 * RA: 2320720
 * Data: 12/02/2025
 */


@Named("listaLivrosBean") // Torna o bean acessível no XHTML, permitindo a injeção e acesso no JSF
public class ListaLivrosBean {
    
    // Injeção da dependência ConsultarLivro e Reserva para obter a lista e 
    // verificar o status de reserva dos livros.
    @Inject
    private ConsultarLivro consultarLivro;

    @Inject
    private Reserva reserva;

    // Retorna a lista de livros com status atualizado
    public List<Livro> getLivros() {
        // .peek: definir o status de reserva de livro
        return consultarLivro.getLivros().values().stream()
                .peek(livro -> livro.setStatus(reserva.isReservado(livro.getIdLivro()) ? "Reservado" : "Disponível"))
                .collect(Collectors.toList());
    }
}
