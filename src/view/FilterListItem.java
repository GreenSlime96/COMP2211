package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class FilterListItem extends HBox {
		
	private final Circle circle;
	private final Label label;
	
	private final String[] COLOURS = {"f3622d","41a9c9","57b757","fba71b","4258c9","9a42c8","c84164","888888"};
	
	public FilterListItem(String text, int index)
	{
		super();
		
		this.setAlignment(Pos.CENTER_LEFT);
		
		circle = new Circle(5, Color.web(COLOURS[index%8]));
		label = new Label(text);
		
		this.getChildren().add(circle);
		this.getChildren().add(label);	
		
		setMargin(circle, new Insets(10, 10, 10, 10));
	}
	
	public final void setText(String text)
	{
		label.setText(text);
	}
	
	
}
