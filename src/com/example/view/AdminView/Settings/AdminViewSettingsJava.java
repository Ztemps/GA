package com.example.view.AdminView.Settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import com.example.Dates.ConverterDates;
import com.example.Entities.User;
import com.example.Logic.UserJPAManager;
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

/**
 * 
 * Gestió d'Amonestacions v1.0
 *
 * Esta obra está sujeta a la licencia Reconocimiento-NoComercial-SinObraDerivada 4.0 Internacional de Creative Commons. 
 * Para ver una copia de esta licencia, visite http://creativecommons.org/licenses/by-nc-nd/4.0/.
 *  
 * @author Francisco Javier Casado Moreno - fcasado@elpuig.xeill.net 
 * @author Daniel Pérez Palacino - dperez@elpuig.xeill.net 
 * @author Gerard Enrique Paulino Decena - gpaulino@elpuig.xeill.net 
 * @author Xavier Murcia Gámez - xmurcia@elpuig.xeill.net 
 * 
 * 
 * 		Clase que permite hacer configuraciones varias al administrador.
 * 
 */

public class AdminViewSettingsJava extends MainContentView {

	private AdminViewSettings adminsettings;
	private ConverterDates dates;
	private ResourceBundle rb = ResourceBundle.getBundle("GA");
	private User usuari;
	UserJPAManager MA;

	public AdminViewSettingsJava() {
		adminsettings = new AdminViewSettings();
		NewPassTextFieldProperties();
		GeneralSettings();
		listeners();

		try {
			readFile();
		} catch (ReadOnlyException | ConversionException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		adminsettings.checkOldPass.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				try {
					CheckPassword();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		
		adminsettings.enterNewPass.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				MA = new UserJPAManager();
				MessageDigest md = null;
				try {
					md = MessageDigest.getInstance("SHA-1");
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HexBinaryAdapter hbinary = new HexBinaryAdapter();
				String password = adminsettings.newPass.getValue();
				String confirmpass = adminsettings.confirmNewPass.getValue();
				
				if(password.equals(confirmpass)){
					String passwordhash = hbinary.marshal(md.digest(password.getBytes())).toLowerCase();
					System.out.println("Encriptada: " + passwordhash);

					int id = Integer.parseInt(getUI().getSession().getAttribute("id").toString());

					
					MA.updateUser(new User(id, passwordhash));
					MA.closeTransaction();
					notif("Contraseña nova acceptada. ");
					ShowNewPassTextField();
					ClearFields();
				}
				else{
					notifWrong("La contrasenya nova no coincideix als dos camps. Torni a intentar-ho.");
					ShowNewPassTextField();
					ClearFields();
				}
				

			}
		});
		vHorizontalMain.removeAllComponents();
		vHorizontalMain.addComponent(adminsettings);

	}
	
	
	/**
	 * Propiedades del campo de texto para introducir nueva contraseña
	 * */
	private void NewPassTextFieldProperties() {
		
		adminsettings.changePassLayout.setCaption("Canvi de password. Introdueixi password actual. ");
		adminsettings.newPass.setVisible(false);	
		adminsettings.confirmNewPass.setVisible(false);
		adminsettings.enterNewPass.setVisible(false);
	}
	
