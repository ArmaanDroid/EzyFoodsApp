package dialogs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by win on 21-Jun-17.
 */

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DateSelectionListner mListener;
    private static final int DAY_IN_FUTURE = 30;
    private boolean setMinimumDate = true;
    private Date selectedDate;

    public DatePickerFragment(Date selectedDate, DateSelectionListner mListener) {
        this.mListener = mListener;
        this.selectedDate = selectedDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        if (selectedDate != null)
            c.setTime(selectedDate);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        if (setMinimumDate)
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return dialog;
    }

    public void setSetMinimumDate(boolean setMinimumDate) {
        this.setMinimumDate = setMinimumDate;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if (mListener != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            calendar.getTime();
            mListener.onDateSelected(calendar.getTime());
        }
    }

    public interface DateSelectionListner {
        public void onDateSelected(Date date);
    }
}