package ui.graphelements;

import java.awt.Dimension;

import javafx.scene.chart.Chart;

public interface ChartElement {

	/**
	 * Gets the JavaFX chart for this element
	 * @return JavaFX chart, either PieChart or some superclass of XYChart
	 */
	public Chart getChart();
	
	/**
	 * Resizes the chart to a new width and height. Accounts for axis/legend sizes.
	 * @param width Width of the space the chart should occupy
	 * @param height Height of the space the chart should occupy
	 */
	public void resizeChart(Dimension dimension);


	
}
