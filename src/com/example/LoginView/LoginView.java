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
package com.example.LoginView;

import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.eclipse.jetty.security.authentication.SessionAuthentication;

import com.example.Entities.User;
import com.example.view.AdminView.AdminView;
import com.example.view.TeacherView.TeacherView;
import com.example.view.TutorView.TutorView;
import com.github.wolfie.popupextension.PopupExtension;
import com.vaadin.annotations.Title;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.ClientConnector.AttachEvent;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

@Title("Login - Plataforma Gestió d'Amonestacions")

public class LoginView extends LoginViewDesign implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4731762934864687953L;
	public static final String NAME = "login";
	private User usuari;
	private boolean isAdmin;
	public boolean conectado;
	private boolean login = true;
	private ArrayList<User> users;
	private Notification notif;
	private Notification notifMayus;

	public LoginView() throws ClassNotFoundException, IOException {

		// Creando una etiqueta con un stylo predefinido
		txtTitle = new Label("<center><p style=\"color:#1156a8\">Plataforma Gestió d'Amonestacions</p><center>",
				Label.CONTENT_RAW);

		txtUsername.setCaption("Usuari");
		txtPassword.setCaption("Contrasenya");
		txtUsername.setIcon(FontAwesome.USER);
		txtUsername.setInvalidAllowed(false);

		txtPassword.setIcon(FontAwesome.LOCK);

		bLogin.setEnabled(false);
		bLogin.setClickShortcut(KeyCode.ENTER);
		bLogin.addStyleName(ValoTheme.BUTTON_PRIMARY);

		txtPassword.addTextChangeListener(new TextChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7811790145041299537L;

			@Override
			public void textChange(TextChangeEvent event) {
				// TODO Auto-generated method stub

				if (txtUsername.getValue().length() != 0) {

					bLogin.setEnabled(true);
				} else if (txtUsername.getValue().length() == 0 && txtPassword.getValue().length() == 0) {
					bLogin.setEnabled(false);

				}
			}
		});

		vMainLogin.setStyleName(Reindeer.LAYOUT_BLUE);
		// viewLayout.setHeight("100%");
		// setCompositionRoot(viewLayout);
		vLogin.addStyleName("loginview");
		bLogin.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				try {
					LoginValidator();
				} catch (ClassNotFoundException | SQLException | NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void notif(String mensaje) {

		Notification notif = new Notification(mensaje, null, Notification.Type.ASSISTIVE_NOTIFICATION, true);

		// Customize it
		notif.show(Page.getCurrent());
		notif.setDelayMsec(500);
		notif.setPosition(Position.TOP_CENTER);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		FileWriter fw;
		try {
			fw = new FileWriter("userList.txt");
			BufferedWriter br = new BufferedWriter(fw);
			br.write("false");
			br.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		txtUsername.focus();

	}

	
			
	
	

	public void LoginValidator() throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {

		 MessageDigest md = MessageDigest.getInstance("SHA-1"); 
		 HexBinaryAdapter hbinary = new HexBinaryAdapter();
		
		 // Conecxión con la base de datos
		String dbURL = "jdbc:postgresql:GAdb";
		Class.forName("org.postgresql.Driver");
		Connection conn = null;
		conn = DriverManager.getConnection(dbURL, "postgres", "postgres");

		// Creamos una lista de resultados de la consulta que hacemos a la tabla
		// usuari.
		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery("SELECT id_docent,contrasenya,usuari,rol FROM usuari");

		// Cogemos los valores de los campos que rellena el usuario
		String username = this.txtUsername.getValue();
		String userpassword = this.txtPassword.getValue();
				
		// cifrar clave con SHA-1
		String passwordhash = hbinary.marshal(md.digest(userpassword.getBytes())).toLowerCase();

		try {
			// Mientras el resultset tenga resultados, cogemos los valores y
			// creamos un usuari con los valores de la consulta para
			// posteriormente
			// validar el login
			while (rs.next()) {
				usuari = new User();
					usuari.setId(Integer.parseInt(rs.getString(1)));
					usuari.setPassword(rs.getString(2));
					usuari.setUsername(rs.getString(3));
					usuari.setRol(rs.getString(4));

				// Si el usuario y el nombre coinciden y su rol es
				// administrador, se abre la vista del administrador
				if (usuari.getUsername().equals(username) && usuari.getPassword().equals(passwordhash)) break;
				// Si el usuario o contraseña no son validos, se muestra un
				// mensaje emergente
			}
			
			// comparamos otra vez si el usuario y el nombre son el mismo
			if (usuari.getUsername().equals(username) && usuari.getPassword().equals(passwordhash)){
				if (usuari.getRol().equals("Administrador")) {

					FileWriter fw = new FileWriter("userList.txt");
					BufferedWriter br = new BufferedWriter(fw);
					br.write("true");
					br.close();

					setAttributeSession(username);
					getUI().getNavigator().navigateTo(AdminView.NAME);//

				}else if (usuari.getRol().equals("Tutor")) {

					setAttributeSession(username);
					getUI().getNavigator().navigateTo(TutorView.NAME);

				}else if(usuari.getRol().equals("Professor")) {
					// Si el usuario y el nombre coinciden y su rol es
					// tutor, se abre la vista del tutor
					setAttributeSession(username);
					getUI().getNavigator().navigateTo(TeacherView.NAME);
				}
				
			}else{
				// si no son el mismo la contraseña o usuario son incorrectos
				notif("Usuari/Contraseña incorrectes");
			}
			
			
//			if (!(usuari.getUsername().equals(username) && usuari.getPassword().equals(userpassword))) {

				 // notif("Usuari/Contraseña incorrectes");

		//	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Cerramos la conexion con la base de datos
		rs.close();
		st.close();
		conn.close();

	}

	public void setAttributeSession(String username) {

		getUI().getSession().setAttribute("user", username);
		getUI().getSession().setAttribute("id", usuari.getId());
		getUI().getSession().setAttribute("login", "true");

		
	}

}
