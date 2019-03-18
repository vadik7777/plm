package org.plm.main;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import java.sql.SQLException;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.plm.impls.SQLiteDAOOptimize;
import org.plm.interfaces.SborkaDao;
import org.plm.listeners.detal.DeleteDetal;
import org.plm.listeners.detal.InsertDetal;
import org.plm.listeners.detal.SelectDetali;
import org.plm.listeners.detal.UpdateDetal;
import org.plm.listeners.detaliSborka.InsertUpdateDeleteDetaliSborka;
import org.plm.listeners.detaliSborka.SelectDetaliSborka;
import org.plm.listeners.sborka.DeleteSborka;
import org.plm.listeners.sborka.InsertSborka;
import org.plm.listeners.sborka.SelectSborka;
import org.plm.listeners.sborka.UpdateSborka;
import org.plm.objects.Detal;
import org.plm.objects.IntReference;
import org.plm.objects.Sborka;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class Main {

	protected Shell shell;
	SborkaDao sqLiteDAO;

	public static void main(String[] args) {
		try {
			Main window = new Main();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open() throws SQLException {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		sqLiteDAO.closeConnection();
		display.dispose();
	}

	protected void createContents() throws SQLException {
		shell = new Shell();
		shell.setSize(970, 480);
		shell.setText("PLM Application");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		// Реализация работы с базой данных SQLite
		sqLiteDAO = new SQLiteDAOOptimize();

		Group main = new Group(shell, SWT.NONE);
		main.setBounds(0, 0, 970, 416);

		// ************************************************
		// ТАБЛИЦА СБОРОК
		//
		// ************************************************

		// Создаем текстовые поля и метки для модификации таблицы 'Сборки'
		Text text_addSborka = new Text(main, SWT.BORDER);
		text_addSborka.setBounds(10, 254, 100, 21);

		Text text_toEditSborka = new Text(main, SWT.BORDER);
		text_toEditSborka.setBounds(10, 344, 100, 21);

		Text text_idSborka = new Text(main, SWT.BORDER | SWT.READ_ONLY);
		text_idSborka.setBounds(124, 305, 76, 21);

		Text text_editSborka = new Text(main, SWT.BORDER | SWT.READ_ONLY);
		text_editSborka.setBounds(10, 305, 100, 21);

		Text text_deleteSborka = new Text(main, SWT.BORDER | SWT.READ_ONLY);
		text_deleteSborka.setBounds(10, 392, 100, 21);

		Label label_addSborka = new Label(main, SWT.LEFT);
		label_addSborka.setBounds(212, 257, 96, 15);

		Label label_editSborka = new Label(main, SWT.LEFT);
		label_editSborka.setBounds(212, 347, 96, 15);

		Label label_removeSborka = new Label(main, SWT.LEFT);
		label_removeSborka.setBounds(212, 395, 96, 15);

		// Инициализируем таблицу сборок
		Table table_Sborka = new Table(main, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		table_Sborka.setBounds(10, 30, 303, 202);
		table_Sborka.setHeaderVisible(true);
		table_Sborka.setLinesVisible(true);

		// Добавляем название колонок
		TableColumn tableColumn_SborkaId = new TableColumn(table_Sborka, SWT.NONE);
		tableColumn_SborkaId.setWidth(31);
		tableColumn_SborkaId.setText("Ид.");

		TableColumn tableColumn_SborkaName = new TableColumn(table_Sborka, SWT.LEFT);
		tableColumn_SborkaName.setWidth(238);
		tableColumn_SborkaName.setText("Наименование");

		// Заполняем данные
		List<Sborka> sborkas = sqLiteDAO.selectSborkas();
		for (Sborka sborka : sborkas) {
			TableItem tableItem_1 = new TableItem(table_Sborka, SWT.NONE);
			tableItem_1.setText(0, Integer.toString(sborka.getId()));
			tableItem_1.setText(1, sborka.getName());
		}

		// Кнопка добавить 'Сборку'
		Button button_addSborka = new Button(main, SWT.CENTER);
		button_addSborka.setBounds(124, 252, 75, 25);
		button_addSborka.setText("Добавить");

		// Кнопка редактировать 'Сборку'
		Button button_editSborka = new Button(main, SWT.NONE);
		button_editSborka.setText("Заменить");
		button_editSborka.setBounds(124, 342, 75, 25);

		// Кнопка удалить 'Сборку'
		Button button_deleteSborka = new Button(main, SWT.NONE);
		button_deleteSborka.setBounds(124, 390, 75, 25);
		button_deleteSborka.setText("Удалить");

		// Оформление
		Label labelSborkaInfo = new Label(main, SWT.NONE);
		labelSborkaInfo.setBounds(71, 11, 175, 15);
		labelSborkaInfo.setText("Меню редактирования сборок");
		Label labelLine_1 = new Label(main, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelLine_1.setBounds(0, 238, 316, 2);
		Label labelLine_2 = new Label(main, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelLine_2.setBounds(0, 418, 316, 15);
		Label labelLine_3 = new Label(main, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelLine_3.setBounds(0, 380, 316, 2);
		Label labelLine_4 = new Label(main, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelLine_4.setBounds(0, 280, 316, 12);

		// ************************************************
		// ТАБЛИЦА ДЕТАЛИ
		//
		// ************************************************

		// Создаем текстовые поля и метки для модификации таблицы 'Деталь'
		Text text_addDetal = new Text(main, SWT.BORDER);
		text_addDetal.setBounds(636, 254, 100, 21);

		Text text_toEditDetal = new Text(main, SWT.BORDER);
		text_toEditDetal.setBounds(636, 344, 100, 21);

		Text text_idDetal = new Text(main, SWT.BORDER | SWT.READ_ONLY);
		text_idDetal.setBounds(750, 305, 76, 21);

		Text text_editDetal = new Text(main, SWT.BORDER | SWT.READ_ONLY);
		text_editDetal.setBounds(636, 305, 100, 21);

		Text text_deleteDetal = new Text(main, SWT.BORDER | SWT.READ_ONLY);
		text_deleteDetal.setBounds(636, 392, 100, 21);

		Label label_addDetal = new Label(main, SWT.HORIZONTAL | SWT.LEFT);
		label_addDetal.setBounds(838, 257, 96, 15);

		Label label_editDetal = new Label(main, SWT.HORIZONTAL | SWT.LEFT);
		label_editDetal.setBounds(838, 347, 96, 15);

		Label label_removeDetal = new Label(main, SWT.HORIZONTAL | SWT.LEFT);
		label_removeDetal.setBounds(838, 395, 96, 15);

		// Инициализируем таблицу деталей
		Table table_Detal = new Table(main, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		table_Detal.setBounds(636, 30, 303, 202);
		table_Detal.setHeaderVisible(true);
		table_Detal.setLinesVisible(true);

		// Добавляем название колонок
		TableColumn tableColumn_DetalId = new TableColumn(table_Detal, SWT.NONE);
		tableColumn_DetalId.setWidth(31);
		tableColumn_DetalId.setText("Ид.");

		TableColumn tableColumn_DetalName = new TableColumn(table_Detal, SWT.LEFT);
		tableColumn_DetalName.setWidth(238);
		tableColumn_DetalName.setText("Наименование");

		// Заполняем данные
		List<Detal> detals = sqLiteDAO.selectDetals();
		for (Detal detal : detals) {
			TableItem tableItem_1 = new TableItem(table_Detal, SWT.NONE);
			tableItem_1.setText(0, Integer.toString(detal.getId()));
			tableItem_1.setText(1, detal.getName());
		}

		// Кнопка добавить 'Деталь'
		Button button_addDetal = new Button(main, SWT.NONE);
		button_addDetal.setBounds(750, 252, 75, 25);
		button_addDetal.setText("Добавить");

		// Кнопка редактировать 'Деталь'
		Button button_editDetal = new Button(main, SWT.NONE);
		button_editDetal.setText("Заменить");
		button_editDetal.setBounds(750, 342, 75, 25);

		// Кнопка удалить 'Деталь'
		Button button_deleteDetal = new Button(main, SWT.NONE);
		button_deleteDetal.setBounds(750, 390, 75, 25);
		button_deleteDetal.setText("Удалить");

		// Оформление
		Label labelDetalInfo = new Label(main, SWT.NONE);
		labelDetalInfo.setBounds(697, 11, 175, 15);
		labelDetalInfo.setText("Меню редактирования деталей");
		Label labelLine_5 = new Label(main, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelLine_5.setBounds(632, 238, 313, 2);
		Label labelLine_6 = new Label(main, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelLine_6.setBounds(632, 418, 313, 15);
		Label labelLine_7 = new Label(main, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelLine_7.setBounds(632, 380, 313, 2);
		Label labelLine_8 = new Label(main, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelLine_8.setBounds(632, 280, 313, 12);

		// ************************************************
		// ТАБЛИЦА ВХОДЯЩИЕ ДЕТАЛИ
		//
		// ************************************************

		// Создаем текстовые поля и метки для модификации таблицы 'Количество'
		Text text_addSborkaKol = new Text(main, SWT.BORDER | SWT.READ_ONLY);
		text_addSborkaKol.setBounds(510, 254, 100, 21);

		Text text_addDetalKol = new Text(main, SWT.BORDER | SWT.READ_ONLY);
		text_addDetalKol.setBounds(400, 254, 100, 21);

		Text text_addKolKol = new Text(main, SWT.BORDER | SWT.CENTER);
		text_addKolKol.setBounds(342, 254, 47, 21);

		// Невидимое поле идентификатора таблицы Kol
		Text text_addIdKol = new Text(main, SWT.NONE);
		text_addIdKol.setBounds(342, 354, 47, 21);
		text_addIdKol.setVisible(false);

		Label label_addKol = new Label(main, SWT.NONE);
		label_addKol.setBounds(415, 283, 166, 15);

		// Инициализируем таблицу входящих деталей
		Table table_SborkaDetal = new Table(main, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		table_SborkaDetal.setBounds(322, 30, 303, 202);
		table_SborkaDetal.setHeaderVisible(true);
		table_SborkaDetal.setLinesVisible(true);

		// Добавляем название колонок
		TableColumn tableColumn_SborkaDetalId = new TableColumn(table_SborkaDetal, SWT.NONE);
		tableColumn_SborkaDetalId.setWidth(31);
		tableColumn_SborkaDetalId.setText("Ид.");

		TableColumn tableColumn_SborkaDetalName = new TableColumn(table_SborkaDetal, SWT.NONE);
		tableColumn_SborkaDetalName.setWidth(188);
		tableColumn_SborkaDetalName.setText("Наименование");

		TableColumn tableColumn_SborkaDetalKol = new TableColumn(table_SborkaDetal, SWT.CENTER);
		tableColumn_SborkaDetalKol.setWidth(45);
		tableColumn_SborkaDetalKol.setText("Кол");

		Button button_addKol = new Button(main, SWT.NONE);
		button_addKol.setText("Добавить / Изменить");
		button_addKol.setBounds(400, 305, 147, 25);

		// Оформление
		Label labelKolInfo = new Label(main, SWT.NONE);
		labelKolInfo.setBounds(356, 11, 236, 15);
		labelKolInfo.setText("Меню редактирования входящих деталей");

		Label label_InfoSborkaKol = new Label(main, SWT.NONE);
		label_InfoSborkaKol.setText("Сборка");
		label_InfoSborkaKol.setBounds(540, 238, 47, 15);

		Label label_InfoDetalKol = new Label(main, SWT.NONE);
		label_InfoDetalKol.setText("Деталь");
		label_InfoDetalKol.setBounds(430, 238, 47, 15);

		Label label_InfoKolKol = new Label(main, SWT.NONE);
		label_InfoKolKol.setText("Кол.");
		label_InfoKolKol.setBounds(354, 238, 31, 15);

		Label labelLine9 = new Label(main, SWT.SEPARATOR | SWT.VERTICAL);
		labelLine9.setBounds(317, 8, 2, 416);

		Label labelLine10 = new Label(main, SWT.SEPARATOR);
		labelLine10.setBounds(630, 8, 2, 416);

		// ***********************************************************
		// Добавляем Listeners
		//
		// ***********************************************************

		// Переменные для проверки повторов при перезаписи в таблице Входящие детали
		IntReference lastId_DetaliSborki = new IntReference();
		IntReference lastKol = new IntReference();

		// Добавляем Listener на таблицу 'Сборок'
		table_Sborka.addListener(SWT.MouseDown,
				new SelectSborka(table_Sborka, table_SborkaDetal, sqLiteDAO, text_editSborka, text_toEditSborka,
						text_idSborka, text_deleteSborka, text_addSborkaKol, text_addKolKol, text_addIdKol));

		// Добавляем Listener для кнопки 'Добавить' Сборку
		button_addSborka.addListener(SWT.MouseDown,
				new InsertSborka(text_addSborka, text_toEditSborka, text_idSborka, text_editSborka, text_deleteSborka,
						text_addSborkaKol, text_addKolKol, text_addIdKol, label_addSborka, table_Sborka,
						table_SborkaDetal, sqLiteDAO));

		// Добавляем Listener для кнопки 'Заменить' Сборку
		button_editSborka.addListener(SWT.MouseDown, new UpdateSborka(text_toEditSborka, text_idSborka, text_editSborka,
				text_deleteSborka, label_editSborka, text_addSborkaKol, table_Sborka, sqLiteDAO));

		// Добавляем Listener для кнопки 'Удалить' Сборку
		button_deleteSborka.addListener(SWT.MouseDown,
				new DeleteSborka(text_toEditSborka, text_idSborka, text_editSborka, text_deleteSborka,
						text_addSborkaKol, text_addKolKol, text_addIdKol, label_removeSborka, table_Sborka,
						table_SborkaDetal, sqLiteDAO));

		// Добавляем Listener на таблицу 'Деталей'
		table_Detal.addListener(SWT.MouseDown, new SelectDetali(table_Detal, text_editDetal, text_toEditDetal,
				text_idDetal, text_deleteDetal, text_addDetalKol, text_addKolKol, text_addIdKol));

		// Добавляем Listener для кнопки 'Добавить' в Деталь
		button_addDetal.addListener(SWT.MouseDown,
				new InsertDetal(text_addDetal, text_toEditDetal, text_idDetal, text_editDetal, text_deleteDetal,
						text_addDetalKol, text_addKolKol, text_addIdKol, label_addDetal, table_Detal, sqLiteDAO));

		// Добавляем Listener для кнопки 'Заменить' Деталь
		button_editDetal.addListener(SWT.MouseDown, new UpdateDetal(text_toEditDetal, text_idDetal, text_editDetal,
				text_deleteDetal, text_addDetalKol, label_editDetal, table_Detal, table_SborkaDetal, sqLiteDAO));

		// Добавляем Listener для кнопки 'Удалить' Деталь
		button_deleteDetal.addListener(SWT.MouseDown, new DeleteDetal(text_toEditDetal, text_idDetal, text_editDetal,
				text_deleteDetal, text_addDetalKol, label_removeDetal, table_Detal, table_SborkaDetal, sqLiteDAO));

		// Добавляем Listener на таблицу 'Входящие Детали'
		table_SborkaDetal.addListener(SWT.MouseDown,
				new SelectDetaliSborka(text_addIdKol, text_addDetalKol, text_addKolKol, text_toEditDetal, text_idDetal,
						text_editDetal, text_deleteDetal, table_SborkaDetal, table_Detal, lastId_DetaliSborki,
						lastKol));

		// Добавляем Listener для кнопки 'Добавить/Изменить' Входящие детали
		button_addKol.addListener(SWT.MouseDown,
				new InsertUpdateDeleteDetaliSborka(text_idSborka, text_idDetal, text_addIdKol, text_addKolKol,
						text_addDetalKol, label_addKol, table_SborkaDetal, sqLiteDAO, lastId_DetaliSborki, lastKol));
	}
}