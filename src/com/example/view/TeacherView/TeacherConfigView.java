package com.example.view.TeacherView;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import com.example.Encrypter.EncryptDecryptStringWithDES;
import com.example.Entities.User;
import com.example.Logic.UserJPAManager;
import com.example.Templates.MainContentView;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class TeacherConfigView extends MainContentView {

	private PasswordField oldPass = new PasswordField();
	private PasswordField newPass = new PasswordField();
	private Button acceptOldPass = new Button("Acceptar");
	private Button acceptNewPass = new Button("Acceptar");
	private Label titlepass = new Label("CANVI DE PASSWORD");
	private User usuari;
	private VerticalLayout vl = new VerticalLayout();
	EncryptDecryptStringWithDES des;
	UserJPAManager MA;

	public TeacherConfigView() {

		
		HideTemplateComponents();
		ButtonProperties();
		OldPassTextFieldProperties();
		NewPassTextFieldProperties();
		vLayoutComponents();
		titlepass.addStyleName("settings");
		vHorizontalMain.addComponent(vl);
		vHorizontalMain.setComponentAlignment(vl, Alignment.MIDDLE_CENTER);
		


		acceptOldPass.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				try {
					CheckPassword();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		acceptNewPass.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				MA = new UserJPAManager();
				MessageDigest md = null;
				try {
					md = MessageDigest.getInstance("SHA-1");
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HexBinaryAdapter hbinary = new HexBinaryAdapter();
				String password = newPass.getValue();

				/*
				 * try { SecretKey key =
				 * KeyGenerator.getInstance("DES").generateKey(); try {
				 * EncryptDecryptStringWithDES.dcipher =
				 * Cipher.getInstance("DES");
				 * EncryptDecryptStringWithDES.dcipher.init(Cipher.ENCRYPT_MODE,
				 * key); } catch (NoSuchPaddingException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); } catch
				 * (InvalidKeyException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); } } catch (NoSuchAlgorithmException e) {
				 * // TODO Auto-generated catch block e.printStackTrace(); }
				 * String passwordEncrypted =
				 * EncryptDecryptStringWithDES.encrypt(password);
				 */

				String passwordhash = hbinary.marshal(md.digest(password.getBytes())).toLowerCase();
				System.out.println("Encriptada: " + passwordhash);

				int id = Integer.parseInt(getUI().getSession().getAttribute("id").toString());
				System.out.println("ID " + id);
				System.out.println("NOMEOLVIDES : " + passwordhash);
				MA.updateUser(id, passwordhash);
				MA.closeTransaction();
				notif("Contraseña nova acceptada. ");
				ShowNewPassTextField();
				ClearFields();

			}
		});
	}

	private void vLayoutComponents() {
		vl.addStyleName("settingBackgroundTeacher");
		vl.setSpacing(true);
		vl.addComponent(titlepass);
		vl.addComponent(oldPass);
		vl.addComponent(acceptOldPass);
		vl.addComponents(newPass);
		vl.addComponent(acceptNewPass);
	}

	public void ButtonProperties() {

		acceptOldPass.setStyleName(ValoTheme.BUTTON_PRIMARY);
		acceptOldPass.setClickShortcut(KeyCode.ENTER);
		acceptOldPass.addStyleName("settings");
		
		acceptNewPass.setStyleName(ValoTheme.BUTTON_PRIMARY);
		acceptNewPass.setClickShortcut(KeyCode.ENTER);
		acceptNewPass.addStyleName("settings");
		oldPass.addStyleName("xyz");

		JavaScript.getCurrent().execute(
			    "document.getElementsByClassName('xyz')[0].setAttribute('autocomplete', 'off')");
		}

	public void ClearFields() {
		oldPass.clear();
		newPass.clear();
	}

	private void ShowNewPassTextField() {

		if (oldPass.isVisible()) {

			oldPass.setVisible(false);
			acceptOldPass.setVisible(false);
			newPass.setVisible(true);
			acceptNewPass.setVisible(true);
		} else {
			oldPass.setVisible(true);
			acceptOldPass.setVisible(true);
			newPass.setVisible(false);
			acceptNewPass.setVisible(false);
		}
	}

	private void HideTemplateComponents() {

		bAdd.setVisible(false);
		bDelete.setVisible(false);
		bRegister.setVisible(false);
		buttonEdit.setVisible(false);
		horizontalTitle.setVisible(false);
		clearTxt.setVisible(false);
		txtSearch.setVisible(false);

	}

	private void OldPassTextFieldProperties() {

		oldPass.setCaption("Introdueixi password actual");
		oldPass.addStyleName("settings");
		oldPass.setVisible(true);
	}

	private void NewPassTextFieldProperties() {

		newPass.setCaption("Introdueixi nou password");
		newPass.addStyleName("settings");
		newPass.setVisible(false);
		acceptNewPass.setVisible(false);
	}

	private void CheckPassword() throws ClassNotFoundException, SQLException {

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HexBinaryAdapter hbinary = new HexBinaryAdapter();

		String dbURL = "jdbc:postgresql:GAdb";
		Class.forName("org.postgresql.Driver");
		Connection conn = null;
		conn = DriverManager.getConnection(dbURL, "postgres", "postgres");

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery("SELECT id_docent,contrasenya,usuari,rol FROM usuari");

		String password = oldPass.getValue();
		String passwordhash = hbinary.marshal(md.digest(password.getBytes())).toLowerCase();
		System.out.println("NOMEOLVIDES : " + passwordhash);

		while (rs.next()) {
			usuari = new User();
			usuari.setId(Integer.parseInt(rs.getString(1)));
			usuari.setPassword(rs.getString(2));
			usuari.setUsername(rs.getString(3));
			usuari.setRol(rs.getString(4));

			if (usuari.getUsername().equals(getUI().getSession().getAttribute("user"))
					&& usuari.getPassword().equals(passwordhash)) {
				ShowNewPassTextField();
				break;

				// Si lo comprueba y es correcto se ocultan estos textfield y
				// aparece el de nueva contraseña
			}

		}

		if (!(usuari.getUsername().equals(getUI().getSession().getAttribute("user"))
				&& usuari.getPassword().equals(passwordhash))) {

			notif("Contraseña no existeix");
		}

		rs.close();
		st.close();
		conn.close();

	}

	public void notif(String mensaje) {

		Notification notif = new Notification(mensaje, null, Notification.Type.ASSISTIVE_NOTIFICATION, true); // Contains
																												// HTML

		// Customize it
		notif.show(Page.getCurrent());
		notif.setDelayMsec(500);
		notif.setPosition(Position.TOP_CENTER);
	}

}