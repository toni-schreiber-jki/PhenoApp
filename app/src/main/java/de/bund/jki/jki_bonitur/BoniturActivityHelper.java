package de.bund.jki.jki_bonitur;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.bund.jki.jki_bonitur.config.Config;
import de.bund.jki.jki_bonitur.db.Akzession;
import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.MarkerWert;
import de.bund.jki.jki_bonitur.db.Passport;
import de.bund.jki.jki_bonitur.db.Standort;
import de.bund.jki.jki_bonitur.db.VersuchWert;
import de.bund.jki.jki_bonitur.dialoge.BbchDialog;
import de.bund.jki.jki_bonitur.dialoge.ManyStandorteDialog;
import de.bund.jki.jki_bonitur.tools.BoniturTextWatcher;
import de.bund.jki.jki_bonitur.tools.DateTool;

/**
 * Created by toni.schreiber on 18.06.2015.
 */
public class BoniturActivityHelper {

    public  Integer[]                 markerIds;
    public  HashMap<Integer, Integer> markerIdPos;
    public  int                       spMarkerCheck      = 0;
    public  Integer[]                 akzessionsIds;
    public  HashMap<Integer, Integer> akzesionIdPos;
    public  int                       spAkzessionCheck   = 0;
    public  Integer[]                 passportIds;
    public  HashMap<Integer, Integer> passportIdPos;
    public  int                       spPassportCheck    = 0;
    public  int                       spBbchStatiumCheck = 0;
    public  int                       spBbchDetailCheck  = 0;
    public  int                       spParzelleCheck    = 0;
    public  String[]                  parzellenNr;
    public  HashMap<String, Integer>  parzellenNrPos;
    //-----------Ende zurück / vor Buttons ---------------------------------------------------------
    public  int                       spReiheCheck       = 0;
    public  int                       setSpReihePos      = - 1;
    public  int[]                     reihenNr;
    public  HashMap<Integer, Integer> reihenNrPos;
    public  HashMap<Integer, Integer> pflanzenPos;
    public  int[]                     pflanzen;
    public  int                       spPflanzenCheck    = 0;
    //-----------Ende Marker Spinner----------------------------------------------------------------
    public  int                       setSpPflanzenPos   = - 1;
    //-----------------Checkbox---------------------------------------------------------------------
    public  int                       cbMarkerCheck      = 1;
    public  MarkerWert[]              werte;
    private int                       gvSelected         = Color.parseColor("#ff80ff80");
    private int                       gvUnSelected       = Color.WHITE;
    private BoniturActivity           mBa;
    //--------------- Marker Spinner----------------------------------------------------------------
    private Spinner                   spMarker           = null;
    //--------------- Akzession Spinner-------------------------------------------------------------
    private Spinner                   spAkzession        = null;
    //-----------Ende Akzession Spinner-------------------------------------------------------------
    //--------------- Passport Spinner-------------------------------------------------------------
    private Spinner                   spPassport         = null;
    //----------------BBCH Stadien Spinner----------------------------------------------------------
    private Spinner                   spBbchStadium      = null;
    //----------------BBCH Detail Spinner-----------------------------------------------------------
    private Spinner                   spBbchDetail       = null;
    //-----------------Parzellen Spinner------------------------------------------------------------
    private Spinner                   spParzelle         = null;
    //-----------------Reihen Spinner---------------------------------------------------------------
    private Spinner                   spReihe            = null;
    //-----------------Pflanzen Spinner-------------------------------------------------------------
    private boolean                   setNewPflanzen     = false;
    private Spinner                   spPflanzen         = null;
    private CheckBox                  cbMarker           = null;
    //-----------Ende Passport Spinner--------------------------------------------------------------
    private boolean                   setCurrentPosition = false;
    //----------------ImageView---------------------------------------------------------------------
    private ImageView                 ivBild             = null;
    //----------------Werte-------------------------------------------------------------------------
    //----------------WerteEingabe------------------------------------------------------------------
    private GridView                  gvWerte            = null;
    private RelativeLayout            rlDatum            = null;
    //-----------Ende BBCH Stadien Spinner----------------------------------------------------------
    private RelativeLayout            rlBemerkung        = null;
    private RelativeLayout            rlMessen           = null;
    private RelativeLayout            rlBBCH             = null;
    private int                       setBbchDetail      = - 1;
    //------------Ende BBCH Detail Spinner----------------------------------------------------------
    //----------------Textfelder--------------------------------------------------------------------
    private EditText                  etDatumEingabe     = null;
    private EditText                  etBbchWert         = null;
    private EditText                  etBemerkungEingabe = null;
    private EditText                  etMessenEingabe    = null;
    private Button                    btnZzUnten         = null;
    private Button                    btnZzOben          = null;
    private Button                    btnZzLinks         = null;
    //------------Ende Parzellen Spinner------------------------------------------------------------
    private Button                    btnZzRechts        = null;

    public BoniturActivityHelper(BoniturActivity boniturActivity) {
        mBa = boniturActivity;
    }

    public void init_Spinner() {
        //Marker:
        getMarkerSpinner().setAdapter(getMarkerSpinnerAdpter());
        markerSpinnerAddSelectListener();

        //Akzession:
        getAkzessionSpinner().setAdapter(getAkzessionAdpter());
        akzessionSpinnerAddSelectListener();

        //Passport:
        getPassportSpinner().setAdapter(getPassportAdpter());
        passportSpinnerAddSelectListener();

        //BBCH:
        updateSpinner(getSpBbchStadium(), R.array.bbch_stadien);
        spBbchStadiumAddSelectListener();
        spBbchDetailAddSelectListener();

        //Pazellen:
        init_spParzelleValues();
        spParzelleAddSelectListener();

        //Reihe:
        spReiheAddSelectListener();

        //Pflanze:
        spPflanzenAddSelectListener();


    }

