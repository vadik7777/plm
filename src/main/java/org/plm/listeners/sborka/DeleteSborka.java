package org.plm.listeners.sborka;

import java.sql.SQLException;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.plm.interfaces.SborkaDao;

public class DeleteSborka implements Listener {
	private Text text_toEditSborka;
	private Text text_idSborka;
	private Text text_editSborka;
	private Text text_deleteSborka;
	private Text text_addSborkaKol;
	private Text text_addKolKol;
	private Text text_addIdKol;
	private Label label_removeSborka;
	private Table table_Sborka;
	private Table table_SborkaDetal;
	private SborkaDao sqLiteDAO;

	public DeleteSborka(Text text_toEditSborka, Text text_idSborka, Text text_editSborka, Text text_deleteSborka,
			Text text_addSborkaKol, Text text_addKolKol, Text text_addIdKol, Label label_removeSborka,
			Table table_Sborka, Table table_SborkaDetal, SborkaDao sqLiteDAO) {
		this.text_toEditSborka = text_toEditSborka;
		this.text_idSborka = text_idSborka;
		this.text_editSborka = text_editSborka;
		this.text_deleteSborka = text_deleteSborka;
		this.text_addSborkaKol = text_addSborkaKol;
		this.text_addKolKol = text_addKolKol;
		this.text_addIdKol = text_addIdKol;
		this.label_removeSborka = label_removeSborka;
		this.table_Sborka = table_Sborka;
		this.table_SborkaDetal = table_SborkaDetal;
		this.sqLiteDAO = sqLiteDAO;
	}

	@Override
	public void handleEvent(Event event) {
		if (!text_idSborka.getText().equals("")) {
			int k = 0;
			int id = Integer.parseInt(text_idSborka.getText());
			try {
				k = sqLiteDAO.deleteSborka(id);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (k == 0) {
				label_removeSborka.setText("Запись не найдена!");
			} else if (k == -1) {
				label_removeSborka.setText("Ошибка удаления!");
			} else {
				for (int i = 0; i < table_Sborka.getItemCount(); i++) {
					if (Integer.parseInt(table_Sborka.getItem(i).getText(0)) == id) {
						table_Sborka.remove(i);
						break;
					}
				}
				table_SborkaDetal.removeAll();
				text_toEditSborka.setText("");
				text_idSborka.setText("");
				text_editSborka.setText("");
				text_deleteSborka.setText("");
				text_addSborkaKol.setText("");
				text_addKolKol.setText("");
				text_addIdKol.setText("");
				label_removeSborka.setText("Запись удалена!");
			}
		} else {
			label_removeSborka.setText("Заполните поле!");
		}
	}
}