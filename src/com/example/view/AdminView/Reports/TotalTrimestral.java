package com.example.view.AdminView.Reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.example.Reports.FinalReports;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;

public class TotalTrimestral {

	private StreamResource sr4;
	private FileDownloader file;
	private FinalReports finalreports;
	private ResourceBundle rb = ResourceBundle.getBundle("GA");
	private AdminReportCSVUpload csv;

	public TotalTrimestral() {
		// TODO Auto-generated constructor stub

		csv = new AdminReportCSVUpload();
		sr4 = getTrimestral();
		file = new FileDownloader(sr4);
		finalreports = new FinalReports();
		csv.generateReportTotal = new Button();
		file.extend(csv.generateReportTotal);

	}

	private StreamResource getTrimestral() {
		// TODO Auto-generated method stub

		StreamResource.StreamSource source = new StreamResource.StreamSource() {

			public InputStream getStream() {
				// return your file/bytearray as an InputStream

				Thread t1 = new Thread() {
					public void run() {
						finalreports.calcularResumenTotal();

					}
				};

				Thread t2 = new Thread() {
					public void run() {
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
				} finally {
					notif("Informe generado correctamente");

				}

				return targetStream;

			}
		};
		StreamResource resource = new StreamResource(source, getFileName());

		return resource;

	}

	private String zipFolder(String destZipFile, String srcFolder) throws Exception {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(fileWriter);

		addFolderToZip("", srcFolder, zip);
		zip.flush();
		zip.close();

		return destZipFile;
	}

	private void addFileToZip(String path, String srcFile, ZipOutputStream zip) throws Exception {

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

	private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
		File folder = new File(srcFolder);

		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			} else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
			}
		}
	}

	public void notif(String mensaje) {

		Notification notif = new Notification(mensaje, null, Notification.Type.WARNING_MESSAGE, true); // Contains

		notif.show(Page.getCurrent());
		notif.setDelayMsec(3000);
		notif.setPosition(Position.BOTTOM_CENTER);

	}
	private String getFileName() {
		// TODO Auto-generated method stub

		String suggestedSaveFile = "InformeTotalCurs.zip";

		return suggestedSaveFile;
	}
}
