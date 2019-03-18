package org.plm.impls;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.plm.interfaces.SborkaDao;
import org.plm.objects.Detal;
import org.plm.objects.Kol;
import org.plm.objects.KolView;
import org.plm.objects.Sborka;

public class SQLiteDAOOptimize implements SborkaDao {

	private static Connection connection;

	private static LinkedList<Detal> detals;
	private static LinkedList<Sborka> sborkas;
	private static HashMap<Integer, LinkedList<KolView>> detaliSborkas = new HashMap<>();
	private static int maxKol;

	private static PreparedStatement stSelectDetals;
	private static PreparedStatement stInsertDetal;
	private static PreparedStatement stUpdateDetal;
	private static PreparedStatement stDeleteDetal;

	private static PreparedStatement stSelectSborkas;
	private static PreparedStatement stInsertSborka;
	private static PreparedStatement stUpdateSborka;
	private static PreparedStatement stDeleteSborka;

	private static PreparedStatement strSelectDetaliSborkas;
	private static PreparedStatement stInsertDetaliSborki;
	private static PreparedStatement stUpdateDetaliSborki;
	private static PreparedStatement stDeleteDetaliSborki;
	private static PreparedStatement stDeleteDetaliSborkiByIdDetali;
	private static PreparedStatement stDeleteDetaliSborkiByIdSborka;

