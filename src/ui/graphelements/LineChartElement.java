package ui.graphelements;

import java.util.List;

import core.fields.TimeGranularity;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class LineChartElement extends ChartElement {
		
	private NumberAxis xAxis, yAxis;
	private TimeGranularity timeGranularity;
	
	public LineChartElement()
	{
		xAxis = new NumberAxis(0,0,1);
		xAxis.setTickUnit(1);
		xAxis.setLabel("Time");
		
		yAxis = new NumberAxis();
		yAxis.setLabel("Some Metric");
		
		chart = new LineChart<Number, Number>(xAxis, yAxis);
	}
	
	/**
	 * Adds a new series to the chart
	 * @param seriesName Name of the series, will be displayed as label on chart
	 * @param data Set of y-axis values separated by the assigned time-granularity
	 */
	public void addSeries(String seriesName, List<Number> data)
	{
		//TODO Change xAxis to support time granularity
		Series series = new Series();
		series.setName(seriesName);
		for(int i=0; i< data.size(); i++)
			series.getData().add(new Data(i, data.get(i)));
		chart.getData().add(series);
		
		if(data.size() > xAxis.getUpperBound())
			xAxis.setUpperBound(data.size());
	}
	
	/**
	 * Sets the time granularity for which each datum is separated.
	 * @param timeGranularity Time granularity of chart
	 */
	public void setTimeGranularity(TimeGranularity timeGranularity)
	{
		this.timeGranularity = timeGranularity;
		switch(timeGranularity)
		{
		case HOURLY: xAxis.setLabel("Time (Hours)"); break;
		case DAILY: xAxis.setLabel("Time (Days)"); break;
		case WEEKLY: xAxis.setLabel("Time (Weeks)"); break;
		case MONTHLY: xAxis.setLabel("Time (Months)"); break;
		}
	}
	
	/**
	 * Sets the metric to update Y-Axis label
	 * @param metric Metric represented by chart
	 */
	public void setMetric(String metric)
	{
		yAxis.setLabel(metric);
	}
	
	/**
	 * Gets the X-Axis
	 * @return Instance of NumberAxis. The lower bound defaults at 0 and the upper bound
	 * is set to match the longest data series. Default tick size is 1. Label is updated
	 * when time granularity is set.
	 */
	public NumberAxis getXAxis()
	{
		return xAxis;
	}
	
	/**
	 * Gets the Y-Axis
	 * @return Instance of NumberAxis. Default values used to match data series.
	 * Label updated when metric is set.
	 */
	public NumberAxis getYAxis()
	{
		return yAxis;
	}
	
}
