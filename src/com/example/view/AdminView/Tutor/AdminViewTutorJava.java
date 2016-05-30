/*******************************************************************************
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
 *******************************************************************************/
package com.example.view.AdminView.Tutor;

import java.sql.SQLException;

import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Entities.Tutor;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.JDBCConnectionPool;
import com.example.Templates.MainContentView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.themes.ValoTheme;

public class AdminViewTutorJava extends MainContentView {

	private EntityManagerUtil etm;
	private Grid grid;
	private JDBCConnectionPool jdbccp;
	private SQLContainer tutors;

	public AdminViewTutorJava() {

		buttonsSettings();
		vHorizontalMain.addComponent(GridProperties());

	}


	
	/**
	 * Configuración principal de botones y campos de texto
	 * */
	private void buttonsSettings() {

		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		bAdd.setVisible(false);
		txtTitle.setValue("Gestió de Tutors");
		bDelete.addStyleName(ValoTheme.BUTTON_DANGER);
		bAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bRegister.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonEdit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bDelete.setEnabled(false);
		bDelete.setVisible(false);
		buttonEdit.setEnabled(false);
		bRegister.setVisible(false);
		clearTxt.setVisible(false);
		bDelete.setVisible(false);
		txtSearch.setVisible(false);
		buttonEdit.setVisible(false);
	}

	
	/**
	 * Configuración principal del componente grid, su container de datos
	 * 
	 * @return componente de tipo Grid
	 * */
	public Grid GridProperties() {

		jdbccp = new JDBCConnectionPool();

		try {
			tutors = new SQLContainer(new FreeformQuery(
					"SELECT d.nom, d.cognoms, t.grup" + " FROM tutor t, docent d" + " WHERE t.docent = d.id ",
					jdbccp.GetConnection()));
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		
		grid = new Grid("", tutors);
		grid.setSizeFull();
		grid.setColumnReorderingAllowed(true);
		grid.setColumns("nom", "cognoms", "grup");
		grid.setSelectionMode(SelectionMode.SINGLE);
	
		grid.setSelectionMode(SelectionMode.SINGLE);
	

		return grid;
	}

	
	/**Actualiza los datos del componente Grid*/
	public void reloadGrid() {

		vHorizontalMain.removeAllComponents();
		vHorizontalMain.addComponent(GridProperties());

	}

	
	/**
	 * Deselecciona las celdas del Grid
	 * */
	public void clear() {
		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		grid.deselectAll();

	}

}
