package org.plm.listeners.detal;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class SelectDetali implements Listener{

	private Table table_Detal;
	private Text text_editDetal;
	private Text text_toEditDetal;
	private Text text_idDetal;
	private Text text_deleteDetal;
	private Text text_addDetalKol;
	private Text text_addKolKol;
	private Text text_addIdKol;
	
	public SelectDetali(Table table_Detal, Text text_editDetal, Text text_toEditDetal, Text text_idDetal,
			Text text_deleteDetal, Text text_addDetalKol, Text text_addKolKol, Text text_addIdKol) {
		this.table_Detal = table_Detal;
		this.text_editDetal = text_editDetal;
		this.text_toEditDetal = text_toEditDetal;
		this.text_idDetal = text_idDetal;
		this.text_deleteDetal = text_deleteDetal;
		this.text_addDetalKol = text_addDetalKol;
		this.text_addKolKol = text_addKolKol;
		this.text_addIdKol = text_addIdKol;
	}
	
	@Override
	public void handleEvent(Event event) {
		Point pt = new Point(event.x, event.y);
		TableItem item = table_Detal.getItem(pt);
		if (item != null) {
			text_editDetal.setText(item.getText(1));
			text_toEditDetal.setText(item.getText(1));
			text_idDetal.setText(item.getText(0));
			text_deleteDetal.setText(item.getText(1));
			text_addDetalKol.setText(item.getText(1));
			text_addKolKol.setText("");
			text_addIdKol.setText("");
		}
	}
}