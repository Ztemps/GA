package com.example.view.AdminView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

import org.eclipse.persistence.internal.oxm.mappings.Login;

import com.example.Entities.Teacher;
import com.example.Logic.UserJPAManager;
import com.example.LoginView.LoginView;
import com.example.Pdf.generatePDF;
import com.example.Templates.MainView;
import com.example.view.AdminView.CSV.AdminViewCSVUploadJava;
import com.example.view.AdminView.Charts.AdminViewCharts;
import com.example.view.AdminView.Group.AdminViewGroupJava;
import com.example.view.AdminView.Reports.AdminViewReportsJava;
import com.example.view.AdminView.Settings.AdminViewSettings;
import com.example.view.AdminView.Settings.AdminViewSettingsJava;
import com.example.view.AdminView.Students.AdminViewStudentJava;
import com.example.view.AdminView.Teacher.AdminViewTeacherJava;
import com.example.view.AdminView.Tutor.AdminViewTutorJava;
import com.example.view.AdminView.User.AdminViewUser;
import com.example.view.AdminView.Warning.AdminViewWarningJava;
import com.example.view.AdminView.Warning.AdminWarning;
import com.example.view.AdminView.Warnings.AdminViewWarnings;
import com.example.view.TeacherView.TeacherOwnWarningsJava;
import com.example.view.TeacherView.TeacherViewWarningJava;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.itextpdf.text.DocumentException;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AdminView extends MainView implements View {

	private static final long serialVersionUID = 4184290153198838185L;
	public static final String NAME = "Admin";
	private static AdminViewGroupJava ViewGrupos;
	private static AdminViewStudentJava ViewStudents;
	private static AdminViewTeacherJava ViewDocents;
	private static AdminViewUser ViewUsers;
	private static AdminViewWarnings ViewListWarnings;
	private static AdminViewWarningJava ViewWarning;
	private static AdminViewCSVUploadJava ViewCSV;
	private static TeacherOwnWarningsJava ViewTeachersWarnings;
	private static AdminViewCharts ViewCharts;
	private static AdminViewReportsJava ViewForms;
	private static AdminViewSettingsJava ViewSettings;
	private UserJPAManager ma;
	private static AdminViewTutorJava Viewtutors;
	private BufferedReader br = null;

	@Override
	public void enter(ViewChangeEvent event) {

		if (getUI().getCurrent().getSession().getAttribute("login") == null) {
			
			getUI().getSession().setAttribute("user", "");
			getUI().getSession().setAttribute("id", "");	
			getUI().getPage().setLocation("/GA");
			
		}else{
			
			setWellcome();
		}

	}

	public AdminView() throws IOException, DocumentException, SQLException {

	
			
			content.addStyleName("contenido");

			loadView();

			ViewGrupos = new AdminViewGroupJava();
			ViewStudents = new AdminViewStudentJava();
			ViewDocents = new AdminViewTeacherJava();
			ViewWarning = new AdminViewWarningJava();
			ViewUsers = new AdminViewUser();
			ViewListWarnings = new AdminViewWarnings();
			ViewCSV = new AdminViewCSVUploadJava();
			ViewTeachersWarnings = new TeacherOwnWarningsJava();
			Viewtutors = new AdminViewTutorJava();
			ViewCharts = new AdminViewCharts();
			ViewForms = new AdminViewReportsJava();
			ViewSettings = new AdminViewSettingsJava();

			content.removeAllComponents();
			content.addComponents(ViewGrupos);
			content.addComponents(ViewStudents);
			content.addComponent(ViewDocents);
			content.addComponent(ViewWarning);
			content.addComponent(ViewUsers);
			content.addComponent(ViewListWarnings);
			content.addComponent(ViewCSV);
			content.addComponent(ViewTeachersWarnings);
			content.addComponent(Viewtutors);
			content.addComponent(ViewCharts);
			content.addComponent(ViewForms);
			content.addComponent(ViewSettings);

			warning.focus();
			ViewDocents.setVisible(false);
			ViewStudents.setVisible(false);
			ViewCharts.setVisible(false);
			ViewForms.setVisible(false);
			ViewGrupos.setVisible(false);
			ViewUsers.setVisible(false);
			ViewListWarnings.setVisible(false);
			ViewCSV.setVisible(false);
			ViewTeachersWarnings.setVisible(false);
			mevesAmonestacions.setVisible(false);
			groupsTutor.setVisible(false);
			Viewtutors.setVisible(false);
			ViewSettings.setVisible(false);

			sep.addClickListener(e -> subMenuWarning());
			sep3.addClickListener(e -> subMenuGeneral());
			sep4.addClickListener(e -> subMenuGeneral2());
			sep5.setDisableOnClick(true);	
			
			
		

	}

	private void subMenuGeneral2() {
		// TODO Auto-generated method stub
		if (amonestacions.isVisible()) {
			amonestacions.setVisible(false);
			charts.setVisible(false);
			cargarCSV.setVisible(false);
			informes.setVisible(false);
			configuracio.setVisible(false);
		} else {

			amonestacions.setVisible(true);
			charts.setVisible(true);
			cargarCSV.setVisible(true);
			informes.setVisible(true);
			configuracio.setVisible(true);
		}

	}

	private void subMenuGeneral() {
		// TODO Auto-generated method stub
		if (students.isVisible()) {
			students.setVisible(false);
			tutors.setVisible(false);
			teachers.setVisible(false);
			groups.setVisible(false);
			usuaris.setVisible(false);
		} else {

			students.setVisible(true);
			tutors.setVisible(true);
			teachers.setVisible(true);
			groups.setVisible(true);
			usuaris.setVisible(true);
		}

	}

	private void subMenuWarning() {
		// TODO Auto-generated method stub

		if (warning.isVisible()) {
			warning.setVisible(false);
		} else {

			warning.setVisible(true);
		}

	}

	private void loadView() throws IOException {
		// TODO Auto-generated method stub

		warning.setVisible(true);
		mevesAmonestacions.setVisible(true);
		students.setVisible(true);
		teachers.setVisible(true);
		groups.setVisible(true);
		logout.setVisible(true);
		cargarCSV.setVisible(true);
		ficurs.setVisible(true);
		configuracio.setVisible(true);
		informes.setVisible(true);
		usuaris.setVisible(true);
		charts.setVisible(true);
		// setWellcome();
		setLogo();

		// AMONESTAR
		warning.addClickListener(e -> viewWarning());

		// LES MEVES AMONESTACIONS
		mevesAmonestacions.addClickListener(e -> ViewTeachersWarnings());

		// PROFESORS
		teachers.addClickListener(e -> viewTeachers());

		// TUTOR
		tutors.addClickListener(e -> ViewTutors());

		// GRUPS
		groups.addClickListener(e -> viewGroup());

		// ALUMNES
		students.addClickListener(e -> viewStudents());

		// USERS
		usuaris.addClickListener(e -> viewUsers());

		// AMONESTACIONS
		amonestacions.addClickListener(e -> viewWarnings());

		// CSV
		cargarCSV.addClickListener(e -> viewCsv());

		// CHARTS
		charts.addClickListener(e -> ViewCharts());

		// FORMS
		informes.addClickListener(e -> ViewReports());

		// CONFIGURACIO
		configuracio.addClickListener(e -> viewConfiguracio());

		// SALIR
		logout.addClickListener(e -> logoutActions());

	} // end loadView

	private void setLogo() throws IOException {
		// TODO Auto-generated method stub
		File currDir = new File(".");
		String path2 = currDir.getCanonicalPath();

		FileResource resource = new FileResource(new File(path2 + "/git/ga2/WebContent/VAADIN/themes/images/logo.png"));
		Image logo = new Image("", resource);
		logo.setWidth("90px");
		logo.setHeight("90px");
		vImage.removeAllComponents();
		vImage.addComponent(logo);

	}

	private void ViewCharts() {
		// TODO Auto-generated method stub

		ViewCharts.clear();
		ViewCharts.reloadChart();

		ViewTeachersWarnings.setVisible(false);
		ViewListWarnings.setVisible(false);
		ViewUsers.setVisible(false);
		ViewDocents.setVisible(false);
		ViewGrupos.setVisible(false);
		ViewStudents.setVisible(false);
		ViewWarning.setVisible(false);
		ViewCSV.setVisible(false);
		Viewtutors.setVisible(false);
		ViewCharts.setVisible(true);
		ViewSettings.setVisible(false);
		ViewForms.setVisible(false);

	}

	private void viewConfiguracio() {
		ViewStudents.setVisible(false);
		ViewGrupos.setVisible(false);
		ViewDocents.setVisible(false);
		ViewWarning.setVisible(false);
		ViewUsers.setVisible(false);
		ViewListWarnings.setVisible(false);
		ViewCSV.setVisible(false);
		ViewTeachersWarnings.setVisible(false);
		Viewtutors.setVisible(false);
		ViewCharts.setVisible(false);
		ViewSettings.setVisible(true);
		ViewForms.setVisible(false);

	}

	private void ViewTutors() {

		Viewtutors.clear();
		Viewtutors.reloadGrid();

		ViewCharts.setVisible(false);

		ViewTeachersWarnings.setVisible(false);
		ViewListWarnings.setVisible(false);
		ViewUsers.setVisible(false);
		ViewDocents.setVisible(false);
		ViewGrupos.setVisible(false);
		ViewStudents.setVisible(false);
		ViewWarning.setVisible(false);
		ViewCSV.setVisible(false);
		Viewtutors.setVisible(true);
		ViewSettings.setVisible(false);
		ViewForms.setVisible(false);

	}

	private void ViewTeachersWarnings() {

		ViewTeachersWarnings.clear();
		ViewUsers.reloadGrid();
		ViewListWarnings.setVisible(false);
		ViewUsers.setVisible(false);
		ViewDocents.setVisible(false);
		ViewGrupos.setVisible(false);
		ViewStudents.setVisible(false);
		ViewWarning.setVisible(false);
		ViewCSV.setVisible(false);
		ViewCharts.setVisible(false);
		Viewtutors.setVisible(false);
		ViewTeachersWarnings.setVisible(true);
		ViewSettings.setVisible(false);
		ViewForms.setVisible(false);

	}

	private void viewWarnings() {
		// TODO Auto-generated method stub

		ViewListWarnings.clear();
		ViewUsers.reloadGrid();
		ViewListWarnings.setVisible(true);
		ViewUsers.setVisible(false);
		ViewDocents.setVisible(false);
		ViewGrupos.setVisible(false);
		ViewStudents.setVisible(false);
		ViewWarning.setVisible(false);
		ViewCSV.setVisible(false);
		ViewTeachersWarnings.setVisible(false);
		Viewtutors.setVisible(false);
		ViewCharts.setVisible(false);
		ViewSettings.setVisible(false);
		ViewForms.setVisible(false);

	}

	private void viewUsers() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		ViewUsers.clear();
		ViewUsers.reloadGrid();
		ViewUsers.setVisible(true);
		ViewDocents.setVisible(false);
		ViewGrupos.setVisible(false);
		ViewStudents.setVisible(false);
		ViewWarning.setVisible(false);
		ViewListWarnings.setVisible(false);
		ViewCSV.setVisible(false);
		ViewTeachersWarnings.setVisible(false);
		Viewtutors.setVisible(false);
		ViewCharts.setVisible(false);
		ViewSettings.setVisible(false);
		ViewForms.setVisible(false);

	}

	private void viewWarning() {
		// TODO Auto-generated method stub

		// ViewWarning.clear();
		ViewWarning.setVisible(true);
		ViewGrupos.setVisible(false);
		ViewDocents.setVisible(false);
		ViewStudents.setVisible(false);
		ViewUsers.setVisible(false);
		ViewListWarnings.setVisible(false);
		ViewCSV.setVisible(false);
		ViewTeachersWarnings.setVisible(false);
		Viewtutors.setVisible(false);
		ViewCharts.setVisible(false);
		ViewSettings.setVisible(false);
		ViewForms.setVisible(false);

	}

	private void viewTeachers() {

		// TODO Auto-generated method stub
		ViewDocents.clear();
		ViewDocents.setVisible(true);
		ViewGrupos.setVisible(false);
		ViewStudents.setVisible(false);
		ViewWarning.setVisible(false);
		ViewUsers.setVisible(false);
		ViewListWarnings.setVisible(false);
		ViewCSV.setVisible(false);
		ViewTeachersWarnings.setVisible(false);
		Viewtutors.setVisible(false);
		ViewCharts.setVisible(false);
		ViewSettings.setVisible(false);
		ViewForms.setVisible(false);

	}

	private void viewGroup() {
		// TODO Auto-generated method stub

		ViewGrupos.clear();
		// mostrar una tabla con los alumnos
		ViewGrupos.setVisible(true);
		ViewDocents.setVisible(false);
		ViewStudents.setVisible(false);
		ViewWarning.setVisible(false);
		ViewUsers.setVisible(false);
		ViewListWarnings.setVisible(false);
		ViewCSV.setVisible(false);
		ViewTeachersWarnings.setVisible(false);
		Viewtutors.setVisible(false);
		ViewCharts.setVisible(false);
		ViewSettings.setVisible(false);
		ViewForms.setVisible(false);

	}

	private void viewStudents() {

		ViewStudents.clear();
		ViewStudents.reloadGrid();
		ViewStudents.setVisible(true);
		ViewGrupos.setVisible(false);
		ViewDocents.setVisible(false);
		ViewWarning.setVisible(false);
		ViewUsers.setVisible(false);
		ViewListWarnings.setVisible(false);
		ViewCSV.setVisible(false);
		ViewTeachersWarnings.setVisible(false);
		Viewtutors.setVisible(false);
		ViewCharts.setVisible(false);
		ViewSettings.setVisible(false);
		ViewForms.setVisible(false);

	}

	public static void viewCsv() {

		// ViewCSV.reloadGrid();
		ViewStudents.setVisible(false);
		ViewGrupos.setVisible(false);
		ViewDocents.setVisible(false);
		ViewWarning.setVisible(false);
		ViewUsers.setVisible(false);
		ViewListWarnings.setVisible(false);
		ViewCSV.setVisible(true);
		ViewTeachersWarnings.setVisible(false);
		Viewtutors.setVisible(false);
		ViewCharts.setVisible(false);
		ViewSettings.setVisible(false);
		ViewForms.setVisible(false);

	}

	private static void ViewReports() {

		ViewStudents.setVisible(false);
		ViewGrupos.setVisible(false);
		ViewDocents.setVisible(false);
		ViewWarning.setVisible(false);
		ViewUsers.setVisible(false);
		ViewListWarnings.setVisible(false);
		ViewCSV.setVisible(false);
		ViewTeachersWarnings.setVisible(false);
		Viewtutors.setVisible(false);
		ViewCharts.setVisible(false);
		ViewSettings.setVisible(false);
		ViewForms.setVisible(true);

	}

	public void logoutActions() {

		getUI().getCurrent().addWindow(DeleteSubWindows());

	}

	public Window DeleteSubWindows() {

		Window win = new Window(" Tancar sessió");

		win.setWidth("300");
		win.setHeight("150");
		win.setIcon(FontAwesome.CLOSE);
		win.setDraggable(false);
		win.setClosable(false);
		win.setResizable(false);
		win.setModal(true);
		win.center();

		Label question = new Label(" Estas segur?");
		Button yes = new Button("Sí");
		Button no = new Button("No");

		no.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -53955506058926623L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				win.close();

			}
		});

		yes.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -4879272457392790648L;

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

	private void setWellcome() {
		// TODO Auto-generated method stub
		ma = new UserJPAManager();

		int id2 = Integer.parseInt(getUI().getCurrent().getSession().getAttribute("id").toString());
		wellcome.addStyleName("wellcome");
		wellcome.setCaption("Benvingut " + ma.getNomTutorHeader(id2));


	}
}
