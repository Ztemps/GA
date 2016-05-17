package com.example.view.AdminView.Forms;

import com.example.Templates.MainContentView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class AdminViewFormsJava extends MainContentView {
	
	
	Button bInforme;
	
	public AdminViewFormsJava(){
		
		bInforme = new Button();
		
		bInforme.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		vHorizontalMain.addComponent(bInforme);
		
	}
	
}
