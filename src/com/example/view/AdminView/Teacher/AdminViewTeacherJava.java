
package com.example.view.AdminView.Teacher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;

import org.postgresql.util.PSQLException;

import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Entities.Teacher;
import com.example.Entities.Tutor;
import com.example.Logic.CurrentCourse;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.GroupJPAManager;
import com.example.Logic.StudentsJPAManager;
import com.example.Logic.TeachersJPAManager;
import com.example.Logic.UserJPAManager;
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
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.Position;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Grid.SingleSelectionModel;
import com.vaadin.ui.Table.TableDragMode;
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
 * 		Clase para gestionar los profesores
 * 
 **/

public class AdminViewTeacherJava extends MainContentView {

	private Grid grid;
	private Window windowAdd = new Window();
	private Window windowEdit = new Window();
	private JPAContainer<Teacher> docents;
	private JPAContainer<Group> container;
	private GroupJPAManager grupsjpa;
	private TeachersJPAManager MA;
	private AdminViewTeacherFormJava professorAddForm;
	private AdminViewTeacherFormJava professorEditForm;
	private EntityManagerUtil entman = new EntityManagerUtil();
	private EntityManager em = entman.getEntityManager();

	public AdminViewTeacherJava() {

		professorAddForm = new AdminViewTeacherFormJava();
		professorEditForm = new AdminViewTeacherFormJava();
		buttonsSettings();
		filterTextProperties();
		WindowPropertiesAddTeacher();
		WindowPropertiesEditTeacher();
		listeners();
		PopulateNativeSelect();
		vHorizontalMain.addComponent(GridProperties());

	}

