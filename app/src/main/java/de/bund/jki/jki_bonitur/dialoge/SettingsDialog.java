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
    public SettingsDialog(Activity ba) {
        SettingsDialogFragment sdf = new SettingsDialogFragment();
        sdf.mBa = ba;
        sdf.setCancelable(false);
        sdf.show(ba.getFragmentManager(), "");
    }

    public static class SettingsDialogFragment extends DialogFragment {
        public  Activity mBa;
        private View     mView;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //return super.onCreateDialog(savedInstanceState);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            mView = inflater.inflate(R.layout.dialog_settings, null);

            builder.setView(mView)
                    .setPositiveButton(getString(R.string.btn_save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences        preferences = PreferenceManager.getDefaultSharedPreferences(mBa);
                            SharedPreferences.Editor editor      = preferences.edit();

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

                            Config.SHOW_EXCEL_DATUM = ((CheckBox) mView.findViewById(R.id.cbExcelDatum)).isChecked();
                            editor.putBoolean(Config.NAME_EXCEL_DATUM, Config.SHOW_EXCEL_DATUM);

                            Config.SHOW_BBCH_FRAGE = ((CheckBox) mView.findViewById(R.id.cbBbch)).isChecked();
                            editor.putBoolean(Config.NAME_BBCH_FRAGE, Config.SHOW_BBCH_FRAGE);

                            Config.IS_LEFT_HAND_MODE = ((CheckBox) mView.findViewById(R.id.cbLeftHand)).isChecked();
                            editor.putBoolean(Config.NAME_LEFT_HAND_MODE, Config.IS_LEFT_HAND_MODE);

                            Config.IS_MULTIPLE_VALUES = ((CheckBox) mView.findViewById(R.id.cbMultiValue)).isChecked();
                            editor.putBoolean(Config.NAME_MULTIPLE_VALUES, Config.IS_MULTIPLE_VALUES);

                            editor.apply();

                            SettingsDialogFragment.this.getDialog().cancel();
                            if (mBa.getLocalClassName().contains("BoniturActivity")) {
                                ((BoniturActivity) mBa).loadSettings();
                            } else {
                                ((VersuchListActivity) mBa).loadSettings();
                            }
                        }
                    })

                    .setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
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
            Typeface          typeface    = Typeface.createFromAsset(mBa.getAssets(), "fonts/fontawesome.ttf");
            ((TextView) mView.findViewById(R.id.tvSettingsIcon)).setTypeface(typeface);
            ((TextView) mView.findViewById(R.id.tvFolderIcon)).setTypeface(typeface);
            ((TextView) mView.findViewById(R.id.tvZickZackIcon)).setTypeface(typeface);
            ((TextView) mView.findViewById(R.id.tvFirstEmptyIcon)).setTypeface(typeface);
            ((TextView) mView.findViewById(R.id.tvElternIcon)).setTypeface(typeface);
            ((TextView) mView.findViewById(R.id.tvCalendar)).setTypeface(typeface);
            ((TextView) mView.findViewById(R.id.tvLeftHand)).setTypeface(typeface);
            ((TextView) mView.findViewById(R.id.tvMultiple)).setTypeface(typeface);
            //((TextView) mView.findViewById(R.id.tvExcel)).setTypeface(typeface);


            ((EditText) mView.findViewById(R.id.etSpeicherOrt)).setText(preferences.getString(Config.NAME_BASE_FOLDER, Config.BaseFolder));
            ((CheckBox) mView.findViewById(R.id.cbZickTack)).setChecked(preferences.getBoolean(Config.NAME_ZICK_ZACK_MODUS, Config.ZICK_ZACK_MODUS));
            ((CheckBox) mView.findViewById(R.id.cbFirstEmpty)).setChecked(preferences.getBoolean(Config.NAME_FIRST_EMPTY, Config.FIRST_EMPTY));
            ((CheckBox) mView.findViewById(R.id.cbEltern)).setChecked(preferences.getBoolean(Config.NAME_SHOW_ELTERN, Config.SHOW_ELTERN));
            ((CheckBox) mView.findViewById(R.id.cbAkzession)).setChecked(preferences.getBoolean(Config.NAME_SHOW_AKZESSION, Config.SHOW_AKZESSION));
            ((CheckBox) mView.findViewById(R.id.cbPassport)).setChecked(preferences.getBoolean(Config.NAME_SHOW_PASSPORT, Config.SHOW_PASSPORT));
            ((CheckBox) mView.findViewById(R.id.cbSorte)).setChecked(preferences.getBoolean(Config.NAME_SHOW_SORTE, Config.SHOW_SORTE));
            ((CheckBox) mView.findViewById(R.id.cbSortNr)).setChecked(preferences.getBoolean(Config.NAME_SHOW_SORTIMENT, Config.SHOW_SORTIMENT));
            ((CheckBox) mView.findViewById(R.id.cbExcelDatum)).setChecked(preferences.getBoolean(Config.NAME_EXCEL_DATUM, Config.SHOW_EXCEL_DATUM));
            ((CheckBox) mView.findViewById(R.id.cbBbch)).setChecked(preferences.getBoolean(Config.NAME_BBCH_FRAGE, Config.SHOW_BBCH_FRAGE));
            ((CheckBox) mView.findViewById(R.id.cbLeftHand)).setChecked(preferences.getBoolean(Config.NAME_LEFT_HAND_MODE, Config.IS_LEFT_HAND_MODE));
            ((CheckBox) mView.findViewById(R.id.cbMultiValue)).setChecked(preferences.getBoolean(Config.NAME_MULTIPLE_VALUES, Config.IS_MULTIPLE_VALUES));

            mView.findViewById((R.id.etSpeicherOrt)).setSelected(false);
        }


    }
}
