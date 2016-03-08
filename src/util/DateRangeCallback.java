package util;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

import java.time.LocalDate;

/**
 * Created by jamesadams on 06/03/2016.
 */
public class DateRangeCallback implements Callback<DatePicker, DateCell> {

    boolean isStart;
    LocalDate startDate, endDate, limitDate;

    public DateRangeCallback(boolean isStart, LocalDate startDate, LocalDate endDate, LocalDate limitDate){
        this.isStart = isStart;
        this.startDate = startDate;
        this.endDate = endDate;
        this.limitDate = limitDate;
    }

    @Override
    public DateCell call(DatePicker datePicker) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.isBefore(startDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #cccccc;");
                }

                if (item.isAfter(endDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #cccccc;");
                }

                if(isStart){
                    if (item.isAfter(limitDate.minusDays(1)) && item.isBefore(endDate.plusDays(1))){
                        setDisable(true);
                        setStyle("-fx-background-color: #FFBBBB;");
                    }
                }else{
                    if (item.isBefore(limitDate.plusDays(1)) && item.isAfter(startDate.minusDays(1))){
                        setDisable(true);
                        setStyle("-fx-background-color: #FFBBBB;");
                    }
                }
            }
        };
    }
}