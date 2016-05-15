package com.example.view.AdminView.Students;

import java.util.Date;

import javax.persistence.EntityManager;

import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Logic.Cursos;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.StudentsJPAManager;
import com.example.Templates.AdminAddStudentForm;
import com.example.Templates.MainContentView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Grid.SingleSelectionModel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

public class AdminViewStudentJava extends MainContentView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8408971161693178388L;

	private Grid grid = new Grid();
	private StudentsJPAManager MA;
	private Window windowAdd = new Window();
	private Window windowEdit = new Window();
	private JPAContainer<Student> alumnes;
	private JPAContainer<Group> container;
	private AdminAddStudentForm alumneformAdd;
	private AdminAddStudentForm alumneformEdit;
	private EntityManagerUtil entman = new EntityManagerUtil();
	private EntityManager em = entman.getEntityManager();

	public AdminViewStudentJava() {

		alumneformEdit = new AdminAddStudentForm();
		alumneformAdd = new AdminAddStudentForm();

		buttonsSettings();
		filterTextProperties();
		WindowPropertiesAddStudent();
		WindowPropertiesEditStudent();
		PopulateNativeSelect();
		Listeners();
		ClearText();
		vHorizontalMain.addComponent(GridProperties());

	}

	private void Listeners() {
		buttonEdit.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {

				editStudent();

			}
		});
		bRegister.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		bDelete.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// deleteStudent(); //Falta mapear o da error
			}
		});
		bAdd.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				addStudent();

			}

		});

	}

	private void addStudent() {
		PopulateNativeSelect();

		alumneformAdd.aceptarButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				try {

					MA = new StudentsJPAManager();
					Student alumn = getStudentAdd();
					MA.updateStudent(alumn);
					MA.closeTransaction();
					reloadGrid();
					windowAdd.close();
					SingleSelectionModel m = (SingleSelectionModel) grid.getSelectionModel();

					int Fila = (grid.getContainerDataSource().getItemIds().size()) - 1;

					Object id = grid.getContainerDataSource().getIdByIndex(Fila);
					m.select(id);
					grid.scrollTo(id);
					clearAddForm();
					notif("Alumne afegit corretament");

				} catch (NullPointerException e) {

					notif("Omple els camps obligatoris");

				}

			}

		});

		alumneformAdd.cancelarButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				windowAdd.close();
			}
		});

		if (windowAdd.isAttached()) {

			getUI().removeWindow(windowAdd);
		}

		UI.getCurrent().addWindow(windowAdd);

	}

	private void editStudent() {
		PopulateNativeSelect();

		UI.getCurrent().addWindow(windowEdit);
		windowEdit.setCaption("Editar alumne");

		Object id = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("id");
		Object name = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("nom");
		Object surname = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("cognoms");
		Object email = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("email");
		Object phone = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("telefon");
		Object data = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("data_naixement");
		Object curs = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("curs");
		Object grup = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("grup");

		// SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		// Date Fecha=null;
		// try {
		// Fecha = formatter.parse(data.toString());
		// } catch (ParseException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// alumneformEdit.fecha.setValue(Fecha);

		alumneformEdit.nom.setValue(name.toString());
		alumneformEdit.cognom.setValue(surname.toString());

		if (email.toString() == null) {
			alumneformEdit.emails.setValue(" ");
		} else {
			alumneformEdit.emails.setValue(email.toString());

		}

		alumneformEdit.teléfons.setValue(phone.toString());
		alumneformEdit.curs.setValue(curs.toString());
		alumneformEdit.grup.setValue(grup.toString());

		alumneformEdit.aceptarButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {

					MA = new StudentsJPAManager();
					Student alumne = getStudentEdit();
					MA.updateStudent(alumne);
					MA.closeTransaction();
					reloadGrid();
					int fila = Integer.parseInt(id.toString());
					windowEdit.close();
					SingleSelectionModel m = (SingleSelectionModel) grid.getSelectionModel();
					m.select(fila);
					grid.scrollTo(fila);
					clearEditForm();

					notif("Alumne modificat correctament");

				} catch (NullPointerException e) {

					notif("Omple els camps obligatoris");

				}

			}

		});

		alumneformEdit.cancelarButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				windowEdit.close();
			}
		});

		if (windowEdit.isAttached()) {

			getUI().removeWindow(windowEdit);
		}
		UI.getCurrent().addWindow(windowEdit);

	}

	private void deleteStudent() {
		MA = new StudentsJPAManager();

		Object id = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("id");
		Object name = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("nom");
		Object surname = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("cognoms");
		Object email = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("email");
		Object phone = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("telefon");
		Object data = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("data_naixement");
		Object curs = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("curs");
		Object grup = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("grup");

		Student alumn = new Student(Integer.parseInt(id.toString()), name.toString(), surname.toString(),
				email.toString(), phone.toString(), curs.toString(), grup.toString());
		MA.removeStudent(alumn);
		MA.closeTransaction();

		notif("Alumne esborrat correctament");

	}

	private Student getStudentAdd() {

		String nom = alumneformAdd.nom.getValue().toString();
		String cognom = alumneformAdd.cognom.getValue().toString();
		String curs = Cursos.ObtenerCursoActual();
		Date fecha = alumneformAdd.fecha.getValue();
		String email = alumneformAdd.emails.getValue().toString();
		String telf = alumneformAdd.teléfons.getValue().toString();
		String grup = alumneformAdd.grup.getValue().toString();

		Student al = new Student(nom, cognom, email, telf, fecha, curs, grup);

		return al;
	}

	private Student getStudentEdit() {
		Object id = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("id");
		String nom = alumneformEdit.nom.getValue().toString();
		String cognom = alumneformEdit.cognom.getValue().toString();
		String curs = Cursos.ObtenerCursoActual();
		Date fecha = alumneformEdit.fecha.getValue();
		String email = alumneformEdit.emails.getValue().toString();
		String telf = alumneformEdit.teléfons.getValue().toString();
		String grup = alumneformEdit.grup.getValue().toString();

		Student al = new Student(Integer.parseInt(id.toString()), nom, cognom, email, telf, curs, fecha, grup);
		return al;
	}

	private TextField filterTextProperties() {
		txtSearch.setInputPrompt("Filtra cognom alumne");
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
				filter = new SimpleStringFilter("cognoms", event.getText(), true, false);
				f.addContainerFilter(filter);
			}
		});
		return txtSearch;
	}

	private void ClearText() {

		clearTxt.setDescription("Limpiar contenido actual...");
		clearTxt.setIcon(FontAwesome.TIMES);
		clearTxt.addClickListener(e -> {
			txtSearch.clear();

		});

		clearTxt.setIcon(FontAwesome.TIMES);

	}

	private void clearEditForm() {
		alumneformEdit.nom.clear();
		alumneformEdit.cognom.clear();
		alumneformEdit.emails.clear();
		alumneformEdit.emails.clear();
		alumneformEdit.teléfons.clear();
		alumneformEdit.curs.clear();
		alumneformEdit.grup.clear();
	}

	private void clearAddForm() {
		alumneformAdd.nom.clear();
		alumneformAdd.cognom.clear();
		alumneformAdd.emails.clear();
		alumneformAdd.emails.clear();
		alumneformAdd.teléfons.clear();
		alumneformAdd.curs.clear();
		alumneformAdd.grup.clear();
	}

	private void buttonsSettings() {

		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		bAdd.setCaption("Afegir");
		txtTitle.setValue("Gestió d'Alumnes");
		bDelete.addStyleName(ValoTheme.BUTTON_DANGER);
		bAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bRegister.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonEdit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		bRegister.setVisible(false);
		bAdd.setEnabled(true);

	}

	public Grid GridProperties() {

		alumnes = JPAContainerFactory.make(Student.class, em);
		grid = new Grid("", alumnes);
		grid.setSizeFull();
		grid.setColumnReorderingAllowed(true);

		grid.setColumns("nom", "cognoms", "curs", "grup", "email", "data_naixement");

		// grid.addRow(new ThemeResource("no_user.png"));

		// grid.setIcon(new ThemeResource("no_user.png"));
	

		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(new SelectionListener() {

			@Override
			public void select(SelectionEvent event) {
				//bAdd.setEnabled(true);
				buttonEdit.setEnabled(true);
				bDelete.setEnabled(true);

			}
		});

		return grid;
	}

	private void WindowPropertiesAddStudent() {

		windowAdd.setCaption("Afegir nou alumne");
		windowAdd.setHeight("60%");
		windowAdd.setWidth("40%");
		windowAdd.setModal(true);
		windowAdd.center();
		windowAdd.setDraggable(false);
		windowAdd.setContent(alumneformAdd);
	}

	private void WindowPropertiesEditStudent() {

		windowEdit.setCaption("Modificar alumne");
		windowEdit.setHeight("60%");
		windowEdit.setWidth("40%");
		windowEdit.setModal(true);
		windowEdit.center();
		windowEdit.setDraggable(false);
		windowEdit.setContent(alumneformEdit);
	}

	private void PopulateNativeSelect() {

		container = JPAContainerFactory.make(Group.class, em);
		alumneformAdd.grup.setContainerDataSource(container);
		alumneformAdd.grup.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		alumneformAdd.grup.setItemCaptionPropertyId("id");

		alumneformEdit.grup.setContainerDataSource(container);
		alumneformEdit.grup.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		alumneformEdit.grup.setItemCaptionPropertyId("id");

	}

	public void reloadGrid() {

		vHorizontalMain.removeAllComponents();
		vHorizontalMain.addComponent(GridProperties());

	}

	public void notif(String mensaje) {

		Notification notif = new Notification(mensaje, null, Notification.Type.TRAY_NOTIFICATION, true); // Contains
																											// HTML

		// Customize it
		notif.show(Page.getCurrent());
		notif.setDelayMsec(500);
		notif.setPosition(Position.TOP_CENTER);
	}

	public void clear() {
		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		grid.deselectAll();

	}

}
