package com.example.view.AdminView.Warning;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.example.Entities.Teacher;
import com.example.Entities.User;
import com.example.Entities.Warning;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.JDBCConnectionPool;
import com.example.Logic.UserJPAManager;
import com.example.Pdf.generatePDF;
import com.example.Templates.ConfirmWarningPDF;
import com.example.Templates.MainContentView;
import com.itextpdf.text.DocumentException;
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
import com.vaadin.server.FileResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.themes.ValoTheme;

public class AdminViewWarning extends MainContentView  {

	File sourceFile;
	static Grid grid;
	String nomCognoms;
	String fecha;
	String hora;
	final Window window = new Window();
	private JPAContainer<Warning> warnings;
	UserJPAManager MA = new UserJPAManager();
	ConfirmWarningPDF pdf = new ConfirmWarningPDF();

	JDBCConnectionPool jdbccp;
	
	public AdminViewWarning() throws SQLException {
		// TODO Auto-generated constructor stub
		
		buttonsSettings();
		filterTextProperties();
		WindowProperties();
		buttonsAction();
		gridProperties();
		
		bAdd.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				try {
					popupPDF();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
		});
		vHorizontalMain.addComponent(gridProperties());

	}
	
	
	public void popupPDF() throws IOException, DocumentException {
		
		generatePDF generatepdf = new generatePDF();
		Embedded c = new Embedded();
		sourceFile = new File(generatepdf.getPath2(getItemNomCognomSelected(), getDateSelected()));
		c.setSource(new FileResource(sourceFile));
		c.setWidth("100%");
		c.setHeight("600px");
		c.setType(Embedded.TYPE_BROWSER);
		pdf.verticalpdf.removeAllComponents();
		pdf.verticalpdf.setSizeFull();
		pdf.verticalpdf.addComponent(c);
		pdf.hbuttons.setVisible(false);
		window.setContent(pdf);
		UI.getCurrent().addWindow(window);
		
		window.setVisible(true);

	}
	
	private String getItemNomCognomSelected() {

		Object name = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("nom").getValue();
		Object surname = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("cognoms").getValue();
		
				
		return name.toString()+" "+surname.toString();

	}
	
	private String getDateSelected(){
		
		Object data = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("data").getValue();
		fecha = data.toString();
	
		return fecha.substring(11,16);
	}
	
	private Grid gridProperties() throws SQLException {
		// TODO Auto-generated method stub
		// Fill the grid with data
		jdbccp = new JDBCConnectionPool();

		SQLContainer AllWarnings = new SQLContainer(new FreeformQuery(
				"select al.nom, " + "al.cognoms," + " a.grup, " + "a.motius_selection," + " a.altres_motius,"
						+ "a.materia, a.data, " + "a.localitzacio " + "from amonestacio a, docent d, alumne al "
						+ "where a.docent=d.id and a.alumne=al.id ",
				jdbccp.GetConnection()));

		
	//	warnings = JPAContainerFactory.make(Warning.class, EntityManagerUtil.getEntityManager());
		grid = new Grid("", AllWarnings);
		grid.setSizeFull();
		grid.setContainerDataSource(AllWarnings);
		grid.setColumnReorderingAllowed(true);
		//docent,grup,gravetat,motiu
		//grid.setColumns("alumne","grup","data","gravetat","localitzacio","materia","tutor","amonestat","expulsat","curs","motiu","altres_motius");

		//grid.setColumns("docent","alumne", "grup", "gravetat","motiu","altres_motius","data");

		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(new SelectionListener() {

			@Override
			public void select(SelectionEvent event) {
				// TODO Auto-generated method stub
				bAdd.setEnabled(true);
//				buttonEdit.setEnabled(true);
//				bDelete.setEnabled(true);

			}
		});

		return grid;
	}

	private void buttonsAction() {
		// TODO Auto-generated method stub
		
	}

	private void WindowProperties() {
		// TODO Auto-generated method stub
		window.setHeight("95%");
		window.setWidth("95%");
		window.setDraggable(false);
		window.setModal(true);
		window.setVisible(false);
		window.setCaption("Visualització de l'amonestació");
		window.center();
	}

	private TextField filterTextProperties() {
		// TODO Auto-generated method stub
		txtSearch.setInputPrompt("Filtra per cognom");
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
				filter = new SimpleStringFilter("motiu", event.getText(), true, false);
				f.addContainerFilter(filter);
			}
			
	});
		return txtSearch;
	}

	private void buttonsSettings() {
		// TODO Auto-generated method stub
		
		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		txtTitle.setValue("Visualitzar Amonestacions");
		bDelete.addStyleName(ValoTheme.BUTTON_DANGER);
		bAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bRegister.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonEdit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		
		bDelete.setVisible(false);
		buttonEdit.setVisible(false);
		bRegister.setVisible(false);
		bAdd.setEnabled(false);
		bAdd.setCaption("Veure Detalls");

	}
	
	public void clear() {
		// TODO Auto-generated method stub

		//bAdd.setEnabled(false);
		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		grid.deselectAll();

	}
	
}
