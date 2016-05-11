package com.example.Templates;

import com.example.LoginView.LoginView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class TemplateView extends MainView implements View {

	public static final String NAME = "main";

	
	public TemplateView() {
		// TODO Auto-generated constructor stub
		
		logout.addClickListener(e->getUI().getNavigator().navigateTo(LoginView.NAME));
	
		content.setHeight("100%");
		
	}
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
