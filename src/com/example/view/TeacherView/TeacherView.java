package com.example.view.TeacherView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Entities.User;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.UserJPAManager;
import com.example.LoginView.LoginView;
import com.example.Templates.MainView;
import com.example.view.AdminView.AdminView;
import com.itextpdf.text.DocumentException;
//import com.example.templates.ProfessorFormJava;
import com.vaadin.ui.Grid;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class TeacherView extends MainView implements View {

	public static final String NAME = "professor";

	
	TeacherViewWarningJava vistaAmonestacion ;
	TeacherOwnWarningsJava vistaOwn;
	UserJPAManager ma;
	public TeacherView() throws MalformedURLException, DocumentException, IOException, SQLException {

		// Side menu button options
		vistaAmonestacion = new TeacherViewWarningJava();
		vistaOwn = new TeacherOwnWarningsJava();
		Items();
		
		content.addComponent(vistaAmonestacion);
		content.addComponent(vistaOwn);
		
		
		

		warning.addClickListener(e -> Amonestacions() );
		mevesAmonestacions.addClickListener(e -> MevesAmonestacions() );


	}

	private void Items() throws IOException {
		
		setWellcome();
		setLogo();
		students.setVisible(false);
		teachers.setVisible(false);
		groups.setVisible(false);
		tutors.setVisible(false);
		usuaris.setVisible(false);
		amonestacions.setVisible(false);
		charts.setVisible(false);
		informes.setVisible(false);
		cargarCSV.setVisible(false);
		ficurs.setVisible(false);
		content.setMargin(true);
		content.setSpacing(true);
		sep5.setVisible(false);
		sep4.setVisible(false);
		sep3.setVisible(false);
		groupsTutor.setVisible(false);
		vistaOwn.setVisible(false);
		warning.focus();
		
		logout.addClickListener(e -> logoutActions());

	}
	
	
	public void MevesAmonestacions(){
		vistaAmonestacion.setVisible(false);
		vistaOwn.setVisible(true);
		
	}
	
	public void Amonestacions(){
		vistaOwn.setVisible(false);
		vistaAmonestacion.setVisible(true);
		
		
	}
	
	

	private void setWellcome() {
		// TODO Auto-generated method stub
		ma = new UserJPAManager();
		int id = Integer.parseInt(getUI().getCurrent().getSession().getAttribute("id").toString());
		// TODO Auto-generated method stub
		wellcome.setCaption("Benvingut "+ma.getNomTutorHeader(id));
	}

	public void logoutActions() {

		getUI().getCurrent().addWindow(DeleteSubWindows());
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
//		if((getSession().getAttribute("user")) == null){
//
//			getUI().getNavigator().navigateTo(LoginView.NAME);//
//
//		} else {
//
//			getUI().getNavigator().navigateTo(ProfessorView.NAME);//
//
//		}

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
				getUI().getNavigator().navigateTo(LoginView.NAME);

			}
		});

		HorizontalLayout buttons = new HorizontalLayout(yes, no);
		buttons.setSpacing(true);

		VerticalLayout content = new VerticalLayout(question, buttons);
		win.setContent(content);

		return win;

	}

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
}
