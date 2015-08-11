package de.bund.jki.jki_bonitur;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.bund.jki.jki_bonitur.config.Config;
import de.bund.jki.jki_bonitur.db.BoniturDatenbank;
import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.Standort;
import de.bund.jki.jki_bonitur.dialoge.DatePicker;
import de.bund.jki.jki_bonitur.dialoge.SettingsDialog;
import de.bund.jki.jki_bonitur.dialoge.SpeichernDialog;
import de.bund.jki.jki_bonitur.excel.Reader;


public class BoniturActivity extends Activity {

    public static String RELOAD_VIEW    = "de.bund.jki.bonitur.reload";
    public static String ERROR_INTENT   = "de.bund.jki.bunitur.error";

    private BoniturDatenbank bonDb;
    public BoniturActivityHelper bah;

    public Marker currentMarker;
    public Standort currentStandort;

    public BoniturIntentReceiver bir = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonitur);
        bir = new BoniturIntentReceiver();
        IntentFilter filter = new IntentFilter(RELOAD_VIEW);
        filter.addAction(ERROR_INTENT);
        try {
            this.registerReceiver(bir, filter);
        }catch (Exception e){
            new ErrorLog(e,this);
        }
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(bir);
    }

    public void init() {
        try {
            //ToDo: Neuer BBCH WERT
            //ToDo: option BBCH_WERT abfragen + Button
            bonDb = new BoniturDatenbank(this);
            bonDb.copy_to_sd();
            BoniturSafe.db = bonDb.getWritableDatabase();

            BoniturSafe.APP_CONTEXT = getApplicationContext();
            BoniturSafe.BON_ACTIVITY = this;

            bah = new BoniturActivityHelper(this);

            Reader reader = new Reader(getIntent().getStringExtra("FILE"));
            if(reader.read()) {

                BoniturSafe.CURRENT_PARZELLE = "-";
                BoniturSafe.CURRENT_REIHE = -1;
                BoniturSafe.CURRENT_PFLANZE = -1;

                BoniturSafe.CURRENT_MARKER = -1;

                BoniturSafe.MARKER_FILTER = new ArrayList<>();

                bah.init_Spinner();
                bah.init_typefaces();
                bah.init_textListener();
                bah.init_checkBox();
                bah.init_TabFarbe();

                Config.load(this);
                if(Config.SHOW_BBCH_FRAGE) {
                    bah.askNewBbch();
                }

                ((TextView) findViewById(R.id.tvDocument)).setText("Datei: " + BoniturSafe.VERSUCH_NAME);

                loadSettings();

                fillView(StandortManager.next());
            }
        } catch (Exception e) {
            new ErrorLog(e,getApplicationContext());
        }
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
        try {
            Standort s = null;

            if (daten[0] == null || daten[1] == null) {
                //ToDo: Fehlermeldung
            }

            if (daten[0] != null) {
                s = (Standort) daten[0];
                currentStandort = s;
                ((TextView) findViewById(R.id.tvStandort)).setText(s.getName());
                ((TextView) findViewById(R.id.tvSorteZucht)).setText(((Standort) daten[0]).sorte + " / " + ((Standort) daten[0]).zuchtstamm);
                ((TextView) findViewById(R.id.tvEltern)).setText(((Standort) daten[0]).mutter + " / " + ((Standort) daten[0]).vater);
                ((TextView) findViewById(R.id.tvSortiment)).setText(((Standort) daten[0]).sortimentsnummer);
                ((TextView) findViewById(R.id.tvFreifeld)).setText(s.freifeld);

                if (bah.akzesionIdPos.containsKey(s.akzessionId)) {
                    if (bah.akzesionIdPos.get(s.akzessionId) != bah.getAkzessionSpinner().getSelectedItemPosition()) {
                        bah.spAkzessionCheck = 0;
                        bah.getAkzessionSpinner().setSelection(bah.akzesionIdPos.get(s.akzessionId), false);
                    } else {
                        bah.spAkzessionCheck++;
                    }
                }

                if (bah.passportIdPos.containsKey(s.passportId)) {
                    if (bah.passportIdPos.get(s.passportId) != bah.getPassportSpinner().getSelectedItemPosition()) {
                        bah.spPassportCheck = 0;
                        bah.getPassportSpinner().setSelection(bah.passportIdPos.get(s.passportId), false);
                    } else {
                        bah.spPassportCheck++;
                    }
                }
            }

            if (daten[1] != null) {
                Marker marker = (Marker) daten[1];
                currentMarker = marker;

                bah.setCbMarkerValue(BoniturSafe.MARKER_FILTER.contains(new Integer(marker.id)));

                ((TextView) findViewById(R.id.tvMerkmal)).setText(marker.name);
                ((TextView) findViewById(R.id.tvBeschreibung)).setText(marker.beschreibung);

                bah.hideAllEingabeTypen();

                try {
                    switch (marker.type) {
                        case Marker.MARKER_TYPE_BONITUR:
                            bah.getGvWerte().setVisibility(View.VISIBLE);
                            bah.fillGridView(marker.werte);
                            bah.showBild();
                            break;
                        case Marker.MARKER_TYPE_DATUM:
                            bah.getRlDatum().setVisibility(View.VISIBLE);
                            bah.getEtDatumEingabe().setText(s.getValue(marker.id));
                            bah.showBild();
                            break;
                        case Marker.MARKER_TYPE_MESSEN:
                            bah.getRlMessen().setVisibility(View.VISIBLE);
                            bah.getEtMessenEingabe().setText(s.getValue(marker.id));
                            bah.showBild();
                            break;
                        case Marker.MARKER_TYPE_BEMERKUNG:
                            bah.getRlBemerkung().setVisibility(View.VISIBLE);
                            bah.getEtBemerkungEingabe().setText(s.getValue(marker.id));
                            bah.showBild();
                            break;
                        case Marker.MARKER_TYPE_BBCH:
                            bah.getRlBBCH().setVisibility(View.VISIBLE);
                            bah.getIvBild().setVisibility(View.VISIBLE);
                            bah.getEtBbchWert().setText(s.getValue(marker.id));
                            bah.setBbchWert(s.getValue(marker.id));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new ErrorLog(e,getApplication());
                }

                bah.setNewPosition();

                bah.updateZickZackRichtung();

                if (bah.markerIdPos.containsKey(marker.id)) {
                    if (bah.getMarkerSpinner().getSelectedItemPosition() != bah.markerIdPos.get(marker.id)) {
                        bah.spMarkerCheck = 0;
                        bah.getMarkerSpinner().setSelection(bah.markerIdPos.get(marker.id), false);
                    } else {
                        bah.spMarkerCheck = 1;
                    }
                }
            }
            //String a = null;
            //a.charAt(2);
        }catch (Exception e){
            new ErrorLog(e, getApplicationContext());
        }
    }


    //---------- OnClick - Events-------------------------------------------------------------------
    public void onClick(final View v){
        try{
            switch (v.getId()){
                case R.id.btnNext:
                case R.id.btnNichtBewerten: bah.onNextMarkerOrStandort(); break;
                case R.id.btnPrev:          bah.onPrevMarkerOrStandort(); break;
                case R.id.btnPrevMarker:    bah.onPrevMarker();break;
                case R.id.btnNextMarker:    bah.onNextMarker(); break;
                case R.id.btnDatumHeute:    bah.setDatumHeute();  break;
                case R.id.btnDatumWaehlen:  new DatePicker(this); break;
                case R.id.btnDatumLoeschen: ((EditText) findViewById(R.id.etDatumEingabe)).setText(""); break;
                case R.id.btnSpeichenClose: new SpeichernDialog(this); break;
                case R.id.btnSettings:      new SettingsDialog(this); break;
                case R.id.btnRichtungLinks:
                case R.id.btnRichtungRechts:StandortManager.changeRichtung(StandortManager.PFLANZEN_RICHTUNG); fillView(new Object[] {currentStandort, currentMarker}); break;
                case R.id.btnRichtungOben:
                case R.id.btnRichtungUnten: StandortManager.changeRichtung(StandortManager.REIHEN_RICHTUNG); fillView(new Object[] {currentStandort, currentMarker}); break;
                case R.id.btnFirstEmpty:    bah.gotoFirstEmpty(); break;
                case R.id.btnRight:         fillView(StandortManager.nextStandort(StandortManager.NEXT, currentMarker)); break;
                case R.id.btnLeft:          fillView(StandortManager.nextStandort(StandortManager.PREV,currentMarker)); break;
                case R.id.btnUp:            fillView(StandortManager.nextReihe(StandortManager.PREV, currentMarker)); break;
                case R.id.btnDown:          fillView(StandortManager.nextReihe(StandortManager.NEXT,currentMarker)); break;
                case R.id.btnFoto:          bah.createFoto(); break;
                case R.id.btnStandortInfo:  bah.openStandortInformation();break;
                case R.id.ivBild:           bah.showBildGross(); break;
                case R.id.ivBildGross:      bah.closeShowBildGross(); break;

            }
        }catch (Exception e){
            new ErrorLog(e,getApplicationContext());
        }
    }

    public void loadSettings(){
        try {
            Config.load(this);
            if (Config.ZICK_ZACK_MODUS) {
                findViewById(R.id.llZickZackRichtung).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.llZickZackRichtung).setVisibility(View.GONE);
            }

            findViewById(R.id.btnFirstEmpty).setVisibility(Config.FIRST_EMPTY ? View.VISIBLE : View.GONE);

            findViewById(R.id.llEltern).setVisibility(Config.SHOW_ELTERN ? View.VISIBLE : View.GONE);

            findViewById(R.id.llAkzession).setVisibility(Config.SHOW_AKZESSION ? View.VISIBLE : View.GONE);
            findViewById(R.id.llPassport).setVisibility(Config.SHOW_PASSPORT ? View.VISIBLE : View.GONE);
            findViewById(R.id.llSorte).setVisibility(Config.SHOW_SORTE ? View.VISIBLE : View.GONE);
            findViewById(R.id.llSortiment).setVisibility(Config.SHOW_SORTIMENT ? View.VISIBLE : View.GONE);
            bah.init_TabFarbe();
        }catch (Exception e){
            new ErrorLog(e,getApplicationContext());
        }
    }

    public class BoniturIntentReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().compareTo(RELOAD_VIEW)==0) {
                init();
            }

            if(intent.getAction().compareTo(ERROR_INTENT)==0){
                Toast.makeText(context,intent.getStringExtra("TEXT"),Toast.LENGTH_LONG);
            }
        }
    }

}
