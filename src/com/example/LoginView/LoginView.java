package com.example.LoginView;

import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

@Title("Login - Plataforma Gestión de Alumnos")

public class LoginView extends CustomComponent implements View, Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4731762934864687953L;
	public static final String NAME = "login";
	private final TextField user;
	private final Label title;
	private final PasswordField password;
	private User usuari;
	private final Button loginButton;
	private boolean isAdmin;
	public boolean conectado;
	private boolean login = true;
	private ArrayList<User> users;
	private Notification notif;
	private Notification notifMayus;
	private final Label Mayus;

	public LoginView() throws ClassNotFoundException, IOException {

		setSizeFull();

		// Creando una etiqueta con un stylo predefinido
		title = new Label("<center><p style=\"color:#1156a8\">Plataforma Gestió d'Amonestacions</p><center>",
				Label.CONTENT_RAW);

		// Create the user input field
		user = new TextField("Usuari:");
		user.setIcon(FontAwesome.USER);
		user.setWidth("150px");
		user.setRequired(true);
		user.setInvalidAllowed(false);

		// Create the password input field
		password = new PasswordField("Contrasenya:");
		password.setIcon(FontAwesome.LOCK);
		password.setWidth("150px");
		// password.addValidator(new PasswordValidator());
		password.setRequired(true);
		password.setValue("");
		password.setNullRepresentation("");

	

		// Create login button
		loginButton = new Button("Entra", this);
		loginButton.setEnabled(false);
		loginButton.setClickShortcut(KeyCode.ENTER);
		loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		// preparamos las notificacion


		Mayus = new Label("Mayus Activada");
		Mayus.setVisible(false);
		password.setNullSettingAllowed(true);

		password.addTextChangeListener(new TextChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7811790145041299537L;

			@Override
			public void textChange(TextChangeEvent event) {
				// TODO Auto-generated method stub

				if (user.getValue().length() != 0) {

					loginButton.setEnabled(true);
				} else if (user.getValue().length() == 0 && password.getValue().length() == 0) {
					loginButton.setEnabled(false);

				}
			}
		});

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(title, user, password, Mayus, loginButton);
		fields.setCaption("");
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();
		fields.addStyleName("ventanalogin");
		fields.setWidth("30%");
		fields.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
		fields.setComponentAlignment(user, Alignment.MIDDLE_CENTER);
		fields.setComponentAlignment(password, Alignment.MIDDLE_CENTER);
		fields.setComponentAlignment(Mayus, Alignment.MIDDLE_CENTER);
		fields.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);

		//
		// commit
		// The view root layout
		VerticalLayout viewLayout = new VerticalLayout(fields);
		viewLayout.setSizeFull();
		viewLayout.setResponsive(true);
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		// viewLayout.setHeight("100%");
		setCompositionRoot(viewLayout);
		fields.addStyleName("loginview");

	}

	// public void keyReleased( KeyEvent e ) {
	// loginButton.setEnabled(
	// user.getValue().length() != 0 && password.getValue().length() !=0
	// );
	// }


	public void notif(String mensaje){
		
		
		 Notification notif = new Notification(
               mensaje,
               null,
               Notification.Type.ASSISTIVE_NOTIFICATION,
               true); // Contains HTML

           // Customize it
		 notif.show(Page.getCurrent());
           notif.setDelayMsec(500);
           notif.setPosition(Position.TOP_CENTER);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// focus the username field when user arrives to the login view

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

		user.focus();
		// notif.show("Benvingut");

	}

	// Validar usuario base de datos

	// static void userValidator() throws SQLException {

	// }

	// Validator for validating the passwords
	/*
	 * private static final class PasswordValidator extends
	 * AbstractValidator<String> {
	 * 
	 * public PasswordValidator() { super("formato de contraseña no válido");
	 * 
	 * }
	 * 
	 * @Override protected boolean isValidValue(String value) { // // Password
	 * must be at least 8 characters long and contain at least // one number //
	 * if (value != null && (value.length() < 8 || !value.matches(".*\\d.*"))) {
	 * return false; } return true; }
	 * 
	 * @Override public Class<String> getType() { return String.class; } }
	 */

	@Override
	public void buttonClick(ClickEvent event) {
		try {
			LoginValidator();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		// Validate the fields using the navigator. By using validors for the
		// fields we reduce the amount of queries we have to use to the database
		// for wrongly entered passwords
		//

		/*
		 * if (!user.isValid() || !password.isValid()) { notif.show(
		 * "Introduce usuario y contraseña"); return; }
		 */

		// String username = this.user.getValue();
		// String password = this.password.getValue();
		//
		// Validate username and password with database here. For examples sake
		// I use a dummy username and password.
		//
		// boolean isValid = username.equals("gerard") &&
		// password.equals("gerard10");

		/*
		 * if(username.equals("admin")){
		 * 
		 * getSession().setAttribute("user", username);
		 * getUI().getNavigator().navigateTo(AdminView.NAME);//
		 * 
		 * }else{ getSession().setAttribute("user", username);
		 * getUI().getNavigator().navigateTo(ProfessorView.NAME); }
		 */

		// if (UserValid(username, password)) {
		// // Store the current user in the service session
		// getSession().setAttribute("user", username);
		//
		//
		// if (!isUserAdmin(username)) {
		// System.out.println("usuario no admin");
		//
		// // Navigate to main view
		//
		//
		// } else {
		// getUI().getNavigator().navigateTo(TemplateView.NAME);//
		//
		// System.out.println("usuario admin");
		//
		// // Navigate to main view
		// //getUI().getNavigator().navigateTo(MainViewAdmin.NAME);//
		//
		//
		// }
		//
		// } else {
		// notif.show("¡Usuario o Contraseña incorrectos!");
		//
		// // Wrong password clear the password field and refocuses it
		// this.password.setValue(null);
		// this.password.focus();
		//
		// }
	}

	/*
	 * public boolean isUserAdmin(String username) {
	 * 
	 * boolean isAdmin = false; String admin = "admin";
	 * 
	 * if (admin.equals(username)) { isAdmin = true; }
	 * 
	 * return isAdmin; }
	 */

	/*
	 * public boolean UserValid(String username, String password) {
	 * 
	 * List<User> usersValid; UserJPAManager MA = new UserJPAManager(); boolean
	 * valido = false;
	 * 
	 * usersValid = MA.listUsers();
	 * 
	 * for (int i = 0; i < usersValid.size(); i++) {
	 * 
	 * if (usersValid.get(i).getName().equals(username) &&
	 * usersValid.get(i).getPassword().equals(password)) {
	 * 
	 * System.out.println("Usuario valido: " + usersValid.get(i).getName());
	 * valido = true; } } return valido; }
	 */

	public void LoginValidator() throws ClassNotFoundException, SQLException {

		// Conecxión con la base de datos
		String dbURL = "jdbc:postgresql:GAdb";
		Class.forName("org.postgresql.Driver");
		Connection conn = null;
		conn = DriverManager.getConnection(dbURL, "postgres", "postgres");

		// Creamos una lista de resultados de la consulta que hacemos a la tabla
		// usuari.
		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery("SELECT id_docent,usuari,rol,contrasenya FROM usuari");

		// Cogemos los valores de los campos que rellena el usuario
		String username = this.user.getValue();
		String userpassword = this.password.getValue();

		try {
			// Mientras el resultset tenga resultados, cogemos los valores y
			// creamos un usuari con los valores de la consulta para
			// posteriormente
			// validar el login
			while (rs.next()) {
				usuari = new User();
				usuari.setId(Integer.parseInt(rs.getString(1)));
				usuari.setUsername(rs.getString(2));
				usuari.setRol(rs.getString(3));
				usuari.setPassword(rs.getString(4));

				// Si el usuario y el nombre coinciden y su rol es
				// administrador, se abre la vista del administrador
				if (usuari.getUsername().equals(username) && usuari.getPassword().equals(userpassword)) {

					break;

				}
				// Si el usuario o contraseña no son validos, se muestra un
				// mensaje emergente

			}

			if (!(usuari.getUsername().equals(username) && usuari.getPassword().equals(userpassword))) {

				notif("Usuari/Contraseña incorrectes");

			}
			
			if (usuari.getRol().equals("Administrador")) {

				FileWriter fw = new FileWriter("userList.txt");
				BufferedWriter br = new BufferedWriter(fw);
				br.write("true");
				br.close();
				
				setAttributeSession(username);
				AdminView adminView = new AdminView();
				
				getUI().getNavigator().navigateTo(AdminView.NAME);//


			}
			if (usuari.getRol().equals("Tutor")) {

				setAttributeSession(username);
			 	getUI().getNavigator().navigateTo(TutorView.NAME);

			}
			if (usuari.getRol().equals("Profesor")) {
				// Si el usuario y el nombre coinciden y su rol es
				// tutor, se abre la vista del tutor
				setAttributeSession(username);
				getUI().getNavigator().navigateTo(TeacherView.NAME);
			}
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
		System.out.println(getUI().getSession().getAttribute("id"));
		
	}

}
