
package com.example.view.AdminView.Teacher;



import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.postgresql.util.PSQLException;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.example.Entities.Group;
import com.example.Entities.Teacher;
import com.example.Entities.Tutor;
import com.example.Entities.User;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.TeachersJPAManager;
import com.example.Logic.TutorJPAManager;
import com.example.Logic.UserJPAManager;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.client.ui.VGridLayout;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;


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
 **/

public class AdminViewTeacherFormJava extends AdminViewTeacherForm {

	
	private static final long serialVersionUID = 1038815527250707361L;
	private TeachersJPAManager MA;
	private UserJPAManager MA2;
	private TutorJPAManager MA3;
	ResourceBundle rb = ResourceBundle.getBundle("GA");
	
	private String username;
	private String rol =  rb.getString("tutor_role");
	private String password = rb.getString("default_password");

	@SuppressWarnings("deprecation")
	public AdminViewTeacherFormJava() {

		nom.focus();

		selectGroup.setVisible(false);

		isTutor.addListener(new Property.ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) {
				boolean value = (Boolean) event.getProperty().getValue();

				if (value) {
					selectGroup.setVisible(true);
				} else {
					selectGroup.setVisible(false);
					selectGroup.clear();
				}
			}
		});

	}

	
	/**
	 * Inserta un nuevo profesor en la base de datos, comprueba si es tutor o no
	 * y además añade su nombre de usuario correspondiente. Asigna la contraseña
	 * por defecto que se encuentra en el archivo properties.
	 * */
	public void insertDocent(Teacher teacher) {

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HexBinaryAdapter hbinary = new HexBinaryAdapter();

		MA = new TeachersJPAManager();
		MA2 = new UserJPAManager();
		MA3 = new TutorJPAManager();
		String passwordhash = hbinary.marshal(md.digest(password.getBytes())).toLowerCase();
		System.out.println("Encriptada: " + passwordhash);

		String group;
		String[] aux;
		aux = teacher.getCognoms().split(" ");
		
		
		MA.addTeacher(new Teacher(teacher.getNom(), teacher.getCognoms(), teacher.getEmail()));

		int id = MA.getIdDocent(teacher.getEmail());

		try {

			if (txtUsername.getValue().length() > 0)
				username = txtUsername.getValue();
			else
				username = teacher.getNom().substring(0, 1) + aux[0];

		} catch (StringIndexOutOfBoundsException e) {

			notif("Selecciona el grup");
		}

		if (isTutor.getValue()) {
			group = this.selectGroup.getValue().toString();
			MA3.addTutor(new Tutor(id, group));
			
			rol = "Tutor";
		}else{
			
			rol = "Professor";
		}
		MA2.addUser(new User(id, passwordhash, username.toLowerCase(), rol));
		MA.closeTransaction();
		MA2.closeTransaction();
		
		
	}
	
	
	/**Notificación
	 *  personalizada */
	public void notif(String mensaje) {

		Notification notif = new Notification(mensaje, null, Notification.Type.ASSISTIVE_NOTIFICATION, true); 

		// Customize it
		notif.show(Page.getCurrent());
		notif.setDelayMsec(500);
		notif.setPosition(Position.TOP_CENTER);
	}

}
