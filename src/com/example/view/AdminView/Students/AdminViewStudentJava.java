package com.example.view.AdminView.Students;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import com.example.Dates.ConverterDates;
import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Logic.CurrentCourse;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.StudentsJPAManager;
import com.example.Templates.MainContentView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
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

/**
 * 
 * Gestió d'Amonestacions v1.0
 *
 * Esta obra está sujeta a la licencia
 * Reconocimiento-NoComercial-SinObraDerivada 4.0 Internacional de Creative
 * Commons. Para ver una copia de esta licencia, visite
 * http://creativecommons.org/licenses/by-nc-nd/4.0/.
 * 
 * @author Francisco Javier Casado Moreno - fcasado@elpuig.xeill.net
 * @author Daniel Pérez Palacino - dperez@elpuig.xeill.net
 * @author Gerard Enrique Paulino Decena - gpaulino@elpuig.xeill.net
 * @author Xavier Murcia Gámez - xmurcia@elpuig.xeill.net
 * 
 * 
 * 		Clase que muestra todos los alumnos del centro.
 * 
 **/

public class AdminViewStudentJava extends MainContentView {

	private static final long serialVersionUID = -8408971161693178388L;

	private ConverterDates datas;
	private Grid grid = new Grid();
	private StudentsJPAManager MA;
	private Window windowAdd = new Window();
	private Window windowEdit = new Window();
	private JPAContainer<Student> students;
	private JPAContainer<Group> container;
	private AdminAddStudentForm studentsFormAdd;
	private AdminAddStudentForm studentsFormEdit;
	private EntityManagerUtil entman;
	private EntityManager em;
	private CurrentCourse course;

