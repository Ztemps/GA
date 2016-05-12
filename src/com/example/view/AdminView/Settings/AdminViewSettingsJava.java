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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class AdminViewSettingsJava extends MainContentView {

	private static AdminViewSettings adminsettings;

	public AdminViewSettingsJava() {
		ButtonSettings();
		listeners();
		
			try {
				readFile();
			} catch (ReadOnlyException | ConversionException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	
	}

	private void ButtonSettings() {

		txtTitle.addStyleName("main-title");
		txtTitle.setValue("Configuració");
		adminsettings = new AdminViewSettings();
		adminsettings.setVisible(true);
		vHorizontalMain.addComponent(adminsettings);
		vHorizontalMain.setComponentAlignment(adminsettings, Alignment.MIDDLE_CENTER);

		adminsettings.checkEmailTutors.addStyleName("emailTutors");
		adminsettings.checkEmailPares.addStyleName("emailPares");
		adminsettings.checkWhatsPares.addStyleName("whatsPares");
		adminsettings.dataIniciCurs.addStyleName("dataInit");
		adminsettings.dataFinalCurs.addStyleName("dataFi");
		adminsettings.dataFinaltrimestre1.addStyleName("dataFi");
		adminsettings.dataFinaltrimestre2.addStyleName("dataFi");;

		txtSearch.setVisible(false);
		clearTxt.setVisible(false);
		bAdd.setVisible(true);
		bAdd.setCaption("Desar configuraió");
		bDelete.setVisible(false);
		buttonEdit.setVisible(false);
		bRegister.setVisible(false);

	}

	private void listeners() {

		bAdd.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("FUNSIONA!");

				writeFile();

			}

		});

	}

	private void writeFile() {

		String fechainicibuena = null;
		String fechafinalbuena = null;
		String datafintrimestre1buena = null;
		String datafintrimestre2buena = null;

		File currDir = new File(".");
		String path2;
		try {

			Date datainici = adminsettings.dataIniciCurs.getValue();
			fechainicibuena = ConverterDates.converterDate(datainici);

			Date datafi = adminsettings.dataFinalCurs.getValue();
			fechafinalbuena = ConverterDates.converterDate(datafi);
			
			Date datafintrimestre1 = adminsettings.dataFinaltrimestre1.getValue();
			datafintrimestre1buena = ConverterDates.converterDate(datafintrimestre1);

			Date datafintrimestre2 = adminsettings.dataFinaltrimestre2.getValue();
			datafintrimestre2buena = ConverterDates.converterDate(datafintrimestre2);
			
			
			boolean checkTutor = adminsettings.checkEmailTutors.getValue();
			boolean checkPares = adminsettings.checkEmailPares.getValue();
			boolean checkTelegram = adminsettings.checkWhatsPares.getValue();

			path2 = currDir.getCanonicalPath();
			File f = new File(path2 + "/git/ga2/WebContent/Settings/settings.txt");
			FileWriter fw = new FileWriter(f, false);
			BufferedWriter bf = new BufferedWriter(fw);

			bf.write(fechainicibuena + ",");
			bf.write(fechafinalbuena + ",");
			bf.write(datafintrimestre1buena + ",");
			bf.write(datafintrimestre2buena + ",");
			bf.write(Boolean.toString(checkTutor) + ",");
			bf.write(Boolean.toString(checkPares) + ",");
			bf.write(Boolean.toString(checkTelegram));

			bf.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	private void readFile() throws ReadOnlyException, ConversionException, IOException {
		FileReader reader;
		String path2 = null;
		File currDir = new File(".");
		String linea = null;
		Date fechaInici=null;
		Date fechaFi=null;
		Date fechafinaltrimestre1=null;
		Date fechafinaltrimestre2=null;
		boolean checkTutor=false;
		boolean checkPares=false;
		boolean checkTelegram=false;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		
	
			try {
				path2 = currDir.getCanonicalPath();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			File f = new File(path2 + "/git/ga2/WebContent/Settings/settings.txt");
			
			BufferedReader br = new BufferedReader(new FileReader(f));     
			if (br.readLine() == null) {
				
			
			
			}
			else{
				
			
			
			reader = new FileReader(f);
			BufferedReader flux = new BufferedReader(reader);

			while ((linea = flux.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(linea, ",");
				while (st.hasMoreTokens()) {

					try {
						
						fechaInici =formatter.parse(st.nextToken());
						fechaFi =formatter.parse(st.nextToken());
						fechafinaltrimestre1 =formatter.parse(st.nextToken());
						fechafinaltrimestre2 =formatter.parse(st.nextToken());


					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
					
					if(st.nextToken().equals("true")){ checkTutor=true;}
					else{checkTutor=false;}
					
					if(st.nextToken().equals("true")){ checkPares=true;}
					else{checkPares=false;}
					
					if(st.nextToken().equals("true")){ checkTelegram=true;}
					else{checkTelegram=false;}
					
				
				}

			}
			
			adminsettings.dataIniciCurs.setValue(fechaInici);
			adminsettings.dataFinalCurs.setValue(fechaFi);
			adminsettings.dataFinaltrimestre1.setValue(fechafinaltrimestre1);
			adminsettings.dataFinaltrimestre2.setValue(fechafinaltrimestre2);
			adminsettings.checkEmailTutors.setValue(checkTutor);
			adminsettings.checkEmailPares.setValue(checkPares);
			adminsettings.checkWhatsPares.setValue(checkTelegram);
			
			

		
		}
	}
		
}
