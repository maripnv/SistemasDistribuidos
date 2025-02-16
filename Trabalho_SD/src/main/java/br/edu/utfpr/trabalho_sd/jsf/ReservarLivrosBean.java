package br.edu.utfpr.trabalho_sd.jsf;

import br.edu.utfpr.trabalho_sd.ejb.Tranca;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import java.util.Random;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

/**
 * Título do Arquivo: ReservarLivrosBean.java
 * 
 * Descrição: Bean gerenciado pelo JSF que controla o processo de reserva de 
 * livros. Ele garante que um usuário pode reservar um livro por vez e gera um 
 * código único para a reserva.
 * 
 * Autora: Mariana Pedroso Naves
 * RA: 2320720
 * Data: 12/02/2025
 */

@Named // Disponibiliza o bean no contexto JSF
@RequestScoped // O bean será recriado a cada requisição
public class ReservarLivrosBean {

    private int idLivro;
    private int idUsuario;
    private Integer codigoReserva; // Código gerado após a reserva.

    @Inject
    private ConsultarLivro consultarLivro; // Bean que consulta os livros cadastrados

    @Inject
    private Tranca trancaReservas; // Controle de reserva para evitar multiplas reservas pelo mesmo usuário

    
    // Método que realiza a reserva de um livro.
    public void reservarLivro() {
        FacesContext context = FacesContext.getCurrentInstance();

        // Verifica se o livro existe
        if (!consultarLivro.getLivros().containsKey(idLivro)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro, livro não encontrado!", "Livro não encontrado!"));
            return;
        }

        // Verifica se o usuário já possui um livro reservado
        if (trancaReservas.usuarioPossuiReserva(idUsuario)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro, você já possui um livro reservado!", "Não é possível reservar outro livro."));
            return;
        }

        // Tenta reservar o livro usando a tranca - controle de concorrência
        boolean sucesso = trancaReservas.reservarLivro(idLivro, idUsuario);
        if (sucesso) {
            // Gera um código aleatório de 6 dígitos para a reserva
            int codigoUsuario = new Random().nextInt(900000) + 100000;
            this.codigoReserva = codigoUsuario;
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso! Reserva concluída!", "Reserva concluída!"));
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aguarde, livro já foi reservado!", "Usuário na fila de espera."));
        }
    }
    
    // Cancela a reserva
    public void cancelarReserva() {
        FacesContext context = FacesContext.getCurrentInstance();

        boolean sucesso = trancaReservas.cancelarReserva(idLivro, idUsuario);
        if (sucesso) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reserva cancelada com sucesso!", "A reserva foi removida."));
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cancelar reserva!", "Somente o usuário que fez a reserva pode cancelá-la!"));
        }
    }

    // Getters e setters
    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(Integer codigoReserva) {
        this.codigoReserva = codigoReserva;
    }
}
