package org.plm.listeners.detaliSborka;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.plm.objects.IntReference;

public class SelectDetaliSborka implements Listener {

	private Text text_addIdKol;
	private Text text_addDetalKol;
	private Text text_addKolKol;
	private Text text_toEditDetal;
	private Text text_idDetal;
	private Text text_editDetal;
	private Text text_deleteDetal;
	private Table table_SborkaDetal;
	private Table table_Detal;
	private IntReference lastId_DetaliSborki;
	private IntReference lastKol;

	public SelectDetaliSborka(Text text_addIdKol, Text text_addDetalKol, Text text_addKolKol, Text text_toEditDetal,
			Text text_idDetal, Text text_editDetal, Text text_deleteDetal, Table table_SborkaDetal, Table table_Detal,
			IntReference lastId_DetaliSborki, IntReference lastKol) {
		this.text_addIdKol = text_addIdKol;
		this.text_addDetalKol = text_addDetalKol;
		this.text_addKolKol = text_addKolKol;
		this.text_toEditDetal = text_toEditDetal;
		this.text_idDetal = text_idDetal;
		this.text_editDetal = text_editDetal;
		this.text_deleteDetal = text_deleteDetal;
		this.table_SborkaDetal = table_SborkaDetal;
		this.table_Detal = table_Detal;
		this.lastId_DetaliSborki = lastId_DetaliSborki;
		this.lastKol = lastKol;
	}

	@Override
	public void handleEvent(Event event) {

		Point pt = new Point(event.x, event.y);
		TableItem item = table_SborkaDetal.getItem(pt);
		if (item != null) {
			text_addIdKol.setText(item.getText(0));
			text_addDetalKol.setText(item.getText(1));
			text_addKolKol.setText(item.getText(2));
			text_toEditDetal.setText(item.getText(1));
			for (int i = 0; i < table_Detal.getItemCount(); i++) {
				if (table_Detal.getItem(i).getText(1).equals(item.getText(1))) {
					text_idDetal.setText(table_Detal.getItem(i).getText(0));
					table_Detal.setSelection(i);
					break;
				}
			}
			text_editDetal.setText(item.getText(1));
			text_deleteDetal.setText(item.getText(1));
			
			lastId_DetaliSborki.setValue(Integer.parseInt(item.getText(0)));
			lastKol.setValue(Integer.parseInt(item.getText(2)));
		}
	}
}