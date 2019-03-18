package org.plm.listeners.detal;

import java.sql.SQLException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.plm.interfaces.SborkaDao;
import org.plm.objects.Detal;

public class InsertDetal implements Listener {

	private Text text_addDetal;
	private Text text_toEditDetal;
	private Text text_idDetal;
	private Text text_editDetal;
	private Text text_deleteDetal;
	private Text text_addDetalKol;
	private Text text_addKolKol;
	private Text text_addIdKol;
	private Label label_addDetal;
	private Table table_Detal;
	private SborkaDao sqLiteDAO;
	
	public InsertDetal(Text text_addDetal, Text text_toEditDetal, Text text_idDetal, Text text_editDetal,
			Text text_deleteDetal, Text text_addDetalKol, Text text_addKolKol, Text text_addIdKol, Label label_addDetal, Table table_Detal,
			SborkaDao sqLiteDAO) {
		this.text_addDetal = text_addDetal;
		this.text_toEditDetal = text_toEditDetal;
		this.text_idDetal = text_idDetal;
		this.text_editDetal = text_editDetal;
		this.text_deleteDetal = text_deleteDetal;
		this.text_addDetalKol = text_addDetalKol;
		this.text_addKolKol = text_addKolKol;
		this.text_addIdKol = text_addIdKol;
		this.label_addDetal = label_addDetal;
		this.table_Detal = table_Detal;
		this.sqLiteDAO = sqLiteDAO;
	}

	@Override
	public void handleEvent(Event event) {
		if (!text_addDetal.getText().equals("")) {
			Detal input = new Detal();
			input.setName(text_addDetal.getText().trim().toLowerCase());
			int k = 0;
			try {
				k = sqLiteDAO.insertDetal(input);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			if (k == 0) {
				label_addDetal.setText("Повтор записи!");
			} else if (k == -1) {
				label_addDetal.setText("Ошибка записи!");
			} else {
				TableItem tableItem_1 = new TableItem(table_Detal, SWT.NONE);
				tableItem_1.setText(0, Integer.toString(k));
				tableItem_1.setText(1, input.getName());
				text_addDetal.setText("");
				text_toEditDetal.setText(input.getName());
				text_idDetal.setText(Integer.toString(k));
				text_editDetal.setText(input.getName());
				text_deleteDetal.setText(input.getName());
				text_addDetalKol.setText(input.getName());
				text_addKolKol.setText("");
				text_addIdKol.setText("");
				table_Detal.setSelection(table_Detal.getItemCount()-1);
				label_addDetal.setText("Запись успешна!");
			}
		} else {
			label_addDetal.setText("Заполните поле!");
		}
	}
}