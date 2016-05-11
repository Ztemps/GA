package com.example.view.AdminView.Teacher;

import java.util.Date;

import javax.persistence.NonUniqueResultException;

import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Entities.Teacher;
import com.example.Logic.Cursos;
import com.example.Logic.EntityManagerUtil;
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

public class AdminViewTeacherJava extends MainContentView {

	private Grid grid;
	private Window windowAdd = new Window();
	private Window windowEdit = new Window();
	private JPAContainer<Teacher> docents;
	private JPAContainer<Group> container;
	private TeachersJPAManager MA;
	private AdminViewTeacherFormJava professorAddForm;
	private AdminViewTeacherFormJava professorEditForm;

	public AdminViewTeacherJava() {

		professorAddForm = new AdminViewTeacherFormJava();
		professorEditForm = new AdminViewTeacherFormJava();
		buttonsSettings();
		filterTextProperties();
		WindowPropertiesAddTeacher();
		WindowPropertiesEditTeacher();
		listeners();
		ClearText();
		PopulateNativeSelect();
		vHorizontalMain.addComponent(GridProperties());
	}

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

				addTeacher();
			}

		});
	}

	private void addTeacher() {
		professorAddForm.aceptarButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				try {

					Teacher teacher = getTeacherAdd();
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

				} catch (NullPointerException e) {

					Notification.show("Omple els camps obligatoris", Notification.TYPE_ERROR_MESSAGE);

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
				try {

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

				} catch (NullPointerException e) {

					Notification.show("Omple els camps obligatoris", Notification.TYPE_ERROR_MESSAGE);

				}

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

	private Teacher getTeacherAdd() {

		String nom = professorAddForm.nom.getValue().toString();
		String cognom = professorAddForm.cognoms.getValue().toString();
		String email = professorAddForm.email.getValue().toString();

		Teacher tc = new Teacher(nom, cognom, email);

		return tc;
	}

	private Teacher getTeacherEdit() {
		Object id = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("id");
		String nom = professorEditForm.nom.getValue().toString();
		String cognom = professorEditForm.cognoms.getValue().toString();
		String email = professorEditForm.email.getValue().toString();

		Teacher tc = new Teacher(Integer.parseInt(id.toString()), nom, cognom, email);
		return tc;
	}

	private TextField filterTextProperties() {
		// TODO Auto-generated method stub
		txtSearch.setInputPrompt("Filtra docent cognom");
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
		professorEditForm.nom.clear();
		professorEditForm.cognoms.clear();
		professorEditForm.email.clear();
		professorEditForm.isTutor.clear();
	}

	private void clearAddForm() {
		professorAddForm.nom.clear();
		professorAddForm.cognoms.clear();
		professorAddForm.email.clear();
		professorAddForm.isTutor.clear();

	}

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
		bAdd.setEnabled(true);
	}

	public Grid GridProperties() {

		// Fill the grid with data
		docents = JPAContainerFactory.make(Teacher.class, EntityManagerUtil.getEntityManager());
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
				bDelete.setEnabled(true);

			}
		});

		return grid;
	}

	private void WindowPropertiesAddTeacher() {

		windowAdd.setCaption("Introdueix nou professor");
		windowAdd.setHeight("45%");
		windowAdd.setWidth("42%");
		windowAdd.setModal(true);
		windowAdd.center();
		windowAdd.setDraggable(false);
		windowAdd.setContent(professorAddForm);
	}

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

	public void notif(String mensaje) {

		Notification notif = new Notification(mensaje, null, Notification.Type.ASSISTIVE_NOTIFICATION, true); // Contains
																												// HTML

		// Customize it
		notif.show(Page.getCurrent());
		notif.setDelayMsec(500);
		notif.setPosition(Position.TOP_CENTER);
	}

	public void reloadGrid() {
		grid.setVisible(false);
		grid = new Grid();
		vHorizontalMain.addComponent(GridProperties());

	}

	private void ClearText() {

		clearTxt.setDescription("Limpiar contenido actual...");
		clearTxt.setIcon(FontAwesome.TIMES);
		clearTxt.addClickListener(e -> {
			txtSearch.clear();

		});

		clearTxt.setIcon(FontAwesome.TIMES);

	}

	private void PopulateNativeSelect() {

		container = JPAContainerFactory.make(Group.class, EntityManagerUtil.getEntityManager());
		professorAddForm.selectGroup.setContainerDataSource(container);
		professorAddForm.selectGroup.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		professorAddForm.selectGroup.setItemCaptionPropertyId("id");

	}

	public void clear() {
		// TODO Auto-generated method stub

		// bAdd.setEnabled(false);
		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		grid.deselectAll();

	}

}
