package ui.graphelements;

import javafx.scene.chart.Chart;

public interface ChartElement {

	public Chart getChart();
	
	/**
	 * Resizes the chart to a new width and height. Accounts for axis/legend sizes.
	 * @param width Width of the space the chart should occupy
	 * @param height Height of the space the chart should occupy
	 */
	public void resizeChart(int width, int height);
	
}
