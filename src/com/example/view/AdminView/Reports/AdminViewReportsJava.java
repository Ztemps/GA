/*******************************************************************************
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
 *******************************************************************************/
package com.example.view.AdminView.Reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.example.Reports.FinalReports;
import com.example.Reports.TrimestralReports;
import com.example.Templates.MainContentView;
import com.example.view.AdminView.AdminView;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AdminViewReportsJava extends MainContentView {
	private File file;
	private AdminReportCSVUpload csv;

	private StreamResource sr = getTrimestral1Zip();
	private StreamResource sr2 = getTrimestral2Zip();
	private StreamResource sr3 = getTrimestral3Zip();
	private StreamResource sr4 = getTrimestral4Zip();
	ResourceBundle rb = ResourceBundle.getBundle("GA");

	private FileDownloader fileDownloader = new FileDownloader(sr);
	private FileDownloader fileDownloader2 = new FileDownloader(sr2);
	private FileDownloader fileDownloader3 = new FileDownloader(sr3);
	private FileDownloader fileDownloader4 = new FileDownloader(sr4);


	TrimestralReports trimestralReports;
	FinalReports finalreports;

	public AdminViewReportsJava() {

		csv = new AdminReportCSVUpload();
		trimestralReports = new TrimestralReports();
		finalreports = new FinalReports();
		generalSettings();
		// INFORMES TRIMESTRALS

		csv.mainTrimestral.addStyleName("whiteBackground");
		csv.mainTrimestral.setWidth("100%");
		csv.mainTotal.addStyleName("whiteBackground");
		csv.txtUpTrimestral.setValue("Carrega d'Informes Trimestrals");
		csv.txtUpTrimestral.addStyleName("marginTitle");

		// Add buttons to layout
		csv.hTrimestral.addStyleName("buttonsLayout");

		// csv.horizontalTrimestral.setComponentAlignment(uploadStudent,
		// Alignment.MIDDLE_CENTER);

		// INFORMES ANUALS
		csv.txtUpTotal.setValue("Carrega d'Informes Anuals");
		csv.txtUpTotal.addStyleName("marginTitle");

		csv.hTotal.addStyleName("buttonsLayout");

		// csv.hTotal.removeAllComponents();
		// csv.hTotal.addComponents(uploadtrimestre1);

		// csv.horizontalTotal.setComponentAlignment(uploadStudent,
		// Alignment.MIDDLE_CENTER);
		fileDownloader.extend(csv.generateReportTrimestre1);
		fileDownloader2.extend(csv.generateReportTrimestre2);
		fileDownloader3.extend(csv.generateReportTrimestre3);
		fileDownloader4.extend(csv.generateReportTotal);

		vHorizontalMain.removeAllComponents();
		vHorizontalMain.addComponent(csv);

	}

	/**
	 * 
	 */

	public void notif(String mensaje) {

		Notification notif = new Notification(mensaje, null, Notification.Type.WARNING_MESSAGE, true); // Contains
		
		notif.show(Page.getCurrent());
		notif.setDelayMsec(3000);
		notif.setPosition(Position.BOTTOM_CENTER);

	}
	private StreamResource getTrimestral4Zip() {
		// TODO Auto-generated method stub


		StreamResource.StreamSource source = new StreamResource.StreamSource() {

			public InputStream getStream() {
				// return your file/bytearray as an InputStream

			
						finalreports.calcularResumenTotal();

				

		
						finalreports.calcularTotalporAlumnos();

			

				finalreports.closeAllConnections();

				File zip = null;
				try {
					zip = new File(zipFolder(rb.getString("location_zip"), rb.getString("zip_folder")));

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				InputStream targetStream = null;
				try {
					targetStream = new FileInputStream(zip);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					notif("Informe generado correctamente");

				}

				return targetStream;

			}
		};
		StreamResource resource = new StreamResource(source, getFileName4());

		return resource;

	}

	private StreamResource getTrimestral3Zip() {
		// TODO Auto-generated method stub
		StreamResource.StreamSource source = new StreamResource.StreamSource() {

			public InputStream getStream() {
				// return your file/bytearray as an InputStream

						trimestralReports.calcularResumenTrimestre3();
						trimestralReports.calcularResumen2Trimestre3();


				
						trimestralReports.calcularTercerTrimestre();

			

				trimestralReports.closeAllConnections();

				File zip = null;
				try {
					zip = new File(zipFolder(rb.getString("locationt3_zip"), rb.getString("zipt3_folder")));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				InputStream targetStream = null;
				try {
					targetStream = new FileInputStream(zip);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return targetStream;

			}
		};
		StreamResource resource = new StreamResource(source, getFileName3());
		return resource;

	}

	private StreamResource getTrimestral2Zip() {
		// TODO Auto-generated method stub
		StreamResource.StreamSource source = new StreamResource.StreamSource() {

			public InputStream getStream() {
				// return your file/bytearray as an InputStream

			
						trimestralReports.calcularResumenTrimestre2();
						trimestralReports.calcularResumen2Trimestre2();

			
						trimestralReports.calcularSegundoTrimestre();


				

				trimestralReports.closeAllConnections();

				File zip = null;
				try {
					zip = new File(zipFolder(rb.getString("locationt2_zip"), rb.getString("zipt2_folder")));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				InputStream targetStream = null;
				try {
					targetStream = new FileInputStream(zip);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return targetStream;

			}
		};
		StreamResource resource = new StreamResource(source, getFileName2());
		return resource;
	}

	private StreamResource getTrimestral1Zip() {
		// TODO Auto-generated method stub
		StreamResource.StreamSource source = new StreamResource.StreamSource() {

			public InputStream getStream() {
				// return your file/bytearray as an InputStream
				Thread t1 = new Thread() {
					public void run() {
						trimestralReports.calcularResumenTrimestre1();
						trimestralReports.calcularResumen2Trimestre1();

					}
				};

				Thread t2 = new Thread() {
					public void run() {
						trimestralReports.calcularPrimerTrimestre();

					}
				};

				

				t1.start();
				t2.start();
				try {
					t1.join();
					t2.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				trimestralReports.closeAllConnections();
				File zip = null;
				try {

					zip = new File(zipFolder(rb.getString("locationt1_zip"), rb.getString("zipt1_folder")));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				InputStream targetStream = null;
				try {
					targetStream = new FileInputStream(zip);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return targetStream;

			}
		};
		StreamResource resource = new StreamResource(source, getFileName1());
		return resource;
	}

	private String getFileName1() {
		// TODO Auto-generated method stub

		String suggestedSaveFile = "Informe1erTrimestre.zip";

		return suggestedSaveFile;
	}

	private String getFileName2() {
		// TODO Auto-generated method stub

		String suggestedSaveFile = "Informe2erTrimestre.zip";

		return suggestedSaveFile;
	}

	private String getFileName3() {
		// TODO Auto-generated method stub

		String suggestedSaveFile = "Informe3erTrimestre.zip";

		return suggestedSaveFile;
	}

	private String getFileName4() {
		// TODO Auto-generated method stub

		String suggestedSaveFile = "InformeTotalCurs.zip";

		return suggestedSaveFile;
	}



	private void generalSettings() {
		// TODO Auto-generated method stub

		// Button trimestral 1 reports
		csv.generateReportTrimestre1.addStyleName(ValoTheme.BUTTON_PRIMARY);

		// csv.generateReportTrimestre1.addStyleName("settings");

		// Button trimestral 2 reports
		csv.generateReportTrimestre2.addStyleName(ValoTheme.BUTTON_PRIMARY);
		// csv.generateReportTrimestre2.addStyleName("settings");

		// Button trimestral 3 reports
		csv.generateReportTrimestre3.addStyleName(ValoTheme.BUTTON_PRIMARY);
		// csv.generateReportTrimestre3.addStyleName("settings");

		// Button reports total
		csv.generateReportTotal.addStyleName(ValoTheme.BUTTON_PRIMARY);

		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		txtTitle.setValue("Informes");

		bAdd.setVisible(false);
		buttonEdit.setVisible(false);
		bDelete.setVisible(false);
		bRegister.setVisible(false);
		txtSearch.setVisible(false);
		clearTxt.setVisible(false);

	}

	static public String zipFolder(String destZipFile, String srcFolder) throws Exception {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(fileWriter);

		addFolderToZip("", srcFolder, zip);
		zip.flush();
		zip.close();

		return destZipFile;
	}

	static private void addFileToZip(String path, String srcFile, ZipOutputStream zip) throws Exception {

		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			addFolderToZip(path, srcFile, zip);
		} else {
			byte[] buf = new byte[1024];
			int len;
			FileInputStream in = new FileInputStream(srcFile);
			zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
			while ((len = in.read(buf)) > 0) {
				zip.write(buf, 0, len);
			}
		}
	}

	static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
		File folder = new File(srcFolder);

		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			} else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
			}
		}
	}

}