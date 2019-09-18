package de.bund.jki.jki_bonitur;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.bund.jki.jki_bonitur.db.Akzession;
import de.bund.jki.jki_bonitur.db.Passport;
import de.bund.jki.jki_bonitur.db.Standort;

/**
 * Created by toni.schreiber on 04.08.2015.
 */
public class StandortInformationActivity extends Activity {

    public static Standort  mStandort;
    private       Akzession akzession = null;
    private       Passport  passport  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standort_informationen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadValues();
        init_typefaces();
    }

    public void init_typefaces() {
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome.ttf");
        int[] buttonIds = new int[]{

                R.id.btnSaveIcon,
                R.id.btnZurueckIcon,

        };

        for (int id : buttonIds) {
            ((Button) this.findViewById(id)).setTypeface(typeface);
        }
    }

    private void loadValues() {
        ((TextView) this.findViewById(R.id.tvStandort)).setText(mStandort.getName());
        if (mStandort.akzessionId != - 1) {
            akzession = Akzession.findByPk(mStandort.akzessionId);
            if (akzession != null) {
                ((TextView) this.findViewById(R.id.tvAkzesion)).setText(akzession.name + " (" + akzession.nummer + ")");
            }
        }
        if (mStandort.passportId != - 1) {
            passport = Passport.findByPk(mStandort.passportId);
            if (passport != null) {
                ((TextView) this.findViewById(R.id.tvPassport)).setText(passport.leitname + " (" + passport.kennNr + ")");
            }
        }

        ((EditText) this.findViewById(R.id.etStandortInfo)).setText(mStandort.info);
        ((EditText) this.findViewById(R.id.etStandortInfo)).setText(mStandort.info);
        if (akzession != null) {
            ((EditText) this.findViewById(R.id.eTcharakterMermale)).setText(akzession.merkmale);
        }

        ((TextView) this.findViewById(R.id.tvDocument)).setText("Datei: " + BoniturSafe.VERSUCH_NAME);
    }

    private void save() {
        mStandort.info = ((EditText) this.findViewById(R.id.etStandortInfo)).getText().toString();
        mStandort.save();
        if (akzession != null) {
            akzession.merkmale = ((EditText) this.findViewById(R.id.eTcharakterMermale)).getText().toString();
            akzession.save();
        }

    }


    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btnZurueckIcon:
            case R.id.btnZurueck:
                onBackPressed();
                break;
            case R.id.btnSaveIcon:
            case R.id.btnSpeichern:
                save();
                onBackPressed();
                break;
        }
    }
}
