package ui.graphelements;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class PieChartElement implements ChartElement {

	private static final int TITLE_HEIGHT = 30;
	private static final int LEGEND_HEIGHT = 50;
	
	private static final float DEFAULT_SIZE = 530f;
	private static final int DEFAULT_LINE_LENGTH = 20;
	
	private PieChart chart;
	
	public PieChartElement(String chartTitle)
	{
		chart = new PieChart();
		System.out.println(chart.getLabelLineLength());
		chart.setTitle(chartTitle);
	}
	
	public void resizeChart(int width, int height)
	{
		chart.setPrefSize(width, height - TITLE_HEIGHT - LEGEND_HEIGHT);
		chart.setLabelLineLength( ((width > height ? width : height) / DEFAULT_SIZE) * DEFAULT_LINE_LENGTH );
	}
	
	/**
	 * Sets the data for the pie chart. 
	 * @param data Supplied as ObservableList of PieChart.Data elements, which are
	 * a string and double pair.
	 */
	public void setData(ObservableList<PieChart.Data> data)
	{
		chart.setData(data);
		
		for (final PieChart.Data d : chart.getData())
		{
			d.getNode().setOnMouseEntered(new ScaleAnimation(chart, d, true));
			d.getNode().setOnMouseExited(new ScaleAnimation(chart, d, false));
			
			Tooltip.install(d.getNode(), new Tooltip(d.getName() + ": " + d.getPieValue() + "%"));
		}
	}
	
	public PieChart getChart()
	{
		return chart;
	}
	
	static class ScaleAnimation implements EventHandler<MouseEvent>
	{

		static final Duration ANIMATION_DURATION = new Duration(100);
		static final double ANIMATION_SCALE = 1.1d;
		
		private PieChart chart;
		private PieChart.Data data;
		
		private boolean entered;
		
		public ScaleAnimation(PieChart chart, PieChart.Data data, boolean entered)
		{
			this.chart = chart;
			this.data = data;
			this.entered = entered;
		}
		
		@Override
		public void handle(MouseEvent e)
		{
			final Animation animation = new Transition() {
			     {
			         setCycleDuration(ANIMATION_DURATION);
			     }
			 
			     protected void interpolate(double frac) {			    	 
			    	 if(entered)
			    	 {
			    		 data.getNode().setScaleX(1+((ANIMATION_SCALE-1)*frac));
			    		 data.getNode().setScaleY(1+((ANIMATION_SCALE-1)*frac));
			    	 }else
			    	 {
			    		 data.getNode().setScaleX(ANIMATION_SCALE-((ANIMATION_SCALE-1)*frac));
			    		 data.getNode().setScaleY(ANIMATION_SCALE-((ANIMATION_SCALE-1)*frac));
			    	 }
			     }
			 
			 };
			 animation.play();
		}
		
	}
	
}
