package com.example.view.AdminView.Settings;

import com.example.Templates.MainContentView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Window;

public class AdminViewSettingsJava extends MainContentView {

	private static AdminViewSettings adminsettings;

	public AdminViewSettingsJava() {

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
		
		txtSearch.setVisible(false);
		clearTxt.setVisible(false);
		bAdd.setVisible(false);
		bDelete.setVisible(false);
		buttonEdit.setVisible(false);
		bRegister.setVisible(false);
		
	}
}
