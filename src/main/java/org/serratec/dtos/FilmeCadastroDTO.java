package org.serratec.dtos;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import org.serratec.models.Filme;

import com.sun.istack.NotNull;

public class FilmeCadastroDTO {

	@NotNull
	@NotBlank
	public String nome;
	
	@NotNull
	public LocalDate diaSessao;
	
	@NotNull
	@NotBlank
	public String classificacao;
	
	public String comentario;
	
	
	public Filme toFilme() {
		Filme filme = new Filme();
		filme.setNome(this.nome);
		filme.setDiaSessao(LocalDate.now());
		filme.setClassificacao(this.classificacao);
		filme.setComentario(this.comentario);
		
		return filme;
	}


	public String getNome() {
		return nome;
	}


	public LocalDate getDiaSessao() {
		return diaSessao;
	}


	public String getClassificacao() {
		return classificacao;
	}


	public String getComentario() {
		return comentario;
	}
	
}