	static {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:db/PLM.db", "", "");

			stSelectDetals = connection.prepareStatement("select * from detal");
			stInsertDetal = connection.prepareStatement("insert into detal (id, name) values (?,?)");
			stUpdateDetal = connection.prepareStatement("update detal set name = ? where id = ?");
			stDeleteDetal = connection.prepareStatement("delete from detal where id = ?");

			stSelectSborkas = connection.prepareStatement("select * from sborka");
			stInsertSborka = connection.prepareStatement("insert into sborka (id, name) values (?,?)");
			stUpdateSborka = connection.prepareStatement("update sborka set name = ? where id = ?");
			stDeleteSborka = connection.prepareStatement("delete from sborka where id = ?");

			strSelectDetaliSborkas = connection.prepareStatement(
					"select kol.id, kol.id_detal, detal.name, kol.kol  from kol, detal where kol.id_detal = detal.id and kol.id_sborka = ?");
			stInsertDetaliSborki = connection
					.prepareStatement("insert into kol(id, id_sborka, id_detal, kol) values(?,?,?,?)");
			stUpdateDetaliSborki = connection.prepareStatement("update kol set kol = ? where id = ?");
			stDeleteDetaliSborki = connection.prepareStatement("delete from kol where id = ?");
			stDeleteDetaliSborkiByIdDetali = connection.prepareStatement("delete from kol where id_detal = ?");
			stDeleteDetaliSborkiByIdSborka = connection.prepareStatement("delete from kol where id_sborka = ?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public LinkedList<KolView> selectDetaliSborka(int id_sborka) throws SQLException {
		if (detaliSborkas.get(id_sborka) != null) {
			return detaliSborkas.get(id_sborka);
		} else {
			strSelectDetaliSborkas.setInt(1, id_sborka);
			ResultSet result = strSelectDetaliSborkas.executeQuery();
			LinkedList<KolView> kolViews = new LinkedList<>();
			while (result.next()) {
				KolView kolView = new KolView(result.getInt(1), result.getInt(2), result.getString(3),
						result.getInt(4));
				kolViews.add(kolView);
			}
			result.close();
			detaliSborkas.put(id_sborka, kolViews);
			return kolViews;
		}
	}

	@Override
	public int insertDetaliSborka(Kol kol) throws SQLException {
		stInsertDetaliSborki.setInt(1, kol.getId());
		stInsertDetaliSborki.setInt(2, kol.getId_sborka());
		stInsertDetaliSborki.setInt(3, kol.getId_detal());
		stInsertDetaliSborki.setInt(4, kol.getKol());
		if (stInsertDetaliSborki.executeUpdate() == 1) {
			String detal_name = null;
			for (Detal dt : detals) {
				if (dt.getId() == kol.getId_detal()) {
					detal_name = dt.getName();
					break;
				}
			}
			KolView kolView = new KolView(kol.getId(), kol.getId_detal(), detal_name, kol.getKol());
			detaliSborkas.get(kol.getId_sborka()).add(kolView);
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public int findNextIdKol() throws SQLException {
		if (maxKol == 0) {
			Statement st = connection.createStatement();
			maxKol = st.executeQuery("select max(id) from kol").getInt(1);
			st.close();
		}
		return ++maxKol;
	}

	@Override
	public Kol findKol(int id_sborka, int id_detal) {
		for (KolView kolView : detaliSborkas.get(id_sborka)) {
			if (kolView.getId_detal() == id_detal) {
				return new Kol(kolView.getId(), id_sborka);
			}
		}
		return null;
	}

	@Override
	public KolView findKolView(int id_sborka, int id_kol) {
		for (KolView kolView : detaliSborkas.get(id_sborka)) {
			if (kolView.getId() == id_kol) {
				return kolView;
			}
		}
		return null;
	}

	@Override
	public int updateDetaliSborka(Kol kol) throws SQLException {
		stUpdateDetaliSborki.setInt(1, kol.getKol());
		stUpdateDetaliSborki.setInt(2, kol.getId());
		if (stUpdateDetaliSborki.executeUpdate() == 1) {
			findKolView(kol.getId_sborka(), kol.getId()).setKol(kol.getKol());
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public int deleteDetaliSborka(Kol kol) throws SQLException {
		stDeleteDetaliSborki.setInt(1, kol.getId());
		if (stDeleteDetaliSborki.executeUpdate() == 1) {
			detaliSborkas.get(kol.getId_sborka()).remove(findKolView(kol.getId_sborka(), kol.getId()));
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public LinkedList<Detal> selectDetals() throws SQLException {
		if (detals != null) {
			return detals;
		}
		detals = new LinkedList<Detal>();
		ResultSet result = stSelectDetals.executeQuery();
		while (result.next()) {
			Detal detal = new Detal(result.getInt(1), result.getString(2));
			detals.add(detal);
		}
		result.close();
		return detals;
	}

	@Override
	public int findIdDetal(String name) {
		for (Detal dt : detals) {
			if (dt.getName().equals(name)) {
				return dt.getId();
			}
		}
		return 0;
	}

	@Override
	public int insertDetal(Detal detal) throws SQLException {
		if (findIdDetal(detal.getName()) != 0) {
			return 0;
		}
		detal.setId(detals.peekLast().getId() + 1);
		stInsertDetal.setInt(1, detal.getId());
		stInsertDetal.setString(2, detal.getName());
		if (stInsertDetal.executeUpdate() == 1) {
			detals.add(detal);
		} else {
			return -1;
		}
		return detal.getId();
	}

	@Override
	public int updateDetal(Detal detal) throws SQLException {
		if (findIdDetal(detal.getName()) != 0) {
			return 0;
		}
		stUpdateDetal.setString(1, detal.getName());
		stUpdateDetal.setInt(2, detal.getId());
		if (stUpdateDetal.executeUpdate() == 1) {
			for (Detal dt : detals) {
				if (dt.getId() == detal.getId()) {
					dt.setName(detal.getName());
					break;
				}
			}
			for (Map.Entry<Integer, LinkedList<KolView>> kolViews : detaliSborkas.entrySet()) {
				for (KolView kolView : kolViews.getValue()) {
					if (kolView.getId_detal() == detal.getId()) {
						kolView.setDetal_name(detal.getName());
						break;
					}
				}
			}
		} else {
			return -1;
		}
		return 1;
	}

	@Override
	public int deleteDetal(int id) throws SQLException {
		stDeleteDetal.setInt(1, id);
		stDeleteDetaliSborkiByIdDetali.setInt(1, id);
		if (stDeleteDetal.executeUpdate() == 1) {
			stDeleteDetaliSborkiByIdDetali.executeUpdate();
			for (Detal dt : detals) {
				if (dt.getId() == id) {
					detals.remove(dt);
					break;
				}
			}
			for (Map.Entry<Integer, LinkedList<KolView>> kolViews : detaliSborkas.entrySet()) {
				LinkedList<KolView> listKolView = kolViews.getValue();
				for (KolView kolView : listKolView) {
					if (kolView.getId_detal() == id) {
						listKolView.remove(kolView);
						break;
					}
				}
			}
		} else {
			return -1;
		}
		return 1;
	}

	@Override
	public LinkedList<Sborka> selectSborkas() throws SQLException {
		if (sborkas != null) {
			return sborkas;
		}
		sborkas = new LinkedList<Sborka>();
		ResultSet result = stSelectSborkas.executeQuery();
		while (result.next()) {
			Sborka sborka = new Sborka(result.getInt(1), result.getString(2));
			sborkas.add(sborka);
		}
		result.close();
		return sborkas;
	}

	@Override
	public int findIdSborka(String name) {
		for (Sborka sb : sborkas) {
			if (sb.getName().equals(name)) {
				return sb.getId();
			}
		}
		return 0;
	}

	@Override
	public int insertSborka(Sborka sborka) throws SQLException {
		if (findIdSborka(sborka.getName()) != 0) {
			return 0;
		}
		sborka.setId(sborkas.peekLast().getId() + 1);
		stInsertSborka.setInt(1, sborka.getId());
		stInsertSborka.setString(2, sborka.getName());
		if (stInsertSborka.executeUpdate() == 1) {
			sborkas.add(sborka);
			detaliSborkas.put(sborka.getId(), new LinkedList<KolView>());
		} else {
			return -1;
		}
		return sborka.getId();
	}

	@Override
	public int updateSborka(Sborka sborka) throws SQLException {
		if (findIdSborka(sborka.getName()) != 0) {
			return 0;
		}
		stUpdateSborka.setString(1, sborka.getName());
		stUpdateSborka.setInt(2, sborka.getId());
		if (stUpdateSborka.executeUpdate() == 1) {
			for (Sborka sb : sborkas) {
				if (sb.getId() == sborka.getId()) {
					sb.setName(sborka.getName());
					break;
				}
			}
		} else {
			return -1;
		}
		return 1;
	}

	@Override
	public int deleteSborka(int id) throws SQLException {
		stDeleteSborka.setInt(1, id);
		if (stDeleteSborka.executeUpdate() == 1) {
			stDeleteDetaliSborkiByIdSborka.setInt(1, id);
			stDeleteDetaliSborkiByIdSborka.executeUpdate();
			for (Sborka sb : sborkas) {
				if (sb.getId() == id) {
					sborkas.remove(sb);
					break;
				}
			}
			detaliSborkas.remove(id);
		} else {
			return -1;
		}
		return 1;
	}

	@Override
	public void closeConnection() {
		try {
			// close PreparedStatements
			stSelectDetals.close();
			stInsertDetal.close();
			stUpdateDetal.close();
			stDeleteDetal.close();
			stSelectSborkas.close();
			stInsertSborka.close();
			stUpdateSborka.close();
			stDeleteSborka.close();
			strSelectDetaliSborkas.close();
			stInsertDetaliSborki.close();
			stUpdateDetaliSborki.close();
			stDeleteDetaliSborki.close();
			stDeleteDetaliSborkiByIdDetali.close();
			stDeleteDetaliSborkiByIdSborka.close();
			
			// close connection
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}