package br.com.dprado.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Tarefa implements Comparable<Tarefa>{
	private long id;
	private LocalDate dataCriacao;
	private LocalDate dataLimite;
	private LocalDate dataConcluida;
	private String descricao;
	private String comentarios;
	private StatusTarefa status;
	private StatusUrgencia grau;
	
	
	public StatusUrgencia getGrau() {
		return grau;
	}
	public void setGrau(StatusUrgencia grau) {
		this.grau = grau;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public LocalDate getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public LocalDate getDataLimite() {
		return dataLimite;
	}
	public void setDataLimite(LocalDate dataLimite) {
		this.dataLimite = dataLimite;
	}
	public LocalDate getDataConcluida() {
		return dataConcluida;
	}
	public void setDataConcluida(LocalDate dataConcluida) {
		this.dataConcluida = dataConcluida;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	public StatusTarefa getStatus() {
		return status;
	}
	public void setStatus(StatusTarefa status) {
		this.status = status;
	}
	
	
	
	public String formatSave() {
		StringBuilder builder = new StringBuilder();
		DateTimeFormatter formatDataPadrao = DateTimeFormatter.ofPattern("dd/MM/yy");
		
		builder.append(this.getId()+";");
		builder.append(this.getDataCriacao().format(formatDataPadrao)+";");
		builder.append(this.getDataLimite().format(formatDataPadrao)+";");
		
		if(this.getDataConcluida() != null) {
			builder.append(this.getDataConcluida().format(formatDataPadrao));
		}
		builder.append(";");
		builder.append(this.getDescricao()+";");
		builder.append(this.getComentarios()+";");
		if(this.getGrau() != null) {
			builder.append(this.getGrau().ordinal()+";");
		}else {
			builder.append(0+";");
		}
		builder.append(this.getStatus().ordinal()+"\n");
			
		return builder.toString();
	}
	@Override
	public int compareTo(Tarefa o) {
		if(this.getDataLimite().isBefore(o.getDataLimite())) {
			return -1;
		}else if(this.getDataLimite().isAfter(o.getDataLimite())) {
			return 1;
		}
		return this.getDescricao().compareTo(o.getDescricao());
	}
	
	
}
