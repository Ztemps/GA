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
package com.example.view.AdminView.Warnings;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.postgresql.util.PSQLException;

import com.example.Entities.Warning;
import com.example.Logic.JDBCConnectionPool;
import com.example.Pdf.generatePDF;
import com.example.Templates.ConfirmWarningPDF;
import com.example.Templates.MainContentView;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.log.SysoCounter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.themes.ValoTheme;

public class AdminViewWarnings extends MainContentView {

	private static final long serialVersionUID = -7507262925682017298L;
	private File sourceFile;
	private Grid grid;
	private String fecha;
	private Window window = new Window();
	private ConfirmWarningPDF pdf = new ConfirmWarningPDF();
	private JDBCConnectionPool jdbccp;
	private SQLContainer AllWarnings;
	private HeaderRow filterRow;
	private TextField filterField;
	private HeaderCell cell;

	public AdminViewWarnings() throws SQLException {

		buttonsSettings();
		filterTextProperties();
		WindowProperties();
		buttonsAction();

		bAdd.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				try {
					popupPDF();
				} catch (IOException | DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		vHorizontalMain.addComponent(gridProperties());

	}

	/**
	 * Muestra la ventana del .pdf del detalles de las amonestaciones que ya han
	 * sido introducidas
	 * 
	 */
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

	/**
	 * Selecciona el nombre y apellido de la row seleccionada en el componente Grid
	 * 
	 * @return Nombre y apellido del alumno (concatenados)
	 * */
	private String getItemNomCognomSelected() {

		String name = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("nom").getValue()
				.toString();
		String surname = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("cognoms")
				.getValue().toString();

		String nomCognom = (name.concat(" " + surname)).replaceFirst(" ", "").replaceAll(" ", "_");

		return nomCognom;

	}

	/**
	 * Selecciona la fecha de la row seleccionada en el componente Grid
	 * 
	 * @return fecha de la amonestación 
	 * */
	private String getDateSelected() {

		Object data = grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("data").getValue();
		fecha = data.toString();

		return fecha.substring(11, 16);
	}

	
	/**
	 * Propiedades principales del componente Grid: Container, columnas...
	 * En doble click abre la ventana del .pdf
	 * 
	 * @returns Grid configurado
	 * */
	private Grid gridProperties() throws PSQLException {

		jdbccp = new JDBCConnectionPool();

		try {
			AllWarnings = new SQLContainer(new FreeformQuery(
					"select al.nom, " + "al.cognoms," + " a.grup, " + "a.materia, a.data, " + "a.localitzacio "
							+ "from amonestacio a, docent d, alumne al " + "where a.docent=d.id and a.alumne=al.id ",
					jdbccp.GetConnection()));

		} catch (SQLException e) {

		}

		grid = new Grid("", AllWarnings);
		grid.setSizeFull();
		grid.setColumns("nom", "cognoms", "grup", "materia", "data", "localitzacio");
		if (AllWarnings != null) {
			grid.setContainerDataSource(AllWarnings);

		}
		grid.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub

				if (event.isDoubleClick()) {

					try {
						popupPDF();
					} catch (IOException | DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		grid.setColumnOrder("data");
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

	private void buttonsAction() {
		// TODO Auto-generated method stub

	}

	private void WindowProperties() {

		window.setHeight("95%");
		window.setWidth("95%");
		window.setDraggable(false);
		window.setModal(true);
		window.setVisible(false);
		window.setCaption("Visualització de l'amonestació");
		window.center();
	}

	
	/**
	 * Configuración de componente TextField
	 * 
	 * @return textfield con filtro por apellido incluido
	 * */
	private TextField filterTextProperties() {

		txtSearch.setInputPrompt("Filtra per cognom");
		txtSearch.addTextChangeListener(new TextChangeListener() {

			SimpleStringFilter filter = null;

			@Override
			public void textChange(TextChangeEvent event) {

				Filterable f = (Filterable) grid.getContainerDataSource();

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
	 * Configuración principal de botones y estilos
	 * */
	private void buttonsSettings() {

		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		txtTitle.setValue("Visualitzar Amonestacions");
		bDelete.addStyleName(ValoTheme.BUTTON_DANGER);
		bAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bRegister.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonEdit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bDelete.setVisible(false);
		buttonEdit.setVisible(false);
		bRegister.setVisible(false);
		bAdd.setEnabled(false);
		bAdd.setCaption("Veure Detalls");

	}

	
	/**
	 * Deselección de cualquier row seleccionada del componente Grid
	 * */
	public void clear() {

		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		grid.deselectAll();

	}

}
