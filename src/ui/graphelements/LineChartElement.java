package ui.graphelements;

import java.util.List;

import core.fields.TimeGranularity;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.GridPane;

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
	
	public void addSeries(String seriesName, List<Number> data)
	{
		Series series = new Series();
		series.setName(seriesName);
		for(int i=0; i< data.size(); i++)
			series.getData().add(new Data(i, data.get(i)));
		chart.getData().add(series);
		
		if(data.size() > xAxis.getUpperBound())
			xAxis.setUpperBound(data.size());
	}
	
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
	
	public NumberAxis getXAxis()
	{
		return xAxis;
	}
	
	public NumberAxis getYAxis()
	{
		return yAxis;
	}
	
}
