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

	public static void printNode(final Node node) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
	    Printer printer = Printer.getDefaultPrinter();
	    PageLayout pageLayout
	        = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
	    PrinterJob job = PrinterJob.createPrinterJob();
	    double scaleX
	        = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
	    double scaleY
	        = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
	    
	    Scale scale = (scaleX < scaleY ? new Scale(scaleX, scaleX) : new Scale(scaleY, scaleY));
	    node.getTransforms().add(scale);

	    if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
	      boolean success = job.printPage(pageLayout, node);
	      if (success) {
	        job.endJob();

	      }
	    }
	    node.getTransforms().remove(scale);
	  }
	
}
