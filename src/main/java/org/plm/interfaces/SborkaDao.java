package org.plm.interfaces;

import java.sql.SQLException;
import java.util.List;
import org.plm.objects.Detal;
import org.plm.objects.Kol;
import org.plm.objects.KolView;
import org.plm.objects.Sborka;

public interface SborkaDao {

	public List<Detal> selectDetals() throws SQLException;

	public int insertDetal(Detal detal) throws SQLException;

	public int updateDetal(Detal detal) throws SQLException;

	public int deleteDetal(int id) throws SQLException;

	public int findIdDetal(String name);

	
	public List<Sborka> selectSborkas() throws SQLException;

	public int insertSborka(Sborka sborka) throws SQLException;

	public int updateSborka(Sborka sborka) throws SQLException;

	public int deleteSborka(int id) throws SQLException;

	public int findIdSborka(String name);
	

	public List<KolView> selectDetaliSborka(int id_sborka) throws SQLException;

	public int insertDetaliSborka(Kol kol) throws SQLException;

	public int updateDetaliSborka(Kol kol) throws SQLException;

	public int deleteDetaliSborka(Kol kol) throws SQLException;
	
	public KolView findKolView(int id_sborka, int id_kol);
	
	public Kol findKol(int id_sborka, int id_detal);
	
	public int findNextIdKol() throws SQLException;
	

	public void closeConnection();
}