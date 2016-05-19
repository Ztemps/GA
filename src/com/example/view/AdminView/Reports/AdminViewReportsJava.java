package com.example.view.AdminView.Reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.example.Reports.FinalReports;
import com.example.Reports.TrimestralReports;
import com.example.Templates.MainContentView;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

public class AdminViewReportsJava extends MainContentView {
	private File file;
	private AdminReportCSVUpload csv;
	Button generateReportTrimestre1;
	Button generateReportTrimestre2;
	Button generateReportTrimestre3;
	Button generateReportTotal;

	StreamResource sr = getTrimestral1Zip();
	StreamResource sr2 = getTrimestral2Zip();
	StreamResource sr3 = getTrimestral3Zip();
	StreamResource sr4 = getTrimestral4Zip();


	FileDownloader fileDownloader = new FileDownloader(sr);
	FileDownloader fileDownloader2 = new FileDownloader(sr2);
	FileDownloader fileDownloader3 = new FileDownloader(sr3);
	FileDownloader fileDownloader4 = new FileDownloader(sr4);

	TrimestralReports trimestralReports;
	FinalReports finalreports;
	/*
	 * private FileReciverTrimestre2 receiver2 = new FileReciverTrimestre2();
	 * private FileReciverTrimestre3 receiver3 = new FileReciverTrimestre3();
	 * private FileReciverTotal receiver4 = new FileReciverTotal();
	 */

	public AdminViewReportsJava() {
		csv = new AdminReportCSVUpload();
		trimestralReports = new TrimestralReports();
		finalreports = new FinalReports();
		
		buttonsSettings();

		// INFORMES TRIMESTRALS

		csv.mainTrimestral.addStyleName("whiteBackground");
		csv.mainTrimestral.setWidth("100%");
		csv.mainTotal.addStyleName("whiteBackground");
		csv.txtUpTrimestral.setValue("Carrega d'Informes Trimestrals");
		csv.txtUpTrimestral.addStyleName("marginTitle");
		csv.hTrimestral.removeAllComponents();

		// Add buttons to layout
		csv.hTrimestral.addStyleName("buttonsLayout");
		csv.hTrimestral.addComponent(generateReportTrimestre1);
		csv.hTrimestral.addComponent(generateReportTrimestre2);
		csv.hTrimestral.addComponent(generateReportTrimestre3);
		csv.hTotal.addComponent(generateReportTotal);
		

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
		fileDownloader.extend(generateReportTrimestre1);
		fileDownloader2.extend(generateReportTrimestre2);
		fileDownloader3.extend(generateReportTrimestre3);
		fileDownloader4.extend(generateReportTotal);
	}

	private StreamResource getTrimestral4Zip() {
		// TODO Auto-generated method stub
		StreamResource.StreamSource source = new StreamResource.StreamSource() {

			public InputStream getStream() {
				// return your file/bytearray as an InputStream
				
				Thread t1 = new Thread(){
					public void run(){
						finalreports.calcularResumenTotal();

					}
				};
				
				Thread t2 = new Thread(){
					public void run(){
						finalreports.calcularTotalporAlumnos();

					}
				};
				
				
				t1.start();
				t2.start();
				try {
					t1.join();
					t2.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				finalreports.closeAllConnections();
				
				
				
				File zip = new File(ZipFiles("totalcurs.zip", "/tmp/total"
						+ ""));
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
		StreamResource resource = new StreamResource(source, getFileName4());
		return resource;
		
	}
	
	
	private StreamResource getTrimestral3Zip() {
		// TODO Auto-generated method stub
		StreamResource.StreamSource source = new StreamResource.StreamSource() {

			public InputStream getStream() {
				// return your file/bytearray as an InputStream
				
				Thread t1 = new Thread(){
					public void run(){
						trimestralReports.calcularResumenTrimestre3();

					}
				};
				
				Thread t2 = new Thread(){
					public void run(){
						trimestralReports.calcularTercerTrimestre();

					}
				};
				
				Thread t3 = new Thread(){
					public void run(){
						trimestralReports.calcularResumen2Trimestre3();

					}
				};
				t1.start();
				t2.start();
				t3.start();
				try {
					t1.join();
					t2.join();
					t3.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				trimestralReports.closeAllConnections();
				
				
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
		StreamResource resource = new StreamResource(source, getFileName3());
		return resource;
		
	}

	private StreamResource getTrimestral2Zip() {
		// TODO Auto-generated method stub	
	        StreamResource.StreamSource source = new StreamResource.StreamSource() {

			public InputStream getStream() {
				// return your file/bytearray as an InputStream
				
				Thread t1 = new Thread(){
					public void run(){
						trimestralReports.calcularResumenTrimestre2();

					}
				};
				
				Thread t2 = new Thread(){
					public void run(){
						trimestralReports.calcularSegundoTrimestre();

					}
				};
				
				Thread t3 = new Thread(){
					public void run(){
						trimestralReports.calcularResumen2Trimestre2();

					}
				};
				t1.start();
				t2.start();
				t3.start();
				try {
					t1.join();
					t2.join();
					t3.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				trimestralReports.closeAllConnections();
				
				
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
		StreamResource resource = new StreamResource(source, getFileName2());
		return resource;
	}

	private StreamResource getTrimestral1Zip() {
		// TODO Auto-generated method stub
		StreamResource.StreamSource source = new StreamResource.StreamSource() {

			public InputStream getStream() {
				// return your file/bytearray as an InputStream
				Thread t1 = new Thread(){
					public void run(){
						trimestralReports.calcularResumenTrimestre1();

					}
				};
				
				Thread t2 = new Thread(){
					public void run(){
						trimestralReports.calcularPrimerTrimestre();

					}
				};
				
				Thread t3 = new Thread(){
					public void run(){
						trimestralReports.calcularResumen2Trimestre1();

					}
				};
				
				t1.start();
				t2.start();
				t3.start();
				try {
					t1.join();
					t2.join();
					t3.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				trimestralReports.closeAllConnections();
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
		
		// Button trimestral 3 reports
		generateReportTotal = new Button("Genera informe total del curs", FontAwesome.CLOUD_DOWNLOAD);
		generateReportTotal.setStyleName(ValoTheme.BUTTON_PRIMARY);
		generateReportTotal.addStyleName("settings");
		

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