    public void init_typefaces() {
        Typeface typeface = Typeface.createFromAsset(mBa.getAssets(), "fonts/fontawesome.ttf");
        int[] buttonIds = new int[]{
                R.id.btnFoto,
                R.id.btnSpeichenClose,
                R.id.btnLeft,
                R.id.btnRight,
                R.id.btnDown,
                R.id.btnUp,
                R.id.btnRichtungLinks,
                R.id.btnRichtungOben,
                R.id.btnRichtungRechts,
                R.id.btnRichtungUnten,
                R.id.btnPrevMarker,
                R.id.btnNextMarker,
                R.id.btnSettings,
                R.id.btnFirstEmpty,
        };

        for (int id : buttonIds) {
            ((Button) mBa.findViewById(id)).setTypeface(typeface);
        }
    }

    public void init_TabFarbe() {
        int[] buttonIds = new int[]{
                R.id.llStandort,
                R.id.llAkzession,
                R.id.llPassport,
                R.id.llSorte,
                R.id.llEltern,
                R.id.llSortiment,
                R.id.llFreifeld,
                R.id.llEigenschaft,
                R.id.llBeschreibungKurz,
                R.id.tvBeschreibung,
        };

        int count = 0;

        for (int id : buttonIds) {
            View v = mBa.findViewById(id);
            if (v.getVisibility() == View.VISIBLE) {
                count++;
                v.setBackgroundResource(count % 2 == 0 ? R.color.row_n : R.color.row_n1);
            }
        }


    }

    public void init_textListener() {
        getEtBbchWert().addTextChangedListener(new BoniturTextWatcher(mBa));
        getEtDatumEingabe().addTextChangedListener(new BoniturTextWatcher(mBa));
        getEtBemerkungEingabe().addTextChangedListener(new BoniturTextWatcher(mBa));
        getEtMessenEingabe().addTextChangedListener(new BoniturTextWatcher(mBa));
    }

    public void init_checkBox() {
        cbMarkerAddSelectionListener();
    }

    //--------------- zurück / vor Buttons ---------------------------------------------------------
    public void onPrevMarkerOrStandort() {
        mBa.fillView(StandortManager.prev());
    }
    //------------Ende Reihen Spinner---------------------------------------------------------------

    public void onNextMarkerOrStandort() {
        mBa.fillView(StandortManager.next());
    }

    public void onPrevMarker() {
        BoniturSafe.MARKER_FILTER_ACTIVE = false;
        mBa.fillView(StandortManager.sameStandort(MarkerManager.prev()[0]));
        BoniturSafe.MARKER_FILTER_ACTIVE = true;
    }

    public void onNextMarker() {
        BoniturSafe.MARKER_FILTER_ACTIVE = false;
        mBa.fillView(StandortManager.sameStandort(MarkerManager.next()[0]));
        BoniturSafe.MARKER_FILTER_ACTIVE = true;
    }

    public void gotoStandort(Standort s) {
        mBa.fillView(StandortManager.gotoStandort(s.parzelle, s.reihe, s.pflanze, mBa.currentMarker));
    }

    public Spinner getMarkerSpinner() {
        if (spMarker != null) return spMarker;
        spMarker = mBa.findViewById(R.id.spMarker);
        return spMarker;
    }

