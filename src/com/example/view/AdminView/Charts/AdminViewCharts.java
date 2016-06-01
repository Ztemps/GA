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
package com.example.view.AdminView.Charts;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.postgresql.util.PSQLException;

import com.example.Entities.Group;
import com.example.Entities.Warning;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.JDBCConnectionPool;
import com.example.Reports.FinalReports;
import com.example.Templates.ChartsContainerView;
import com.example.Templates.MainContentView;
import com.itextpdf.text.TabStop.Alignment;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ContainerDataSeries;
import com.vaadin.addon.charts.model.HorizontalAlign;
import com.vaadin.addon.charts.model.LayoutDirection;
import com.vaadin.addon.charts.model.Legend;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.Stacking;
import com.vaadin.addon.charts.model.VerticalAlign;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Clase de la vista administrador, que muestra los gráficos.
 */

public class AdminViewCharts extends ChartsContainerView {

	
	private static final long serialVersionUID = 4404820936112694539L;
	private SQLContainer container;
	private SQLContainer container2;
	private SQLContainer container3;
	private JDBCConnectionPool jdbccp;
	private Panel panel;
	private VerticalLayout vertical;
	private FinalReports finalReports;

	public AdminViewCharts() throws ReadOnlyException, ConversionException, IOException, PSQLException {

		generalSettings();

		panel = PanelProp();
		vertical = VerticalProp();

		panel.setContent(vertical);
		vHorizontalMain.addComponent(panel);

	}

	/**
	 * Configuración de los componentes que incluye el VerticaLayout vertical
	 */
	private VerticalLayout VerticalProp() throws ReadOnlyException, ConversionException, IOException, PSQLException {

		vertical = new VerticalLayout();
		vertical.setSpacing(true);
		vertical.addComponent(WarningsPerGroup());
		vertical.addComponent(WarningsPerTeacher());
		vertical.addComponent(WarningsPerTrimestre1());
		vertical.addComponent(WarningsPerTrimestre2());
		vertical.addComponent(WarningsPerTrimestre3());
		vertical.addComponent(WarningsPerTrim());

		return vertical;

	}

	/**
	 * Configuración del panel principal que incluye los gráficos
	 */
	private Panel PanelProp() {
		panel = new Panel();
		panel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);
		panel.setWidth("100%");
		panel.setHeight("100%");

