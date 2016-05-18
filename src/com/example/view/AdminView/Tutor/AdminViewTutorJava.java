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
		filterTextProperties();
		vHorizontalMain.addComponent(GridProperties());

	}

	private TextField filterTextProperties() {
		txtSearch.setInputPrompt("Filtra per grup");
		txtSearch.addTextChangeListener(new TextChangeListener() {

			SimpleStringFilter filter = null;

			@Override
			public void textChange(TextChangeEvent event) {
				// TODO Auto-generated method stub

				Filterable f = (Filterable) grid.getContainerDataSource();

				// Remove old filter
				if (filter != null)
					f.removeContainerFilter(filter);

				// Set new filter for the "Name" column
				filter = new SimpleStringFilter("grup", event.getText(), true, false);
				f.addContainerFilter(filter);
			}
		});
		return txtSearch;
	}


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
		buttonEdit.setEnabled(false);
		bRegister.setVisible(false);
		clearTxt.setVisible(false);


	}

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
		grid.addSelectionListener(new SelectionListener() {

			@Override
			public void select(SelectionEvent event) {
				bDelete.setVisible(true);
				buttonEdit.setVisible(true);
			}
		});

		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(new SelectionListener() {

			@Override
			public void select(SelectionEvent event) {
				bAdd.setEnabled(true);
				buttonEdit.setEnabled(true);
				bDelete.setEnabled(true);

			}
		});

		return grid;
	}

	public void reloadGrid() {

		vHorizontalMain.removeAllComponents();
		vHorizontalMain.addComponent(GridProperties());

	}

	public void clear() {
		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		grid.deselectAll();

	}

}
