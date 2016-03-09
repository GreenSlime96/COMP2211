package view.animation;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class PieChartScaleAnimation implements EventHandler<MouseEvent>
{

	static final Duration ANIMATION_DURATION = new Duration(100);
	static final double ANIMATION_SCALE = 1.1d;
	
	private PieChart chart;
	private PieChart.Data data;
	
	private boolean entered;
	
	public PieChartScaleAnimation(PieChart chart, PieChart.Data data, boolean entered)
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