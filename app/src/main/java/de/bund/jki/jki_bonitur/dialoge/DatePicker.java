package de.bund.jki.jki_bonitur.dialoge;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

import de.bund.jki.jki_bonitur.BoniturActivity;

/**
 * Created by toni.schreiber on 19.06.2015.
 */
public class DatePicker {

    private BoniturActivity mBa;

    public DatePicker(BoniturActivity ba) {
        mBa = ba;
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.mBa = ba;
        newFragment.show(ba.getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public BoniturActivity mBa;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c     = Calendar.getInstance();
            int            year  = c.get(Calendar.YEAR);
            int            month = c.get(Calendar.MONTH);
            int            day   = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
            mBa.bah.getEtDatumEingabe().setText(String.format("%02d.%02d.%04d", day, month + 1, year));
        }
    }
}
