package org.plm.listeners.sborka;

import java.sql.SQLException;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.plm.interfaces.SborkaDao;
import org.plm.objects.Sborka;

public class UpdateSborka implements Listener {
	private Text text_toEditSborka;
	private Text text_idSborka;
	private Text text_editSborka;
	private Text text_deleteSborka;
	private Label label_editSborka;
	private Text text_addSborkaKol;
	private Table table_Sborka;
	private SborkaDao sqLiteDAO;

	public UpdateSborka(Text text_toEditSborka, Text text_idSborka, Text text_editSborka, Text text_deleteSborka,
			Label label_editSborka, Text text_addSborkaKol, Table table_Sborka, SborkaDao sqLiteDAO) {
		this.text_toEditSborka = text_toEditSborka;
		this.text_idSborka = text_idSborka;
		this.text_editSborka = text_editSborka;
		this.text_deleteSborka = text_deleteSborka;
		this.label_editSborka = label_editSborka;
		this.text_addSborkaKol = text_addSborkaKol;
		this.table_Sborka = table_Sborka;
		this.sqLiteDAO = sqLiteDAO;
	}

	@Override
	public void handleEvent(Event event) {
		if (text_toEditSborka.getText().equals("") || text_editSborka.getText().equals("")) {
			label_editSborka.setText("Заполните поля!");
			return;
		}
		if (text_toEditSborka.getText().trim().toLowerCase().equals(text_editSborka.getText())) {
			label_editSborka.setText("Измените поля!");
			return;
		}
		Sborka edit = new Sborka();
		edit.setName(text_toEditSborka.getText().trim().toLowerCase());
		edit.setId(Integer.parseInt(text_idSborka.getText()));
		int k = 0;
		try {
			k = sqLiteDAO.updateSborka(edit);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if (k == 0) {
			label_editSborka.setText("Повтор записи!");
		} else if (k == -1) {
			label_editSborka.setText("Ошибка замены!");
		} else {
			for (int i = 0; i < table_Sborka.getItemCount(); i++) {
				if (Integer.parseInt(table_Sborka.getItem(i).getText(0)) == edit.getId()) {
					table_Sborka.getItem(i).setText(1, edit.getName());
					break;
				}
			}
			text_editSborka.setText(edit.getName());
			text_deleteSborka.setText(edit.getName());
			text_addSborkaKol.setText(edit.getName());
			label_editSborka.setText("Замена успешна!");
		}
	}
}