package ui.graphelements;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import extfx.scene.chart.DateAxis;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tooltip;

public class LineChartElement implements ChartElement {
		
	private AreaChart<Date, Number> chart;
	
	private DateAxis xAxis;
	private NumberAxis yAxis;
	private int timeGranularity;
	
	public LineChartElement()
	{		
		xAxis = new DateAxis();
		xAxis.setLabel("Time");
		
		yAxis = new NumberAxis();
		yAxis.setLabel("Some Metric");
		
		chart = new AreaChart<Date, Number>(xAxis, yAxis);
		chart.setPrefSize(1920, 1080);
	}
	
	private void addTooltips()
	{
		for(Series<Date, Number> s : chart.getData())
		{
			for(Data<Date, Number> d : s.getData())
			{
				//Tooltips
				Tooltip.install(d.getNode(), new Tooltip(
		                d.getXValue().toString() + "\n" + 
		        yAxis.getLabel() + d.getYValue()));
				
				
				//Adding class on hover
		        d.getNode().setOnMouseEntered(new EventHandler<Event>() {
		
		            @Override
		            public void handle(Event event) {
		                d.getNode().getStyleClass().add("onHover");                     
		            }
		        });
		
		        //Removing class on exit
		        d.getNode().setOnMouseExited(new EventHandler<Event>() {
		
		            @Override
		            public void handle(Event event) {
		                d.getNode().getStyleClass().remove("onHover");      
		            }
		        });
		        
		        
			}
		}
	}
	
	//TODO Update this description
	/**
	 * Adds a new series to the chart
	 * @param seriesName Name of the series, will be displayed as label on chart
	 * @param data Set of y-axis values separated by the assigned time-granularity
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addSeries(String seriesName, List<Number> data, LocalDateTime startDate, LocalDateTime endDate)
	{
		Series series = new Series();
		series.setName(seriesName);
		
		LocalDateTime offset = null;
		System.out.println(timeGranularity);
		for(int i=0; i< data.size(); i++)
		{
			offset = startDate.plusSeconds(timeGranularity * i);
			Data d = new Data(Date.from(offset.atZone(ZoneId.systemDefault()).toInstant()), data.get(i));
			series.getData().add(d);			
		}
		chart.getData().add(series);
		addTooltips();
	}
		
	/**
	 * Sets the time granularity for which each datum is separated.
	 * @param timeGranularity Time granularity of chart
	 */
	public void setTimeGranularity(int timeGranularity) 
	{
		this.timeGranularity = timeGranularity;
	}
	
	/**
	 * Sets the metric to update Y-Axis label
	 * @param metric Metric represented by chart
	 */
	public void setMetric(String metric)
	{
		yAxis.setLabel(metric);
	}
	
	//TODO Update this description
	/**
	 * Gets the X-Axis
	 * @return Instance of DateAxis. The lower bound defaults at 0 and the upper bound
	 * is set to match the longest data series. Default tick size is 1. Label is updated
	 * when time granularity is set.
	 */
	public DateAxis getXAxis()
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
	
	public XYChart getChart()
	{
		return chart;
	}
	
}