	public AdminViewStudentJava() {

		studentsFormEdit = new AdminAddStudentForm();
		studentsFormAdd = new AdminAddStudentForm();
		entman = new EntityManagerUtil();
		em = entman.getEntityManager();
		course = new CurrentCourse();

		generalSettings();
		filterTextProperties();
		WindowPropertiesAddStudent();
		WindowPropertiesEditStudent();
		PopulateNativeSelect();
		Listeners();

		vHorizontalMain.addComponent(GridProperties());

		studentsFormEdit.cancelarButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				windowEdit.close();
				buttonEdit.setEnabled(false);
			}
		});
		
		//VALIDATORS FORM
		
		studentsFormAdd.nom.addValidator(new RegexpValidator("[a-zA-Z]{1,30}", "Nombre incorrecte"));
		studentsFormAdd.cognom.addValidator(new RegexpValidator("[a-zA-Z]{1,20}.{1,}", "Cognom incorrecte"));

		studentsFormAdd.teléfons.addValidator(new RegexpValidator(
				"^(0034|\\+34)?(\\d\\d\\d)-? ?(\\d\\d)-? ?(\\d)-? ?(\\d)-? ?(\\d\\d)$", "Teléfon incorrecte"));

		studentsFormAdd.emails.addValidator(new EmailValidator("Aquest mail no és válid"));

	}

	/**
	 * En dobleclick aparece la ventana de edición del alumno con los campos
	 * autocompletados para su edición
	 */
	private void editStudent(ItemClickEvent event) {
		// TODO Auto-generated method stub

		datas = new ConverterDates();

		UI.getCurrent().addWindow(windowEdit);
		windowEdit.setCaption("Editar alumne");

		Object id = event.getItem().getItemProperty("id");
		Object name = event.getItem().getItemProperty("nom");
		Object surname = event.getItem().getItemProperty("cognoms");
		Object email = event.getItem().getItemProperty("email");
		Object phone = event.getItem().getItemProperty("telefon");
		Object data = event.getItem().getItemProperty("data_naixement");
		Object curs = event.getItem().getItemProperty("curs");
		Object grup = event.getItem().getItemProperty("grup");

		String fecha = datas.converterDate2(data.toString());
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = formatter.parse(fecha);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		studentsFormEdit.fecha.setValue(date);

		studentsFormEdit.nom.setValue(name.toString());
		studentsFormEdit.cognom.setValue(surname.toString());

		if (email.toString() == null) {
			studentsFormEdit.emails.setValue(" ");
		} else {
			studentsFormEdit.emails.setValue(email.toString());

		}

		studentsFormEdit.teléfons.setValue(phone.toString());
		studentsFormEdit.grup.setValue(grup.toString());

		if (windowEdit.isAttached()) {

			getUI().removeWindow(windowEdit);
		}
		UI.getCurrent().addWindow(windowEdit);

	}

	/**
	 * Listeners de los botones inferiores, edición, registro, añadido
	 */
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

	/**
	 * Añade un nuevo alumno, aparecerá la fila del grid (la última posición)
	 * para que el usuario compruebe que se ha introducido el nuevo alumno
	 */
	private void addStudent() {

		studentsFormAdd.aceptarButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				if (!studentsFormAdd.emails.isValid() || !studentsFormAdd.teléfons.isValid() || !studentsFormAdd.nom.isValid() || !studentsFormAdd.cognom.isValid()) {

					notif("Omple correctament els camps");

				} else {

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

			}

		});

		studentsFormAdd.cancelarButton.addClickListener(new ClickListener() {
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

	/**
	 * Añade un nuevo alumno, aparecerá la fila del grid (la última posición)
	 * para que el usuario compruebe que se ha introducido el nuevo alumno
	 */
	private void editStudent() {
		PopulateNativeSelect();

		datas = new ConverterDates();

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

		String fecha = datas.converterDate2(data.toString());
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = formatter.parse(fecha);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		studentsFormEdit.fecha.setValue(date);

		studentsFormEdit.nom.setValue(name.toString());
		studentsFormEdit.cognom.setValue(surname.toString());

		if (email.toString() == null) {
			studentsFormEdit.emails.setValue(" ");
		} else {
			studentsFormEdit.emails.setValue(email.toString());

		}

		studentsFormEdit.teléfons.setValue(phone.toString());
		studentsFormEdit.grup.setValue(grup.toString());

		studentsFormEdit.aceptarButton.addClickListener(new ClickListener() {

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

		String nom = studentsFormAdd.nom.getValue().toString();
		String cognom = studentsFormAdd.cognom.getValue().toString();
		String curs = course.currentCourse();
		Date fecha = studentsFormAdd.fecha.getValue();
		String email = studentsFormAdd.emails.getValue().toString();
		String telf = studentsFormAdd.teléfons.getValue().toString();
		String grup = studentsFormAdd.grup.getValue().toString();

		Student al = new Student(nom, cognom, email, telf, fecha, curs, grup);

		return al;
	}

	private Student getStudentEdit() {
		Object id = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("id");
		String nom = studentsFormEdit.nom.getValue().toString();
		String cognom = studentsFormEdit.cognom.getValue().toString();
		String curs = course.currentCourse();
		Date fecha = studentsFormEdit.fecha.getValue();
		String email = studentsFormEdit.emails.getValue().toString();
		String telf = studentsFormEdit.teléfons.getValue().toString();
		String grup = studentsFormEdit.grup.getValue().toString();

		Student al = new Student(Integer.parseInt(id.toString()), nom, cognom, email, telf, curs, fecha, grup);
		return al;
	}

	private TextField filterTextProperties() {
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
				filter = new SimpleStringFilter("cognoms", event.getText(), true, false);
				f.addContainerFilter(filter);
			}
		});
		return txtSearch;
	}

	private void clearEditForm() {
		studentsFormEdit.nom.clear();
		studentsFormEdit.cognom.clear();
		studentsFormEdit.emails.clear();
		studentsFormEdit.emails.clear();
		studentsFormEdit.teléfons.clear();
		studentsFormEdit.grup.clear();
	}

	private void clearAddForm() {
		studentsFormAdd.nom.clear();
		studentsFormAdd.cognom.clear();
		studentsFormAdd.emails.clear();
		studentsFormAdd.emails.clear();
		studentsFormAdd.teléfons.clear();
		studentsFormAdd.grup.clear();
	}

	private void generalSettings() {

		bDelete.addStyleName(ValoTheme.BUTTON_DANGER);
		bAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bRegister.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonEdit.addStyleName(ValoTheme.BUTTON_PRIMARY);

		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		bAdd.setCaption("Afegir");
		txtTitle.setValue("Gestió d'Alumnes");

		bDelete.setEnabled(false);
		bDelete.setVisible(false);
		buttonEdit.setEnabled(false);
		bRegister.setVisible(false);
		bAdd.setEnabled(true);

	}

	public Grid GridProperties() {

		students = JPAContainerFactory.make(Student.class, em);
		grid = new Grid("", students);
		grid.setSizeFull();
		grid.setColumnReorderingAllowed(true);
		grid.setColumns("nom", "cognoms", "curs", "grup", "email", "data_naixement");
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(new SelectionListener() {

			@Override
			public void select(SelectionEvent event) {
				// bAdd.setEnabled(true);
				buttonEdit.setEnabled(true);
				bDelete.setEnabled(false);

			}
		});

		grid.addItemClickListener(new ItemClickListener() {

			private static final long serialVersionUID = 991142819147193429L;

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				// IF EVENT DOUBLE CLICK, WINDOW WARNING APPEARS
				if (event.isDoubleClick()) {
					try {
						editStudent(event);
					} catch (NullPointerException e) {
						notif("L'alumne no te asignat un tutor");

					}
				}

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
		windowAdd.setContent(studentsFormAdd);
	}

	private void WindowPropertiesEditStudent() {

		windowEdit.setCaption("Modificar alumne");
		windowEdit.setHeight("60%");
		windowEdit.setWidth("40%");
		windowEdit.setModal(true);
		windowEdit.center();
		windowEdit.setDraggable(false);
		windowEdit.setContent(studentsFormEdit);
	}

	private void PopulateNativeSelect() {

		container = JPAContainerFactory.make(Group.class, em);
		studentsFormAdd.grup.setContainerDataSource(container);
		studentsFormAdd.grup.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		studentsFormAdd.grup.setItemCaptionPropertyId("id");

		studentsFormEdit.grup.setContainerDataSource(container);
		studentsFormEdit.grup.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		studentsFormEdit.grup.setItemCaptionPropertyId("id");

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
