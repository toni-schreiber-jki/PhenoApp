package de.bund.jki.jki_bonitur.dialoge;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.test.ActivityTestCase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import de.bund.jki.jki_bonitur.BoniturActivity;
import de.bund.jki.jki_bonitur.R;
import de.bund.jki.jki_bonitur.VersuchListActivity;
import de.bund.jki.jki_bonitur.config.Config;

/**
 * Created by toni.schreiber on 23.06.2015.
 */
public class SettingsDialog {
    public SettingsDialog(Activity ba){
        SettingsDialogFragment sdf = new SettingsDialogFragment();
        sdf.mBa = ba;
        sdf.setCancelable(false);
        sdf.show(ba.getFragmentManager(), "");;
    }

    public static class SettingsDialogFragment extends DialogFragment
    {
        public Activity mBa;
        private View mView;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //return super.onCreateDialog(savedInstanceState);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            mView = inflater.inflate(R.layout.dialog_settings,null);

            builder.setView(mView)
                    .setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mBa);
                            SharedPreferences.Editor editor = preferences.edit();

                            Config.ZICK_ZACK_MODUS = ((CheckBox) mView.findViewById(R.id.cbZickTack)).isChecked();
                            editor.putBoolean(Config.NAME_ZICK_ZACK_MODUS, Config.ZICK_ZACK_MODUS);

                            Config.BaseFolder = ((TextView) mView.findViewById(R.id.etSpeicherOrt)).getText().toString();
                            editor.putString(Config.NAME_BASE_FOLDER, Config.BaseFolder);

                            Config.FIRST_EMPTY = ((CheckBox) mView.findViewById(R.id.cbFirstEmpty)).isChecked();
                            editor.putBoolean(Config.NAME_FIRST_EMPTY, Config.FIRST_EMPTY);

                            Config.SHOW_ELTERN = ((CheckBox) mView.findViewById(R.id.cbEltern)).isChecked();
                            editor.putBoolean(Config.NAME_SHOW_ELTERN, Config.SHOW_ELTERN);

                            Config.SHOW_AKZESSION = ((CheckBox) mView.findViewById(R.id.cbAkzession)).isChecked();
                            editor.putBoolean(Config.NAME_SHOW_AKZESSION, Config.SHOW_AKZESSION);

                            Config.SHOW_PASSPORT = ((CheckBox) mView.findViewById(R.id.cbPassport)).isChecked();
                            editor.putBoolean(Config.NAME_SHOW_PASSPORT, Config.SHOW_PASSPORT);

                            Config.SHOW_SORTE = ((CheckBox) mView.findViewById(R.id.cbSorte)).isChecked();
                            editor.putBoolean(Config.NAME_SHOW_SORTE, Config.SHOW_SORTE);

                            Config.SHOW_SORTIMENT = ((CheckBox) mView.findViewById(R.id.cbSortNr)).isChecked();
                            editor.putBoolean(Config.NAME_SHOW_SORTIMENT, Config.SHOW_SORTIMENT);

                            editor.apply();

                            SettingsDialogFragment.this.getDialog().cancel();
                            if (mBa.getLocalClassName().toString().contains("BoniturActivity")) {
                                ((BoniturActivity) mBa).loadSettings();
                            } else {
                                ((VersuchListActivity) mBa).loadSettings();
                            }
                        }
                    })

                    .setNegativeButton("Nicht Speichern", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            SettingsDialogFragment.this.getDialog().cancel();
                        }
                    });


            return builder.create();
        }

        @Override
        public void onResume() {
            super.onResume();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mBa);
            Typeface typeface = Typeface.createFromAsset(mBa.getAssets(),"fonts/fontawesome.ttf");
            ((TextView) mView.findViewById(R.id.tvSettingsIcon)).setTypeface(typeface);
            ((TextView) mView.findViewById(R.id.tvFolderIcon)).setTypeface(typeface);
            ((TextView) mView.findViewById(R.id.tvZickZackIcon)).setTypeface(typeface);
            ((TextView) mView.findViewById(R.id.tvFirstEmptyIcon)).setTypeface(typeface);
            ((TextView) mView.findViewById(R.id.tvElternIcon)).setTypeface(typeface);


            ((EditText) mView.findViewById(R.id.etSpeicherOrt)).setText(preferences.getString(Config.NAME_BASE_FOLDER, Config.BaseFolder));
            ((CheckBox) mView.findViewById(R.id.cbZickTack)).setChecked(preferences.getBoolean(Config.NAME_ZICK_ZACK_MODUS, Config.ZICK_ZACK_MODUS));
            ((CheckBox) mView.findViewById(R.id.cbFirstEmpty)).setChecked(preferences.getBoolean(Config.NAME_FIRST_EMPTY, Config.FIRST_EMPTY));
            ((CheckBox) mView.findViewById(R.id.cbEltern)).setChecked(preferences.getBoolean(Config.NAME_SHOW_ELTERN, Config.SHOW_ELTERN));
            ((CheckBox) mView.findViewById(R.id.cbAkzession)).setChecked(preferences.getBoolean(Config.NAME_SHOW_AKZESSION, Config.SHOW_AKZESSION));
            ((CheckBox) mView.findViewById(R.id.cbPassport)).setChecked(preferences.getBoolean(Config.NAME_SHOW_PASSPORT, Config.SHOW_PASSPORT));
            ((CheckBox) mView.findViewById(R.id.cbSorte)).setChecked(preferences.getBoolean(Config.NAME_SHOW_SORTE, Config.SHOW_SORTE));
            ((CheckBox) mView.findViewById(R.id.cbSortNr)).setChecked(preferences.getBoolean(Config.NAME_SHOW_SORTIMENT, Config.SHOW_SORTIMENT));
        }
    }
}
