package org.plm.objects;

public class KolView {
	
	private int id;
	private int id_detal;
	private String detal_name;
	private int kol;
	
	public KolView() {}
	
	public KolView(int id, int id_detal, String detal_name, int kol) {
		this.id = id;
		this.id_detal = id_detal;
		this.detal_name = detal_name;
		this.kol = kol;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId_detal() {
		return id_detal;
	}
	
	public void setId_detal(int id_detal) {
		this.id_detal = id_detal;
	}
	
	public String getDetal_name() {
		return detal_name;
	}
	
	public void setDetal_name(String detal_name) {
		this.detal_name = detal_name;
	}
	
	public int getKol() {
		return kol;
	}
	
	public void setKol(int kol) {
		this.kol = kol;
	}
}