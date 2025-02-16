package br.edu.utfpr.trabalho_sd.jsf;

/**
 * Título do Arquivo: Livro.java
 * 
 * Descrição: A classe Livro representa os livros do projeto, incluindo um
 * identificador único (idLivro), título, autor e status de disponibilidade. 
 * 
 * Autora: Mariana Pedroso Naves
 * RA: 2320720
 * Data: 08/02/2025
 */

public class Livro {
    
    // Atributos
    private int idLivro;
    private String titulo;
    private String autor;
    private String status;
    
    // Construtor padrão
    public Livro() {
    }
    
    // Construtor
    public Livro(int idLivro, String titulo, String autor) {
        this.idLivro = idLivro;
        this.titulo = titulo;
        this.autor = autor;
        this.status = "Disponível"; // Assume que está disponível - por padrão.
    }
    
    // *** Getters e Setters ***

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
