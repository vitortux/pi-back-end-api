package br.com.codaedorme.backapi.domain.usuario;

import br.com.codaedorme.backapi.domain.usuario.enums.Grupo;
import br.com.codaedorme.backapi.domain.usuario.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "Usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	@Email
	@Column(unique = true)
	private String email;

	@Pattern(regexp = "^\\d{11}$")
	private String cpf;

	@Enumerated(EnumType.STRING)
	private Grupo grupo;

	private String senha;

	@Enumerated(EnumType.STRING)
	private Status status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String toString() {
		return id + " | " + nome + " | " + email +
				" | " + cpf + " | "  + grupo +
				" | " + status;
	}

	public String toString2() {

		return id + " | " + nome + " | " + email +
				" | " + grupo +
				" | " + status;

	}

}
