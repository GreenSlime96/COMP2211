package util;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

import java.time.LocalDate;

/**
 * Created by jamesadams on 06/03/2016.
 */
public class DateRangeCallback implements Callback<DatePicker, DateCell> {

    LocalDate startDate, endDate;

    public DateRangeCallback(LocalDate startDate, LocalDate endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public DateCell call(DatePicker datePicker) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.isBefore(startDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }

                if (item.isAfter(endDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        };
    }
}