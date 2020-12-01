package de.bund.jki.jki_bonitur.dialoge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import de.bund.jki.jki_bonitur.BoniturActivity;
import de.bund.jki.jki_bonitur.R;

/**
 * Created by toni.schreiber on 19.06.2015.
 */
public class WartenDialog {

    public static int MELDUNG_LADEN     = 1;
    public static int MELDUNG_SPEICHERN = 2;
    WartenDialogFragment wdf;


    public WartenDialog(BoniturActivity ba) {
        wdf = new WartenDialogFragment();
        wdf.ba = ba;
        wdf.showMeldung = MELDUNG_LADEN;
        wdf.setCancelable(false);
        wdf.show(ba.getFragmentManager(), "");
    }

    public WartenDialog(BoniturActivity ba, int meldung) {
        wdf = new WartenDialogFragment();
        wdf.ba = ba;
        wdf.showMeldung = meldung;
        wdf.setCancelable(false);
        wdf.show(ba.getFragmentManager(), "");
    }

    public void close() {
        wdf.close();
    }

    public static class WartenDialogFragment extends DialogFragment {
        public  int             showMeldung = 1;
        public  BoniturActivity ba;
        private String[]        meldungen   = new String[]{
                getString(R.string.label_loading_data),
                getString(R.string.label_data_will_saved)
        };
        private View            mView;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //return super.onCreateDialog(savedInstanceState);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            mView = inflater.inflate(R.layout.dialog_warten, null);

            builder.setView(mView);

            return builder.create();
        }

        @Override
        public void onResume() {
            super.onResume();

            ((TextView) mView.findViewById(R.id.tvMeldung)).setText(meldungen[showMeldung - 1]);
            /*
            Typeface typeface = Typeface.createFromAsset(ba.getAssets(),"fonts/fontawesome.ttf");
            ((TextView) mView.findViewById(R.id.tvFragezeichen)).setTypeface(typeface);
            ((EditText) mView.findViewById(R.id.etFilename)).setText(filename);
            */

        }

        public void close() {
            try {
                WartenDialogFragment.this.getDialog().cancel();
            } catch (Exception e) {

            }
        }
    }
}
