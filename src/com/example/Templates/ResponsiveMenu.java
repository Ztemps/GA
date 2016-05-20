package com.example.Templates;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.themes.ValoTheme;

import elemental.html.Notification;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ResponsiveMenu extends CustomComponent{
	
		public static final String ID = "dashboard-menu";
	    public static final String REPORTS_BADGE_ID = "dashboard-menu-reports-badge";
	    public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
	    private static final String STYLE_VISIBLE = "valo-menu-visible";
	    com.vaadin.ui.Notification notif;
	    
	public ResponsiveMenu(){
		
		setPrimaryStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();


        setCompositionRoot(buildContent());
		
	}
	
    private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildToggleButton());

        return menuContent;
    }
	
	
	
	 private Component buildToggleButton() {
	        Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	                if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
	                    getCompositionRoot().removeStyleName(STYLE_VISIBLE);
	                } else {
	                    getCompositionRoot().addStyleName(STYLE_VISIBLE);
	                }
	            }
	        });
	        valoMenuToggleButton.setIcon(FontAwesome.LIST);
	        valoMenuToggleButton.addStyleName("valo-menu-toggle");
	        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
	        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
	        return valoMenuToggleButton;
	    }
}
