
package com.example.view.AdminView.CSV;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ResourceBundle;

import com.example.CSVLoader.CSVLoader;
import com.example.Pdf.generatePDF;
import com.example.Templates.MainContentView;
import com.example.view.AdminView.AdminView;
import com.google.gwt.thirdparty.guava.common.io.Files;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.ChangeEvent;
import com.vaadin.ui.Upload.ChangeListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * Gestió d'Amonestacions v1.0
 *
 * Esta obra está sujeta a la licencia
 * Reconocimiento-NoComercial-SinObraDerivada 4.0 Internacional de Creative
 * Commons. Para ver una copia de esta licencia, visite
 * http://creativecommons.org/licenses/by-nc-nd/4.0/.
 * 
 * @author Francisco Javier Casado Moreno - fcasado@elpuig.xeill.net
 * @author Daniel Pérez Palacino - dperez@elpuig.xeill.net
 * @author Gerard Enrique Paulino Decena - gpaulino@elpuig.xeill.net
 * @author Xavier Murcia Gámez - xmurcia@elpuig.xeill.net
 * 
 * 
 *         Clase necesaria para poder subir archivos CSV
 * 
 */

public class AdminViewCSVUploadJava extends MainContentView {

	private FileReciverStudents receiver = new FileReciverStudents();
	private FileReciverTeachers receiver2 = new FileReciverTeachers();
	private AdminCSVUpload csv;
	private File file;
	private Upload uploadStudent;
	private Upload uploadTeacher;
	private ResourceBundle rb = ResourceBundle.getBundle("GA");
	private Window window;
	private FileResource resource;

	public AdminViewCSVUploadJava() throws IOException {
		csv = new AdminCSVUpload();
		uploadTeacher = new Upload("", receiver2);
		uploadStudent = new Upload("", receiver);

		buttonsSettings();
		WindowProperties();

		FileResource resource = new FileResource(new File(rb.getString("upload_icon")));
		Image logo = new Image("", resource);
		logo.setWidth("15%");
		logo.setHeight("15%");

		FileResource resourceTeacher = new FileResource(new File(rb.getString("upload_icon")));
		Image upload_teacher = new Image("", resourceTeacher);
		upload_teacher.setWidth("15%");
		upload_teacher.setHeight("15%");

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

		csv.txtUpStudents.setValue("Alumnes");
		csv.txtUpStudents.addStyleName("upload-title");
		csv.vStudents.addStyleName("whiteBackground");
		csv.vStudents.addComponents(logo, uploadStudent);
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
		csv.txtUpTeachers.setValue("Professors");
		csv.txtUpTeachers.addStyleName("upload-title");
		csv.vTeachers.addComponents(upload_teacher, uploadTeacher);
		csv.vTeachers.setComponentAlignment(uploadTeacher, Alignment.MIDDLE_CENTER);

	}

	/**
	 * Configuración de la ventana emergente de la amonestación
	 */
	private void WindowProperties() {

		window = new Window();
		window.setHeight("95%");
		window.setWidth("95%");
		window.setDraggable(false);
		window.setModal(true);
		window.setVisible(false);
		window.setCaption("Visualització de l'amonestació");
		window.center();

	}

	/**
	 * Configuración de botones y estilos
	 */
	private void buttonsSettings() {

		vHorizontalMain.addComponent(csv);
		bAdd.setVisible(false);
		buttonEdit.setVisible(false);
		bDelete.setVisible(false);
		bRegister.setVisible(false);
		txtSearch.setVisible(false);
		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		txtTitle.setValue("Càrrega CSV");
		csv.bButtonHelp.setReadOnly(true);
	}

	/**
	 * Clase que implementa Receiver y SuccededListener, para poder subir el
	 * archivo del usuario
	 * 
	 */
	public class FileReciverStudents implements Receiver, SucceededListener {
		CSVLoader csvloader = new CSVLoader();

		public FileReciverStudents() {

		}

		/**
		 * Sube el archivo del usuario, comprueba que sea el formato correcto.
		 * 
		 * @param filename
		 *            ruta del fichero del usuario
		 * @param mimeType
		 *            extensión del archivo
		 * 
		 * @return salida del flujo de datos.
		 */
		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			FileOutputStream fos = null; // Stream to write to
			String path = null;

			String extension = filename.substring(filename.indexOf("."));

			if (extension.equals(".csv")) {
				try {
					File currDir = new File(".");
					try {
						path = currDir.getCanonicalPath();
					} catch (IOException e) {
						e.printStackTrace();
					}

					file = new File(rb.getString("path_csv") + filename);
					fos = new FileOutputStream(file);

				} catch (FileNotFoundException e) {
					new Notification("No s'ha pogut obrir el fitxer", e.getMessage(), Notification.Type.ERROR_MESSAGE)
							.show(Page.getCurrent());
					return null;
				}

			} else {
				new Notification("Error al carregar el fitxer: Comproba que el format del" + "fitxer sigui .csv",
						Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());

				getUI().getNavigator().navigateTo(AdminView.NAME);
				AdminView.viewCsv();

				return null;

			}

			return fos;
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			try {
				String ext = Files.getFileExtension(file.getAbsolutePath());

				if (file.exists()) {
					csvloader.loadStudents(file);
				}

			} catch (IOException | SQLException | ParseException e) {
				new Notification("El fitxer no está estructurat com ha de ser", Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
			}

		}
	}

	/**
	 * Clase que implementa Receiver y SuccededListener, para poder subir el
	 * archivo del usuario
	 * 
	 */
	public class FileReciverTeachers implements Receiver, SucceededListener {
		public File file;
		CSVLoader csvloader = new CSVLoader();

		public FileReciverTeachers() {

		}

		/**
		 * Sube el archivo del usuario, comprueba que sea el formato correcto.
		 * 
		 * @param filename
		 *            ruta del fichero del usuario
		 * @param mimeType
		 *            extensión del archivo
		 * 
		 * @return salida del flujo de datos.
		 */
		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			FileOutputStream fos = null;

			String extension = filename.substring(filename.indexOf("."));
			if (extension.equals(".csv")) {

				try {
					// Open the file for writing.
				

					file = new File(rb.getString("path_csv") + filename);
					fos = new FileOutputStream(file);
				} catch (final java.io.FileNotFoundException e) {
					new Notification("No s'ha pogut obrir el fitxer", e.getMessage(), Notification.Type.ERROR_MESSAGE)
							.show(Page.getCurrent());
					return null;
				}
			} else {
				new Notification("Error al carregar el fitxer: Comproba que el format del" + "fitxer sigui .csv",
						Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());


				getUI().getNavigator().navigateTo(AdminView.NAME);
				AdminView.viewCsv();

				return null;

			}

			return fos;
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			try {
				csvloader.loadTeachers(file);
			} catch (IOException | SQLException | ParseException | NoSuchAlgorithmException e) {
				new Notification("El fitxer no está estructurat com ha de ser", Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
			}

		}
	}

}