		return panel;

	}

	/**
	 * Configuración general de botones y etiquetas
	 */
	private void generalSettings() {

		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		txtTitle.setValue("Gràfiques");

		txtSearch.setVisible(false);

	}

	/**
	 * Configuración del gráfico que muestra en forma de columna las
	 * amonestaciones por grupo
	 */
	public Chart WarningsPerGroup() {

		Chart chart = new Chart(ChartType.COLUMN);
		chart.setWidth("50%");
		jdbccp = new JDBCConnectionPool();
		// Query for get amonestats per group
		try {
			container = new SQLContainer(new FreeformQuery(
					"select count(amonestat) AS amonestacions,grup from amonestacio where amonestat = TRUE group by grup",
					jdbccp.GetConnection()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Query for get amonestats per group
		try {
			container2 = new SQLContainer(new FreeformQuery("select count(expulsat) AS expulsats,grup "
					+ "from amonestacio where expulsat = TRUE group by grup", jdbccp.GetConnection()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Configuration configuration = chart.getConfiguration();
		configuration.setTitle("Grups");
		configuration.setSubTitle("Amonestacions/Expulsions totals per grup");

		PlotOptionsColumn plotOptions = new PlotOptionsColumn();
		plotOptions.setPointPadding(0.2);
		plotOptions.setBorderWidth(0);
		configuration.setPlotOptions(plotOptions);

		ContainerDataSeries series = new ContainerDataSeries(container);
		series.setName("Amonestats");
		ContainerDataSeries series1 = new ContainerDataSeries(container2);

		series.setNamePropertyId("grup");
		series.setYPropertyId("amonestacions");

		series1.setNamePropertyId("grup");
		series1.setYPropertyId("expulsats");
		series1.setName("Expulsats");

		configuration.addSeries(series);
		configuration.addSeries(series1);

		XAxis xaxis = new XAxis();
		xaxis.setTitle("Grupos");
		String names[] = null;

		try {

			names = new String[container.size()];
			xaxis.setCategories(names);

		} catch (NullPointerException e) {

		}

		configuration.addxAxis(xaxis);

		YAxis yaxis = new YAxis();
		yaxis.setTitle("Amonestacions");
		configuration.addyAxis(yaxis);

		Legend legend = new Legend();
		legend.setLayout(LayoutDirection.VERTICAL);
		legend.setBackgroundColor(new SolidColor("#FFFFFF"));
		legend.setAlign(HorizontalAlign.LEFT);
		legend.setVerticalAlign(VerticalAlign.TOP);
		legend.setX(100);
		legend.setY(70);
		legend.setFloating(true);
		legend.setShadow(true);
		configuration.setLegend(legend);

		chart.drawChart(configuration);

		return chart;
	}

	/**
	 * Configuración del gráfico que muestra en forma de columna las
	 * amonestaciones por profesor
	 */
	public Chart WarningsPerTeacher() {

		Chart chart = new Chart(ChartType.COLUMN);
		chart.setWidth("50%");
		jdbccp = new JDBCConnectionPool();
		// Query for get amonestats per group
		try {
			container = new SQLContainer(
					new FreeformQuery(
							"select count(amonestat) AS amonestacions,nom as profe " + "from amonestacio a, docent d "
									+ "where a.docent=d.id and amonestat = TRUE group by profe;",
							jdbccp.GetConnection()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Configuration configuration = chart.getConfiguration();
		configuration.setTitle("Professors");
		configuration.setSubTitle("Amonestacions per professor");

		PlotOptionsColumn plotOptions = new PlotOptionsColumn();
		plotOptions.setPointPadding(0.2);
		plotOptions.setBorderWidth(0);
		configuration.setPlotOptions(plotOptions);

		ContainerDataSeries series = new ContainerDataSeries(container);
		series.setName("Amonestats");

		series.setNamePropertyId("profe");
		series.setYPropertyId("amonestacions");
		configuration.addSeries(series);

		XAxis xaxis = new XAxis();
		xaxis.setTitle("Professors");
		String names[] = null;

		try {
			names = new String[container.size()];
			xaxis.setCategories(names);
			configuration.addxAxis(xaxis);
		} catch (NullPointerException e) {

		}


		

		YAxis yaxis = new YAxis();
		yaxis.setTitle("Amonestacions");
		configuration.addyAxis(yaxis);

		Legend legend = new Legend();
		legend.setLayout(LayoutDirection.VERTICAL);
		legend.setBackgroundColor(new SolidColor("#FFFFFF"));
		legend.setAlign(HorizontalAlign.LEFT);
		legend.setVerticalAlign(VerticalAlign.TOP);
		legend.setX(100);
		legend.setY(70);
		legend.setFloating(true);
		legend.setShadow(true);
		configuration.setLegend(legend);

		chart.drawChart(configuration);

		return chart;
	}

	/**
	 * Obtiene las fechas de ubn fichero y las añade al ArrayList<Date>
	 * 
	 * @return fechas de inicio de los trimestres
	 * @see FinalReports, método readFile()
	 */
	public ArrayList<Date> Dates() throws ReadOnlyException, ConversionException, IOException {

		finalReports = new FinalReports();
		ArrayList<Date> dates = new ArrayList<>();
		dates = finalReports.readFile();

		return dates;

	}

	/**
	 * Configuración del gráfico que muestra en forma de columna las
	 * amonestaciones por trimestre de cada grupo
	 */
	public Chart WarningsPerTrim() throws ReadOnlyException, ConversionException, IOException, PSQLException {

		ArrayList<Date> dates = Dates();

		Chart chart = new Chart(ChartType.COLUMN);
		chart.setWidth("50%");
		jdbccp = new JDBCConnectionPool();
		// Query for get amonestats per group
		try {
			container = new SQLContainer(
					new FreeformQuery(
							"select count(amonestat) AS amonestacions, grup from amonestacio where amonestat = TRUE and data BETWEEN '"
									+ dates.get(0) + "' AND '" + dates.get(1) + "' group by grup;",
							jdbccp.GetConnection()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Query for get amonestats per group
		try {
			container2 = new SQLContainer(
					new FreeformQuery(
							"select count(amonestat) AS amonestacions, grup from amonestacio where amonestat = TRUE and data BETWEEN '"
									+ dates.get(2) + "' AND '" + dates.get(3) + "' group by grup;",
							jdbccp.GetConnection()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			container3 = new SQLContainer(
					new FreeformQuery(
							"select count(amonestat) AS amonestacions, grup from amonestacio where amonestat = TRUE and data BETWEEN '"
									+ dates.get(4) + "' AND '" + dates.get(5) + "' group by grup;",
							jdbccp.GetConnection()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Configuration configuration = chart.getConfiguration();
		configuration.setTitle("Grups");
		configuration.setSubTitle("Amonestacions trimestrals");

		PlotOptionsColumn plotOptions = new PlotOptionsColumn();
		plotOptions.setPointPadding(0.2);
		plotOptions.setBorderWidth(0);
		configuration.setPlotOptions(plotOptions);

		ContainerDataSeries series = new ContainerDataSeries(container);
		ContainerDataSeries series1 = new ContainerDataSeries(container2);
		ContainerDataSeries series2 = new ContainerDataSeries(container3);

		series.setNamePropertyId("grup");
		series.setYPropertyId("amonestacions");
		series.setName("Trimestre 1");

		series1.setNamePropertyId("grup");
		series1.setYPropertyId("amonestacions");
		series1.setName("Trimestre 2");

		series2.setNamePropertyId("grup");
		series2.setYPropertyId("amonestacions");
		series2.setName("Trimestre 3");

		configuration.addSeries(series);
		configuration.addSeries(series1);
		configuration.addSeries(series2);

		XAxis xaxis = new XAxis();
		xaxis.setTitle("Grupos");
		String names[] = null;

		try {

			names = new String[container.size()];
			xaxis.setCategories(names);

		} catch (NullPointerException e) {

		}

		configuration.addxAxis(xaxis);

		YAxis yaxis = new YAxis();
		yaxis.setTitle("Amonestacions");
		configuration.addyAxis(yaxis);

		Legend legend = new Legend();
		legend.setLayout(LayoutDirection.VERTICAL);
		legend.setBackgroundColor(new SolidColor("#FFFFFF"));
		legend.setAlign(HorizontalAlign.LEFT);
		legend.setVerticalAlign(VerticalAlign.TOP);
		legend.setX(100);
		legend.setY(70);
		legend.setFloating(true);
		legend.setShadow(true);
		configuration.setLegend(legend);

		chart.drawChart(configuration);

		return chart;
	}
	
	
	/**
	 * Configuración del gráfico que muestra en forma de columna las
	 * amonestaciones para el primer trimestre
	 */
	public Chart WarningsPerTrimestre1() throws ReadOnlyException, ConversionException, IOException {

		ArrayList<Date> dates = Dates();

		System.out.println("FECHAAAAAA " + dates.get(0));

		// dates.get(0);

		Chart chart = new Chart(ChartType.COLUMN);
		chart.setWidth("50%");
		jdbccp = new JDBCConnectionPool();
		// Query for get amonestats per group
		try {
			container = new SQLContainer(
					new FreeformQuery(
							"select count(amonestat) AS amonestacions, grup from amonestacio where amonestat = TRUE and data BETWEEN '"
									+ dates.get(0) + "' AND '" + dates.get(1) + "' group by grup;",
							jdbccp.GetConnection()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Configuration configuration = chart.getConfiguration();
		configuration.setTitle("Amonestacions per grup");
		configuration.setSubTitle("Primer trimestre");

		PlotOptionsColumn plotOptions = new PlotOptionsColumn();
		plotOptions.setPointPadding(0.2);
		plotOptions.setBorderWidth(0);
		configuration.setPlotOptions(plotOptions);

		ContainerDataSeries series = new ContainerDataSeries(container);
		series.setName("Amonestats");

		series.setNamePropertyId("grup");
		series.setYPropertyId("amonestacions");
		configuration.addSeries(series);

		XAxis xaxis = new XAxis();
		xaxis.setTitle("Grups/Trimestre");
		String names[] = null;

		try {
			names = new String[container.size()];
			xaxis.setCategories(names);
			configuration.addxAxis(xaxis);
		} catch (NullPointerException e) {

		}

		

		YAxis yaxis = new YAxis();
		yaxis.setTitle("Amonestacions");
		configuration.addyAxis(yaxis);

		Legend legend = new Legend();
		legend.setLayout(LayoutDirection.VERTICAL);
		legend.setBackgroundColor(new SolidColor("#FFFFFF"));
		legend.setAlign(HorizontalAlign.LEFT);
		legend.setVerticalAlign(VerticalAlign.TOP);
		legend.setX(100);
		legend.setY(70);
		legend.setFloating(true);
		legend.setShadow(true);
		configuration.setLegend(legend);

		chart.drawChart(configuration);

		return chart;
	}

	
	/**
	 * Configuración del gráfico que muestra en forma de columna las
	 * amonestaciones para el segundo trimestre
	 */
	public Chart WarningsPerTrimestre2() throws ReadOnlyException, ConversionException, IOException {

		ArrayList<Date> dates = Dates();

		// dates.get(0);

		Chart chart = new Chart(ChartType.COLUMN);
		chart.setWidth("50%");
		jdbccp = new JDBCConnectionPool();
		// Query for get amonestats per group
		try {
			container = new SQLContainer(
					new FreeformQuery(
							"select count(amonestat) AS amonestacions, grup from amonestacio where amonestat = TRUE and data BETWEEN '"
									+ dates.get(2) + "' AND '" + dates.get(3) + "' group by grup;",
							jdbccp.GetConnection()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Configuration configuration = chart.getConfiguration();
		configuration.setTitle("Amonestacions per grup");
		configuration.setSubTitle("Segon trimestre");

		PlotOptionsColumn plotOptions = new PlotOptionsColumn();
		plotOptions.setPointPadding(0.2);
		plotOptions.setBorderWidth(0);
		configuration.setPlotOptions(plotOptions);

		ContainerDataSeries series = new ContainerDataSeries(container);
		series.setName("Amonestats");

		series.setNamePropertyId("grup");
		series.setYPropertyId("amonestacions");
		configuration.addSeries(series);

		XAxis xaxis = new XAxis();
		xaxis.setTitle("Grups/Trimestre");
		String names[] = null;

		try {
			names = new String[container.size()];
		} catch (NullPointerException e) {

		}

		xaxis.setCategories(names);
		configuration.addxAxis(xaxis);

		YAxis yaxis = new YAxis();
		yaxis.setTitle("Amonestacions");
		configuration.addyAxis(yaxis);

		Legend legend = new Legend();
		legend.setLayout(LayoutDirection.VERTICAL);
		legend.setBackgroundColor(new SolidColor("#FFFFFF"));
		legend.setAlign(HorizontalAlign.LEFT);
		legend.setVerticalAlign(VerticalAlign.TOP);
		legend.setX(100);
		legend.setY(70);
		legend.setFloating(true);
		legend.setShadow(true);
		configuration.setLegend(legend);

		chart.drawChart(configuration);

		return chart;
	}

	
	/**
	 * Configuración del gráfico que muestra en forma de columna las
	 * amonestaciones para el tercer trimestre
	 */
	public Chart WarningsPerTrimestre3() throws ReadOnlyException, ConversionException, IOException {

		ArrayList<Date> dates = Dates();

		// dates.get(0);

		Chart chart = new Chart(ChartType.COLUMN);
		chart.setWidth("50%");
		jdbccp = new JDBCConnectionPool();
		// Query for get amonestats per group
		try {
			container = new SQLContainer(
					new FreeformQuery(
							"select count(amonestat) AS amonestacions, grup from amonestacio where amonestat = TRUE and data BETWEEN '"
									+ dates.get(4) + "' AND '" + dates.get(5) + "' group by grup;",
							jdbccp.GetConnection()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Configuration configuration = chart.getConfiguration();
		configuration.setTitle("Amonestacions per grup");
		configuration.setSubTitle("Tercer trimestre");

		PlotOptionsColumn plotOptions = new PlotOptionsColumn();
		plotOptions.setPointPadding(0.2);
		plotOptions.setBorderWidth(0);
		configuration.setPlotOptions(plotOptions);

		ContainerDataSeries series = new ContainerDataSeries(container);
		series.setName("Amonestats");

		series.setNamePropertyId("grup");
		series.setYPropertyId("amonestacions");
		configuration.addSeries(series);

		XAxis xaxis = new XAxis();
		xaxis.setTitle("Grups/Trimestre");
		String names[] = null;

		try {
			names = new String[container.size()];
		} catch (NullPointerException e) {

		}

		xaxis.setCategories(names);
		configuration.addxAxis(xaxis);

		YAxis yaxis = new YAxis();
		yaxis.setTitle("Amonestacions");
		configuration.addyAxis(yaxis);

		Legend legend = new Legend();
		legend.setLayout(LayoutDirection.VERTICAL);
		legend.setBackgroundColor(new SolidColor("#FFFFFF"));
		legend.setAlign(HorizontalAlign.LEFT);
		legend.setVerticalAlign(VerticalAlign.TOP);
		legend.setX(100);
		legend.setY(70);
		legend.setFloating(true);
		legend.setShadow(true);
		configuration.setLegend(legend);

		chart.drawChart(configuration);

		return chart;
	}
	

	
	/**
	 * Actualización de la gráfica
	 * */
	public void reloadChart() {

		vHorizontalMain.removeAllComponents();
		vHorizontalMain.addComponent(panel);

	}

	

}
