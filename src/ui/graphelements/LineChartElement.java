package ui.graphelements;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.GridPane;

public class LineChartElement extends ChartElement {
		
	private NumberAxis xAxis, yAxis;
	
	public LineChartElement()
	{
		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		
		chart = new LineChart<Number, Number>(xAxis, yAxis);
		
		Series series = new Series();
		series.setName("Some series");
		series.getData().add(new Data(1, 23));
        series.getData().add(new Data(2, 14));
        series.getData().add(new Data(3, 15));
        series.getData().add(new Data(4, 24));
        series.getData().add(new Data(5, 34));
        series.getData().add(new Data(6, 36));
        series.getData().add(new Data(7, 22));
        series.getData().add(new Data(8, 45));
        series.getData().add(new Data(9, 43));
        series.getData().add(new Data(10, 17));
        series.getData().add(new Data(11, 29));
        series.getData().add(new Data(12, 25));
        
        chart.getData().add(series);
	}
	
	public void addSeries(Series series)
	{
		chart.getData().add(series);
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
