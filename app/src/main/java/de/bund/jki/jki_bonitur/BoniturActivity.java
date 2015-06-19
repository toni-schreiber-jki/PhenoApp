package de.bund.jki.jki_bonitur;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import de.bund.jki.jki_bonitur.db.BoniturDatenbank;
import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.Standort;
import de.bund.jki.jki_bonitur.dialoge.DatePicker;
import de.bund.jki.jki_bonitur.dialoge.SpeichernDialog;
import de.bund.jki.jki_bonitur.excel.Reader;


public class BoniturActivity extends Activity {

    private BoniturDatenbank bonDb;
    public BoniturActivityHelper bah;
    public Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonitur);
        init();
    }

    private void init(){
        bonDb = new BoniturDatenbank(this);
        BoniturSafe.db = bonDb.getWritableDatabase();

        bah = new BoniturActivityHelper(this);

        Reader reader = new Reader(getIntent().getStringExtra("FILE"));
        reader.read();

        BoniturSafe.CURRENT_PARZELLE    = "-";
        BoniturSafe.CURRENT_REIHE       = -1;
        BoniturSafe.CURRENT_PFLANZE     = -1;

        BoniturSafe.CURRENT_MARKER      = -1;

        bah.init_Spinner();
        bah.init_typefaces();
        bah.init_textListener();
        fillView(StandortManager.next());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bonitur, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fillView(Object[] daten){
        Standort s = null;
        if(daten[0] != null ) {
            s = (Standort) daten[0];
            ((TextView) findViewById(R.id.tvStandort)).setText(s.getName());
            ((TextView) findViewById(R.id.tvSorteZucht)).setText(((Standort) daten[0]).sorte + " / " + ((Standort) daten[0]).zuchtstamm);
            ((TextView) findViewById(R.id.tvEltern)).setText(((Standort) daten[0]).mutter + " / " + ((Standort) daten[0]).vater);
            ((TextView) findViewById(R.id.tvSortiment)).setText(((Standort) daten[0]).sortimentsnummer);

            if(bah.akzesionIdPos.containsKey(s.akzessionId)){
                bah.spAkzessionCheck = 0;
                bah.getAkzessionSpinner().setSelection(bah.akzesionIdPos.get(s.akzessionId),false);
            }

            if(bah.passportIdPos.containsKey(s.passportId)){
                bah.spPassportCheck = 0;
                bah.getPassportSpinner().setSelection(bah.passportIdPos.get(s.passportId),false);
            }
        }

        if(daten[1] != null){
            Marker marker = (Marker) daten[1];
            currentMarker  = marker;

            ((TextView) findViewById(R.id.tvMerkmal)).setText(marker.name);
            ((TextView) findViewById(R.id.tvBeschreibung)).setText(marker.beschreibung);

            bah.hideAllEingabeTypen();

            switch(marker.type) {
                case Marker.MARKER_TYPE_BONITUR:
                    bah.getGvWerte().setVisibility(View.VISIBLE);
                    bah.fillGridView(marker.werte);
                    break;
                case Marker.MARKER_TYPE_DATUM:
                    bah.getRlDatum().setVisibility(View.VISIBLE);
                    bah.getEtDatumEingabe().setText(s.getValue(marker.id));
                    break;
                case Marker.MARKER_TYPE_MESSEN:
                    bah.getRlMessen().setVisibility(View.VISIBLE);
                    bah.getEtMessenEingabe().setText(s.getValue(marker.id));
                    break;
                case Marker.MARKER_TYPE_BEMERKUNG:
                    bah.getRlBemerkung().setVisibility(View.VISIBLE);
                    bah.getEtBemerkungEingabe().setText(s.getValue(marker.id));
                    break;
                case Marker.MARKER_TYPE_BBCH:
                    bah.getRlBBCH().setVisibility(View.VISIBLE);
                    bah.getIvBild().setVisibility(View.VISIBLE);
                    bah.getEtBbchWert().setText(s.getValue(marker.id));
                    bah.setBbchWert(s.getValue(marker.id));
                    break;
            }

            if(bah.markerIdPos.containsKey(marker.id))
            {
                bah.spMarkerCheck = 0;
                bah.getMarkerSpinner().setSelection(bah.markerIdPos.get(marker.id),false);
            }
        }
    }


    //---------- OnClick - Events-------------------------------------------------------------------
    public void onClick(final View v){
        switch (v.getId())
        {
            case R.id.btnNext:
            case R.id.btnNichtBewerten: bah.onNextMarkerOrStandort(); break;
            case R.id.btnPrev:          bah.onPrevMarkerOrStandort(); break;
            case R.id.btnPrevMarker:    bah.onPrevMarker();break;
            case R.id.btnNextMarker:    bah.onNextMarker(); break;
            case R.id.btnDatumHeute:    bah.setDatumHeute();  break;
            case R.id.btnDatumWaehlen:  new DatePicker(this); break;
            case R.id.btnDatumLoeschen: ((EditText) findViewById(R.id.etDatumEingabe)).setText(""); break;
            case R.id.btnSpeichenClose: new SpeichernDialog(this); break;
        }
    }
}
