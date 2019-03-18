package org.plm.listeners.detal;

import java.sql.SQLException;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.plm.interfaces.SborkaDao;
import org.plm.objects.Detal;

public class UpdateDetal implements Listener {
	
	private Text text_toEditDetal;
	private Text text_idDetal;
	private Text text_editDetal;
	private Text text_deleteDetal;
	private Text text_addDetalKol;
	private Label label_editDetal;
	private Table table_Detal;
	private Table table_SborkaDetal;
	private SborkaDao sqLiteDAO;
	
	public UpdateDetal(Text text_toEditDetal, Text text_idDetal, Text text_editDetal, Text text_deleteDetal,
			Text text_addDetalKol, Label label_editDetal, Table table_Detal, Table table_SborkaDetal,
			SborkaDao sqLiteDAO) {
		this.text_toEditDetal = text_toEditDetal;
		this.text_idDetal = text_idDetal;
		this.text_editDetal = text_editDetal;
		this.text_deleteDetal = text_deleteDetal;
		this.text_addDetalKol = text_addDetalKol;
		this.label_editDetal = label_editDetal;
		this.table_Detal = table_Detal;
		this.table_SborkaDetal = table_SborkaDetal;
		this.sqLiteDAO = sqLiteDAO;
	}

	@Override
	public void handleEvent(Event event) {
		if (text_toEditDetal.getText().equals("") || text_editDetal.getText().equals("")) {
			label_editDetal.setText("Заполните поля!");
			return;
		}
		if (text_toEditDetal.getText().trim().toLowerCase().equals(text_editDetal.getText())) {
			label_editDetal.setText("Измените поля!");
			return;
		}
		int id = Integer.parseInt(text_idDetal.getText());
		Detal edit = new Detal();
		edit.setName(text_toEditDetal.getText().trim().toLowerCase());
		edit.setId(id);
		int k = 0;
		try {
			k = sqLiteDAO.updateDetal(edit);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if (k == 0) {
			label_editDetal.setText("Повтор записи!");
		} else if (k == -1) {
			label_editDetal.setText("Ошибка замены!");
		} else {
			for (int i = 0; i < table_Detal.getItemCount(); i++) {
				if (Integer.parseInt(table_Detal.getItem(i).getText(0)) == edit.getId()) {
					table_Detal.getItem(i).setText(1, edit.getName());
					break;
				}
			}
			String editName = text_editDetal.getText().trim().toLowerCase();
			for (int i = 0; i < table_SborkaDetal.getItemCount(); i++) {
				if (table_SborkaDetal.getItem(i).getText(1).equals(editName)) {
					table_SborkaDetal.getItem(i).setText(1, edit.getName());
					break;
				}
			}
			label_editDetal.setText("Замена успешна!");
			text_editDetal.setText(edit.getName());
			text_deleteDetal.setText(edit.getName());
			text_addDetalKol.setText(edit.getName());
		}
	}
}