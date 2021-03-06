package com.example.view.AdminView.Settings;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.declarative.Design;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class AdminViewSettings extends CssLayout {
	protected DateField startDateGrade;
	protected DateField firstTrimesterEndDate;
	protected DateField secondTrimesterStartDate;
	protected DateField secondTrimesterEndDate;
	protected DateField thirdTrimesterStartDate3;
	protected DateField endGradeDate3;
	protected HorizontalLayout changePassLayout;
	protected PasswordField oldPass;
	protected Button checkOldPass;
	protected PasswordField newPass;
	protected PasswordField confirmNewPass;
	protected Button enterNewPass;
	protected CheckBox checkEmailPares;
	protected CheckBox checkWhatsPares;
	protected CheckBox checkEmailTutors;

	public AdminViewSettings() {
		Design.read(this);
	}
}