	/**
	 * Listeners de los botones principales
	 */
	private void listeners() {
		buttonEdit.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				editTeacher();

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
				// deleteTeacher(); //Falta mapear o da error
			}
		});
		bAdd.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				try {
					addTeacher();
				} catch (PSQLException e) {
					// TODO Auto-generated catch block
					notif("El correu ja existeix");
				}
			}

		});
	}

	/**
	 * Listener que dispara la comprobación del correo del profesor, si no
	 * existe lo introduciremos, si existe lanzamos notificación
	 * 
	 * @throws PSQLException
	 */
	private void addTeacher() throws PSQLException {
		professorAddForm.aceptarButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				Teacher teacher = getTeacherAdd();
				MA = new TeachersJPAManager();

				List<Teacher> lista = MA.getNoms();
				MA.closeTransaction();

				boolean noexiste = true;
				System.out.println("tamaño: " + lista.size());
				for (int i = 0; i < lista.size(); i++) {
					System.out.println(lista.get(i).toString());
					if (lista.get(i).getEmail().equals(teacher.getEmail())) {
						notif("El correo ja esta en us");
						noexiste = false;
					}
				}

				if (noexiste) {

					professorAddForm.insertDocent(teacher);
					reloadGrid();
					windowAdd.close();
					SingleSelectionModel m = (SingleSelectionModel) grid.getSelectionModel();

					int Fila = (grid.getContainerDataSource().getItemIds().size()) - 1;

					Object id = grid.getContainerDataSource().getIdByIndex(Fila);
					m.select(id);
					grid.scrollTo(id);
					clearAddForm();

					notif("Professor creat correctament");
				}

			}

		});

		professorAddForm.cancelarButton.addClickListener(new ClickListener() {
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
	 * los datos profesor que seleccionamos en el componente grid, se incorporan
	 * a la ventana emergente donde podremos editarlos, el listener del botón
	 * aceptar hace los cambios en la base de datos mediante JPA
	 */
	private void editTeacher() {

		UI.getCurrent().addWindow(windowEdit);
		professorEditForm.isTutor.setVisible(false);
		professorEditForm.selectGroup.setVisible(false);
		professorEditForm.txtUsername.setVisible(false);
		windowEdit.setCaption("Editar profesor");

		Object id = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("id");
		Object nom = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("nom");
		Object email = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("email");
		Object cognoms = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("cognoms");

		professorEditForm.nom.setValue(nom.toString());
		professorEditForm.cognoms.setValue(cognoms.toString());
		professorEditForm.email.setValue(email.toString());

		professorEditForm.aceptarButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				MA = new TeachersJPAManager();
				Teacher teacher = getTeacherEdit();
				MA.updateTeacher(teacher);
				MA.closeTransaction();
				reloadGrid();
				int fila = Integer.parseInt(id.toString());
				windowEdit.close();
				SingleSelectionModel m = (SingleSelectionModel) grid.getSelectionModel();
				m.select(fila);
				grid.scrollTo(fila);
				clearEditForm();
				notif("Professor editat correctament");

			}

		});

		professorEditForm.cancelarButton.addClickListener(new ClickListener() {
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

	private void deleteTeacher() {
		MA = new TeachersJPAManager();
		Object id = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("id");
		Object nom = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("nom");
		Object cognoms = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("cognoms");
		Object email = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("email");

		Teacher teacher = new Teacher(Integer.parseInt(id.toString()), nom.toString(), cognoms.toString(),
				email.toString());
		MA.removeTeacher(teacher);
		MA.closeTransaction();
		notif("Professor esborrat correctament");

	}

	/**
	 * Si los campos estan vacios notifica al usuario de que es obligatorio
	 * llenarlos. Sin embargo, si todos los campos son correctos obtenemos el
	 * valor para construir un objeto de tipo profesor
	 *
	 * 
	 * @return Objeto de tipo Teacher
	 */
	private Teacher getTeacherAdd() {

		Teacher tc = null;
		if (professorAddForm.nom.getValue().toString() == "" || professorAddForm.cognoms.getValue().toString() == ""
				|| professorAddForm.email.getValue().toString() == "") {

			notif("Omple els camps obligatoris");

		} else {

			String nom = professorAddForm.nom.getValue().toString();
			String cognom = professorAddForm.cognoms.getValue().toString();
			String email = professorAddForm.email.getValue().toString();
			tc = new Teacher(nom, cognom, email);

		}

		return tc;
	}

	/**
	 * Selección de los campos que nos permiten obtener un profesor en concreto
	 * 
	 * @return Objeto de tipo Teacher
	 */
	private Teacher getTeacherEdit() {
		Object id = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("id");
		String nom = professorEditForm.nom.getValue().toString();
		String cognom = professorEditForm.cognoms.getValue().toString();
		String email = professorEditForm.email.getValue().toString();

		Teacher tc = new Teacher(Integer.parseInt(id.toString()), nom, cognom, email);
		return tc;
	}

	/**
	 * Filtro para encontrar profesores por apellido
	 * 
	 * @return componente de tipo TextField con su listener configurado
	 */
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
				filter = new SimpleStringFilter("cognoms", event.getText(), true, false);
				f.addContainerFilter(filter);

			}
		});
		return txtSearch;
	}

	/**
	 * Limpia los campos del formulario de edición de profesor
	 */
	private void clearEditForm() {
		professorEditForm.nom.clear();
		professorEditForm.cognoms.clear();
		professorEditForm.email.clear();
		professorEditForm.isTutor.clear();
	}

	/**
	 * Limpia los campos del formulario de añadir de profesor
	 */
	private void clearAddForm() {
		professorAddForm.nom.clear();
		professorAddForm.cognoms.clear();
		professorAddForm.email.clear();
		professorAddForm.isTutor.clear();

	}

	/**
	 * Configuración principal de botones y estilos de la clase
	 */
	private void buttonsSettings() {
		// TODO Auto-generated method stub

		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		bAdd.setCaption("Afegir");
		txtTitle.setValue("Gestió de Professors");
		bDelete.addStyleName(ValoTheme.BUTTON_DANGER);
		bAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bRegister.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonEdit.addStyleName(ValoTheme.BUTTON_PRIMARY);

		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		bRegister.setVisible(false);
		bDelete.setVisible(false);
		bAdd.setEnabled(true);

	}

	/**
	 * Configuración principal de las funciones principales del componente Grid
	 * 
	 * @return Componente Grid
	 */
	public Grid GridProperties() {

		// Fill the grid with data
		docents = JPAContainerFactory.make(Teacher.class, em);
		grid = new Grid("", docents);
		grid.setSizeFull();
		grid.setContainerDataSource(docents);
		grid.setColumnReorderingAllowed(true);
		grid.setColumns("nom", "cognoms", "email");

		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(new SelectionListener() {

			@Override
			public void select(SelectionEvent event) {
				// TODO Auto-generated method stub
				bAdd.setEnabled(true);
				buttonEdit.setEnabled(true);
				bDelete.setEnabled(false);

			}
		});

		return grid;
	}

	/**
	 * Configuración principal de la ventana emergente de añadir profesor
	 */
	private void WindowPropertiesAddTeacher() {

		windowAdd.setCaption("Introdueix nou professor");
		windowAdd.setHeight("45%");
		windowAdd.setWidth("42%");
		windowAdd.setModal(true);
		windowAdd.center();
		windowAdd.setDraggable(false);
		windowAdd.setContent(professorAddForm);
	}

	/**
	 * Configuración principal de la ventana emergente de editar profesor
	 */
	private void WindowPropertiesEditTeacher() {

		// window.setContent(form);
		windowEdit.setHeight("45%");
		windowEdit.setWidth("42%");
		windowEdit.setDraggable(false);
		windowEdit.setModal(true);
		windowEdit.setCaption("Introdueix nou professor");
		windowEdit.center();
		windowEdit.setContent(professorEditForm);
	}

	/**
	 * Configuración personalizada de las notificaciones al usuario
	 */
	public void notif(String mensaje) {

		Notification notif = new Notification(mensaje, null, Notification.Type.ASSISTIVE_NOTIFICATION, true);

		notif.show(Page.getCurrent());
		notif.setDelayMsec(500);
		notif.setPosition(Position.TOP_CENTER);
	}

	/**
	 * Recarga de la Grid, para actualizacion de datos al momento
	 */
	public void reloadGrid() {
		grid.setVisible(false);
		grid = new Grid();
		vHorizontalMain.addComponent(GridProperties());

	}

	/**
	 * Relleno de datos para el componente NativeSelect de los grupos. Compara
	 * los grupos totales creados con los que tienen asignado profesor, para
	 * así, mostrar en el componente solo los grupos que no tienen asignado un
	 * tutor
	 */
	private void PopulateNativeSelect() {
		List<Group> lista1 = new ArrayList<Group>();
		List<Tutor> lista2 = new ArrayList<Tutor>();
		List<String> listaBuena = new ArrayList<String>();

		grupsjpa = new GroupJPAManager();
		lista1 = grupsjpa.getGroups();
		lista2 = grupsjpa.getGroups2();

		boolean existe = false;

		System.out.println(lista1.size());

		for (int i = 0; i < lista1.size(); i++) {
			for (int j = 0; j < lista2.size(); j++) {
				if (lista1.get(i).getId().equals(lista2.get(j).getGrup())) {
					lista1.remove(i);

				}

			}
		}
		professorAddForm.selectGroup.removeAllItems();

		for (int i = 0; i < lista1.size(); i++) {

			professorAddForm.selectGroup.addItem(lista1.get(i).getId());
		}

	}

	/**
	 * Limpia las celdas seleccionadas del componente Grid
	 */
	public void clear() {
		// TODO Auto-generated method stub

		// bAdd.setEnabled(false);
		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		grid.deselectAll();

	}

}
