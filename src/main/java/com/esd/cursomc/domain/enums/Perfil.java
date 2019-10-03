package com.esd.cursomc.domain.enums;

public enum Perfil {
	//O prefixo "ROLE_" é exigência do framework
	ADMIN(1, "ROLE_ADMIN"),
	CLIENTE(2, "ROLE_CLIENTE");	
	
	private int cod;
	private String descricao;
	
	private Perfil(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	//enum não pode ser modificado, por isso só tem o get
		
	public int getCod() {
		return cod;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static Perfil toEnum(Integer cod) {		
		if(cod == null) {
			return null;
		}
		
		for(Perfil x : Perfil.values() ) {
			if(cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
}
