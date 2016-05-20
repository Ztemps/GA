/*******************************************************************************
 * Gestió d'Amonestacions v1.0
 *
 * Esta obra está sujeta a la licencia Reconocimiento-NoComercial-SinObraDerivada 4.0 Internacional de Creative Commons. 
 * Para ver una copia de esta licencia, visite http://creativecommons.org/licenses/by-nc-nd/4.0/.
 *  
 * @author Francisco Javier Casado Moreno - fcasasdo@elpuig.xeill.net 
 * @author Daniel Pérez Palacino - dperez@elpuig.xeill.net 
 * @author Gerard Enrique Paulino Decena - gpaulino@elpuig.xeill.net 
 * @author Xavier Murcia Gámez - xmurica@elpuig.xeill.net 
 *******************************************************************************/
package com.example.view.AdminView.Group;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.postgresql.util.PSQLException;

import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Logic.Cursos;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.GroupJPAManager;
import com.example.Logic.StudentsJPAManager;
import com.example.Logic.UserJPAManager;
import com.example.Templates.MainContentView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.Position;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Grid.SingleSelectionModel;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.themes.ValoTheme;

public class AdminViewGroupJava extends MainContentView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8190160807165820720L;
	private Grid grid;
	private JPAContainer<Group> grupos;
	private AdminViewGroupForm grupFormAdd;
	private AdminViewGroupForm grupFormEdit;
	private Window windowAdd = new Window();
	private Window windowEdit = new Window();
	private GroupJPAManager MA;
	private EntityManagerUtil entman;
	private EntityManager em;

	public AdminViewGroupJava() throws PersistenceException, PSQLException {
		entman = new EntityManagerUtil();
		em = entman.getEntityManager();
		// TODO Auto-generated constructor stub
		grupFormAdd = new AdminViewGroupForm();
		grupFormAdd = new AdminViewGroupForm();
		buttonsSettings();
		GridProperties();
		WindowPropertiesEditGroup();
		WindowPropertiesAddGroup();
		Listeners();

		filterTextProperties();

		vHorizontalMain.addComponent(GridProperties());
	}

	private void Listeners() {
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
				addGroup();

			}

		});

	}

	private void addGroup() {

		grupFormAdd.aceptarButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				try {

					MA = new GroupJPAManager();

					String grupValue = grupFormAdd.txtGrup.getValue().toString();

					if (!grupValue.contains("ESO ") || grupValue.toString().length() > 6) {

						notif("Format incorrecte. Exemple (ESO 1A)");

					} else {
						
						Group grup = getGroupAdd();
						MA.updateGroup(grup);
						MA.closeTransaction();
						reloadGrid();
						windowAdd.close();
						SingleSelectionModel m = (SingleSelectionModel) grid.getSelectionModel();

						int Fila = (grid.getContainerDataSource().getItemIds().size()) - 1;

						Object id = grid.getContainerDataSource().getIdByIndex(Fila);
						System.out.println("Fila: " + Fila);
						System.out.println("Grup: " + id);

						m.select(id);
						grid.scrollTo(id);
						clearAddForm();

						notif("Grup creat correctament");

					}

				} catch (NullPointerException e) {

					notif("Omple els camps obligatoris");
				}

			}

		});

		grupFormAdd.cancelarButton.addClickListener(new ClickListener() {
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

	private void deleteGroup() {
		MA = new GroupJPAManager();

		Object id = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("id");
		Object numAlumnes = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("max_alumnes");

		Group gr = new Group(id.toString(), Integer.parseInt(numAlumnes.toString()));

		MA.removeGroup(gr);
		MA.closeTransaction();

		notif("Grup esborrat correctament");

	}

	private Group getGroupAdd() {

		String id = grupFormAdd.txtGrup.getValue().toString().toUpperCase();
		int numAlumnes = 35;

		Group gr = new Group(id, numAlumnes);

		return gr;
	}

	private Group getGroupEdit() {

		String id = grupFormEdit.txtGrup.getValue().toString();
		int numAlumnes = 0;
		try {
			// Por algún motivo, al editar por segunda vez, da error de formato
			// del número
			numAlumnes = Integer.parseInt(grupFormEdit.txtMaxAl.getValue().toString());

		} catch (NumberFormatException nfe) {
			notif("Format del número incorrecte");

		}

		Group gr = new Group(id, numAlumnes);

		return gr;
	}

	private TextField filterTextProperties() {
		txtSearch.setInputPrompt("Filtra grup id");
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
				filter = new SimpleStringFilter("id", event.getText(), true, false);
				f.addContainerFilter(filter);
			}
		});
		return txtSearch;
	}

	private void clearEditForm() {
		grupFormEdit.txtGrup.clear();
		grupFormEdit.txtMaxAl.clear();

	}

	private void clearAddForm() {
		grupFormAdd.txtGrup.clear();
		grupFormAdd.txtMaxAl.clear();

	}

	private void buttonsSettings() {
		// TODO Auto-generated method stub

		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		clearTxt.setIcon(FontAwesome.TIMES);

		txtTitle.setValue("Llista de Grups");
		bDelete.addStyleName(ValoTheme.BUTTON_DANGER);
		bAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bRegister.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonEdit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bDelete.setVisible(false);
		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		buttonEdit.setVisible(false);
		bRegister.setVisible(false);
		bAdd.setEnabled(true);
		clearTxt.setVisible(false);
		txtSearch.setVisible(false);

	}

	public Grid GridProperties() {

		grupos = JPAContainerFactory.make(Group.class, em);
		grid = new Grid("", grupos);

		grid.setSizeFull();
		grid.setContainerDataSource(grupos);
		grid.setColumnReorderingAllowed(true);
		grid.setColumns("id");

		// vGrid = new Grid("", grupos);
		grid.setSizeFull();
		// vGrid.setContainerDataSource(grupos);
		grid.setColumnReorderingAllowed(true);
		// vGrid.setColumns("id", "max_alumnes");

		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(new SelectionListener() {

			@Override
			public void select(SelectionEvent event) {
				// TODO Auto-generated method stub
				// getItemSelectedToAmonestacioForm();
				bDelete.setEnabled(true);
				buttonEdit.setEnabled(true);

			}
		});

		return grid;

	}

	private void WindowPropertiesAddGroup() {

		windowAdd.setCaption("Afegir nou grup");
		windowAdd.setHeight("40%");
		windowAdd.setWidth("40%");
		windowAdd.setModal(true);
		windowAdd.center();
		windowAdd.setDraggable(false);
		grupFormAdd.txtMaxAl.setVisible(false);
		windowAdd.setContent(grupFormAdd);
	}

	private void WindowPropertiesEditGroup() {

		windowEdit.setCaption("Editar grup");
		windowEdit.setHeight("40%");
		windowEdit.setWidth("40%");
		windowEdit.setModal(true);
		windowEdit.center();
		windowEdit.setDraggable(false);
		windowEdit.setContent(grupFormEdit);
	}

	private void SetVisible(boolean visible) {
		grid.setVisible(visible);

	}

	public void clear() {
		// TODO Auto-generated method stub

		// bAdd.setEnabled(false);
		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		grid.deselectAll();

	}

	public void reloadGrid() {
		grid.setVisible(false);
		grid = new Grid();
		vHorizontalMain.addComponent(GridProperties());

	}

	/*
	 * public Window DeleteSubWindows() {
	 * 
	 * Window win = new Window(" Borrar grupo");
	 * 
	 * win.setWidth("300"); win.setHeight("150");
	 * win.setIcon(FontAwesome.CLOSE); win.setDraggable(false);
	 * win.setClosable(false); win.setResizable(false); win.setModal(true);
	 * win.center();
	 * 
	 * // Label text = new Label("Borrar grupo "+ txtGrup.getValue()); // Label
	 * question = new Label(" Estas seguro?"); Button yes = new Button("Sí");
	 * Button no = new Button("No");
	 * 
	 * no.addClickListener(new ClickListener() {
	 * 
	 * @Override public void buttonClick(ClickEvent event) { // TODO
	 * Auto-generated method stub win.close(); } });
	 * 
	 * yes.addClickListener(new ClickListener() {
	 * 
	 * @Override public void buttonClick(ClickEvent event) { // TODO
	 * Auto-generated method stub
	 * 
	 * Object id = grid.getContainerDataSource().getItem(grid.getSelectedRow()).
	 * getItemProperty("id"); Object max_alumnes =
	 * grid.getContainerDataSource().getItem(grid.getSelectedRow()).
	 * getItemProperty("max_alumnes");
	 * 
	 * String id2 = id.toString(); int maxalumnes =
	 * Integer.parseInt(max_alumnes.toString());
	 * 
	 * 
	 * Group grup = new Group(id2, maxalumnes);
	 * 
	 * MA.deleteGrup(grup);
	 * 
	 * win.close(); notif("Grupo borrado Correctamente");
	 * 
	 * } });
	 * 
	 * HorizontalLayout buttons = new HorizontalLayout(yes, no);
	 * buttons.setSpacing(true);
	 * 
	 * VerticalLayout content = new VerticalLayout(buttons);
	 * win.setContent(content);
	 * 
	 * return win;
	 * 
	 * }
	 */

	public void notif(String mensaje) {

		Notification notif = new Notification(mensaje, null, Notification.Type.ASSISTIVE_NOTIFICATION, true); // Contains
																												// HTML

		// Customize it
		notif.show(Page.getCurrent());
		notif.setDelayMsec(500);
		notif.setPosition(Position.TOP_CENTER);
	}

}
