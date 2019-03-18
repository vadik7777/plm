package org.plm.listeners.detaliSborka;

import java.sql.SQLException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.plm.interfaces.SborkaDao;
import org.plm.objects.IntReference;
import org.plm.objects.Kol;

public class InsertUpdateDeleteDetaliSborka implements Listener {
	private Text text_idSborka;
	private Text text_idDetal;
	private Text text_addIdKol;
	private Text text_addKolKol;
	private Text text_addDetalKol;
	private Label label_addKol;
	private Table table_SborkaDetal;
	private SborkaDao sqLiteDAO;

	private IntReference lastId_DetaliSborki;
	private IntReference lastKol;

	private static int lastId_Detali = 0;
	private static int lastId_Sborka = 0;
	private static int lastId_Kol = 0;

	public InsertUpdateDeleteDetaliSborka(Text text_idSborka, Text text_idDetal, Text text_addIdKol,
			Text text_addKolKol, Text text_addDetalKol, Label label_addKol, Table table_SborkaDetal,
			SborkaDao sqLiteDAO, IntReference lastId_DetaliSborki, IntReference lastKol) {
		this.text_idSborka = text_idSborka;
		this.text_idDetal = text_idDetal;
		this.text_addIdKol = text_addIdKol;
		this.text_addKolKol = text_addKolKol;
		this.text_addDetalKol = text_addDetalKol;
		this.label_addKol = label_addKol;
		this.table_SborkaDetal = table_SborkaDetal;
		this.sqLiteDAO = sqLiteDAO;
		this.lastId_DetaliSborki = lastId_DetaliSborki;
		this.lastKol = lastKol;
	}

	@Override
	public void handleEvent(Event event) {
		// Проверка полей
		if (text_idSborka.getText().equals("")) {
			label_addKol.setText("   Выберете сборку!");
			return;
		}
		if (text_idDetal.getText().equals("")) {
			label_addKol.setText("   Выберете деталь!");
			return;
		}
		if (text_addKolKol.getText().equals("")) {
			label_addKol.setText("Укажите количество!");
			return;
		}
		// инициализация переменных
		int id_sborka = Integer.parseInt(text_idSborka.getText());
		int id_detal = Integer.parseInt(text_idDetal.getText());
		int kolValue = Integer.parseInt(text_addKolKol.getText());
		if (!text_addIdKol.getText().equals("")) {
			// update or delete by id
			int id_sborkaDetali = Integer.parseInt(text_addIdKol.getText());
			if (lastId_DetaliSborki.getValue() == id_sborkaDetali && lastKol.getValue() == kolValue) {
				label_addKol.setText("  Повтор изменения!");
				return;
			}
			// Глобальные int для проверки повторов
			lastId_DetaliSborki.setValue(id_sborkaDetali);
			lastKol.setValue(kolValue);
			// Локальные int для проверки повторов
			lastId_Sborka = id_sborka;
			lastId_Detali = Integer.parseInt(text_idDetal.getText());
			lastId_Kol = kolValue;

			if (kolValue > 0) {
				// update by id
				Kol kol = new Kol(id_sborkaDetali, id_sborka, kolValue);
				update(kol);
			} else {
				// delete by id
				Kol kol = new Kol(id_sborkaDetali, id_sborka);
				delete(kol);
			}
		} else {
			// insert, update, delete by id_sborka, id_detal
			if (id_sborka == lastId_Sborka && id_detal == lastId_Detali && kolValue == lastId_Kol) {
				label_addKol.setText("  Повтор изменения!");
				return;
			}
			// Локальные int для проверки повторов
			lastId_Sborka = id_sborka;
			lastId_Detali = id_detal;
			lastId_Kol = kolValue;
			Kol kol = sqLiteDAO.findKol(id_sborka, id_detal);
			if (kol != null) {
				if (kolValue > 0) {
					// update id_sborka, id_detal
					kol.setKol(kolValue);
					update(kol);
				} else {
					// delete by id_sborka, id_detal
					delete(kol);
				}
			} else if (kolValue > 0) {
				// insert kol by id_sborka, id_detal
				int k = 0;
				try {
					kol = new Kol(sqLiteDAO.findNextIdKol(), id_sborka, id_detal, kolValue);
					k = sqLiteDAO.insertDetaliSborka(kol);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (k > 0) {
					label_addKol.setText("Добавление успешно!");
					TableItem tableItem_1 = new TableItem(table_SborkaDetal, SWT.NONE);
					tableItem_1.setText(0, Integer.toString(kol.getId()));
					tableItem_1.setText(1, text_addDetalKol.getText());
					tableItem_1.setText(2, text_addKolKol.getText());
				} else {
					label_addKol.setText("Ошибка изменения!");
				}
			} else {
				label_addKol.setText("Количество неверно!");
			}
		}
	}

	// update
	private void update(Kol kol) {
		int k = 0;
		try {
			k = sqLiteDAO.updateDetaliSborka(kol);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (k > 0) {
			label_addKol.setText("Изменения успешны!");
			for (int i = 0; i < table_SborkaDetal.getItemCount(); i++) {
				if (Integer.parseInt(table_SborkaDetal.getItem(i).getText(0)) == kol.getId()) {
					table_SborkaDetal.getItem(i).setText(2, Integer.toString(kol.getKol()));
					break;
				}
			}
		} else {
			label_addKol.setText("Ошибка изменения!");
		}
	}

	// delete
	private void delete(Kol kol) {
		int k = 0;
		try {
			k = sqLiteDAO.deleteDetaliSborka(kol);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (k > 0) {
			label_addKol.setText("Деталь удалена из сборки!");
			for (int i = 0; i < table_SborkaDetal.getItemCount(); i++) {
				if (Integer.parseInt(table_SborkaDetal.getItem(i).getText(0)) == kol.getId()) {
					table_SborkaDetal.remove(i);
					break;
				}
			}
			text_addIdKol.setText("");
		} else {
			label_addKol.setText("Ошибка изменения!");
		}
	}
}