
package com.example.view.TutorView;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.Logic.EntityManagerUtil;
import com.example.Logic.JDBCConnectionPool;
import com.example.Logic.TeachersJPAManager;
import com.example.Pdf.generatePDF;
import com.example.Templates.ConfirmWarningPDF;
import com.example.Templates.MainContentView;
import com.itextpdf.text.DocumentException;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

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
 * 	En esta clase se mostrarán los partes de los alumnos del Tutor
 * 
 *******************************************************************************/

public class TutorOwnWarningsJava extends MainContentView {

	private File sourceFile;
	private String nomCognoms;
	private String fecha;
	private String hora;
	private SQLContainer container;
	private SQLContainer containerGroups;
	private EntityManagerUtil entman = new EntityManagerUtil();

	private EntityManager em = entman.getEntityManager();

	private String usuari;
	private Grid grid;
	private Grid gridGroups;

	private Query query = null;

	private Window window = new Window();
	private TeachersJPAManager teacherJPA = new TeachersJPAManager();
	private JDBCConnectionPool jdbccp = new JDBCConnectionPool();
	private ConfirmWarningPDF pdf = new ConfirmWarningPDF();
	private Button b = new Button();

	private boolean gridUsed = false;
	private boolean gridUsedGroup = false;

	/**
	 * @throws MalformedURLException
	 * @throws DocumentException
	 * @throws IOException
	 */
	public TutorOwnWarningsJava() throws MalformedURLException, DocumentException, IOException {

		buttonsSettings();
		gridProperties();
		gridGroupProperties();
		WindowProperties();

		bRegister.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				try {

					if (gridUsed) {
						ownpopupPDF();

					} else if (gridUsedGroup) {
						popupPDF();

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		buttonEdit.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				vHorizontalMain.removeAllComponents();

				if (gridUsed) {
					vHorizontalMain.addComponent(gridGroupProperties());
					gridUsed = false;

				} else if (gridUsedGroup) {
					vHorizontalMain.addComponent(gridProperties());
					gridUsedGroup = false;

				}
			}

		});
		vHorizontalMain.addComponent(gridProperties());
	}

	/**
	 *	Método que contiene las propiedas de la grid de alumnos
	 * @return grid
	 * 			Contiene los datos de los alumnos
	 */
	public Grid gridProperties() {

		gridUsed = true;
		txtTitle.setVisible(true);
		txtTitle.addStyleName("main-title");
		txtTitle.setValue("Les meves amonestacions");
		buttonEdit.setCaption("Els meus Grups/Amonestats");

		usuari = teacherJPA.currentTeacherName();
		try {

			container = new SQLContainer(new FreeformQuery(
					"select al.nom, " + "al.cognoms," + " a.grup, " + "a.motius_selection," + " a.altres_motius,"
							+ "a.materia, a.data, " + "a.localitzacio " + "from amonestacio a, docent d, alumne al "
							+ "where a.docent=d.id and a.alumne=al.id and d.nom LIKE '" + usuari + "'",
					jdbccp.GetConnection()));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		grid = new Grid("", container);
		grid.setContainerDataSource(container);
		grid.setColumns("nom", "cognoms", "grup", "data");
		grid.setSizeFull();
		grid.setColumnReorderingAllowed(true);
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(new SelectionListener() {

			@Override
			public void select(SelectionEvent event) {
				// TODO Auto-generated method stub
				bAdd.setEnabled(true);
				// buttonEdit.setEnabled(true);
				// bDelete.setEnabled(true);

			}
		});

		return grid;
	}

	/**
	 *	Método que contiene las propiedades de la grid de grupos
	 * @return grid
	 * 			Con datos de los grupos
	 */
	public Grid gridGroupProperties() {
		gridUsedGroup = true;

		txtTitle.setVisible(true);
		txtTitle.addStyleName("main-title");
		txtTitle.setValue("Els meus Grups/Amonestats");
		buttonEdit.setCaption("Les meves amonestacions");

		int id = Integer.parseInt(getUI().getCurrent().getSession().getAttribute("id").toString());

		usuari = teacherJPA.currentTeacherName();
		em.getTransaction().begin();

		query = em.createNativeQuery(

				"SELECT grup FROM usuari u, tutor t WHERE t.docent= u.id_docent AND u.id_docent = " + id + " LIMIT 1");

		em.getTransaction().commit();

		try {
	
			String tutorGroup = query.getSingleResult().toString();
			containerGroups = new SQLContainer(new FreeformQuery(
					"select al.nom, " + "al.cognoms," + " a.grup, " + "a.motius_selection," + " a.altres_motius,"
							+ "a.materia, a.data, " + "a.localitzacio " + "from amonestacio a, docent d, alumne al "
							+ "where a.docent=d.id and a.alumne=al.id and d.nom LIKE '" + usuari + "' and a.grup LIKE '"
							+ tutorGroup + "'",
					jdbccp.GetConnection()));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gridGroups = new Grid("", containerGroups);
		gridGroups.setContainerDataSource(containerGroups);
		gridGroups.setColumns("nom", "cognoms", "grup", "data");
		gridGroups.setSizeFull();
		gridGroups.setColumnReorderingAllowed(true);
		gridGroups.setSelectionMode(SelectionMode.SINGLE);
		gridGroups.addSelectionListener(new SelectionListener() {

			@Override
			public void select(SelectionEvent event) {
				// TODO Auto-generated method stub
				bAdd.setEnabled(true);
				// buttonEdit.setEnabled(true);
				// bDelete.setEnabled(true);

			}
		});

		return gridGroups;
	}

	/**
	 * Método que limpiará los campos
	 */
	public void clear() {
		
		// TODO Auto-generated method stub
		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		grid.deselectAll();
	}

	/**
	 * Método que contiene las opciones de los botones
	 */
	private void buttonsSettings() {
		// TODO Auto-generated method stub

		bAdd.setVisible(false);
		buttonEdit.setVisible(true);
		buttonEdit.setCaption("Amonestacions dels meus Grups");
		bDelete.setVisible(false);
		bRegister.setVisible(true);
		bRegister.setCaption("Detalls");
		bRegister.setStyleName(ValoTheme.BUTTON_PRIMARY);
		txtSearch.setVisible(false);
		clearTxt.setVisible(false);

		// AdminViewCarregarCSVJava upload = new AdminViewCarregarCSVJava();

	}

	/**
	 * 	Método que contiene las opciones de los botones
	 */
	private void WindowProperties(){

		window.setHeight("95%");
		window.setWidth("95%");
		window.setDraggable(false);
		window.setModal(true);
		window.setVisible(false);
		window.setCaption("Visualització de l'amonestació");
		window.center();

	}

	/**
	 * Método que consigue el nombre y apellido del item seleccionado
	 * @return nomCognom
	 */
	private String getItemNomCognomSelected() {

		String name = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("nom").getValue()
				.toString();
		String surname = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("cognoms")
				.getValue().toString();

		String nomCognom = (name.concat(" " + surname)).replaceFirst(" ", "").replaceAll(" ", "_");

		return nomCognom;

	}

	/**
	 *	Método que consigue la hora del item seleccionado
	 * @return hora
	 */
	private String getDateSelected() {

		Object data = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("data").getValue();
		fecha = data.toString();
		hora = fecha.substring(11, 16);

		return hora;
	}

	/**
	 * 
	 *  Método que consigue el nombre y apellido del grupo seleccionado
	 * @return nomCognom
	 */
	private String getItemGroupsNomCognomSelected() {

		String name = gridGroups.getContainerDataSource().getItem(gridGroups.getSelectedRow()).getItemProperty("nom")
				.getValue().toString();
		String surname = gridGroups.getContainerDataSource().getItem(gridGroups.getSelectedRow())
				.getItemProperty("cognoms").getValue().toString();

		String nomCognom = (name.concat(" " + surname)).replaceFirst(" ", "").replaceAll(" ", "_");

		return nomCognom;

	}

	/**
	 * Método que consigue la hora del grupo seleccionado
	 * @return
	 */
	private String getGroupsDateSelected() {

		Object data = gridGroups.getContainerDataSource().getItem(gridGroups.getSelectedRow()).getItemProperty("data")
				.getValue();
		fecha = data.toString();
		hora = fecha.substring(11, 16);

		return hora;
	}

	
	/**
	 * 
	 * Método que genera un PDF una ventana con el PDF ya generado a partir de la ruta seleccionada
	 * @throws IOException
	 * @throws DocumentException
	 */
	@SuppressWarnings("deprecation")
	public void popupPDF() throws IOException, DocumentException {

		generatePDF generatepdf = new generatePDF();
		Embedded c = new Embedded();
		sourceFile = new File(generatepdf.getPath2(getItemGroupsNomCognomSelected(), getGroupsDateSelected()));
		c.setSource(new FileResource(sourceFile));
		c.setWidth("100%");
		c.setHeight("600px");
		c.setType(Embedded.TYPE_BROWSER);
		pdf.verticalpdf.removeAllComponents();
		pdf.verticalpdf.setSizeFull();
		pdf.verticalpdf.addComponent(c);
		pdf.hbuttons.setVisible(false);
		window.setContent(pdf);
		UI.getCurrent().addWindow(window);

		window.setVisible(true);

	}

	/**
	 * 
	 * Método que genera un PDF una ventana con el PDF ya generado a partir de la ruta seleccionada
	 * @throws IOException
	 * @throws DocumentException
	 */
	@SuppressWarnings("deprecation")
	public void ownpopupPDF() throws IOException, DocumentException {

		generatePDF generatepdf = new generatePDF();
		Embedded c = new Embedded();
		sourceFile = new File(generatepdf.getPath2(getItemNomCognomSelected(), getDateSelected()));
		c.setSource(new FileResource(sourceFile));
		c.setWidth("100%");
		c.setHeight("600px");
		c.setType(Embedded.TYPE_BROWSER);
		pdf.verticalpdf.removeAllComponents();
		pdf.verticalpdf.setSizeFull();
		pdf.verticalpdf.addComponent(c);
		pdf.hbuttons.setVisible(false);
		window.setContent(pdf);
		UI.getCurrent().addWindow(window);

		window.setVisible(true);

	}

}
