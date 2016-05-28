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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;

import com.example.Entities.Student;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.TutorJPAManager;
import com.example.Logic.UserJPAManager;
import com.example.LoginView.LoginView;
import com.example.Templates.MainView;
import com.example.view.AdminView.AdminView;
import com.example.view.AdminView.Group.AdminViewGroupJava;
import com.example.view.AdminView.Students.AdminViewStudentJava;
import com.example.view.AdminView.Teacher.AdminViewTeacherJava;
import com.example.view.TeacherView.TeacherConfigView;
import com.example.view.TeacherView.TeacherViewWarningJava;
import com.itextpdf.text.DocumentException;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.themes.ValoTheme;

public class TutorView extends MainView implements View {
	public static final String NAME = "Tutor";
	private TutorOwnWarningsJava tutorownwarning;
	private TeacherViewWarningJava vistaAmonestacion;
	private JPAContainer<Student> alumnes;
	private Grid grid;
	private TeacherConfigView vistaConfig;
	private TutorJPAManager tutorJPA;
	private EntityManagerUtil entman = new EntityManagerUtil();
	private EntityManager em = entman.getEntityManager();
	private ResourceBundle rb = ResourceBundle.getBundle("GA");
	private TutorViewGrupsJava TutorViewGrups;

	public TutorView() throws MalformedURLException, DocumentException, IOException, SQLException {

		content.addStyleName("contenido");
		setLogo();
		// Side menu button options
		vistaAmonestacion = new TeacherViewWarningJava();
		TutorViewGrups = new TutorViewGrupsJava();
		vistaConfig = new TeacherConfigView();
		tutorownwarning = new TutorOwnWarningsJava();
		// false para que no aparezca junto con las amonestaciones nada mas
		// entrar
		TutorViewGrups.setVisible(false);
		tutorownwarning.setVisible(false);
		vistaConfig.setVisible(false);

		students.setVisible(false);
		teachers.setVisible(false);
		groups.setVisible(false);
		tutors.setVisible(false);
		groupsTutor.setVisible(true);
		usuaris.setVisible(false);
		amonestacions.setVisible(false);
		mevesAmonestacions.setVisible(true);
		warning.focus();
		charts.setVisible(false);
		informes.setVisible(false);
		cargarCSV.setVisible(false);
		ficurs.setVisible(false);
		content.setMargin(false);
		content.setSpacing(true);
		sep5.setVisible(false);
		sep4.setVisible(false);
		sep3.setVisible(false);
		
		warning.addClickListener(e -> viewWarnings());
		mevesAmonestacions.addClickListener(e -> ownWarnings());
		configuracio.addClickListener(e -> Config());
		logout.addClickListener(e -> logoutActions());

		content.removeAllComponents();
		content.addComponents(vistaAmonestacion, TutorViewGrups, tutorownwarning,vistaConfig);

		groupsTutor.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				vistaAmonestacion.setVisible(false);
				TutorViewGrups.setVisible(true);
				tutorownwarning.setVisible(false);
			}
		});

	}

	
	public void Config() {
		vistaConfig.setVisible(true);
		TutorViewGrups.setVisible(false);
		vistaAmonestacion.setVisible(false);
		tutorownwarning.setVisible(false);
		

	}
	private void viewWarnings() {
		// TODO Auto-generated method stub

		TutorViewGrups.setVisible(false);
		vistaAmonestacion.setVisible(true);
		tutorownwarning.setVisible(false);
		vistaConfig.setVisible(false);


	}

	private void ownWarnings() {
		// TODO Auto-generated method stub
		tutorownwarning.reloadGrid();
		TutorViewGrups.setVisible(false);
		vistaAmonestacion.setVisible(false);
		tutorownwarning.setVisible(true);
		vistaConfig.setVisible(false);

	}

	public void logoutActions() {

		getUI().getCurrent().addWindow(DeleteSubWindows());

	}

	private void setWellcome() {
		// TODO Auto-generated method stub
		tutorJPA = new TutorJPAManager();

		try {

			int id = Integer.parseInt(getUI().getCurrent().getSession().getAttribute("id").toString());
			wellcome.setCaption("Benvingut/uda " + tutorJPA.getNomTutorHeader(id));

		} catch (NullPointerException e) {

		}

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

		// en caso de no haberse logeado anteriormente se mandara a la vista
		// principal(login)
		if (getUI().getCurrent().getSession().getAttribute("login") == null) {
			// getUI().getSession().setAttribute("user", "");
			getUI().getSession().setAttribute("id", 0);
			getUI().getPage().setLocation("http://localhost:8082/GA");
			VaadinService.getCurrentRequest().getWrappedSession().invalidate();

		} else {

			setWellcome();
		}

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
		grid.addSelectionListener(new SelectionListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void select(SelectionEvent event) {
				// TODO Auto-generated method stub
				// bAdd.setEnabled(true);
				// buttonEdit.setEnabled(true);
				// bDelete.setEnabled(true);

			}
		});

		return grid;

	}

	public Window DeleteSubWindows() {

		Window win = new Window(" Tancar sessió");

		win.setWidth("25%");
		win.setHeight("15%");
		win.setIcon(FontAwesome.CLOSE);
		win.setDraggable(false);
		win.setClosable(false);
		win.setResizable(false);
		win.setModal(true);
		win.center();

		Label question = new Label(" Estas segur?");
		Button yes = new Button("Sí");
		yes.addStyleName(ValoTheme.BUTTON_PRIMARY);
		Button no = new Button("No");
		no.addStyleName(ValoTheme.BUTTON_DANGER);

		no.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				win.close();

			}
		});

		yes.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				win.close();
				logout();
			}
		});

		HorizontalLayout buttons = new HorizontalLayout(yes, no);
		buttons.setSpacing(true);

		VerticalLayout content = new VerticalLayout(question, buttons);
		win.setContent(content);

		return win;

	}

	public void logout() {

		getUI().getNavigator().navigateTo(LoginView.NAME);
		getUI().getCurrent().getSession().setAttribute("id", null);
		getUI().getCurrent().getSession().setAttribute("user", null);
		getUI().getCurrent().getSession().close();
		notif("Sessió tancada correctament!");

	}

	public void notif(String mensaje) {

		Notification notif = new Notification(mensaje, null, Notification.Type.ASSISTIVE_NOTIFICATION, true); // Contains
																												// HTML

		// Customize it
		notif.show(Page.getCurrent());
		notif.setDelayMsec(500);
		notif.setPosition(Position.TOP_CENTER);
	}

	private void setLogo() throws IOException {
		// TODO Auto-generated method stub
		File currDir = new File(".");
		String path2 = currDir.getCanonicalPath();

		FileResource resource = new FileResource(new File(rb.getString("main_logo")));
		Image logo = new Image("", resource);
		logo.setWidth("90px");
		logo.setHeight("90px");
		vImage.removeAllComponents();
		vImage.addComponent(logo);

	}

}
