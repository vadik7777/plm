package org.plm.listeners.sborka;

import java.sql.SQLException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.plm.interfaces.SborkaDao;
import org.plm.objects.Sborka;

public class InsertSborka implements Listener {

	private Text text_addSborka;
	private Text text_toEditSborka;
	private Text text_idSborka; 
	private Text text_editSborka;
	private Text text_deleteSborka;
	private Text text_addSborkaKol;
	private Text text_addKolKol;
	private Text text_addIdKol;
	private Label label_addSborka;
	private Table table_Sborka;
	private Table table_SborkaDetal;
	private SborkaDao sqLiteDAO;
	
	public InsertSborka(Text text_addSborka, Text text_toEditSborka, Text text_idSborka, Text text_editSborka,
			Text text_deleteSborka, Text text_addSborkaKol, Text text_addKolKol, Text text_addIdKol, Label label_addSborka,
			Table table_Sborka, Table table_SborkaDetal, SborkaDao sqLiteDAO) {
		this.text_addSborka = text_addSborka;
		this.text_toEditSborka = text_toEditSborka;
		this.text_idSborka = text_idSborka;
		this.text_editSborka = text_editSborka;
		this.text_deleteSborka = text_deleteSborka;
		this.text_addSborkaKol = text_addSborkaKol;
		this.text_addKolKol = text_addKolKol;
		this.text_addIdKol = text_addIdKol;
		this.label_addSborka = label_addSborka;
		this.table_Sborka = table_Sborka;
		this.table_SborkaDetal = table_SborkaDetal;
		this.sqLiteDAO = sqLiteDAO;
	}

	@Override
	public void handleEvent(Event event) {
		if (!text_addSborka.getText().equals("")) {
			Sborka input = new Sborka();
			input.setName(text_addSborka.getText().trim().toLowerCase());
			int k = 0;
			try {
				k = sqLiteDAO.insertSborka(input);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			if (k == 0) {
				label_addSborka.setText("Повтор записи!");
			} else if (k == -1) {
				label_addSborka.setText("Ошибка записи!");
			} else {
				TableItem tableItem_1 = new TableItem(table_Sborka, SWT.NONE);
				tableItem_1.setText(0, Integer.toString(k));
				tableItem_1.setText(1, input.getName());
				text_addSborka.setText("");
				text_toEditSborka.setText(input.getName());
				text_idSborka.setText(Integer.toString(k));
				text_editSborka.setText(input.getName());
				text_deleteSborka.setText(input.getName());
				text_addSborkaKol.setText(input.getName());
				text_addKolKol.setText("");
				text_addIdKol.setText("");
				table_Sborka.setSelection(table_Sborka.getItemCount()-1);
				table_SborkaDetal.removeAll();
				label_addSborka.setText("Запись успешна!");
			}
		} else {
			label_addSborka.setText("Заполните поле!");
		}
	}
}