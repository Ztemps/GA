package com.example.view.AdminView.Reports;

import java.io.File;
import org.zeroturnaround.zip.ZipUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.example.CSVLoader.CSVLoader;
import com.example.Reports.TrimestralReports;
import com.example.Templates.MainContentView;
import com.example.view.AdminView.AdminView;
import com.example.view.AdminView.CSV.AdminViewCSVUploadJava.FileReciverStudents;
import com.example.view.AdminView.CSV.AdminViewCSVUploadJava.FileReciverTeachers;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.themes.ValoTheme;

public class AdminViewReportsJava extends MainContentView {
	private File file;
	private AdminReportCSVUpload csv;
	Button generateReportTrimestre1;
	Button generateReportTrimestre2;
	Button generateReportTrimestre3;


	StreamResource sr = getTrimestral1Zip();
	StreamResource sr2 = getTrimestral2Zip();
	StreamResource sr3 = getTrimestral3Zip();

	FileDownloader fileDownloader = new FileDownloader(sr);
	FileDownloader fileDownloader2 = new FileDownloader(sr2);
	FileDownloader fileDownloader3 = new FileDownloader(sr3);

	TrimestralReports trimestrasReports;

	/*
	 * private FileReciverTrimestre2 receiver2 = new FileReciverTrimestre2();
	 * private FileReciverTrimestre3 receiver3 = new FileReciverTrimestre3();
	 * private FileReciverTotal receiver4 = new FileReciverTotal();
	 */

	public AdminViewReportsJava() {
		csv = new AdminReportCSVUpload();
		trimestrasReports = new TrimestralReports();
		buttonsSettings();

		// INFORMES TRIMESTRALS

		csv.mainTrimestral.addStyleName("whiteBackground");
		csv.txtUpTrimestral.addStyleName("settings");
		csv.txtUpTrimestral.setValue("Carrega d'Informes Trimestrals");
		csv.hTrimestral.addStyleName("csvstudent");
		csv.hTrimestral.removeAllComponents();

		// Add buttons to layout
		csv.hTrimestral.addComponent(generateReportTrimestre1);
		csv.hTrimestral.addComponent(generateReportTrimestre2);
		csv.hTrimestral.addComponent(generateReportTrimestre3);
		
		

		// csv.horizontalTrimestral.setComponentAlignment(uploadStudent,
		// Alignment.MIDDLE_CENTER);

		// INFORMES ANUALS
		csv.txtUpTotal.setValue("Carrega d'Informes Anuals");
		csv.hTotal.addStyleName("csvstudent");
		// csv.hTotal.removeAllComponents();
		// csv.hTotal.addComponents(uploadtrimestre1);

		// csv.horizontalTotal.setComponentAlignment(uploadStudent,
		// Alignment.MIDDLE_CENTER);
		fileDownloader.extend(generateReportTrimestre1);
		fileDownloader2.extend(generateReportTrimestre2);
		fileDownloader3.extend(generateReportTrimestre3);
	}

	private StreamResource getTrimestral3Zip() {
		// TODO Auto-generated method stub
		StreamResource.StreamSource source = new StreamResource.StreamSource() {

			public InputStream getStream() {
				// return your file/bytearray as an InputStream
				trimestrasReports.calcularResumenTrimestre3();
				trimestrasReports.calcularTercerTrimestre();
				trimestrasReports.calcularResumen2Trimestre3();
				File zip = new File(ZipFiles("trimestre3.zip", "/tmp/trimestre3"));
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
		StreamResource resource = new StreamResource(source, getFileName());
		return resource;
		
	}

	private StreamResource getTrimestral2Zip() {
		// TODO Auto-generated method stub
		StreamResource.StreamSource source = new StreamResource.StreamSource() {

			public InputStream getStream() {
				// return your file/bytearray as an InputStream
				trimestrasReports.calcularResumenTrimestre2();
				trimestrasReports.calcularSegundoTrimestre();
				trimestrasReports.calcularResumen2Trimestre2();
				File zip = new File(ZipFiles("trimestre2.zip", "/tmp/trimestre2"));
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
		StreamResource resource = new StreamResource(source, getFileName());
		return resource;
	}

	private StreamResource getTrimestral1Zip() {
		// TODO Auto-generated method stub
		StreamResource.StreamSource source = new StreamResource.StreamSource() {

			public InputStream getStream() {
				// return your file/bytearray as an InputStream
				trimestrasReports.calcularResumenTrimestre1();
				trimestrasReports.calcularPrimerTrimestre();
				trimestrasReports.calcularResumen2Trimestre1();
				File zip = new File(ZipFiles("trimestre1.zip", "/tmp/trimestre1"));
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
		StreamResource resource = new StreamResource(source, getFileName());
		return resource;
	}

	private String getFileName() {
		// TODO Auto-generated method stub

		String suggestedSaveFile = "Informe1erTrimestre.zip";

		return suggestedSaveFile;
	}

	private void buttonsSettings() {
		// TODO Auto-generated method stub

		// Button trimestral 1 reports
		generateReportTrimestre1 = new Button("Genera informe 1r trimestre", FontAwesome.CLOUD_DOWNLOAD);
		generateReportTrimestre1.setStyleName(ValoTheme.BUTTON_PRIMARY);
		generateReportTrimestre1.addStyleName("settings");

		// Button trimestral 2 reports
		generateReportTrimestre2 = new Button("Genera informe 2on trimestre", FontAwesome.CLOUD_DOWNLOAD);
		generateReportTrimestre2.setStyleName(ValoTheme.BUTTON_PRIMARY);
		generateReportTrimestre2.addStyleName("settings");

		// Button trimestral 3 reports
		generateReportTrimestre3 = new Button("Genera informe 3er trimestre", FontAwesome.CLOUD_DOWNLOAD);
		generateReportTrimestre3.setStyleName(ValoTheme.BUTTON_PRIMARY);
		generateReportTrimestre3.addStyleName("settings");
		

		vHorizontalMain.addComponent(csv);
		vHorizontalMain.setComponentAlignment(csv, Alignment.TOP_LEFT);
		bAdd.setVisible(false);
		buttonEdit.setVisible(false);
		bDelete.setVisible(false);
		bRegister.setVisible(false);
		txtSearch.setVisible(false);
		clearTxt.setVisible(false);

		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		txtTitle.setValue("Informes");

		// AdminViewCarregarCSVJava upload = new AdminViewCarregarCSVJava();

	}

	public String ZipFiles(String zipFile, String folder) {

		try {
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);

			Files.walk(Paths.get(folder)).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					String path = filePath.toString();
					System.out.println("PATHHH " + path);
					try {
						addToZipFile(path, zos);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			zos.close();
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return zipFile;
	}

	public static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

		System.out.println("Writing '" + fileName + "' to zip file");

		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1048576];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

}
