package ui.graphelements;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

public class PieChartElement implements ChartElement {

	private PieChart chart;

	private static int LEGEND_HEIGHT = 80;
	
	public PieChartElement(String chartTitle)
	{
		chart = new PieChart();
		chart.setTitle(chartTitle);
	}
	
	public void resizeChart(int width, int height)
	{
		chart.setPrefSize(width, height - LEGEND_HEIGHT);
	}
	
	/**
	 * Sets the data for the pie chart. 
	 * @param data Supplied as ObservableList of PieChart.Data elements, which ar
	 * a string and double pair.
	 */
	public void setData(ObservableList<PieChart.Data> data)
	{
		chart.setData(data);
	}
	
	public PieChart getChart()
	{
		return chart;
	}
	
}
