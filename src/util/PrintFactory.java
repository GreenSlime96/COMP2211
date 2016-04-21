package util;

import java.lang.reflect.InvocationTargetException;

import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.transform.Scale;

public class PrintFactory {

	public static void printNodes(PageOrientation pageOrientation, final Node... nodes) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		if(nodes.length == 0) return;

		Printer printer = Printer.getDefaultPrinter();
	    PageLayout pageLayout
	        = printer.createPageLayout(Paper.A4, pageOrientation, Printer.MarginType.HARDWARE_MINIMUM);
	    PrinterJob job = PrinterJob.createPrinterJob();
	    job.showPrintDialog(nodes[0].getScene().getWindow());
	    
	    boolean success = true;
	    
	    for(Node node : nodes)
	    {
		    double scaleX
		        = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
		    double scaleY
		        = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
		    
		    Scale scale = (scaleX < scaleY ? new Scale(scaleX, scaleX) : new Scale(scaleY, scaleY));
		    node.getTransforms().add(scale);
	
		    if (job != null) {
		      if(!job.printPage(pageLayout, node))
		      {
		    	  success = false;
		    	  break;
		      }
		    }
		    node.getTransforms().remove(scale);
	    }
	    if (success)
	    	job.endJob();

	  }
	
	
}