    public ArrayAdapter getMarkerSpinnerAdpter() {
        try {
            List<String>       markerList   = new ArrayList<>();
            ArrayList<Integer> markerIdList = new ArrayList<>();
            markerIdPos = new HashMap<>();
            int p = 0;
            for (Marker m : MarkerManager.getAllMarker()) {
                markerList.add(m.code);
                markerIdList.add(m.id);
                markerIdPos.put(m.id, p);
                p++;
            }
            markerIds = markerIdList.toArray(new Integer[]{});
            return new ArrayAdapter<String>(mBa, R.layout.jki_simple_list_item_1, markerList) {
            };
        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplicationContext());
            return new ArrayAdapter<String>(mBa, R.layout.jki_simple_list_item_1, new ArrayList<String>());
        }
    }

    private void markerSpinnerAddSelectListener() {
        getMarkerSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    spMarkerCheck++;
                    if (spMarkerCheck > 1) {
                        Marker marker = Marker.findByPk(markerIds[position]);
                        BoniturSafe.CURRENT_MARKER = marker.id;
                        mBa.fillView(StandortManager.sameStandort(marker));
                    }
                } catch (Exception e) {
                    new ErrorLog(e, mBa.getApplicationContext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public Spinner getAkzessionSpinner() {
        if (spAkzession != null) return spAkzession;
        spAkzession = mBa.findViewById(R.id.spAkzession);
        return spAkzession;
    }

    private ArrayAdapter getAkzessionAdpter() {
        try {
            List<String>  akzessionList   = new ArrayList<>();
            List<Integer> akzessionNrList = new ArrayList<>();
            akzesionIdPos = new HashMap<>();
            akzessionNrList.add(- 1);
            akzessionList.add("---");
            akzesionIdPos.put(- 1, 0);
            int p = 1;
            for (Akzession a : StandortManager.getAllAkzessionen()) {
                akzesionIdPos.put(a.id, p);
                akzessionNrList.add(a.id);
                akzessionList.add(a.name + " (" + a.nummer + ")");
                p++;
            }
            akzessionsIds = akzessionNrList.toArray(new Integer[]{});
            return new ArrayAdapter<String>(mBa, R.layout.jki_simple_list_item_1, akzessionList) {
            };
        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplicationContext());
            return new ArrayAdapter<String>(mBa, R.layout.jki_simple_list_item_1, new ArrayList<String>());
        }
    }

    //------------Ende Pflanzen Spinner-------------------------------------------------------------

    private void akzessionSpinnerAddSelectListener() {
        getAkzessionSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    spAkzessionCheck++;
                    if (position > 0 && spAkzessionCheck > 1) {
                        Akzession a = Akzession.findByPk(akzessionsIds[position]);
                        gotoAkzession(a);
                    }
                } catch (Exception e) {
                    new ErrorLog(e, mBa.getApplicationContext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void gotoAkzession(Akzession akzession) {
        Cursor c = null;
        try {
            c = BoniturSafe.db.query(
                    Standort.TABLE_NAME,
                    new String[]{Standort.COLUMN_ID},
                    Standort.COLUMN_AKZESSION + "=?",
                    new String[]{akzession.id + ""},
                    null,
                    null,
                    Standort.COLUMN_PARZELLE + " ASC, " + Standort.COLUMN_REIHE + " ASC, " + Standort.COLUMN_PFLANZE + " ASC"
            );

            if (c.getCount() == 1) {
                c.moveToFirst();
                Standort s = Standort.findByPk(c.getInt(c.getColumnIndex(Standort.COLUMN_ID)));
                mBa.fillView(new Object[]{s, mBa.currentMarker});
            } else {
                Standort[] standorte = new Standort[c.getCount()];
                c.moveToFirst();
                int s = 0;
                do {
                    standorte[s] = Standort.findByPk(c.getInt(c.getColumnIndex(Standort.COLUMN_ID)));
                    s++;
                } while (c.moveToNext());

                if (c.getCount() > 0)
                    new ManyStandorteDialog(mBa, standorte);
            }

            //c.close();
        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplicationContext());
        } finally {
            if (c != null)
                c.close();
        }
    }

    public Spinner getPassportSpinner() {
        if (spPassport != null) return spPassport;
        spPassport = mBa.findViewById(R.id.spPassport);
        return spPassport;
    }

    private ArrayAdapter getPassportAdpter() {
        try {
            List<String>  passportList   = new ArrayList<>();
            List<Integer> passportNrList = new ArrayList<>();
            passportIdPos = new HashMap<>();
            passportNrList.add(- 1);
            passportList.add("---");
            passportIdPos.put(- 1, 0);
            int p = 1;
            for (Passport pp : StandortManager.getAllPassport()) {
                passportIdPos.put(pp.id, p);
                passportNrList.add(pp.id);
                passportList.add(pp.leitname + " (" + pp.kennNr + ")");
                p++;
            }
            passportIds = passportNrList.toArray(new Integer[]{});
            return new ArrayAdapter<String>(mBa, R.layout.jki_simple_list_item_1, passportList) {
            };
        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplicationContext());
            return new ArrayAdapter<String>(mBa, R.layout.jki_simple_list_item_1, new ArrayList<String>());
        }
    }

    private void passportSpinnerAddSelectListener() {
        getPassportSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    spPassportCheck++;
                    if (position > 0 && spPassportCheck > 1) {
                        Passport p = Passport.findByPk(passportIds[position]);
                        gotoPassport(p);
                    }
                } catch (Exception e) {
                    new ErrorLog(e, mBa.getApplicationContext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //------------Ende Checkbox---------------------------------------------------------------------

    private void gotoPassport(Passport passport) {
        Cursor c = null;
        try {
            c = BoniturSafe.db.query(
                    Standort.TABLE_NAME,
                    new String[]{Standort.COLUMN_ID},
                    Standort.COLUMN_PASSPORT + "=?",
                    new String[]{passport.id + ""},
                    null,
                    null,
                    Standort.COLUMN_PARZELLE + " ASC, " + Standort.COLUMN_REIHE + " ASC, " + Standort.COLUMN_PFLANZE + " ASC"
            );

            if (c.getCount() == 1) {
                c.moveToFirst();
                Standort s = Standort.findByPk(c.getInt(c.getColumnIndex(Standort.COLUMN_ID)));
                mBa.fillView(new Object[]{s, mBa.currentMarker});
            } else {
                Standort[] standorte = new Standort[c.getCount()];
                c.moveToFirst();
                int s = 0;
                do {
                    standorte[s] = Standort.findByPk(c.getInt(c.getColumnIndex(Standort.COLUMN_ID)));
                    s++;
                } while (c.moveToNext());

                if (c.getCount() > 0)
                    new ManyStandorteDialog(mBa, standorte);
            }

            //c.close();
        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplicationContext());
        } finally {
            if (c != null)
                c.close();
        }
    }

    public Spinner getSpBbchStadium() {
        if (spBbchStadium != null) return spBbchStadium;
        spBbchStadium = mBa.findViewById(R.id.spBbchStadium);
        return spBbchStadium;
    }

    private void spBbchStadiumAddSelectListener() {
        getSpBbchStadium().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    spBbchStatiumCheck++;
                    if (spBbchStatiumCheck > 1) {
                        int res      = 0;
                        int imageRes = 0;
                        switch (position) {
                            case 0:
                                return;
                            case 1:
                                res = R.array.bbch_stadien_details_0;
                                imageRes = R.drawable.bbch_0;
                                break;
                            case 2:
                                res = R.array.bbch_stadien_details_1;
                                imageRes = R.drawable.bbch_1;
                                break;
                            case 3:
                                res = R.array.bbch_stadien_details_5;
                                imageRes = R.drawable.bbch_5;
                                break;
                            case 4:
                                res = R.array.bbch_stadien_details_6;
                                imageRes = R.drawable.bbch_6;
                                break;
                            case 5:
                                res = R.array.bbch_stadien_details_7;
                                imageRes = R.drawable.bbch_7;
                                break;
                            case 6:
                                res = R.array.bbch_stadien_details_8;
                                imageRes = R.drawable.bbch_8;
                                break;
                            case 7:
                                res = R.array.bbch_stadien_details_9;
                                imageRes = R.drawable.bbch_9;
                                break;
                        }
                        getIvBild().setVisibility(View.GONE);
                        if (res == 0) return;
                        getIvBild().setVisibility(View.VISIBLE);
                        getIvBild().setImageResource(imageRes);
                        updateSpinner(getSpBbchDetail(), res);
                        if (setBbchDetail != - 1 && setBbchDetail != getSpBbchDetail().getSelectedItemPosition()) {
                            if (getSpBbchDetail().getCount() < setBbchDetail)
                                return;
                            spBbchDetailCheck = 0;
                            getSpBbchDetail().setSelection(setBbchDetail, false);
                        } else {
                            spBbchStatiumCheck++;
                            spBbchDetailCheck++;
                        }
                    }
                } catch (Exception e) {
                    new ErrorLog(e, mBa.getApplicationContext());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public Spinner getSpBbchDetail() {
        if (spBbchDetail != null) return spBbchDetail;
        spBbchDetail = mBa.findViewById(R.id.spBbchDetail);
        return spBbchDetail;
    }

    //-----------Ende ImageView---------------------------------------------------------------------

    private void spBbchDetailAddSelectListener() {
        getSpBbchDetail().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    spBbchDetailCheck++;
                    if (spBbchDetailCheck > 1) {
                        String text = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                        //if(ignoreBbchChange) return;
                        if (text.contains(":")) {
                            ((EditText) mBa.findViewById(R.id.etBbchWert)).setText(text.split(":")[0]);
                            //ignoreBbchChange = true;
                        }
                    }
                } catch (Exception e) {
                    new ErrorLog(e, mBa.getApplicationContext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public Spinner getSpParzelle() {
        if (spParzelle != null) return spParzelle;
        spParzelle = mBa.findViewById(R.id.spParzelle);
        return spParzelle;
    }

    private void spParzelleAddSelectListener() {
        getSpParzelle().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    spParzelleCheck++;
                    if (spParzelleCheck > 1) {
                        setNewPflanzen = true;
                        updateSpReihenValues(parzellenNr[position]);
                    }
                } catch (Exception e) {
                    new ErrorLog(e, mBa.getApplicationContext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void init_spParzelleValues() {
        Cursor c = null;
        try {
            List<String> parzellenList = new ArrayList<>();
            c = BoniturSafe.db.query(
                    true,
                    Standort.TABLE_NAME,
                    new String[]{Standort.COLUMN_PARZELLE},
                    Standort.COLUMN_VERSUCH + "=?",
                    new String[]{"" + BoniturSafe.VERSUCH_ID},
                    null,
                    null,
                    Standort.COLUMN_PARZELLE + "*1 ASC, " + Standort.COLUMN_PARZELLE + " ASC",
                    null
            );
            if (c.getCount() > 0) {
                parzellenNrPos = new HashMap<>();
                parzellenNr = new String[c.getCount()];
                c.moveToNext();
                int p = 0;
                do {
                    parzellenNr[p] = c.getString(c.getColumnIndex(Standort.COLUMN_PARZELLE));
                    parzellenNrPos.put(parzellenNr[p], p);
                    parzellenList.add(parzellenNr[p]);
                    p++;
                } while (c.moveToNext());
                getSpParzelle().setAdapter(new ArrayAdapter<String>(mBa, R.layout.jki_simple_list_item_1_top, parzellenList) {
                });
            }

            //c.close();
        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplicationContext());
        } finally {
            if (c != null)
                c.close();
        }
    }

    public Spinner getSpReihe() {
        if (spReihe != null) return spReihe;
        spReihe = mBa.findViewById(R.id.spReihe);
        return spReihe;
    }

    private void updateSpReihenValues(String parzelle) {
        Cursor c = null;
        try {
            List<String> reihenList = new ArrayList<>();
            c = BoniturSafe.db.query(
                    true,
                    Standort.TABLE_NAME,
                    new String[]{Standort.COLUMN_REIHE},
                    Standort.COLUMN_VERSUCH + "=? AND " + Standort.COLUMN_PARZELLE + " = ?",
                    new String[]{"" + BoniturSafe.VERSUCH_ID, parzelle},
                    null,
                    null,
                    Standort.COLUMN_REIHE + " ASC",
                    null
            );
            if (c.getCount() > 0) {
                reihenNrPos = new HashMap<>();
                reihenNr = new int[c.getCount()];
                c.moveToNext();
                int p = 0;
                do {
                    reihenNr[p] = c.getInt(c.getColumnIndex(Standort.COLUMN_REIHE));
                    reihenNrPos.put(reihenNr[p], p);
                    reihenList.add("" + reihenNr[p]);
                    p++;
                } while (c.moveToNext());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mBa, R.layout.jki_simple_list_item_1_top, reihenList) {};
                getSpReihe().setAdapter(adapter);
                if (setCurrentPosition) {
                    spReiheCheck = 0;
                    getSpReihe().setSelection(reihenNrPos.get(BoniturSafe.CURRENT_REIHE), false);
                    updateSpPflanzenValues(BoniturSafe.CURRENT_REIHE);
                }
                if (setNewPflanzen) {
                    updateSpPflanzenValues(reihenNr[0]);
                }
            }
        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplication());
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
    }

    private void spReiheAddSelectListener() {
        getSpReihe().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    spReiheCheck++;
                    if (spReiheCheck > 1) {
                        updateSpPflanzenValues(reihenNr[position]);
                    }
                } catch (Exception e) {
                    new ErrorLog(e, mBa.getApplicationContext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public Spinner getSpPflanzen() {
        if (spPflanzen != null) return spPflanzen;
        spPflanzen = mBa.findViewById(R.id.spPflanze);
        return spPflanzen;
    }

    private void updateSpPflanzenValues(int reihe) {
        Cursor c = null;
        try {
            List<String> pflanzenList = new ArrayList<>();
            c = BoniturSafe.db.query(
                    true,
                    Standort.TABLE_NAME,
                    new String[]{Standort.COLUMN_PFLANZE},
                    Standort.COLUMN_VERSUCH + "=? AND " + Standort.COLUMN_PARZELLE + " = ? AND " + Standort.COLUMN_REIHE + "=?",
                    new String[]{"" + BoniturSafe.VERSUCH_ID, parzellenNr[getSpParzelle().getSelectedItemPosition()], "" + reihe},
                    null,
                    null,
                    Standort.COLUMN_PFLANZE + " ASC",
                    null
            );
            if (c.getCount() > 0) {
                pflanzenPos = new HashMap<>();
                pflanzen = new int[c.getCount()];
                c.moveToNext();
                int p = 0;
                do {
                    pflanzen[p] = c.getInt(c.getColumnIndex(Standort.COLUMN_PFLANZE));
                    pflanzenPos.put(pflanzen[p], p);
                    pflanzenList.add("" + pflanzen[p]);
                    p++;
                } while (c.moveToNext());
                getSpPflanzen().setAdapter(new ArrayAdapter<String>(mBa, R.layout.jki_simple_list_item_1_top, pflanzenList) {
                });
                if (setCurrentPosition) {
                    spPflanzenCheck = 0;
                    getSpPflanzen().setSelection(pflanzenPos.get(BoniturSafe.CURRENT_PFLANZE), false);
                    setCurrentPosition = false;
                }
            }

            //c.close();
        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplication());
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
    }

    private void spPflanzenAddSelectListener() {
        getSpPflanzen().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    spPflanzenCheck++;
                    if (spPflanzenCheck > 1) {
                        mBa.fillView(StandortManager.gotoStandort(
                                parzellenNr[getSpParzelle().getSelectedItemPosition()],
                                reihenNr[getSpReihe().getSelectedItemPosition()],
                                pflanzen[getSpPflanzen().getSelectedItemPosition()],
                                mBa.currentMarker
                                     )
                        );
                    }
                    setNewPflanzen = false;
                    setCurrentPosition = false;
                } catch (Exception e) {
                    new ErrorLog(e, mBa.getApplicationContext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public CheckBox getCbMarker() {
        if (cbMarker != null) return cbMarker;
        cbMarker = mBa.findViewById(R.id.cbMarker);
        return cbMarker;
    }

    private void cbMarkerAddSelectionListener() {
        getCbMarker().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbMarkerCheck++;
                if (cbMarkerCheck > 1) {
                    if (isChecked) {
                        BoniturSafe.MARKER_FILTER.add(new Integer(BoniturSafe.CURRENT_MARKER));
                    } else {
                        BoniturSafe.MARKER_FILTER.remove(new Integer(BoniturSafe.CURRENT_MARKER));
                    }
                }
            }
        });
    }

    public void setCbMarkerValue(boolean value) {
        if (value == getCbMarker().isChecked())
            return;
        cbMarkerCheck = 0;
        getCbMarker().setChecked(value);
    }

    public void setNewPosition() {
        try {
            if (parzellenNr[getSpParzelle().getSelectedItemPosition()].compareTo(BoniturSafe.CURRENT_PARZELLE) == 0) {
                if (getSpReihe().getCount() > 0) {
                    if (reihenNr[getSpReihe().getSelectedItemPosition()] == BoniturSafe.CURRENT_REIHE) {
                        if (pflanzen[getSpPflanzen().getSelectedItemPosition()] == BoniturSafe.CURRENT_PFLANZE) {

                        } else {
                            spPflanzenCheck = 0;
                            getSpPflanzen().setSelection(pflanzenPos.get(BoniturSafe.CURRENT_PFLANZE), false);
                        }
                    } else {
                        spReiheCheck = spPflanzenCheck = 0;
                        setCurrentPosition = true;
                        getSpReihe().setSelection(reihenNrPos.get(BoniturSafe.CURRENT_REIHE), false);
                        updateSpPflanzenValues(BoniturSafe.CURRENT_REIHE);
                    }
                } else {
                    spReiheCheck = spPflanzenCheck = 0;
                    setCurrentPosition = true;
                    getSpParzelle().setSelection(parzellenNrPos.get(BoniturSafe.CURRENT_PARZELLE), false);
                    updateSpReihenValues(BoniturSafe.CURRENT_PARZELLE);
                }
            } else {
                spParzelleCheck = spReiheCheck = spPflanzenCheck = 0;
                setCurrentPosition = true;
                getSpParzelle().setSelection(parzellenNrPos.get(BoniturSafe.CURRENT_PARZELLE), false);
                updateSpReihenValues(BoniturSafe.CURRENT_PARZELLE);
            }
        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplicationContext());
        }
    }

    public ImageView getIvBild() {
        if (ivBild != null) return ivBild;
        ivBild = mBa.findViewById(R.id.ivBild);
        return ivBild;
    }

    public GridView getGvWerte() {
        if (gvWerte != null) return gvWerte;
        gvWerte = mBa.findViewById(R.id.gvWerte);
        return gvWerte;
    }

    public RelativeLayout getRlDatum() {
        if (rlDatum != null) return rlDatum;
        rlDatum = mBa.findViewById(R.id.rlDatum);
        return rlDatum;
    }

    public RelativeLayout getRlBemerkung() {
        if (rlBemerkung != null) return rlBemerkung;
        rlBemerkung = mBa.findViewById(R.id.rlBemerkung);
        return rlBemerkung;
    }

    public RelativeLayout getRlMessen() {
        if (rlMessen != null) return rlMessen;
        rlMessen = mBa.findViewById(R.id.rlMessen);
        return rlMessen;
    }

    public RelativeLayout getRlBBCH() {
        if (rlBBCH != null) return rlBBCH;
        rlBBCH = mBa.findViewById(R.id.rlBBCH);
        return rlBBCH;
    }

    public void hideAllEingabeTypen() {
        getGvWerte().setVisibility(View.GONE);
        getRlDatum().setVisibility(View.GONE);
        getRlBemerkung().setVisibility(View.GONE);
        getRlMessen().setVisibility(View.GONE);
        getRlBBCH().setVisibility(View.GONE);
        getIvBild().setVisibility(View.GONE);
    }

    public void fillGridView(final MarkerWert[] werte) {
        try {
            this.werte = werte;
            ArrayAdapter arrayAdapter = new ArrayAdapter(mBa, android.R.layout.simple_list_item_2, android.R.id.text1, werte) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    Cursor c = null;
                    try {
                        View view = super.getView(position, convertView, parent);

                        ((TextView) view.findViewById(android.R.id.text1)).setText(werte[position].value);
                        ((TextView) view.findViewById(android.R.id.text2)).setText(werte[position].label);

                        String   where    = VersuchWert.COLUMN_VERSUCH + "=? AND " + VersuchWert.COLUMN_STANDORT + "=? AND " + VersuchWert.COLUMN_MARKER + "=? AND " + VersuchWert.COLUMN_WERT_ID + "=?";
                        String[] whereArg = new String[]{"" + BoniturSafe.VERSUCH_ID, "" + BoniturSafe.CURRENT_STANDORT_ID, "" + mBa.currentMarker.id, "" + werte[position].id};

                        c = BoniturSafe.db.query(
                                VersuchWert.TABLE_NAME, new String[]{VersuchWert.COLUMN_ID},
                                where, whereArg, null, null, null
                        );

                        if (c.getCount() > 0)
                            view.setBackgroundColor(gvSelected);
                        else
                            view.setBackgroundColor(gvUnSelected);

                        //c.close();

                        return view;
                    } catch (Exception e) {
                        new ErrorLog(e, mBa.getApplication());
                    } finally {
                        if (c != null)
                            c.close();
                    }
                    return null;
                }
            };

            getGvWerte().setAdapter(arrayAdapter);
            gvWerteAddOnClickListener();
        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplicationContext());
        }
    }

    private void updateSpinner(Spinner spin, int res) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mBa, res, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        //spin.setSelection(Adapter.NO_SELECTION, false);
        //adapter.notifyDataSetChanged();
    }

    public void setBbchWert(String wert) {
        spBbchStatiumCheck++;
        spBbchDetailCheck++;
        try {
            boolean setNull = false;
            if (wert == null)
                setNull = true;
            else {
                if (wert.length() == 0) {
                    setNull = true;
                }
            }

            if (setNull) {
                if (getSpBbchStadium().getSelectedItemPosition() == 0) {
                    spBbchStatiumCheck++;
                    return;
                }

                spBbchStatiumCheck = 0;
                setBbchDetail = 0;
                getSpBbchStadium().setSelection(0, false);
                spBbchDetailCheck = 0;
                getSpBbchDetail().setSelection(0, false);
            } else {
                int bbch              = Integer.parseInt(wert.replaceAll("^0", ""));
                int setSpBbchStatdium = 0;
                switch (bbch / 10) {
                    case 0:
                        switch (bbch % 10) {
                            case 0:
                                setBbchDetail = 1;
                                break;
                            case 1:
                                setBbchDetail = 2;
                                break;
                            case 3:
                                setBbchDetail = 3;
                                break;
                            case 5:
                                setBbchDetail = 4;
                                break;
                            case 7:
                                setBbchDetail = 5;
                                break;
                            case 9:
                                setBbchDetail = 6;
                                break;
                        }
                        setSpBbchStatdium = 1;
                        break;
                    case 1:
                        setBbchDetail = bbch % 10;
                        setSpBbchStatdium = 2;
                        break;
                    case 5:
                        switch (bbch % 10) {
                            case 3:
                                setBbchDetail = 1;
                                break;
                            case 5:
                                setBbchDetail = 2;
                                break;
                            case 7:
                                setBbchDetail = 3;
                                break;
                        }
                        setSpBbchStatdium = 3;
                        break;
                    case 6:
                        setBbchDetail = (bbch % 10) + 1;
                        setSpBbchStatdium = 4;
                        break;
                    case 7:
                        switch (bbch % 10) {
                            case 1:
                                setBbchDetail = 1;
                                break;
                            case 3:
                                setBbchDetail = 2;
                                break;
                            case 5:
                                setBbchDetail = 3;
                                break;
                            case 7:
                                setBbchDetail = 4;
                                break;
                            case 9:
                                setBbchDetail = 5;
                                break;
                        }
                        setSpBbchStatdium = 5;
                        break;
                    case 8:
                        switch (bbch % 10) {
                            case 1:
                                setBbchDetail = 1;
                                break;
                            case 3:
                                setBbchDetail = 2;
                                break;
                            case 5:
                                setBbchDetail = 3;
                                break;
                            case 9:
                                setBbchDetail = 4;
                                break;
                        }
                        setSpBbchStatdium = 6;
                        break;
                    case 9:
                        switch (bbch % 10) {
                            case 1:
                                setBbchDetail = 1;
                                break;
                            case 2:
                                setBbchDetail = 2;
                                break;
                            case 3:
                                setBbchDetail = 3;
                                break;
                            case 5:
                                setBbchDetail = 4;
                                break;
                            case 7:
                                setBbchDetail = 5;
                                break;
                            case 9:
                                setBbchDetail = 6;
                                break;
                        }
                        setSpBbchStatdium = 7;
                        break;
                }
                if (getSpBbchStadium().getSelectedItemPosition() == setSpBbchStatdium)
                    setSpBbchDetailSelection(setBbchDetail);
                else
                    setSpBbchStadiumSelection(setSpBbchStatdium);

            }
        } catch (Exception e) {
            getSpBbchStadium().setSelection(0);
            getSpBbchDetail().setSelection(0);
        }
    }

    private void setSpBbchStadiumSelection(int selection) {
        if (getSpBbchStadium().getSelectedItemPosition() == selection)
            spBbchStatiumCheck = 1;
        else {
            spBbchStatiumCheck = 1;
            getSpBbchStadium().setSelection(selection, true);
        }
    }

    private void setSpBbchDetailSelection(int selection) {
        if (getSpBbchDetail().getSelectedItemPosition() == selection)
            spBbchDetailCheck = 1;
        else {
            spBbchDetailCheck = 0;
            getSpBbchDetail().setSelection(selection, false);
        }
    }
    //-----------Ende Textfelder--------------------------------------------------------------------


    //----------Ende: WerteEingabe------------------------------------------------------------------

    public EditText getEtDatumEingabe() {
        if (etDatumEingabe != null) return etDatumEingabe;
        etDatumEingabe = mBa.findViewById(R.id.etDatumEingabe);
        return etDatumEingabe;
    }

    public EditText getEtBbchWert() {
        if (etBbchWert != null) return etBbchWert;
        etBbchWert = mBa.findViewById(R.id.etBbchWert);
        return etBbchWert;
    }

    public EditText getEtBemerkungEingabe() {
        if (etBemerkungEingabe != null) return etBemerkungEingabe;
        etBemerkungEingabe = mBa.findViewById(R.id.etBemerkungEingabe);
        return etBemerkungEingabe;
    }

    //----------Ende: WerteSpeichern----------------------------------------------------------------

    public EditText getEtMessenEingabe() {
        if (etMessenEingabe != null) return etMessenEingabe;
        etMessenEingabe = mBa.findViewById(R.id.etMessenEingabe);
        return etMessenEingabe;
    }
    //----------Ende: Datum-------------------------------------------------------------------------

    //----------Ende: Werte-------------------------------------------------------------------------


    //----------------ZickZackModus-----------------------------------------------------------------

    //----------------WerteSpeichern----------------------------------------------------------------
    public void saveValue() {
        try {
            switch (mBa.currentMarker.type) {
                case Marker.MARKER_TYPE_BONITUR: //im gvWerteAddOnClickListener implementiert
                    break;
                case Marker.MARKER_TYPE_MESSEN:
                    try {
                        writeValue(VersuchWert.COLUMN_WERT_INT, Integer.parseInt("" + getEtMessenEingabe().getText()));
                    } catch (NumberFormatException e) {
                        //ToDo: implement Fehlermeldung "NUR ZAHLEN ERLAUBT!"
                        writeValue(VersuchWert.COLUMN_WERT_INT, 0);
                    }
                    break;
                case Marker.MARKER_TYPE_DATUM:
                    writeValue(VersuchWert.COLUMN_WERT_TEXT, getEtDatumEingabe().getText().toString());
                    break;
                case Marker.MARKER_TYPE_BEMERKUNG:
                    writeValue(VersuchWert.COLUMN_WERT_TEXT, getEtBemerkungEingabe().getText().toString());
                    break;
                case Marker.MARKER_TYPE_BBCH:
                    writeValue(VersuchWert.COLUMN_WERT_TEXT, getEtBbchWert().getText().toString());
                    break;
            }
        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplicationContext());
        }
    }

    private void writeValue(String column, Object wert) {
        try {
            BoniturSafe.db.delete(
                    VersuchWert.TABLE_NAME,
                    VersuchWert.COLUMN_VERSUCH + "=? AND " + VersuchWert.COLUMN_MARKER + "=? AND " + VersuchWert.COLUMN_STANDORT + "=?",
                    new String[]{"" + BoniturSafe.VERSUCH_ID, "" + BoniturSafe.CURRENT_MARKER, "" + BoniturSafe.CURRENT_STANDORT_ID}
            );

            ContentValues values = new ContentValues();
            values.put(VersuchWert.COLUMN_VERSUCH, BoniturSafe.VERSUCH_ID);
            values.put(VersuchWert.COLUMN_MARKER, BoniturSafe.CURRENT_MARKER);
            values.put(VersuchWert.COLUMN_STANDORT, BoniturSafe.CURRENT_STANDORT_ID);
            values.put(VersuchWert.COLUMN_WERT_DATUM, DateTool.currentDate());

            if (wert instanceof String)
                values.put(column, wert.toString());
            else
                values.put(column, (int) wert);

            BoniturSafe.db.insert(VersuchWert.TABLE_NAME, null, values);
        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplicationContext());
        }
    }

    public void gvWerteAddOnClickListener() {
        getGvWerte().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = null;
                try {

                    String   where    = VersuchWert.COLUMN_VERSUCH + "=? AND " + VersuchWert.COLUMN_STANDORT + "=? AND " + VersuchWert.COLUMN_MARKER + "=? AND " + VersuchWert.COLUMN_WERT_ID + "=?";
                    String[] whereArg = new String[]{"" + BoniturSafe.VERSUCH_ID, "" + BoniturSafe.CURRENT_STANDORT_ID, "" + mBa.currentMarker.id, "" + werte[position].id};

                    c = BoniturSafe.db.query(
                            VersuchWert.TABLE_NAME, new String[]{VersuchWert.COLUMN_ID},
                            where, whereArg, null, null, null
                    );

                    if (c.getCount() == 0) {
                        view.setBackgroundColor(gvSelected);
                        ContentValues values = new ContentValues();
                        values.put(VersuchWert.COLUMN_VERSUCH, BoniturSafe.VERSUCH_ID);
                        values.put(VersuchWert.COLUMN_MARKER, BoniturSafe.CURRENT_MARKER);
                        values.put(VersuchWert.COLUMN_STANDORT, BoniturSafe.CURRENT_STANDORT_ID);
                        values.put(VersuchWert.COLUMN_WERT_ID, werte[position].id);
                        values.put(VersuchWert.COLUMN_WERT_DATUM, DateTool.currentDate());
                        BoniturSafe.db.insert(VersuchWert.TABLE_NAME, null, values);

                        if (! mBa.is_multiple_value) {
                            onNextMarkerOrStandort();
                        }
                    } else {
                        view.setBackgroundColor(gvUnSelected);
                        BoniturSafe.db.delete(
                                VersuchWert.TABLE_NAME, where, whereArg);
                    }

                    //c.close();
                } catch (Exception e) {
                    new ErrorLog(e, mBa.getApplicationContext());
                } finally {
                    if (c != null)
                        c.close();
                }

            }
        });
    }

    //----------------Datum-------------------------------------------------------------------------
    public void setDatumDiff(int diff) {
        try {
            Calendar         c        = Calendar.getInstance();
            SimpleDateFormat df       = new SimpleDateFormat("dd.MM.yyyy");
            String           datum    = df.format(new Date(c.getTimeInMillis() + diff * 24 * 60 * 60 * 1000));
            EditText         editText = getEtDatumEingabe();
            editText.setText(datum);

            if (! mBa.is_multiple_value) {
                onNextMarkerOrStandort();
            }
        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplication());
            e.printStackTrace();
        }
    }

    public Button getBtnZzUnten() {
        if (btnZzUnten != null) return btnZzUnten;
        btnZzUnten = mBa.findViewById(R.id.btnRichtungUnten);
        return btnZzUnten;
    }

    public Button getBtnZzOben() {
        if (btnZzOben != null) return btnZzOben;
        btnZzOben = mBa.findViewById(R.id.btnRichtungOben);
        return btnZzOben;
    }

    public Button getBtnZzLinks() {
        if (btnZzLinks != null) return btnZzLinks;
        btnZzLinks = mBa.findViewById(R.id.btnRichtungLinks);
        return btnZzLinks;
    }

    public Button getBtnZzRechts() {
        if (btnZzRechts != null) return btnZzRechts;
        btnZzRechts = mBa.findViewById(R.id.btnRichtungRechts);
        return btnZzRechts;
    }

    public void updateZickZackRichtung() {
        getBtnZzLinks().setVisibility(BoniturSafe.PFLANZEN_RICHTUNG == StandortManager.PREV ? View.VISIBLE : View.GONE);
        getBtnZzRechts().setVisibility(BoniturSafe.PFLANZEN_RICHTUNG == StandortManager.NEXT ? View.VISIBLE : View.GONE);
        getBtnZzOben().setVisibility(BoniturSafe.REIHEN_RICHTUNG == StandortManager.PREV ? View.VISIBLE : View.GONE);
        getBtnZzUnten().setVisibility(BoniturSafe.REIHEN_RICHTUNG == StandortManager.NEXT ? View.VISIBLE : View.GONE);
    }
    //-----------Ende ZickZackModus-----------------------------------------------------------------

    //---------------First Empty--------------------------------------------------------------------
    public void gotoFirstEmpty() {
        try {
            mBa.fillView(StandortManager.gotoFirstEmpty());
        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplicationContext());
        }
    }

    //----------Ende First Empty--------------------------------------------------------------------
    public void createFoto() {
        try {
            // Create an image file name
            String timeStamp     = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
            String imageFileName = mBa.currentStandort.getName() + "_" + timeStamp;

            String preName = "";
            if (mBa.currentStandort.zuchtstamm != null) {
                if (mBa.currentStandort.zuchtstamm.compareTo("") != 0 && mBa.currentStandort.zuchtstamm.compareTo("null") != 0) {
                    preName += mBa.currentStandort.zuchtstamm + "_";
                }
            }

            if (mBa.currentStandort.sorte != null) {
                if (mBa.currentStandort.sorte.compareTo("") != 0 && mBa.currentStandort.sorte.compareTo("null") != 0) {
                    preName += mBa.currentStandort.sorte + "_";
                }
            }

            imageFileName = preName + imageFileName;

            File storageDir = new File(Environment.getExternalStorageDirectory().toString() + Config.BaseFolder + "/fotos/");

            if (! storageDir.exists()) {
                storageDir.mkdir();
            }

            File image = new File(Environment.getExternalStorageDirectory().toString() + Config.BaseFolder + "/fotos/" + imageFileName + ".jpg");
            //image.createNewFile();

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(mBa.getPackageManager()) != null) {
                // Create the File where the photo should go
                // Continue only if the File was successfully created
                if (image != null) {
                    if (Build.VERSION.SDK_INT < 24) {
                        takePictureIntent.putExtra(
                                MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(image)
                        );
                    } else {
                        Uri photoURI = FileProvider.getUriForFile(
                                mBa,
                                "de.bund.jki.jki_bonitur.fileprovider",
                                image
                        );

                        takePictureIntent.putExtra(
                                MediaStore.EXTRA_OUTPUT,
                                photoURI
                        );
                    }
                    mBa.startActivityForResult(takePictureIntent, 1);
                }
            }


        } catch (Exception e) {
            new ErrorLog(e, mBa.getApplication());
            e.printStackTrace();
        }
    }

    public void openStandortInformation() {
        StandortInformationActivity.mStandort = mBa.currentStandort;
        Intent i = new Intent(mBa, StandortInformationActivity.class);
        mBa.startActivity(i);
    }

    public void showBild() {
        String    code = mBa.currentMarker.code;
        ImageView iv   = getIvBild();
        if (new File(Environment.getExternalStorageDirectory().toString() + Config.BaseFolder + "/boniturBilder/" + code + ".JPG").exists()) {
            iv.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + Config.BaseFolder + "/boniturBilder/" + code + ".JPG"));
            iv.setVisibility(View.VISIBLE);
        } else {
            iv.setVisibility(View.GONE);
        }
    }

    private void setVisibilityBildHintergrund(int visible) {
        int[] ids = new int[]{
                R.id.llInfos,
                R.id.rlPfadenkreuz,
                R.id.btnStandortInfo
        };

        for (int id : ids) {
            mBa.findViewById(id).setVisibility(visible);
        }
    }

    public void showBildGross() {
        String    code = mBa.currentMarker.code;
        ImageView iv   = mBa.findViewById(R.id.ivBildGross);
        if (new File(Environment.getExternalStorageDirectory().toString() + Config.BaseFolder + "/boniturBilder/" + code + ".JPG").exists()) {
            iv.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + Config.BaseFolder + "/boniturBilder/" + code + ".JPG"));
            iv.setVisibility(View.VISIBLE);
            setVisibilityBildHintergrund(View.GONE);
        } else {
            iv.setVisibility(View.GONE);
            setVisibilityBildHintergrund(View.VISIBLE);
        }
    }

    public void closeShowBildGross() {
        mBa.findViewById(R.id.ivBildGross).setVisibility(View.GONE);
        setVisibilityBildHintergrund(View.VISIBLE);
    }

    public void askNewBbch() {
        Calendar         c      = Calendar.getInstance();
        SimpleDateFormat df     = new SimpleDateFormat("ddMMyy");
        Marker[]         marker = MarkerManager.getAllMarker();
        for (int m = 0; m < marker.length; m++) {
            if (marker[m].code.equals("BBCH " + df.format(c.getTime())))
                return;
        }
        new BbchDialog(mBa, "BBCH " + df.format(c.getTime()));
    }

}
