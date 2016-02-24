package ui.graphelements;

import java.awt.Dimension;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import extfx.scene.chart.DateAxis;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tooltip;

public class LineChartElement implements ChartElement {
	private int chartNumber;
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, u, HH:mm");
	
	private static int TITLE_HEIGHT = 30;
	
	private AreaChart<Date, Number> chart;
	
	private DateAxis xAxis;
	private NumberAxis yAxis;
	private int timeGranularity;
	
	public LineChartElement(String chartTitle)
	{		
		xAxis = new DateAxis();
		xAxis.setLabel("Time");
		
		yAxis = new NumberAxis();
		yAxis.setLabel("Some Metric");
		
		chart = new AreaChart<Date, Number>(xAxis, yAxis);
		chart.setTitle(chartTitle);
	}
	
	private void addTooltips()
	{
		for(Series<Date, Number> s : chart.getData())
		{
			for(Data<Date, Number> d : s.getData())
			{
				//Tooltips
				Tooltip.install(d.getNode(), new Tooltip(
						LocalDateTime.ofInstant(d.getXValue().toInstant(), ZoneId.systemDefault()).format(formatter) + "\n" + 
		        yAxis.getLabel() + ": " + d.getYValue()));
				
				
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
	public void addSeries(List<? extends Number> data, LocalDateTime startDate)
	{
		System.out.println("Add series");
		Series series = new Series();
		series.setName(startDate.format(formatter) + " to " + startDate.plusSeconds(timeGranularity * data.size()).format(formatter));
		
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

	public void clearSeries()
	{
		chart.getData().removeAll(chart.getData().get(0));
	}
	
	public void resizeChart(Dimension dimension)
	{
		chart.setPrefSize(dimension.getWidth(), dimension.getHeight()-yAxis.getHeight()-TITLE_HEIGHT);
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
