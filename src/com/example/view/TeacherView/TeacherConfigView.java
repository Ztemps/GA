package com.example.view.TeacherView;

import java.security.InvalidKeyException;
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

import com.example.Encrypter.EncryptDecryptStringWithDES;
import com.example.Entities.User;
import com.example.Logic.UserJPAManager;
import com.example.Templates.MainContentView;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class TeacherConfigView extends MainContentView{

	private TextField oldPass= new TextField();
	private TextField newPass = new TextField();
	private Button acceptOldPass = new Button("Acceptar");
	private Button acceptNewPass = new Button("Acceptar");
	private User usuari;
	private VerticalLayout vl = new VerticalLayout();
	EncryptDecryptStringWithDES des;
	UserJPAManager MA = new UserJPAManager();
	
	public TeacherConfigView(){
		
		HideTemplateComponents();
		
		OldPassTextFieldProperties();
		NewPassTextFieldProperties();
		vLayoutComponents();
		vHorizontalMain.addComponent(vl);
	
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

				String password = newPass.getValue();
				try {
					SecretKey key = KeyGenerator.getInstance("DES").generateKey();
					try {
						EncryptDecryptStringWithDES.dcipher = Cipher.getInstance("DES");
						EncryptDecryptStringWithDES.dcipher.init(Cipher.ENCRYPT_MODE, key);
					} catch (NoSuchPaddingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidKeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String passwordEncrypted = EncryptDecryptStringWithDES.encrypt(password);
				System.out.println(passwordEncrypted);
				int id = Integer.parseInt(getUI().getSession().getAttribute("id").toString());
				System.out.println("IDDDDD "+id);
				MA.updateUser(id, passwordEncrypted);
				notif("Contraseña nova acceptada. ");
				
				// pass profe U+u+rz0n7ak=


			}
		});
	}
	
	private void vLayoutComponents(){
		vl.setSpacing(true);
		vl.addComponent(oldPass);
		vl.addComponent(acceptOldPass);
		vl.addComponents(newPass);
		vl.addComponent(acceptNewPass);
	}
	
	
	
	private void ShowNewPassTextField(){
				
		if(oldPass.isVisible()){
		
			oldPass.setVisible(false);
			acceptOldPass.setVisible(false);
			newPass.setVisible(true);
			acceptNewPass.setVisible(true);
		}else{
			oldPass.setVisible(true);
			acceptOldPass.setVisible(true);
			newPass.setVisible(false);
			acceptNewPass.setVisible(false);
		}
	}
	
	private void HideTemplateComponents(){
		
		bAdd.setVisible(false);
		bDelete.setVisible(false);
		bRegister.setVisible(false);
		buttonEdit.setVisible(false);
		horizontalTitle.setVisible(false);
		clearTxt.setVisible(false);
		txtSearch.setVisible(false);
		
	}
	
	private  void OldPassTextFieldProperties(){

		oldPass.setCaption("Introdueixi password actual");
		oldPass.setVisible(true);
	}
	
	private void NewPassTextFieldProperties(){

		newPass.setCaption("Introdueixi nou password");		
		newPass.setVisible(false);
		acceptNewPass.setVisible(false);
	}
	
	private void CheckPassword() throws ClassNotFoundException, SQLException{
		
		String dbURL = "jdbc:postgresql:GAdb";
		Class.forName("org.postgresql.Driver");
		Connection conn = null;
		conn = DriverManager.getConnection(dbURL, "postgres", "postgres");

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery("SELECT id_docent,contrasenya,usuari,rol FROM usuari");
		
		String password = oldPass.getValue();
		
		while (rs.next()) {
			usuari = new User();
				usuari.setId(Integer.parseInt(rs.getString(1)));
				usuari.setPassword(rs.getString(2));
				usuari.setUsername(rs.getString(3));
				usuari.setRol(rs.getString(4));

			if (usuari.getUsername().equals(getUI().getSession().getAttribute("user")) && usuari.getPassword().equals(password)){
				ShowNewPassTextField();
				break;
				
				//Si lo comprueba y es correcto se ocultan estos textfield y aparece el de nueva contraseña
			}
	
		}
		
		if (!(usuari.getUsername().equals(getUI().getSession().getAttribute("user")) && usuari.getPassword().equals(password))){
			
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