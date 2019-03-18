package org.plm.listeners.detal;

import java.sql.SQLException;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.plm.interfaces.SborkaDao;

public class DeleteDetal implements Listener {

	private Text text_toEditDetal;
	private Text text_idDetal;
	private Text text_editDetal;
	private Text text_deleteDetal;
	private Text text_addDetalKol;
	private Label label_removeDetal;
	private Table table_Detal;
	private Table table_SborkaDetal;
	private SborkaDao sqLiteDAO;

	public DeleteDetal(Text text_toEditDetal, Text text_idDetal, Text text_editDetal, Text text_deleteDetal,
			Text text_addDetalKol, Label label_removeDetal, Table table_Detal, Table table_SborkaDetal,
			SborkaDao sqLiteDAO) {
		this.text_toEditDetal = text_toEditDetal;
		this.text_idDetal = text_idDetal;
		this.text_editDetal = text_editDetal;
		this.text_deleteDetal = text_deleteDetal;
		this.text_addDetalKol = text_addDetalKol;
		this.label_removeDetal = label_removeDetal;
		this.table_Detal = table_Detal;
		this.table_SborkaDetal = table_SborkaDetal;
		this.sqLiteDAO = sqLiteDAO;
	}

	@Override
	public void handleEvent(Event event) {
		if (!text_idDetal.getText().equals("")) {
			int k = 0;
			int id = Integer.parseInt(text_idDetal.getText());
			try {
				k = sqLiteDAO.deleteDetal(id);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (k == 0) {
				label_removeDetal.setText("Запись не найдена!");
			} else if (k == -1) {
				label_removeDetal.setText("Ошибка удаления!");
			} else {
				for (int i = 0; i < table_Detal.getItemCount(); i++) {
					if (Integer.parseInt(table_Detal.getItem(i).getText(0)) == id) {
						table_Detal.remove(i);
						break;
					}
				}
				String deleteName = text_deleteDetal.getText().trim().toLowerCase();
				for (int i = 0; i < table_SborkaDetal.getItemCount(); i++) {
					if (table_SborkaDetal.getItem(i).getText(1).equals(deleteName)) {
						table_SborkaDetal.remove(i);
						break;
					}
				}
				text_toEditDetal.setText("");
				text_idDetal.setText("");
				text_editDetal.setText("");
				text_deleteDetal.setText("");
				text_addDetalKol.setText("");
				label_removeDetal.setText("Запись удалена!");
			}
		} else {
			label_removeDetal.setText("Заполните поле!");
		}
	}
}