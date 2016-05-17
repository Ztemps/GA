package com.example.view.AdminView.Reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.ParseException;

import com.example.CSVLoader.CSVLoader;
import com.example.Reports.TrimestralReports;
import com.example.Templates.MainContentView;
import com.example.view.AdminView.AdminView;
import com.example.view.AdminView.CSV.AdminViewCSVUploadJava.FileReciverStudents;
import com.example.view.AdminView.CSV.AdminViewCSVUploadJava.FileReciverTeachers;
import com.vaadin.server.Page;
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

public class AdminViewReportsJava extends MainContentView {
	private File file;
	private AdminReportCSVUpload csv;
	/*private FileReciverTrimestre2 receiver2 = new FileReciverTrimestre2();
	private FileReciverTrimestre3 receiver3 = new FileReciverTrimestre3();
	private FileReciverTotal receiver4 = new FileReciverTotal();*/
	
	public AdminViewReportsJava(){
		csv = new AdminReportCSVUpload();
		buttonsSettings();

		// INFORMES TRIMESTRALS
		
		csv.txtUpTrimestral.setValue("Carrega d'Informes Trimestrals");

		csv.hTrimestral.addStyleName("csvstudent");
		csv.hTrimestral.removeAllComponents();

	//	csv.horizontalTrimestral.setComponentAlignment(uploadStudent, Alignment.MIDDLE_CENTER);
		
		
		//INFORMES ANUALS

		csv.txtUpTotal.setValue("Carrega d'Informes Anuals");

		csv.hTotal.addStyleName("csvstudent");
	//	csv.hTotal.removeAllComponents();
	//	csv.hTotal.addComponents(uploadtrimestre1);

	//	csv.horizontalTotal.setComponentAlignment(uploadStudent, Alignment.MIDDLE_CENTER);
		
		
	}
	
	
	
	

	private void buttonsSettings() {
		// TODO Auto-generated method stub
	//	vHorizontalMain.addComponent(csv);
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
	
	
	
}
