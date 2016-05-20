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
package com.example.view.TeacherView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

import com.example.Entities.Warning;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.JDBCConnectionPool;
import com.example.Logic.UserJPAManager;
import com.example.Pdf.generatePDF;
import com.example.Templates.ConfirmWarningPDF;
import com.example.Templates.MainContentView;
import com.itextpdf.text.DocumentException;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

import sun.text.normalizer.UBiDiProps;

public class TeacherOwnWarningsJava extends MainContentView {
	
	private File sourceFile;
	private String nomCognoms;
	private String fecha;
	private String hora;
	private SQLContainer container;
	private String usuari;
	private Grid grid;
	private Window window = new Window();
	private UserJPAManager MA = new UserJPAManager();
	private JDBCConnectionPool jdbccp = new JDBCConnectionPool();
	private ConfirmWarningPDF pdf = new ConfirmWarningPDF();
	private Button b = new Button();

	public TeacherOwnWarningsJava() throws MalformedURLException, DocumentException, IOException {

		buttonsSettings();
		b.setCaption("PRUEBA");
		WindowProperties();
		
		
		bRegister.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				try {
					popupPDF();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
		});

		vHorizontalMain.addComponent(gridProperties());

	}

	public Grid gridProperties() {

		usuari = MA.currentTeacherName();
		try {

			container = new SQLContainer(new FreeformQuery(
					"select al.nom, " + "al.cognoms," + " a.grup, " + "a.motius_selection," + " a.altres_motius,"
							+ "a.materia, a.data, " + "a.localitzacio " + "from amonestacio a, docent d, alumne al "
							+ "where a.docent=d.id and a.alumne=al.id and d.nom LIKE '" + usuari + "'",
					jdbccp.GetConnection()));
			grid = new Grid("", container);
			grid.setContainerDataSource(container);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
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

	public void clear() {
		// TODO Auto-generated method stub
		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		 grid.deselectAll();
	}

	private void buttonsSettings() {
		// TODO Auto-generated method stub

		txtTitle.setVisible(true);
		txtTitle.addStyleName("main-title");
		txtTitle.setValue("Les meves amonestacions");
		bAdd.setVisible(false);
		buttonEdit.setVisible(false);
		bDelete.setVisible(false);
		bRegister.setVisible(true);
		bRegister.setCaption("Detalls");
		bRegister.setStyleName(ValoTheme.BUTTON_PRIMARY);
		txtSearch.setVisible(false);
		clearTxt.setVisible(false);
	

		// AdminViewCarregarCSVJava upload = new AdminViewCarregarCSVJava();

	}

	private void WindowProperties() throws MalformedURLException, DocumentException, IOException {

		window.setHeight("95%");
		window.setWidth("95%");
		window.setDraggable(false);
		window.setModal(true);
		window.setVisible(false);
		window.setCaption("Visualització de l'amonestació");
		window.center();

	}
	
	private String getItemNomCognomSelected() {

		String name = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("nom").getValue().toString();
		String surname = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("cognoms")
				.getValue().toString();

		String nomCognom = (name.concat(" "+surname)).replaceFirst(" ", "").replaceAll(" ", "_");

		return nomCognom;

	}
	private String getDateSelected(){
		
		Object data = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("data").getValue();
		fecha = data.toString();
		hora = fecha.substring(11,16);
	
		return hora;
	}
	
	@SuppressWarnings("deprecation")
	public void popupPDF() throws IOException, DocumentException {
	
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

	/*
	 * package com.vaadin.demo.ejemplovaadin; import com.vaadin.Application;
	 * import com.vaadin.ui.*; import java.sql.*; import
	 * com.vaadin.addon.sqlcontainer.SQLContainer; import
	 * com.vaadin.addon.sqlcontainer.connection.SimpleJDBCConnectionPool; import
	 * com.vaadin.addon.sqlcontainer.query.FreeformQuery;
	 * 
	 * public class EjemploVaadinAplicacion extends Application {
	 * 
	 * @Override public void init() { Window mainWindow = new Window(
	 * "Consulta demo Vaadin"); SimpleJDBCConnectionPool connectionPool; Table
	 * table = null; try { connectionPool = new
	 * SimpleJDBCConnectionPool("com.microsoft.sqlserver.jdbc.SQLServerDriver",
	 * "jdbc:sqlserver://direccionDelServidor:1433;databaseName=nombreBaseDatos;",
	 * "usuario", "contraseña", 2, 5);
	 * 
	 * SQLContainer container = null; container = new SQLContainer(new
	 * FreeformQuery("SELECT * FROM Prueba" , connectionPool));
	 * 
	 * table = new Table(null, container); //Establecemos el tamaño de Grid
	 * table.setWidth("100%"); //Ocupa todo el ancho del navegador
	 * table.setHeight("170px"); //Altura del Grid.
	 * 
	 * //Opciones en la selección del Grid table.setSelectable(true); //Hacemos
	 * que se puedan seleccionar las filas del Grid.
	 * table.setMultiSelect(true);//Selección de múltiples filas del Grid.
	 * 
	 * table.setContainerDataSource(container);
	 * 
	 * //Establecemos el nombre de las cabeceras de las columnas
	 * table.setColumnHeaders(new String[] { "ID Contacto", "Nombre Contacto"
	 * }); } catch (SQLException e){ System.out.println("SQL Exception: "+
	 * e.toString()); } mainWindow.addComponent(table);
	 * setMainWindow(mainWindow); } }
	 */

}
