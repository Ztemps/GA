package com.example.view.AdminView.Warning;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
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
public class AdminWarning extends CssLayout {
	protected TextField nom;
	protected TextField cognoms;
	protected TextField materia;
	protected ComboBox comboProf;
	protected PopupDateField datefield;
	protected NativeSelect circunstancia;
	protected TextField tutor;
	protected TextField grup;
	protected NativeSelect caracter;
	protected TextField time;
	protected OptionGroup accio;
	protected OptionGroup motiu;
	protected OptionGroup motiu2;
	protected TextArea amotius;
	protected Button bcancelar;
	protected Button baceptar;

	public AdminWarning() {
		Design.read(this);
	}
}