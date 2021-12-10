package br.com.dprado.model;

public enum StatusTarefa {
	ABERTA("Aberta"), ADIADA("Adiada"), CONCLUIDA("Conclu�da");
	
	private StatusTarefa(String descricao) {
		this.descricao = descricao;
	}
	
	String descricao;

	@Override
	public String toString() {
		
		
		return descricao;
	}
}
