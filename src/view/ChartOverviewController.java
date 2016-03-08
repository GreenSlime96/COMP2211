package view;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import core.campaigns.Campaign;
import core.data.DataProcessor;
import core.data.Metric;
import core.users.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ChartOverviewController {
	
	// ==== Constants ====
	
	private static final ObservableList<Metric> METRICS = FXCollections.observableArrayList(Arrays.asList(Metric.values()));
	
	
	// ==== FXML Properties ====
	
	@FXML
	private AreaChart<Number, Number> areaChart;
	
	@FXML
	private ChoiceBox<Metric> metricsBox;
	
	@FXML
	private ChoiceBox<Campaign> campaignsBox;
	
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

	// ==== End Filter Stuff ====
	
	// ==== Begin User Stuff ====
	
	@FXML
	private PieChart genderChart;
	
	@FXML
	private PieChart ageChart;
	
	@FXML
	private PieChart incomeChart;
	
	@FXML
	private PieChart contextChart;
	
	// ==== End User Stuff ====
	
	// ==== Begin Bar Goodies ====
	
	@FXML
	private Label impressionsLabel;
	
	@FXML
	private Label clicksLabel;
	
	@FXML
	private Label uniquesLabel;
	
	@FXML
	private Label costLabel;
	
	@FXML
	private Label cpaLabel;
	
	@FXML
	private Label ctrLabel;
	
	@FXML
	private Label bounceRateLabel;
	
	// ==== End Bar Goodies
	
	// ==== Begin Bounces ====
	
	@FXML
	private TextField bounceViews;
	
	@FXML
	private TextField bounceTime;
	
	// ==== End Bounces ====
	
//	private ObservableList<PieChart.Data> genderData;
//	private ObservableList<PieChart.Data> ageData;
//	private ObservableList<PieChart.Data> incomeData;
//	private ObservableList<PieChart.Data> contextData;
	private DataProcessor dataProcessor;
	
	private boolean isReady;
	
	public void setCampaigns(ObservableList<Campaign> campaigns) {
		campaignsBox.setItems(campaigns);
	}
	
	public void setDataProcessor(DataProcessor dataProcessor) {
		this.dataProcessor = dataProcessor;
		
		if (dataProcessor != null)
			refreshData();
	}
	
	@FXML
	private void initialize() {			
		// update Metrics box with current metrics
		metricsBox.setItems(METRICS);
		
		// listener to update DataProcessor metric
		metricsBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Metric>() {
			@Override
			public void changed(ObservableValue<? extends Metric> observable, Metric oldValue, Metric newValue) {
		    	if (!isReady)
		    		return;
		    	
				dataProcessor.setMetric(newValue);		
				refreshData();
			}			
		});
		
		// campaign change listener
		campaignsBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Campaign>() {
			@Override
			public void changed(ObservableValue<? extends Campaign> observable, Campaign oldValue, Campaign newValue) {
		    	if (!isReady)
		    		return;
				
		    	if (newValue == null)
		    		return;
		    	
				dataProcessor.setCampaign(newValue);
				refreshData();
			}
		});
		
		// locks to integers only
		timeGranularity.textProperty().addListener(new ChangeListener<String>() {
		    @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if (!isReady)
		    		return;
		    	
		        if (!newValue.isEmpty() && newValue.matches("\\d*")) {
		            int value = Integer.parseInt(newValue);
		            
		            if (value > 2000) {
			           	timeGranularity.setText(oldValue);
			           	return;
		            }
		            
		            dataProcessor.setDataPoints(value);
		            refreshData();
		        } else {
		           	timeGranularity.setText(oldValue);
		        }
		    }
		});	
		
		// locks to integers only
		bounceViews.textProperty().addListener(new ChangeListener<String>() {
		    @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if (!isReady)
		    		return;
		    	
		        if (!newValue.isEmpty() && newValue.matches("\\d*")) {
		            int value = Integer.parseInt(newValue);
		            
		            if (value > 2000) {
		            	bounceViews.setText(oldValue);
			           	return;
		            }
		            
		            dataProcessor.setBounceMinimumPagesViewed(value);
		            refreshData();
//		        } else {
//		        	bounceViews.setText(oldValue);
		        }
		    }
		});	
		
		// locks to integers only
		bounceTime.textProperty().addListener(new ChangeListener<String>() {
		    @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if (!isReady)
		    		return;
		    	
		    	System.out.println("bounce changed");
		        if (!newValue.isEmpty() && newValue.matches("\\d*")) {
		            int value = Integer.parseInt(newValue);
		            
		            if (value > 2000) {
		            	bounceTime.setText(oldValue);
			           	return;
		            }
		            
		            dataProcessor.setBounceMinimumSecondsOnPage(value);
		            refreshData();
//		        } else {
//		        	bounceTime.setText(oldValue);
		        }
		    }
		});	
	}
	
	private void refreshData() {
		isReady = false;
		
		drawChart();
		drawUsers();
		
		updateCampaigns();
		updateStats();
		updateDates();
		updateFilter();
		updateMetric();
		updateBounce();
		
		isReady = true;
	}
	
	private void updateBounce() {
		bounceTime.setText(Integer.toString(dataProcessor.getBounceMinimumSecondsOnPage()));
		bounceViews.setText(Integer.toString(dataProcessor.getBounceMinimumPagesViewed()));
	}
	
	private void updateCampaigns() {
		campaignsBox.getSelectionModel().select(dataProcessor.getCampaign());
	}
	
	private void updateStats() {
		final NumberFormat numberFormatter = NumberFormat.getInstance();
		final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
		final NumberFormat percentFormatter = NumberFormat.getPercentInstance();
		
		final String impressions = numberFormatter.format(dataProcessor.numberOfImpressions);
		final String clicks = numberFormatter.format(dataProcessor.numberOfClicks);
		final String uniques = numberFormatter.format(dataProcessor.numberOfUniques);
		final String cost = currencyFormatter.format((dataProcessor.costOfImpressions + dataProcessor.costOfClicks) / 100);
		final String cpa = currencyFormatter.format((dataProcessor.costOfImpressions + dataProcessor.costOfClicks) / 100 / dataProcessor.numberOfAcquisitions);
		final String ctr = numberFormatter.format(dataProcessor.numberOfClicks / (double) dataProcessor.numberOfImpressions);
		final String bounceRate = percentFormatter.format((double) dataProcessor.numberOfBounces / dataProcessor.numberOfClicks);
		
		impressionsLabel.setText(impressions);
		clicksLabel.setText(clicks);
		uniquesLabel.setText(uniques);
		costLabel.setText(cost);
		cpaLabel.setText(cpa);
		ctrLabel.setText(ctr);
		bounceRateLabel.setText(bounceRate);
	}
	
	private void drawUsers() {
		final EnumMap<User, Integer> users = dataProcessor.getContextData();
		
		if (users == null)
			System.exit(0);
		
		final ObservableList<PieChart.Data> genderData = genderChart.getData();
		final ObservableList<PieChart.Data> ageData = ageChart.getData();
		final ObservableList<PieChart.Data> incomeData = incomeChart.getData();
		final ObservableList<PieChart.Data> contextData = contextChart.getData();
				
		genderData.clear();
		genderData.add(new PieChart.Data(User.GENDER_MALE.toString(), users.get(User.GENDER_MALE)));
		genderData.add(new PieChart.Data(User.GENDER_FEMALE.toString(), users.get(User.GENDER_FEMALE)));
		
		ageData.clear();
		ageData.add(new PieChart.Data(User.AGE_BELOW_25.toString(), users.get(User.AGE_BELOW_25)));
		ageData.add(new PieChart.Data(User.AGE_25_TO_34.toString(), users.get(User.AGE_25_TO_34)));
		ageData.add(new PieChart.Data(User.AGE_35_TO_44.toString(), users.get(User.AGE_35_TO_44)));
		ageData.add(new PieChart.Data(User.AGE_45_TO_54.toString(), users.get(User.AGE_45_TO_54)));
		ageData.add(new PieChart.Data(User.AGE_ABOVE_54.toString(), users.get(User.AGE_ABOVE_54)));
		
		incomeData.clear();
		incomeData.add(new PieChart.Data(User.INCOME_LOW.toString(), users.get(User.INCOME_LOW)));
		incomeData.add(new PieChart.Data(User.INCOME_MEDIUM.toString(), users.get(User.INCOME_MEDIUM)));
		incomeData.add(new PieChart.Data(User.INCOME_HIGH.toString(), users.get(User.INCOME_HIGH)));
		
		contextData.clear();
		contextData.add(new PieChart.Data(User.CONTEXT_NEWS.toString(), users.get(User.CONTEXT_NEWS)));
		contextData.add(new PieChart.Data(User.CONTEXT_SHOPPING.toString(), users.get(User.CONTEXT_SHOPPING)));
		contextData.add(new PieChart.Data(User.CONTEXT_SOCIAL_MEDIA.toString(), users.get(User.CONTEXT_SOCIAL_MEDIA)));
		contextData.add(new PieChart.Data(User.CONTEXT_BLOG.toString(), users.get(User.CONTEXT_BLOG)));
		contextData.add(new PieChart.Data(User.CONTEXT_HOBBIES.toString(), users.get(User.CONTEXT_HOBBIES)));
		contextData.add(new PieChart.Data(User.CONTEXT_TRAVEL.toString(), users.get(User.CONTEXT_TRAVEL)));
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
		
		refreshData();
	}
	
	@FXML
	private void handleStartDate() {
		final LocalDate date = startDate.getValue();
		final LocalTime time = LocalTime.of(0, 0);
		System.out.println("hsd fired");
		dataProcessor.setDataStartDateTime(LocalDateTime.of(date, time));
		
		refreshData();
	}
	
	@FXML
	private void handleEndDate() {
		final LocalDate date = endDate.getValue();
		final LocalTime time = LocalTime.of(0, 0);
		
		dataProcessor.setDataEndDateTime(LocalDateTime.of(date, time));
		
		refreshData();
	}
	
	@FXML
	private void handleMetrics() {
		dataProcessor.setMetric(metricsBox.getValue());
	}
	
	private void drawChart() {
		areaChart.getData().clear();
				
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		
		List<? extends Number> list = dataProcessor.getData();
		int counter = 0;
		for (Number n : list) {
			series.getData().add(new XYChart.Data<Number, Number>(counter++, n));
		}
		
		areaChart.getData().add(series);
	}
}
