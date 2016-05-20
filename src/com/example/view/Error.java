/*******************************************************************************
 * Gestió d'Amonestacions v1.0
 *
 * Esta obra está sujeta a la licencia Reconocimiento-NoComercial-SinObraDerivada 4.0 Internacional de Creative Commons. 
 * Para ver una copia de esta licencia, visite http://creativecommons.org/licenses/by-nc-nd/4.0/.
 *  
 * @author Francisco Javier Casado Moreno - fcasasdo@elpuig.xeill.net 
 * @author Daniel Pérez Palacino - dperez@elpuig.xeill.net 
 * @author Gerard Enrique Paulino Decena - gpaulino@elpuig.xeill.net 
 * @author Xavier Murcia Gámez - xmurica@elpuig.xeill.net 
 *******************************************************************************/
package com.example.view;

import com.example.Templates.MainContentView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

public class Error extends MainContentView implements View {
	private static final long serialVersionUID = 1L;
	public static final String NAME = "Error";

	public Error() {

	}

	public void notif(String mensaje) {

		Notification notif = new Notification(mensaje, null, Notification.Type.ASSISTIVE_NOTIFICATION, true); // Contains
		// HTML

		// Customize it
		notif.show(Page.getCurrent());
		notif.setDelayMsec(500);
		notif.setPosition(Position.TOP_CENTER);
	}

	

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

		notif("DONDE VASSSSSSSSSSS VUELVE POR DONDE HAS VENIDO");
		getUI().getPage().setLocation("http://localhost:8082/GA/Error404");

	}

}
