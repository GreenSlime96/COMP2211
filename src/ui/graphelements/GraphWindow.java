package ui.graphelements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.Model;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;



public class GraphWindow extends JFrame {
	Dimension fullViewDimension = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) fullViewDimension.getWidth();
	int height = (int) fullViewDimension.getHeight();
	private JFXPanel centerPanel;
	private Scene scene;
	private GridPane chartElementPane;
	private ChartElement chartElement;
	
	public GraphWindow(Model model, String title){
		this.setTitle(title);
		this.setPreferredSize(new Dimension(1366, 740));
		this.setVisible(true);
//		this.setResizable(false);
		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.init();
	}
	
	public void init(){
		Container contentPane = this.getContentPane();
		centerPanel = new JFXPanel();

		centerPanel.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {	
				
				if (e.getClickCount() == 2 && !e.isConsumed()) {
				    e.consume();
				    dispose();
				}
			}

			public void mousePressed(MouseEvent e) {
			}
			public void mouseReleased(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
		});
		

		centerPanel.setBackground(new Color(120, 100, 0));
		centerPanel.setPreferredSize(new Dimension(1360, 740));
		centerPanel.setVisible(true);
		
		JPanel northPanel = new JPanel();
		northPanel.add(new JLabel("Information about the chart (filters, etc.) here "));
		
		contentPane.add(northPanel, BorderLayout.NORTH);
		contentPane.add(centerPanel, BorderLayout.CENTER);
				
		
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	
	public void setScene(Scene scene)
	{
		centerPanel.setScene(scene);
		this.pack();
	}
}
