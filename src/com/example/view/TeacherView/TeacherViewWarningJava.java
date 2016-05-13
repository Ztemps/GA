package com.example.view.TeacherView;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.EntityManager;
import com.example.Entities.Student;
import com.example.Entities.Teacher;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.JDBCConnectionPool;
import com.example.Logic.UserJPAManager;
import com.example.Logic.WarningJPAManager;
import com.example.Pdf.generatePDF;
import com.example.Templates.ConfirmWarningPDF;
import com.example.Templates.MainContentView;
import com.example.view.AdminView.AdminView;
import com.itextpdf.text.DocumentException;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.themes.ValoTheme;

import java.io.*;

public class TeacherViewWarningJava extends MainContentView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6224440074961752155L;
	private Grid grid;
	private Window window = new Window();
	private Window windowpdf = new Window();
	private ConfirmWarningPDF pdf = new ConfirmWarningPDF();
	private JPAContainer<Student> alumnes;
	private WarningTeacher amonestacioForm;
	private UserJPAManager MA;
	private WarningJPAManager MA1;
	private File sourceFile;
	private FileResource resource;
	private String timewarning;
	private String nomCognom;
	private EntityManagerUtil entman = new EntityManagerUtil();
	private EntityManager em = entman.getEntityManager();
	private JDBCConnectionPool jdbccp;


	public TeacherViewWarningJava() throws MalformedURLException, DocumentException, IOException, SQLException {

		GridProperties();
		filterTextProperties();
		WindowProperties();
		buttonsSettings();
		WindowPdfProperties();
		PopulateComboBoxProf();

		try {

			amonestacioForm.baceptar.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {

					// REVISAR!!!! PARA COMPROBAR QUE ALGUNO DE LOS MOTIVOS NO
					// HA DE SER NULO
					/*
					 * if(amonestacioForm.motiu.getValue() == null &&
					 * amonestacioForm.motiu2.getValue() == null &&
					 * amonestacioForm.amotius.getValue() == ""){ notif.show(
					 * "S'ha de seleccionar almenys un motiu"); }
					 */

					if (!check()) {

						notif("Omple els camps obligatoris");

					} else {

						if (windowpdf.isAttached()) {
							getUI().removeWindow(windowpdf);
						}

						UI.getCurrent().addWindow(windowpdf);

						try {
							popupPDF();
						} catch (IOException | DocumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}

				private boolean check() {
					// TODO Auto-generated method stub

					if (amonestacioForm.nom.getValue() == "" || amonestacioForm.cognoms.getValue() == ""
							|| amonestacioForm.accio.getValue() == null || amonestacioForm.caracter.getValue() == null
							|| amonestacioForm.motiu.getValue() == null || amonestacioForm.motiu2.getValue() == null
							|| amonestacioForm.circunstancia.getValue() == null || amonestacioForm.grup.getValue() == ""
							|| amonestacioForm.tutor.getValue() == "") {

						return false;
					} else {

						return true;
					}

				}

			});

		} catch (NullPointerException e) {

		}

		pdf.aceptarButton.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					// printPDF(FicheroPdf(),choosePrinter());
					WarningJPAManager war = new WarningJPAManager();
					war.introducirParte(returnQuery());
					notif("Amonestació posada correctament");
				} catch (DocumentException | IOException | NullPointerException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					window.close();
					windowpdf.close();

				}

			}

		});

		pdf.cancelarButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				sourceFile.delete();
				windowpdf.close();
				

			}
		});
		amonestacioForm.bcancelar.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				amonestacioForm.nom.setReadOnly(false);
				amonestacioForm.cognoms.setReadOnly(false);
				amonestacioForm.tutor.setReadOnly(false);
				amonestacioForm.grup.setReadOnly(false);
				clearFields();
				window.close();

			}
		});

		bAdd.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				getItemSelectedToAmonestacioForm();

			}
		});
		clearTxt.setDescription("Limpiar contenido actual...");
		clearTxt.addClickListener(e -> {

			txtSearch.clear();
			getUI().getNavigator().navigateTo(AdminView.NAME);

		});
		clearTxt.setIcon(FontAwesome.TIMES);

		vHorizontalMain.addComponent(GridProperties());

	}
	
	private void PopulateComboBoxProf() throws SQLException {
		
		jdbccp = new JDBCConnectionPool();
		
		SQLContainer containerComboBox = new SQLContainer(new FreeformQuery("select  nom ||' '|| cognoms as professors from docent",
					jdbccp.GetConnection()));
		
		
		amonestacioForm.comboProf.setContainerDataSource(containerComboBox);
		amonestacioForm.comboProf.setItemCaptionPropertyId("professors");
		amonestacioForm.comboProf.setNullSelectionAllowed(true);
		amonestacioForm.comboProf.setFilteringMode(FilteringMode.CONTAINS);

		amonestacioForm.comboProf.setInputPrompt("Seleccioni professor");
	
	}
	
	private int TeacherIDComboBox(ItemClickEvent event){
		
		
		/*Item item = amonestacioForm.comboProf.getItem(amonestacioForm.comboProf.getValue());
		
		String cadena = item.getItemProperty("professors").toString();
		
		/*Object cadena = amonestacioForm.comboProf.getContainerProperty(amonestacioForm.comboProf.getValue(), "professors").toString();
		Object id1 = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("id");
		
		String cadena = amonestacioForm.comboProf.getPropertyDataSource().toString();
		String cadena2 = amonestacioForm.comboProf.getConvertedValue().toString();
		
		System.out.println("cadenaaa" +cadena);
		System.out.println("cadena nombrw??" +cadena2);

		//Object id = amonestacioForm.comboProf.getContainerDataSource().getItem(1).getItemProperty("professors");
		

		System.out.println("PRUEBAAAA" +cadena.toString());
		System.out.println("cadenaaa" +cadena);*/

		
		return 0;	
	
	}
	

	private String mailStudent() {

		return null;

	}

	private void buttonsSettings() {
		// TODO Auto-generated method stub

		bAdd.setStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		bAdd.setIcon(FontAwesome.INFO);
		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		bAdd.setCaption("Amonestar");
		txtTitle.setValue("Gestió d'Amonestacions");
		bDelete.addStyleName(ValoTheme.BUTTON_DANGER);
		bAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bRegister.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonEdit.addStyleName(ValoTheme.BUTTON_PRIMARY);

		bDelete.setVisible(false);
		buttonEdit.setVisible(false);
		bRegister.setVisible(false);
		bAdd.setEnabled(false);

	}

	public void notif(String mensaje) {

		Notification notif = new Notification(mensaje, null, Notification.Type.ASSISTIVE_NOTIFICATION, true); // Contains
																												// HTML

		// Customize it
		notif.show(Page.getCurrent());
		notif.setDelayMsec(500);
		notif.setPosition(Position.TOP_CENTER);
	}

	private TextField filterTextProperties() {
		// TODO Auto-generated method stub
		txtSearch.setInputPrompt("Filtra alumno por nombre");
		txtSearch.addTextChangeListener(new TextChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			SimpleStringFilter filter = null;

			@Override
			public void textChange(TextChangeEvent event) {
				// TODO Auto-generated method stub

				Filterable f = (Filterable) grid.getContainerDataSource();

				// Remove old filter
				if (filter != null)
					f.removeContainerFilter(filter);

				// Set new filter for the "Name" column
				filter = new SimpleStringFilter("cognoms", event.getText(), true, false);
				f.addContainerFilter(filter);
			}
		});
		return txtSearch;
	}

	private Grid GridProperties() {

		// Fill the grid with data
		alumnes = JPAContainerFactory.make(Student.class, em);
		grid = new Grid("", alumnes);
		grid.setSizeFull();
		grid.setContainerDataSource(alumnes);
		grid.setColumnReorderingAllowed(true);
		grid.setColumns("nom", "cognoms", "curs", "grup");

		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addItemClickListener(new ItemClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 991142819147193429L;

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				// IF EVENT DOUBLE CLICK, WINDOW WARNING APPEARS
				if (event.isDoubleClick()) {
					getItemSelectedToAmonestacioForm(event);

				}
			}
		});
		grid.addSelectionListener(new SelectionListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void select(SelectionEvent event) {
				// TODO Auto-generated method stub
				bAdd.setEnabled(true);
				buttonEdit.setEnabled(true);
				bDelete.setEnabled(true);

			}
		});

		return grid;

	}

	private void WindowProperties() throws MalformedURLException, DocumentException, IOException {

		amonestacioForm = new WarningTeacher();

		window.setWidth(900.0f, Unit.PIXELS);
		// window.setContent(form);
		window.setHeight("80%");
		window.setWidth("70%");
		window.setDraggable(false);
		window.setModal(true);
		window.setVisible(false);
		window.setCaption("Introducció de l'amonestació");
		window.center();
		window.setContent(amonestacioForm);

	}

	public void getItemSelectedToAmonestacioForm(ItemClickEvent event) {
		
		amonestacioForm.nom.setReadOnly(false);
		amonestacioForm.cognoms.setReadOnly(false);
		amonestacioForm.tutor.setReadOnly(false);
		amonestacioForm.grup.setReadOnly(false);
		
		if (window.isAttached())
			getUI().getWindows().remove(window);

		UI.getCurrent().addWindow(window);
		window.setVisible(true);
		clearFields();

		MA1 = new WarningJPAManager();
		MA = new UserJPAManager();

		Object name = event.getItem().getItemProperty("nom");
		Object surname = event.getItem().getItemProperty("cognoms");
		Object grup = event.getItem().getItemProperty("grup");

		int idtutor = MA1.getIdTutor(grup.toString());
		String nametutor = MA.getNomTutor(idtutor);

		amonestacioForm.nom.setValue(name.toString());
		amonestacioForm.cognoms.setValue(surname.toString());
		amonestacioForm.grup.setValue(grup.toString());
		amonestacioForm.tutor.setValue(nametutor);

		fieldsRequired();

		amonestacioForm.motiu.setMultiSelect(true);
		amonestacioForm.motiu2.setMultiSelect(true);
		amonestacioForm.nom.setReadOnly(true);
		amonestacioForm.cognoms.setReadOnly(true);
		amonestacioForm.tutor.setReadOnly(true);
		amonestacioForm.grup.setReadOnly(true);
	}

	private void getItemSelectedToAmonestacioForm() {
		
		amonestacioForm.nom.setReadOnly(false);
		amonestacioForm.cognoms.setReadOnly(false);
		amonestacioForm.tutor.setReadOnly(false);
		amonestacioForm.grup.setReadOnly(false);
		
		if (window.isAttached())
			getUI().getWindows().remove(window);

		UI.getCurrent().addWindow(window);
		window.setVisible(true);

		MA1 = new WarningJPAManager();
		MA = new UserJPAManager();
		clearFields();

		Object name = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("nom");
		Object surname = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("cognoms");
		Object grup = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("grup");

		int idtutor=0;
		String nametutor="";
		try {
			idtutor = MA1.getIdTutor(grup.toString());
			nametutor = MA.getNomTutor(idtutor);

		} catch (Exception e) {
			 Notification notif = new Notification(
	                 "ATENCIÓ:",
	                 "<br>L'alumne no té cap tutor<br/>",
	                 Notification.Type.HUMANIZED_MESSAGE,
	                 true); // Contains HTML
		}
		
		

		amonestacioForm.nom.setValue(name.toString());
		amonestacioForm.cognoms.setValue(surname.toString());
		amonestacioForm.grup.setValue(grup.toString());
		amonestacioForm.tutor.setValue(nametutor);

		nomCognom = amonestacioForm.nom.getValue() + " " + amonestacioForm.cognoms.getValue();

		fieldsRequired();

		amonestacioForm.motiu.setMultiSelect(true);
		amonestacioForm.motiu2.setMultiSelect(true);
		amonestacioForm.nom.setReadOnly(true);
		amonestacioForm.cognoms.setReadOnly(true);
		amonestacioForm.tutor.setReadOnly(true);
		amonestacioForm.grup.setReadOnly(true);

	}

	private void fieldsRequired() {
		// TODO Auto-generated method stub
		amonestacioForm.nom.setRequired(true);
		amonestacioForm.cognoms.setRequired(true);
		amonestacioForm.materia.setRequired(false);
		amonestacioForm.circunstancia.setRequired(true);
		amonestacioForm.tutor.setRequired(true);
		amonestacioForm.grup.setRequired(true);
		amonestacioForm.caracter.setRequired(true);
		amonestacioForm.accio.setRequired(true);
		amonestacioForm.nom.setRequired(true);

		amonestacioForm.nom.setRequiredError("El camp nom és obligatori");
		amonestacioForm.cognoms.setRequiredError("El camp cognoms és obligatori");
		amonestacioForm.materia.setRequiredError("El camp materia és obligatori");
		amonestacioForm.circunstancia.setRequiredError("El camp circunstancia és obligatori");
		amonestacioForm.tutor.setRequiredError("El camp tutor és obligatori");
		amonestacioForm.grup.setRequiredError("El camp grup és obligatori");
		amonestacioForm.caracter.setRequiredError("El camp caracter és obligatori");
		amonestacioForm.accio.setRequiredError("El camp acció és obligatori");
	}

	@SuppressWarnings("deprecation")
	public void popupPDF() throws IOException, DocumentException {
		// TODO Auto-generated method stub
		// A user interface for a (trivial) data model from which
		// the PDF is generated.
		generatePDF generatepdf = new generatePDF();
		timewarning = generatepdf.generate(returnQuery());
		

		String nomCognom = amonestacioForm.nom.getValue() + " " + amonestacioForm.cognoms.getValue();

		Embedded c = new Embedded();
		sourceFile = new File(generatepdf.getPath(nomCognom));

		c.setSource(new FileResource(sourceFile));
		c.setWidth("100%");
		c.setHeight("600px");
		c.setType(Embedded.TYPE_BROWSER);
		pdf.verticalpdf.removeAllComponents();

		pdf.verticalpdf.setSizeFull();
		pdf.verticalpdf.addComponent(c);
		amonestacioForm.baceptar.setEnabled(true);

		windowpdf.setContent(pdf);

		windowpdf.setVisible(true);

		// windowpdf.setContent(pdf);
		// windowpdf.setVisible(true);
		// layout.addComponent(pdf);

		// layout.setSizeFull();
	}

	// public boolean required() {
	// // TODO Auto-generated method stub
	//
	// if (amonestacioForm.nom.getValue().equals("") ||
	// amonestacioForm.cognom.getValue().equals("")
	// || amonestacioForm.curs.getValue().equals("") ||
	// amonestacioForm.grup.getValue().equals("")
	// || amonestacioForm.motiu.getValue().equals("") ||
	// amonestacioForm.tipus.getValue().equals(null)
	// || amonestacioForm.accio.getValue().equals(null) ||
	// amonestacioForm.assignatura.getValue().equals("")) {
	//
	// return false;
	//
	// } else {
	//
	// return true;
	// }
	//
	// }

	private void clearFields() {
		// TODO Auto-generated method stub

		amonestacioForm.nom.clear();
		amonestacioForm.cognoms.clear();
		amonestacioForm.amotius.clear();
		amonestacioForm.caracter.clear();
		amonestacioForm.motiu.clear();
		amonestacioForm.motiu2.clear();
		amonestacioForm.circunstancia.clear();
		amonestacioForm.accio.clear();
		amonestacioForm.materia.clear();
		amonestacioForm.baceptar.click();
	}

	public String[] returnQuery() throws MalformedURLException, DocumentException, IOException {
		// TODO Auto-generated method stub
		String expulsat = "";

		// Obtener la id del alumno
		String name = amonestacioForm.nom.getValue();
		String surname = amonestacioForm.cognoms.getValue();

		// Obtener campos del formulario
		String grup = null;
		String gravetat = null;
		String motiu = null;
		String motiu2 = null;
		String amonestat = null;
		String tutor = null;
		String localitzacio = null;
		String assignatura = null;
		String altres_motius = null;
		String amonestat2 = null;
		int teacherid = TeacherIDComboBox(null);
		
		

		int id = (int) getUI().getCurrent().getSession().getAttribute("id");

		tutor = MA.getNomTutor(id);
		try {
			grup = amonestacioForm.grup.getValue();
			gravetat = amonestacioForm.caracter.getValue().toString();
			motiu = amonestacioForm.motiu.getValue().toString();
			motiu2 = amonestacioForm.motiu2.getValue().toString();
			amonestat = amonestacioForm.accio.getValue().toString();
			localitzacio = amonestacioForm.circunstancia.getValue().toString();

			if (amonestat.equals("Amonestat")) {
				amonestat2 = "true";
			} else
				amonestat2 = "false";

			altres_motius = amonestacioForm.amotius.getValue();

		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			Notification.show("Els camps obligatoris s'han d'emplenar");
			e.printStackTrace();
		}
		if ((amonestacioForm.materia.getValue().toString()).equals("")) {

			assignatura = null;
		} else {
			assignatura = amonestacioForm.materia.getValue().toString();
		}

		System.out.println("amonestat:" + amonestat + " gravetat: " + gravetat);
		System.out.println("amonestat2:" + amonestat2 + " gravetat: " + gravetat);

		String[] query = { name, surname, grup, gravetat, localitzacio, assignatura, tutor, amonestat2, expulsat, motiu,
				altres_motius, motiu2,timewarning };

		// DATOS PARA INTRODUCIR EN EL PARTE

		return query;
	}

	private void WindowPdfProperties() throws MalformedURLException, DocumentException, IOException {
		windowpdf.setHeight("95%");
		windowpdf.setWidth("95%");
		windowpdf.setDraggable(false);
		windowpdf.setModal(true);
		windowpdf.setVisible(false);
		windowpdf.setCaption("Confirmar amonestació");
		windowpdf.center();
		amonestacioForm.baceptar.setEnabled(true);
	}

	public void clear() {
		// TODO Auto-generated method stub

		bAdd.setEnabled(false);
		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		grid.deselectAll();

	}

	public File FicheroPdf() throws IOException, DocumentException {

		generatePDF generatepdf1 = new generatePDF();
		File file = new File(generatepdf1.getPath(nomCognom));

		return file;
	}

}
