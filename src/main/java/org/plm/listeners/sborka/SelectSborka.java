package org.plm.listeners.sborka;

import java.sql.SQLException;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.plm.interfaces.SborkaDao;
import org.plm.objects.KolView;

public class SelectSborka implements Listener {

	private Table table_Sborka;
	private Table table_SborkaDetal;
	private SborkaDao sqLiteDAO;

	private Text text_editSborka;
	private Text text_toEditSborka;
	private Text text_idSborka;
	private Text text_deleteSborka;
	private Text text_addSborkaKol;
	private Text text_addKolKol;
	private Text text_addIdKol;

	private int last = 0;

	public SelectSborka(Table table_Sborka, Table table_SborkaDetal, SborkaDao sqLiteDAO, Text text_editSborka,
			Text text_toEditSborka, Text text_idSborka, Text text_deleteSborka, Text text_addSborkaKol,
			Text text_addKolKol, Text text_addIdKol) {
		this.table_Sborka = table_Sborka;
		this.table_SborkaDetal = table_SborkaDetal;
		this.sqLiteDAO = sqLiteDAO;
		this.text_editSborka = text_editSborka;
		this.text_toEditSborka = text_toEditSborka;
		this.text_idSborka = text_idSborka;
		this.text_deleteSborka = text_deleteSborka;
		this.text_addSborkaKol = text_addSborkaKol;
		this.text_addKolKol = text_addKolKol;
		this.text_addIdKol = text_addIdKol;
	}

	@Override
	public void handleEvent(Event event) {
		int id_sborka = 0;
		Point pt = new Point(event.x, event.y);
		TableItem item = table_Sborka.getItem(pt);
		if (item != null) {
			id_sborka = Integer.parseInt(item.getText(0));
			text_editSborka.setText(item.getText(1));
			text_toEditSborka.setText(item.getText(1));
			text_idSborka.setText(item.getText(0));
			text_deleteSborka.setText(item.getText(1));
			text_addSborkaKol.setText(item.getText(1));
			text_addKolKol.setText("");
			text_addIdKol.setText("");
		}
		if (last == id_sborka) {
			return;
		}
		last = id_sborka;
		table_SborkaDetal.removeAll();
		List<KolView> kolViews = null;
		try {
			kolViews = sqLiteDAO.selectDetaliSborka(id_sborka);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (kolViews != null && !kolViews.isEmpty()) {
			for (KolView kolView : kolViews) {
				TableItem tableItem_1 = new TableItem(table_SborkaDetal, SWT.NONE);
				tableItem_1.setText(0, Integer.toString(kolView.getId()));
				tableItem_1.setText(1, kolView.getDetal_name());
				tableItem_1.setText(2, Integer.toString(kolView.getKol()));
			}
		}
	}
}