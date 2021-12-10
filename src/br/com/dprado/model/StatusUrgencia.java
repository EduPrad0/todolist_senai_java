package br.com.dprado.model;

public enum StatusUrgencia {
	LEVE("Leve"), MEDIA("Preocupante"), URGENTE("Urgente");
	
	String descricao;
	
	private StatusUrgencia(String d) {
		this.descricao = d;
	}
	@Override
	public String toString() {
		return descricao;
	}
}
