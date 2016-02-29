package view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import core.data.DataProcessor;
import core.data.Metric;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ChartOverviewController {
	
	@FXML
	private AreaChart<Number, Number> areaChart;
	
	@FXML
	private ChoiceBox<Metric> metricsBox;
	
	@FXML
	private DatePicker startDate;
	
	@FXML 
	private DatePicker endDate;
	
	@FXML
	private TextField timeGranularity;
	
	
	private DataProcessor dataProcessor;
	
	
	public void setDataProcessor(DataProcessor dataProcessor) {
		this.dataProcessor = dataProcessor;
		
		metricsBox.getSelectionModel().select(dataProcessor.getMetric());
		
		startDate.setValue(dataProcessor.getDataStartDateTime().toLocalDate());
		endDate.setValue(dataProcessor.getDataEndDateTime().toLocalDate());
		
		drawChart();
	}
	
	@FXML
	private void initialize() {
		ObservableList<Metric> metricsList = FXCollections.observableArrayList();
		
		for (Metric metric : Metric.values()) {
			metricsList.add(metric);
		}
		
		metricsBox.setItems(metricsList);
		
		metricsBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Metric>() {
			@Override
			public void changed(ObservableValue<? extends Metric> observable, Metric oldValue, Metric newValue) {
				dataProcessor.setMetric(newValue);		
				drawChart();
			}			
		});
		
		timeGranularity.textProperty().addListener(new ChangeListener<String>() {
		    @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if (newValue.isEmpty())
		    		return;
		    	
		        if (newValue.matches("\\d*")) {
		            int value = Integer.parseInt(newValue);		
		            	
		            dataProcessor.setDataPoints(value);
		            drawChart();
		        } else {
		           	timeGranularity.setText(oldValue);
		        }
		    }
		});
	}
	
	@FXML
	private void handleStartDate() {
		final LocalDate date = startDate.getValue();
		final LocalTime time = LocalTime.of(0, 0);
		
		dataProcessor.setDataStartDateTime(LocalDateTime.of(date, time));
		
		drawChart();
	}
	
	@FXML
	private void handleEndDate() {
		final LocalDate date = endDate.getValue();
		final LocalTime time = LocalTime.of(0, 0);
		
		dataProcessor.setDataStartDateTime(LocalDateTime.of(date, time));
		
		drawChart();
	}
	
	@FXML
	private void handleMetrics() {
		dataProcessor.setMetric(metricsBox.getValue());
	}
	
	private void drawChart() {
		areaChart.getData().clear();
		
		areaChart.setTitle(dataProcessor.getMetric().toString());
		
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		
		List<? extends Number> list = dataProcessor.getData();
		int counter = 0;
		for (Number n : list) {
			series.getData().add(new XYChart.Data<Number, Number>(counter++, n));
		}
		
		areaChart.getData().add(series);
	}
}
