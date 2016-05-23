/*******************************************************************************
 * Gestió d'Amonestacions v1.0
 *
 * Esta obra está sujeta a la licencia Reconocimiento-NoComercial-SinObraDerivada 4.0 Internacional de Creative Commons. 
 * Para ver una copia de esta licencia, visite http://creativecommons.org/licenses/by-nc-nd/4.0/.
 *  
 * @author Francisco Javier Casado Moreno - fcasasdo@elpuig.xeill.net 
 * @author Daniel Pérez Palacino - dperez@elpuig.xeill.net 
 * @author Gerard Enrique Paulino Decena - gpaulino@elpuig.xeill.net 
 * @author Xavier Murcia Gámez - xmurica@elpuig.xeill.net 
 *******************************************************************************/
package com.example.view.AdminView.Settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import com.example.Dates.ConverterDates;
import com.example.Templates.MainContentView;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class AdminViewSettingsJava extends MainContentView {

	private AdminViewSettings adminsettings;
	private ConverterDates dates;

	public AdminViewSettingsJava() {
		adminsettings = new AdminViewSettings();

		GeneralSettings();
		listeners();

		try {
			readFile();
		} catch (ReadOnlyException | ConversionException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void GeneralSettings() {

		vHorizontalMain.addComponent(adminsettings);
		vHorizontalMain.setComponentAlignment(adminsettings, Alignment.MIDDLE_CENTER);
		adminsettings.setVisible(true);
		adminsettings.setStyleName("whiteBackground");

		adminsettings.checkEmailPares.addStyleName("settings");
		adminsettings.checkWhatsPares.addStyleName("settings");
		adminsettings.dataIniciCurs.addStyleName("settings");
		adminsettings.dataFinalCurs.addStyleName("settings");
		adminsettings.dataFinaltrimestre1.addStyleName("settings");
		adminsettings.dataFinaltrimestre2.addStyleName("settings");
		adminsettings.dataInicitrimestre2.addStyleName("settings");
		adminsettings.dataInicitrimestre3.addStyleName("settings");

		txtTitle.addStyleName("main-title");
		txtTitle.setValue("Configuració");
		txtSearch.setVisible(false);
		clearTxt.setVisible(false);
		bAdd.setVisible(true);
		bAdd.setCaption("Desar configuració");
		bAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bDelete.setVisible(false);
		buttonEdit.setVisible(false);
		bRegister.setVisible(false);

	}

	private void listeners() {

		bAdd.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				writeFile();
				notif("Configuració guardada correctament");

			}

		});

	}

	public void notif(String msg) {

		Notification notif = new Notification(msg, null, Notification.Type.ASSISTIVE_NOTIFICATION, true); // Contains
																											// HTML

		// Customize it
		notif.show(Page.getCurrent());
		notif.setDelayMsec(500);
		notif.setPosition(Position.TOP_CENTER);

	}

	private void writeFile() {

		String datainicitrimestre1buena = null;
		String datafinaltrimestre3buena = null;
		String datafintrimestre1buena = null;
		String datafintrimestre2buena = null;
		String fechaIniciTrimestre3buena = null;
		String fechaIniciTrimestre2buena = null;

		dates = new ConverterDates();
		File currDir = new File(".");
		String path2;
		try {

			Date datainicitrimestre1 = adminsettings.dataIniciCurs.getValue();
			datainicitrimestre1buena = dates.converterDate(datainicitrimestre1);
			Date datafinaltrimestre3 = adminsettings.dataFinalCurs.getValue();
			datafinaltrimestre3buena = dates.converterDate(datafinaltrimestre3);
			Date datafintrimestre1 = adminsettings.dataFinaltrimestre1.getValue();
			datafintrimestre1buena = dates.converterDate(datafintrimestre1);
			Date datafintrimestre2 = adminsettings.dataFinaltrimestre2.getValue();
			datafintrimestre2buena = dates.converterDate(datafintrimestre2);
			Date fechaIniciTrimestre3 = adminsettings.dataInicitrimestre3.getValue();
			fechaIniciTrimestre3buena = dates.converterDate(fechaIniciTrimestre3);
			Date fechaIniciTrimestre2 = adminsettings.dataInicitrimestre2.getValue();
			fechaIniciTrimestre2buena = dates.converterDate(fechaIniciTrimestre2);

			boolean checkPares = adminsettings.checkEmailPares.getValue();
			boolean checkTelegram = adminsettings.checkWhatsPares.getValue();
			boolean checkTutor = adminsettings.checkEmailTutors.getValue();

			path2 = currDir.getCanonicalPath();
			File f = new File(path2 + "/git/ga2/WebContent/Settings/settings.txt");
			FileWriter fw = new FileWriter(f, false);
			BufferedWriter bf = new BufferedWriter(fw);

			bf.write(datainicitrimestre1buena + ",");
			bf.write(datafintrimestre1buena + ",");
			bf.write(fechaIniciTrimestre2buena + ",");
			bf.write(datafintrimestre2buena + ",");
			bf.write(fechaIniciTrimestre3buena + ",");
			bf.write(datafinaltrimestre3buena + ",");

			bf.write(Boolean.toString(checkTutor) + ",");
			bf.write(Boolean.toString(checkPares) + ",");
			bf.write(Boolean.toString(checkTelegram));

			bf.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readFile() throws ReadOnlyException, ConversionException, IOException {
		FileReader reader;
		String path2 = null;
		File currDir = new File(".");
		String linea = null;
		Date fechaIniciTrimestre1 = null;
		Date fechaFinalTrimestre3 = null;
		Date fechafinaltrimestre1 = null;
		Date fechafinaltrimestre2 = null;
		Date fechaIniciTrimestre3 = null;
		Date fechaIniciTrimestre2 = null;
		boolean checkTutor = false;
		boolean checkPares = false;
		boolean checkTelegram = false;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		try {
			path2 = currDir.getCanonicalPath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		File f = new File(path2 + "/webapps/GALite/Settings/settings.txt");

		if (!f.exists()) {
			f.createNewFile();
		}

		BufferedReader br = new BufferedReader(new FileReader(f));
		if (br.readLine() == null) {

		} else {

			reader = new FileReader(f);
			BufferedReader flux = new BufferedReader(reader);

			while ((linea = flux.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(linea, ",");
				while (st.hasMoreTokens()) {

					try {

						fechaIniciTrimestre1 = formatter.parse(st.nextToken());
						fechafinaltrimestre1 = formatter.parse(st.nextToken());
						fechaIniciTrimestre2 = formatter.parse(st.nextToken());
						fechafinaltrimestre2 = formatter.parse(st.nextToken());
						fechaIniciTrimestre3 = formatter.parse(st.nextToken());
						fechaFinalTrimestre3 = formatter.parse(st.nextToken());

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (st.nextToken().equals("true")) {
						checkTutor = true;
					} else {
						checkTutor = false;
					}

					if (st.nextToken().equals("true")) {
						checkPares = true;
					} else {
						checkPares = false;
					}

					if (st.nextToken().equals("true")) {
						checkTelegram = true;
					} else {
						checkTelegram = false;
					}

				}

			}

			adminsettings.dataIniciCurs.setValue(fechaIniciTrimestre1);
			adminsettings.dataFinalCurs.setValue(fechaFinalTrimestre3);
			adminsettings.dataFinaltrimestre1.setValue(fechafinaltrimestre1);
			adminsettings.dataFinaltrimestre2.setValue(fechafinaltrimestre2);
			adminsettings.checkEmailTutors.setValue(checkTutor);
			adminsettings.checkEmailPares.setValue(checkPares);
			adminsettings.checkWhatsPares.setValue(checkTelegram);
			adminsettings.dataInicitrimestre2.setValue(fechaIniciTrimestre2);
			adminsettings.dataInicitrimestre3.setValue(fechaIniciTrimestre3);

		}
	}

}
