package ui.graphelements;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import core.fields.TimeGranularity;
import extfx.scene.chart.DateAxis;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tooltip;

public class LineChartElement implements ChartElement {
		
	private LineChart<Date, Number> chart;
	
	private DateAxis xAxis;
	private NumberAxis yAxis;
	private TimeGranularity timeGranularity;
	
	public LineChartElement()
	{		
		xAxis = new DateAxis();
		xAxis.setLabel("Time");
		
		yAxis = new NumberAxis();
		yAxis.setLabel("Some Metric");
		
		chart = new LineChart<Date, Number>(xAxis, yAxis);
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
		
		for(int i=0; i< data.size(); i++)
		{
			switch(timeGranularity)
			{
			case HOURLY: offset = startDate.plusHours(i); break;
			case DAILY: offset = startDate.plusDays(i); break;
			case WEEKLY: offset = startDate.plusWeeks(i); break;
			case MONTHLY: offset = startDate.plusMonths(i); break;
			}
			
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
