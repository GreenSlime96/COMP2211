package ui.graphelements;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import core.campaigns.Campaign;
import extfx.scene.chart.DateAxis;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
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
	
	/**
	 * Adds a new series to the line chart. Series name is automatically computed for given arguments.
	 * @param data List of metric values separated by the defined time granularity
	 * @param startDate LocalDateTime of first value in data list.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addSeries(List<Number> data, LocalDateTime startDate)
	{
		Series series = new Series();
		series.setName(startDate.format(Campaign.formatter) + " - " + startDate.plusSeconds(timeGranularity * data.size()).format(Campaign.formatter));
		
		LocalDateTime offset = null;
		for(int i=0; i< data.size(); i++)
		{
			offset = startDate.plusSeconds(timeGranularity * i);
			Data d = new Data(Date.from(offset.atZone(ZoneId.systemDefault()).toInstant()), data.get(i));
			series.getData().add(d);			
		}
		chart.getData().add(series);
		addTooltips();
	}
	
	public void resizeChart(int width, int height)
	{
		chart.setPrefSize(width, height-yAxis.getHeight());
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
	
	/**
	 * Gets the X-Axis
	 * @return Instance of DateAxis. 
	 */
	public DateAxis getXAxis()
	{
		return xAxis;
	}
	
	/**
	 * Gets the Y-Axis
	 * @return Instance of NumberAxis. Default values used to match data series.
	 */
	public NumberAxis getYAxis()
	{
		return yAxis;
	}
	
	public AreaChart<Date, Number> getChart()
	{
		return chart;
	}
	
}
