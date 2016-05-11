package com.example.ga;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.example.LoginView.LoginView;
import com.example.Templates.TemplateView;
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
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("ga")
public class GaUI extends UI {

	
	//@VaadinServletConfiguration(productionMode = true, ui = GaUI.class, widgetset = "com.example.WarningManagement.widgetset.GestioamonestacionsWidgetset")
	public static class Servlet extends VaadinServlet  {
		@Override
	    protected void servletInitialized()
	            throws ServletException {
	        super.servletInitialized();

	    }
		
		

	}

	@Override
	protected void init(VaadinRequest request) {

		getPage().setTitle("Gestió d'Amonestacions El Puig Castellar");
		
		addStyleName(ValoTheme.UI_WITH_MENU);
		
		String name = VaadinServlet.getCurrent().getServletName();
		System.out.println(name);

		new Navigator(this, this);
		setTheme("ga");
		getNavigator().addView(LoginView.NAME, LoginView.class);//
		getNavigator().addView(TemplateView.NAME, TemplateView.class);//
		getNavigator().addView(TeacherView.NAME, TeacherView.class);//
		getNavigator().addView(AdminView.NAME, AdminView.class);//
		getNavigator().addView(TutorView.NAME, TutorView.class );
		//Falta implementar la vista de tutor

		getUI().getConnectorTracker().cleanConnectorMap();
		getUI().getNavigator().navigateTo(LoginView.NAME);
//			
			
		//	getSession().setAttribute("user", username)

		
		
	
		// Defino la vista principal que se mostrara nada mas carge la app

//		MainView main = new MainView();
//		setContent(main);
//
//		HorizontalLayout hl = new HorizontalLayout();
//		hl.setSpacing(true);
//
//		grid.setColumns("firstName", "lastName", "email");
//		main.addComponent(hl);
//		hl.addComponent(grid);
//		hl.addComponent(grid2);
//
//		main.setExpandRatio(hl, 20);
	}



}