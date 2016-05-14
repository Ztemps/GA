package com.example.view.AdminView.Charts;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.Entities.Group;
import com.example.Entities.Warning;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.JDBCConnectionPool;
import com.example.Templates.MainContentView;
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
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

public class AdminViewCharts extends MainContentView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4404820936112694539L;
	private SQLContainer container;
	private SQLContainer container2;
	private JDBCConnectionPool jdbccp ;
	private GridLayout gl;

	public AdminViewCharts() {
		buttonsSettings();
		vHorizontalMain.addComponent(ConfigureGridLayout());
	}

	public GridLayout ConfigureGridLayout() {

		gl = new GridLayout(2, 6);
		gl.addComponent(WarningsPerGroup());

		return gl;
	}

	private void buttonsSettings() {

		horizontalTitle.addStyleName("horizontal-title");
		txtTitle.addStyleName("main-title");
		txtSearch.setVisible(false);
		bAdd.setCaption("Generar informe");
		txtTitle.setValue("Gràfiques");
		buttonEdit.setVisible(false);
		bDelete.setVisible(false);
		bDelete.addStyleName(ValoTheme.BUTTON_DANGER);
		bAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bRegister.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonEdit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);
		bRegister.setVisible(false);
		bAdd.setEnabled(true);
		clearTxt.setVisible(false);

	}

	public Table taula() {

		jdbccp = new JDBCConnectionPool();
		try {
			container = new SQLContainer(new FreeformQuery(
					"select count(amonestat) AS amonestacions ,grup from amonestacio where amonestat = FALSE group by grup",
					jdbccp.GetConnection()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Table table = new Table("Grups", container);
		table.setPageLength(container.size());

		return table;

	}

	public Chart WarningsPerGroup() {

		Chart chart = new Chart(ChartType.COLUMN);
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
			container2 = new SQLContainer(new FreeformQuery(
					"select count(expulsat) AS expulsats,grup from amonestacio where expulsat = TRUE group by grup",
					jdbccp.GetConnection()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Configuration configuration = chart.getConfiguration();
		configuration.setTitle("Grups");
		configuration.setSubTitle("Amonestacions/Expulsions per grup");

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
		
		try{
			
			names = new String[container.size()];
		}catch(NullPointerException e){
			
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

	public void reloadChart() {

		vHorizontalMain.removeAllComponents();
		vHorizontalMain.addComponent(WarningsPerGroup());

	}

	public void clear() {
		// TODO Auto-generated method stub
		bDelete.setEnabled(false);
		buttonEdit.setEnabled(false);

	}

}
