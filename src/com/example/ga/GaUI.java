package com.example.ga;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.example.LoginView.LoginView;
import com.example.view.AdminView.AdminView;
import com.example.view.TeacherView.TeacherView;
import com.example.view.TutorView.TutorView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


/**
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
 * 
 * 		Classe UI, la clase principal que arranca el servlet
 * 
 */

@SuppressWarnings("serial")
@Theme("gatheme")
public class GaUI extends UI {
	private static final String TITLE = "Gestió d'Amonestacions El Puig Castellar";
	private InlineDateField datePicker;
	public static class Servlet extends VaadinServlet {
		@Override
		protected void servletInitialized() throws ServletException {
			super.servletInitialized();

		}
	}
	
	/**
	 * En el método init cargamos todas las vista de nuestra aplicación
	 * 
	 */
	
	@Override
	protected void init(VaadinRequest request) {

		datePicker = new InlineDateField();
		datePicker.setLocale(java.util.Locale.getDefault());
		getPage().setTitle(TITLE);
		addStyleName(ValoTheme.UI_WITH_MENU);

		new Navigator(this, this);
		getNavigator().addView(LoginView.NAME, LoginView.class);//
		getNavigator().addView(TeacherView.NAME, TeacherView.class);//
		getNavigator().addView(AdminView.NAME, AdminView.class);//
		getNavigator().addView(TutorView.NAME, TutorView.class);

		getUI().getConnectorTracker().cleanConnectorMap();
		getUI().getNavigator().navigateTo(LoginView.NAME);

	}

}