	public void ClearFields() {
		adminsettings.oldPass.clear();
		adminsettings.newPass.clear();
		adminsettings.confirmNewPass.clear();
	}
	
	
	/**
	 * Muestra el nuevo campo de texto para nuevo password si el campo 
	 * de password antiguo es visible, si no al revés.
	 * */
	private void ShowNewPassTextField() {

		if (adminsettings.oldPass.isVisible()) {

			adminsettings.oldPass.setVisible(false);
			adminsettings.checkOldPass.setVisible(false);
			adminsettings.changePassLayout.setCaption("Introdueixi password nou als dos camps. ");
			adminsettings.newPass.setVisible(true);
			adminsettings.confirmNewPass.setVisible(true);
			adminsettings.enterNewPass.setVisible(true);
		} else {
			adminsettings.oldPass.setVisible(true);
			adminsettings.checkOldPass.setVisible(true);
			adminsettings.newPass.setVisible(false);
			adminsettings.changePassLayout.setCaption("Introdueixi password actual. ");
			adminsettings.confirmNewPass.setVisible(false);
			adminsettings.enterNewPass.setVisible(false);
		}
	}

	
	/**
	 * Estilos y componentes principales de la vista. Configuración de botones y etiquetas.
	 * */
	private void GeneralSettings() {

		adminsettings.checkEmailPares.addStyleName("settings");
		adminsettings.checkWhatsPares.addStyleName("settings");
		adminsettings.startDateGrade.addStyleName("settings");
		adminsettings.endGradeDate3.addStyleName("settings");
		adminsettings.firstTrimesterEndDate.addStyleName("settings");
		adminsettings.secondTrimesterEndDate.addStyleName("settings");
		adminsettings.secondTrimesterStartDate.addStyleName("settings");
		adminsettings.thirdTrimesterStartDate3.addStyleName("settings");
		
	

		/*adminsettings.oldPass.setCaption("Introdueixi contrasenya actual per canviar. ");
		adminsettings.newPass.setCaption("Introdueixi nova contrasenya. ");
		adminsettings.confirmNewPass.setCaption("Repeteixi nova contrasenya");*/
		
		
		
		adminsettings.checkEmailTutors.setVisible(false);
		
		txtTitle.addStyleName("main-title");
		txtTitle.setValue("Configuració");
		txtSearch.setVisible(false);
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
	
	/**
	 *Configuración del fichero de texto donde el administrador guardará las fechas de inicio de trimestres 
	 * */
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

			Date datainicitrimestre1 = adminsettings.startDateGrade.getValue();
			datainicitrimestre1buena = dates.converterDate(datainicitrimestre1);
			Date datafinaltrimestre3 = adminsettings.endGradeDate3.getValue();
			datafinaltrimestre3buena = dates.converterDate(datafinaltrimestre3);
			Date datafintrimestre1 = adminsettings.firstTrimesterEndDate.getValue();
			datafintrimestre1buena = dates.converterDate(datafintrimestre1);
			Date datafintrimestre2 = adminsettings.secondTrimesterEndDate.getValue();
			datafintrimestre2buena = dates.converterDate(datafintrimestre2);
			Date fechaIniciTrimestre3 = adminsettings.thirdTrimesterStartDate3.getValue();
			fechaIniciTrimestre3buena = dates.converterDate(fechaIniciTrimestre3);
			Date fechaIniciTrimestre2 = adminsettings.secondTrimesterStartDate.getValue();
			fechaIniciTrimestre2buena = dates.converterDate(fechaIniciTrimestre2);

			boolean checkPares = adminsettings.checkEmailPares.getValue();
			boolean checkTelegram = adminsettings.checkWhatsPares.getValue();
			boolean checkTutor = adminsettings.checkEmailTutors.getValue();
			

			path2 = currDir.getCanonicalPath();
			File f = new File(rb.getString("file_settings"));
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

	/**
	 *Lectura del fichero de  las fechas de inicio de trimestres 
	 * */
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

		File f = new File(rb.getString("file_settings"));

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

			adminsettings.startDateGrade.setValue(fechaIniciTrimestre1);
			adminsettings.endGradeDate3.setValue(fechaFinalTrimestre3);
			adminsettings.firstTrimesterEndDate.setValue(fechafinaltrimestre1);
			adminsettings.secondTrimesterEndDate.setValue(fechafinaltrimestre2);
			adminsettings.checkEmailTutors.setValue(checkTutor);	
			adminsettings.checkEmailPares.setValue(checkPares);
			adminsettings.checkWhatsPares.setValue(checkTelegram);
			adminsettings.secondTrimesterStartDate.setValue(fechaIniciTrimestre2);
			adminsettings.thirdTrimesterStartDate3.setValue(fechaIniciTrimestre3);

		}
	}
	
	
	/**
	 * Comprobar que el password actual es correcto para poder mostrarle los nuevos
	 * campos de texto para introducir el nuevo
	 * */
	private void CheckPassword() throws ClassNotFoundException, SQLException {

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HexBinaryAdapter hbinary = new HexBinaryAdapter();

		String dbURL = "jdbc:postgresql:GAdb";
		Class.forName("org.postgresql.Driver");
		Connection conn = null;
		conn = DriverManager.getConnection(dbURL, "postgres", "postgres");

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery("SELECT id_docent,contrasenya,usuari,rol FROM usuari");

		String password = adminsettings.oldPass.getValue();
		String passwordhash = hbinary.marshal(md.digest(password.getBytes())).toLowerCase();
		System.out.println("NOMEOLVIDES : " + passwordhash);

		while (rs.next()) {
			usuari = new User();
			usuari.setId(Integer.parseInt(rs.getString(1)));
			usuari.setPassword(rs.getString(2));
			usuari.setUsername(rs.getString(3));
			usuari.setRol(rs.getString(4));

			if (usuari.getUsername().equals(getUI().getSession().getAttribute("user"))
					&& usuari.getPassword().equals(passwordhash)) {
				ShowNewPassTextField();
				break;

				// Si lo comprueba y es correcto se ocultan estos textfield y
				// aparece el de nueva contraseña
			}

		}

		if (!(usuari.getUsername().equals(getUI().getSession().getAttribute("user"))
				&& usuari.getPassword().equals(passwordhash))) {

			notif("Contraseña no existeix");
		}

		rs.close();
		st.close();
		conn.close();

	}
	
	
	/**Notificación personalizada*/
	public void notifWrong(String mensaje) {

		Notification notif = new Notification(mensaje, null, Notification.Type.WARNING_MESSAGE, true); // Contains
																												// HTML

		// Customize it
		notif.show(Page.getCurrent());
		notif.setDelayMsec(500);
		notif.setPosition(Position.TOP_CENTER);
	}

}
