package it.polito.tdp.crimes.model;

public class Collegamento {

	private String tipo1;
	private String tipo2;
	private Integer peso;
	
	
	public Collegamento(String tipo1, String tipo2, Integer peso) {
		super();
		this.tipo1 = tipo1;
		this.tipo2 = tipo2;
		this.peso = peso;
	}
	
	
	public String getTipo1() {
		return tipo1;
	}
	public void setTipo1(String tipo1) {
		this.tipo1 = tipo1;
	}
	public String getTipo2() {
		return tipo2;
	}
	public void setTipo2(String tipo2) {
		this.tipo2 = tipo2;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}


	@Override
	public String toString() {
		return  tipo1 + " - " + tipo2 ;
	}
	
	
}
