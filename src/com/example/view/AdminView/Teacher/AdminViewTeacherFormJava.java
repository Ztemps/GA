package com.example.view.AdminView.Teacher;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import com.example.Logic.UserJPAManager;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.client.ui.VGridLayout;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class AdminViewTeacherFormJava extends AdminViewTeacherForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1038815527250707361L;
	private TeachersJPAManager MA;
	private UserJPAManager MA2;

	private String username;
	private String rol = "Profesor";
	private String password = "Nomeolvides1";

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

	public void insertDocent(Teacher teacher) {

		MA = new TeachersJPAManager();
		MA2 = new UserJPAManager();

		String group;
		String[] aux;
		aux = teacher.getCognoms().split(" ");

		// add new teacher
		MA.addTeacher(new Teacher(teacher.getNom(), teacher.getCognoms(), teacher.getEmail()));
		int id = MA.getIdDocent(teacher.getEmail());

		if (txtUsername.getValue().length() > 0)
			username = txtUsername.getValue();
		else
			username = teacher.getNom().substring(0, 1) + aux[0];

		if (isTutor.getValue()) {
			group = this.selectGroup.getValue().toString();
			MA2.addTutor(new Tutor(id, group));
			rol = "Tutor";
		}
		MA2.addUser(new User(id, password, username.toLowerCase(), rol));
		MA.closeTransaction();
		MA2.closeTransaction();
	}

}
