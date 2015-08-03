package de.bund.jki.jki_bonitur.dialoge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import de.bund.jki.jki_bonitur.BoniturActivity;
import de.bund.jki.jki_bonitur.BoniturSafe;
import de.bund.jki.jki_bonitur.R;
import de.bund.jki.jki_bonitur.config.Config;
import de.bund.jki.jki_bonitur.excel.Writer;

/**
 * Created by toni.schreiber on 19.06.2015.
 */
public class WartenDialog {

    WartenDialogFragment wdf;


    public WartenDialog(BoniturActivity ba)
    {
        wdf = new WartenDialogFragment();
        wdf.ba          = ba;
        wdf.setCancelable(false);
        wdf.show(ba.getFragmentManager(), "");
    }

    public void close()
    {
        wdf.close();
    }

    public static class WartenDialogFragment extends DialogFragment
    {
        public BoniturActivity ba;
        private View mView;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //return super.onCreateDialog(savedInstanceState);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            mView = inflater.inflate(R.layout.dialog_warten,null);

            builder.setView(mView);

            return builder.create();
        }

        @Override
        public void onResume() {
            super.onResume();
            /*
            Typeface typeface = Typeface.createFromAsset(ba.getAssets(),"fonts/fontawesome.ttf");
            ((TextView) mView.findViewById(R.id.tvFragezeichen)).setTypeface(typeface);
            ((EditText) mView.findViewById(R.id.etFilename)).setText(filename);
            */

        }

        public void close(){
            WartenDialogFragment.this.getDialog().cancel();
        }
    }
}
