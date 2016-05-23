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
package com.example.view.TutorView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.Dates.ConverterDates;
import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Entities.User;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.GroupJPAManager;
import com.example.Logic.StudentsJPAManager;
import com.example.Logic.UserJPAManager;
import com.example.LoginView.LoginView;
import com.example.Templates.MainContentView;
import com.example.view.TutorView.TutorViewDetailsForm;
import com.itextpdf.text.DocumentException;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Grid.FooterCell;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Grid.SingleSelectionModel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.themes.ValoTheme;

public class TutorViewGrupsJava extends MainContentView {

	private Grid mygrid;
	private JPAContainer<Student> students;
	private JPAContainer<Group> container;

	private Window window = new Window();
	private Window windowDetails = new Window();
	private TutorViewDetailsForm tutorviewdetailsform = new TutorViewDetailsForm();
	private GroupJPAManager MA = new GroupJPAManager();
	private EntityManagerUtil entman = new EntityManagerUtil();
	private EntityManager em = entman.getEntityManager();

	private ConverterDates converter = new ConverterDates();
	private Query query = null;

	public TutorViewGrupsJava() {
		// TODO Auto-generated constructor stub

		buttonsSettings();
		gridProperties();
		filterTextProperties();
		WindowDetailProperties();

		vHorizontalMain.addComponent(gridProperties());

		bAdd.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				// window.setCaption("Afegir nou grup");
				// grupForm.txtGrup.setValue("");
				// grupForm.txtMaxAl.setValue("");
				// AddItemToGrupForm();
			}
		});
		// Re-utilización del botón Edit para Detalls.
		buttonEdit.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				detailsStudent();

			}
		});

	}

	private void WindowDetailProperties() {
		// TODO Auto-generated method stub
		windowDetails.setHeight("650px");
		windowDetails.setWidth("50%");
		windowDetails.setDraggable(false);
		windowDetails.setModal(true);
		windowDetails.setCaption("Detalls de l'alumne");
		windowDetails.center();
		windowDetails.setContent(tutorviewdetailsform);
		windowDetails.setEnabled(true);
		windowDetails.setVisible(true);
	}

	private void detailsStudent() {

		UI.getCurrent().addWindow(windowDetails);

		tutorviewdetailsform.imageStudent.setVisible(false);
		tutorviewdetailsform.dateStudent.setReadOnly(false);
		tutorviewdetailsform.lastnameStudent.setReadOnly(false);
		tutorviewdetailsform.nameStudent.setReadOnly(false);

		Object id = mygrid.getContainerDataSource().getItem(mygrid.getSelectedRow()).getItemProperty("id");
		Object name = mygrid.getContainerDataSource().getItem(mygrid.getSelectedRow()).getItemProperty("nom");
		Object surname = mygrid.getContainerDataSource().getItem(mygrid.getSelectedRow()).getItemProperty("cognoms");
		Object email = mygrid.getContainerDataSource().getItem(mygrid.getSelectedRow()).getItemProperty("email");
		Object phone = mygrid.getContainerDataSource().getItem(mygrid.getSelectedRow()).getItemProperty("telefon");
		Object data = mygrid.getContainerDataSource().getItem(mygrid.getSelectedRow())
				.getItemProperty("data_naixement");
		Object curs = mygrid.getContainerDataSource().getItem(mygrid.getSelectedRow()).getItemProperty("curs");
		Object grup = mygrid.getContainerDataSource().getItem(mygrid.getSelectedRow()).getItemProperty("grup");

		System.out.println(id);

		em.getTransaction().begin();

		query = em.createNativeQuery(

				"SELECT count(id) FROM alumne a, amonestacio amo WHERE a.id = amo.alumne AND a.id=" + id);

		em.getTransaction().commit();

		tutorviewdetailsform.nameStudent.setValue(name.toString());
		tutorviewdetailsform.lastnameStudent.setValue(surname.toString());
		tutorviewdetailsform.dateStudent.setValue(converter.converterDate2(data.toString()));
		tutorviewdetailsform.dateStudent.setReadOnly(true);
		tutorviewdetailsform.lastnameStudent.setReadOnly(true);
		tutorviewdetailsform.nameStudent.setReadOnly(true);

		if (email.toString() == null) {
			tutorviewdetailsform.emailStudent.setValue(" ");
		} else {
			tutorviewdetailsform.emailStudent.setValue(email.toString());

		}

		String countWarning = null;
		tutorviewdetailsform.phoneStudent.setValue(phone.toString());
		tutorviewdetailsform.coursStudent.setValue(curs.toString());
		if (query.getSingleResult() == null) {
			countWarning = "0";
		} else {
			countWarning = query.getSingleResult().toString();

		}
		tutorviewdetailsform.nameStudent2.setValue(countWarning);
		tutorviewdetailsform.cancelButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				windowDetails.close();
			}
		});

		if (windowDetails.isAttached()) {

			getUI().removeWindow(windowDetails);
		}
		UI.getCurrent().addWindow(windowDetails);

	}

	private void buttonsSettings() {
		// TODO Auto-generated method stub

		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		clearTxt.setIcon(FontAwesome.TIMES);

		txtTitle.setValue("Gestió de Grups");
		bDelete.addStyleName(ValoTheme.BUTTON_DANGER);
		bAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bRegister.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonEdit.addStyleName(ValoTheme.BUTTON_PRIMARY);

		buttonEdit.setCaption("Detalls");

		txtSearch.setVisible(false);
		clearTxt.setVisible(false);

		bDelete.setEnabled(false);
		bDelete.setVisible(false);
		buttonEdit.setEnabled(true);
		buttonEdit.setVisible(true);
		bRegister.setEnabled(false);
		bRegister.setVisible(false);
		bAdd.setEnabled(false);
		bAdd.setVisible(false);

	}

	private TextField filterTextProperties() {
		// TODO Auto-generated method stub
		txtSearch.setInputPrompt("Filtra per grup");
		txtSearch.addTextChangeListener(new TextChangeListener() {

			SimpleStringFilter filter = null;
			SimpleStringFilter filter2 = null;

			@Override
			public void textChange(TextChangeEvent event) {
				// TODO Auto-generated method stub

				Filterable f = (Filterable) mygrid.getContainerDataSource();

				// Remove old filter
				if (filter != null)
					f.removeContainerFilter(filter);

				// Set new filter for the "Name" column
				filter = new SimpleStringFilter("id", event.getText(), true, false);
				// filter2 = new SimpleStringFilter("cognom", event.getText(),
				// true, false);
				f.addContainerFilter(filter);
				// f.addContainerFilter(filter2);
			}
		});
		return txtSearch;
	}

	public Grid gridProperties() {

		students = JPAContainerFactory.make(Student.class, em);
		int id = 0;
		String grupTutor = "";
		try {
			id = Integer.parseInt(getUI().getCurrent().getSession().getAttribute("id").toString());
			grupTutor = MA.getGroupTutor(id);
		} catch (NullPointerException e) {

		}

		Filter filter = new Compare.Equal("grup", grupTutor);
		students.addContainerFilter(filter);

		mygrid = new Grid("", students);

		mygrid.setSizeFull();
		mygrid.setContainerDataSource(students);
		mygrid.setColumnReorderingAllowed(true);
		mygrid.setColumns("nom", "cognoms", "grup");
		mygrid.setColumnReorderingAllowed(true);

		mygrid.setSelectionMode(SelectionMode.SINGLE);
		mygrid.addSelectionListener(new SelectionListener() {

			@Override
			public void select(SelectionEvent event) {
				// getItemSelectedToAmonestacioForm();
				bDelete.setEnabled(true);
				buttonEdit.setEnabled(true);

			}
		});

		return mygrid;

	}

	private void SetVisible(boolean visible) {
		mygrid.setVisible(visible);

	}

}
