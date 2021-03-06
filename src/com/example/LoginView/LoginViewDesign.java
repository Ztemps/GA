package com.example.LoginView;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
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
public class LoginViewDesign extends CssLayout {
	protected VerticalLayout vMainLogin;
	protected VerticalLayout vLogin;
	protected Label txtTitle;
	protected TextField txtUsername;
	protected PasswordField txtPassword;
	protected Button bLogin;
	protected HorizontalLayout login_footer;
	protected Label footer_title;

	public LoginViewDesign() {
		Design.read(this);
	}
}
