package com.example.view.AdminView.CSV;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { … }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class AdminCSVUpload extends CssLayout {
	protected VerticalLayout mainStudent;
	protected Label txtUpStudents;
	protected VerticalLayout vStudents;
	protected VerticalLayout mainTeacer;
	protected Label txtUpTeachers;
	protected VerticalLayout vTeachers;

	public AdminCSVUpload() {
		Design.read(this);
	}
}
