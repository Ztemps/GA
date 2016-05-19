package com.example.view.AdminView.CSV;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;

import com.example.CSVLoader.CSVLoader;
import com.example.Templates.MainContentView;
import com.example.view.AdminView.AdminView;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.ChangeEvent;
import com.vaadin.ui.Upload.ChangeListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.themes.ValoTheme;

public class AdminViewCSVUploadJava extends MainContentView {

	private FileReciverStudents receiver = new FileReciverStudents();
	private FileReciverTeachers receiver2 = new FileReciverTeachers();
	private AdminCSVUpload csv;
	private File file;
	private Upload uploadStudent;
	private Upload uploadTeacher;
	private Label studentlabel = new Label(
			"Selecciona un fitxer amb extensió .\"csv\" en que els seus camps estiguin separats per"
					+ " comes. \n El fitxer ha de tenir les següents columnes en el següent ordre: id de l'alumne, cognoms, nom, grup, date de naixement, pais, nacionalitat i telefons");

	public AdminViewCSVUploadJava() throws IOException {
		csv = new AdminCSVUpload();
		uploadTeacher = new Upload("", receiver2);
		uploadStudent = new Upload("", receiver);
		
		buttonsSettings();
		File currDir = new File(".");
		String path = currDir.getCanonicalPath();
		FileResource resource = new FileResource(
				new File(path + "/git/ga2/WebContent/VAADIN/themes/images/upload-icon.png"));

		// CSV STUDENTS
		uploadStudent.setButtonCaption(null);
		uploadStudent.addSucceededListener(receiver);
		uploadStudent.setStyleName(ValoTheme.BUTTON_PRIMARY);
		uploadStudent.addChangeListener(new ChangeListener() {

			@Override
			public void filenameChanged(ChangeEvent event) {

				uploadStudent.setButtonCaption("Carregar CSV");

			}
		});

		csv.txtUpStudents.setValue("Carrega de alumnes");
		csv.txtUpStudents.addStyleName("upload-title");
		csv.vStudents.addStyleName("whiteBackground");
		csv.vStudents.removeAllComponents();
		csv.vStudents.addComponents(studentlabel, new Image("", resource), uploadStudent);

		csv.vStudents.setComponentAlignment(uploadStudent, Alignment.MIDDLE_CENTER);

		// CSV DOCENTS
		uploadTeacher.setButtonCaption(null);
		uploadTeacher.addSucceededListener(receiver2);
		uploadTeacher.setStyleName(ValoTheme.BUTTON_PRIMARY);

		uploadTeacher.addChangeListener(new ChangeListener() {

			@Override
			public void filenameChanged(ChangeEvent event) {

				uploadTeacher.setButtonCaption("Carregar CSV");

			}
		});

		csv.vTeachers.addStyleName("whiteBackground");

		csv.txtUpTeachers.setValue("Carrega de profesors");
		csv.txtUpTeachers.addStyleName("upload-title");

		csv.vTeachers.removeAllComponents();
		csv.vTeachers.addComponents(new Image("", resource), uploadTeacher);
		csv.vTeachers.setComponentAlignment(uploadTeacher, Alignment.MIDDLE_CENTER);

	}

	private void buttonsSettings() {
		// TODO Auto-generated method stub
		vHorizontalMain.addComponent(csv);
		bAdd.setVisible(false);
		buttonEdit.setVisible(false);
		bDelete.setVisible(false);
		bRegister.setVisible(false);
		txtSearch.setVisible(false);
		clearTxt.setVisible(false);

		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		txtTitle.setValue("Carrega de CSV");

		// AdminViewCarregarCSVJava upload = new AdminViewCarregarCSVJava();

	}

	public class FileReciverStudents implements Receiver, SucceededListener {
		CSVLoader csvloader = new CSVLoader();

		public FileReciverStudents() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			FileOutputStream fos = null; // Stream to write to
			String path = null;

			String extension = filename.substring(filename.indexOf("."));

			if (extension.equals(".csv")) {

				try {
					// Open the file for writing.
					File currDir = new File(".");
					try {
						path = currDir.getCanonicalPath();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					file = new File(path + "/git/ga2/WebContent/CSVContent/" + filename);
					fos = new FileOutputStream(file);

				} catch (final java.io.FileNotFoundException e) {
					new Notification("No s'ha pogut obrir el fitxer", e.getMessage(), Notification.Type.ERROR_MESSAGE)
							.show(Page.getCurrent());
					return null;
				}

			} else {
				new Notification("Error al carregar el fitxer: Comproba que el format del" + "fitxer sigui .csv",
						Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());

				// Si no es csv, pim pam
				getUI().getNavigator().navigateTo(AdminView.NAME);
				AdminView.viewCsv();

				return null;

			}

			return fos;
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			try {

				if (file.exists()) {
					csvloader.loaderStudents(file);
				}

			} catch (IOException | SQLException | ParseException e) {
				new Notification("El fitxer no está estructurat com ha de ser", Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
			}

		}
	}

	public class FileReciverTeachers implements Receiver, SucceededListener {
		public File file;
		CSVLoader csvloader = new CSVLoader();

		public FileReciverTeachers() {

			// TODO Auto-generated constructor stub
		}

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			FileOutputStream fos = null; // Stream to write to
			String path = null;

			String extension = filename.substring(filename.indexOf("."));
			if (extension.equals(".csv")) {

				try {
					// Open the file for writing.
					File currDir = new File(".");
					try {
						path = currDir.getCanonicalPath();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					file = new File(path + "/git/ga2/WebContent/CSVContent/" + filename);
					fos = new FileOutputStream(file);
				} catch (final java.io.FileNotFoundException e) {
					new Notification("No s'ha pogut obrir el fitxer", e.getMessage(), Notification.Type.ERROR_MESSAGE)
							.show(Page.getCurrent());
					return null;
				}
			} else {
				new Notification("Error al carregar el fitxer: Comproba que el format del" + "fitxer sigui .csv",
						Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());

				// Si no es csv, pim pam

				return null;

			}

			return fos;
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			try {
				csvloader.loaderTeachers(file);
			} catch (IOException | SQLException | ParseException | NoSuchAlgorithmException e) {
				new Notification("El fitxer no está estructurat com ha de ser", Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
			}

		}
	}

}
