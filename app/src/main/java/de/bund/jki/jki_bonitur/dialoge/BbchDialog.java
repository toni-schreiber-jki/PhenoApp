package de.bund.jki.jki_bonitur.dialoge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import de.bund.jki.jki_bonitur.BoniturActivity;
import de.bund.jki.jki_bonitur.BoniturSafe;
import de.bund.jki.jki_bonitur.R;
import de.bund.jki.jki_bonitur.db.Marker;

/**
 * Created by toni.schreiber on 19.06.2015.
 */
public class BbchDialog {

    public BbchDialog(BoniturActivity ba, String name) {
        BbchDialogFragment bdf = new BbchDialogFragment();
        bdf.ba = ba;
        bdf.markername = name;
        bdf.setCancelable(false);
        bdf.show(ba.getFragmentManager(), "");
    }

    public void speichern(String fileName) {

    }

    public static class BbchDialogFragment extends DialogFragment {
        public BoniturActivity ba;
        public String          markername;

        private View mView;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //return super.onCreateDialog(savedInstanceState);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            mView = inflater.inflate(R.layout.dialog_add_bbch, null);

            builder.setView(mView)
                    .setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Marker marker = new Marker();
                            marker.code = ((EditText) mView.findViewById(R.id.et_BBCH_Code)).getText().toString();
                            marker.beschreibung = "BBCH WERT";
                            marker.type = Marker.MARKER_TYPE_BBCH;
                            marker.name = marker.code;
                            marker.versuchId = BoniturSafe.VERSUCH_ID;
                            marker.save();

                            ba.bah.spMarkerCheck = 0;
                            ba.bah.getMarkerSpinner().setAdapter(ba.bah.getMarkerSpinnerAdpter());
                            ba.fillView(new Object[]{ba.currentStandort, ba.currentMarker});


                            BbchDialogFragment.this.getDialog().cancel();
                        }
                    })

                    .setNegativeButton("Nicht Speichern", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            BbchDialogFragment.this.getDialog().cancel();
                        }
                    });


            return builder.create();
        }

        @Override
        public void onResume() {
            super.onResume();
            Typeface typeface = Typeface.createFromAsset(ba.getAssets(), "fonts/fontawesome.ttf");
            ((TextView) mView.findViewById(R.id.tvFragezeichen)).setTypeface(typeface);
            ((EditText) mView.findViewById(R.id.et_BBCH_Code)).setText(markername);

        }
    }
}
