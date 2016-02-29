package view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import core.data.DataProcessor;
import core.data.Metric;
import core.users.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckMenuItem;
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
	
	// ==== Begin Filter Stuff ====
	
	@FXML
	private CheckMenuItem filterMale;
	
	@FXML
	private CheckMenuItem filterFemale;
	
	@FXML
	private CheckMenuItem filterBelow25;
	
	@FXML
	private CheckMenuItem filter25to34;
	
	@FXML
	private CheckMenuItem filter35to44;
	
	@FXML
	private CheckMenuItem filter45to54;
	
	@FXML
	private CheckMenuItem filterAbove54;
	
	@FXML
	private CheckMenuItem filterLow;
	
	@FXML
	private CheckMenuItem filterMedium;
	
	@FXML
	private CheckMenuItem filterHigh;
	
	@FXML
	private CheckMenuItem filterNews;
	
	@FXML
	private CheckMenuItem filterShopping;
	
	@FXML
	private CheckMenuItem filterSocialMedia;
	
	@FXML
	private CheckMenuItem filterBlog;
	
	@FXML
	private CheckMenuItem filterHobbies;
	
	@FXML
	private CheckMenuItem filterTravel;

	// ==== End Filter ====
	
	private DataProcessor dataProcessor;
	
	
	public void setDataProcessor(DataProcessor dataProcessor) {
		this.dataProcessor = dataProcessor;		

		refreshData();
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
	
	private void refreshData() {
		updateDates();
		updateFilter();
		updateMetric();
		
		drawChart();
	}
	
	private void updateDates() {
		startDate.setValue(dataProcessor.getDataStartDateTime().toLocalDate());
		endDate.setValue(dataProcessor.getDataEndDateTime().toLocalDate());
	}
	
	private void updateMetric() {
		metricsBox.getSelectionModel().select(dataProcessor.getMetric());
	}
	
	private void updateFilter() {
		filterMale.setSelected(dataProcessor.getFilterValue(User.GENDER_MALE));
		filterFemale.setSelected(dataProcessor.getFilterValue(User.GENDER_FEMALE));
		
		filterBelow25.setSelected(dataProcessor.getFilterValue(User.AGE_BELOW_25));
		filter25to34.setSelected(dataProcessor.getFilterValue(User.AGE_25_TO_34));
		filter35to44.setSelected(dataProcessor.getFilterValue(User.AGE_35_TO_44));
		filter45to54.setSelected(dataProcessor.getFilterValue(User.AGE_45_TO_54));
		filterAbove54.setSelected(dataProcessor.getFilterValue(User.AGE_ABOVE_54));
		
		filterLow.setSelected(dataProcessor.getFilterValue(User.INCOME_LOW));
		filterMedium.setSelected(dataProcessor.getFilterValue(User.INCOME_MEDIUM));
		filterHigh.setSelected(dataProcessor.getFilterValue(User.INCOME_HIGH));
		
		filterNews.setSelected(dataProcessor.getFilterValue(User.CONTEXT_NEWS));
		filterShopping.setSelected(dataProcessor.getFilterValue(User.CONTEXT_SHOPPING));
		filterSocialMedia.setSelected(dataProcessor.getFilterValue(User.CONTEXT_SHOPPING));
		filterBlog.setSelected(dataProcessor.getFilterValue(User.CONTEXT_BLOG));
		filterHobbies.setSelected(dataProcessor.getFilterValue(User.CONTEXT_HOBBIES));
		filterTravel.setSelected(dataProcessor.getFilterValue(User.CONTEXT_TRAVEL));
	}
	
	@FXML
	private void handleFilter() {
		dataProcessor.setFilterValue(User.GENDER_MALE, filterMale.isSelected());
		dataProcessor.setFilterValue(User.GENDER_FEMALE, filterFemale.isSelected());
		
		dataProcessor.setFilterValue(User.AGE_BELOW_25, filterBelow25.isSelected());
		dataProcessor.setFilterValue(User.AGE_25_TO_34, filter25to34.isSelected());
		dataProcessor.setFilterValue(User.AGE_35_TO_44, filter35to44.isSelected());
		dataProcessor.setFilterValue(User.AGE_45_TO_54, filter45to54.isSelected());
		dataProcessor.setFilterValue(User.AGE_ABOVE_54, filterAbove54.isSelected());
		
		dataProcessor.setFilterValue(User.INCOME_LOW, filterLow.isSelected());
		dataProcessor.setFilterValue(User.INCOME_MEDIUM, filterMedium.isSelected());
		dataProcessor.setFilterValue(User.INCOME_HIGH, filterHigh.isSelected());

		dataProcessor.setFilterValue(User.CONTEXT_NEWS, filterNews.isSelected());
		dataProcessor.setFilterValue(User.CONTEXT_SHOPPING, filterShopping.isSelected());
		dataProcessor.setFilterValue(User.CONTEXT_SOCIAL_MEDIA, filterSocialMedia.isSelected());
		dataProcessor.setFilterValue(User.CONTEXT_BLOG, filterBlog.isSelected());
		dataProcessor.setFilterValue(User.CONTEXT_HOBBIES, filterHobbies.isSelected());
		dataProcessor.setFilterValue(User.CONTEXT_TRAVEL, filterTravel.isSelected());
		
		drawChart();
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
