package org.plm.objects;

public class Kol {
	private int id;
	private int id_sborka;
	private int id_detal;
	private int kol;

	public Kol() {
	}
	
	public Kol(int id, int id_sborka, int id_detal, int kol) {
		this.id = id;
		this.id_sborka = id_sborka;
		this.id_detal = id_detal;
		this.kol = kol;
	}
	
	public Kol(int id, int id_sborka, int kol) {
		this.id = id;
		this.id_sborka = id_sborka;
		this.kol = kol;
	}

	public Kol(int id, int id_sborka) {
		this.id = id;
		this.id_sborka = id_sborka;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_sborka() {
		return id_sborka;
	}

	public void setId_sborka(int id_sborka) {
		this.id_sborka = id_sborka;
	}

	public int getId_detal() {
		return id_detal;
	}

	public void setId_detal(int id_detal) {
		this.id_detal = id_detal;
	}

	public int getKol() {
		return kol;
	}

	public void setKol(int kol) {
		this.kol = kol;
	}
}