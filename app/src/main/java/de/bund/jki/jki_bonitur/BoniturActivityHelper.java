package de.bund.jki.jki_bonitur;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.bund.jki.jki_bonitur.db.Akzession;
import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.MarkerWert;
import de.bund.jki.jki_bonitur.db.Passport;
import de.bund.jki.jki_bonitur.db.Standort;
import de.bund.jki.jki_bonitur.db.Versuch;
import de.bund.jki.jki_bonitur.db.VersuchWert;
import de.bund.jki.jki_bonitur.tools.BoniturTextWatcher;

/**
 * Created by toni.schreiber on 18.06.2015.
 */
public class BoniturActivityHelper {

    private int gvSelected  = Color.parseColor("#ff80ff80");
    private int gvUnSelected= Color.WHITE;

    private BoniturActivity mBa;

    public BoniturActivityHelper(BoniturActivity boniturActivity)
    {
        mBa = boniturActivity;
    }

    public void init_Spinner(){
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
        updateSpinner(getSpBbchStadium(),R.array.bbch_stadien);
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
        Typeface typeface = Typeface.createFromAsset(mBa.getAssets(),"fonts/fontawesome.ttf");
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
        };

        for (int id: buttonIds) {
            ((Button) mBa.findViewById(id)).setTypeface(typeface);
        }
    }

    public void init_textListener() {
        getEtBbchWert().addTextChangedListener(new BoniturTextWatcher(mBa));
        getEtDatumEingabe().addTextChangedListener(new BoniturTextWatcher(mBa));
        getEtBemerkungEingabe().addTextChangedListener(new BoniturTextWatcher(mBa));
        getEtMessenEingabe().addTextChangedListener(new BoniturTextWatcher(mBa));
    }

    //--------------- zurück / vor Buttons ---------------------------------------------------------
    public void onPrevMarkerOrStandort(){
        mBa.fillView(StandortManager.prev());
    }

    public void onNextMarkerOrStandort(){
        mBa.fillView(StandortManager.next());
    }

    public void onPrevMarker() {
        mBa.fillView(StandortManager.sameStandort(MarkerManager.prev()[0]));
    }

    public void onNextMarker(){
        mBa.fillView(StandortManager.sameStandort(MarkerManager.next()[0]));
    }
    //-----------Ende zurück / vor Buttons ---------------------------------------------------------





    //--------------- Marker Spinner----------------------------------------------------------------
    private Spinner spMarker = null;
    public Integer[] markerIds;
    public HashMap<Integer, Integer> markerIdPos;
    public int spMarkerCheck = 0;
    public Spinner getMarkerSpinner(){
        if(spMarker != null) return spMarker;
        spMarker = (Spinner) mBa.findViewById(R.id.spMarker);
        return  spMarker;
    }
    private ArrayAdapter getMarkerSpinnerAdpter(){
        List<String> markerList = new ArrayList<>();
        ArrayList<Integer> markerIdList = new ArrayList<>();
        markerIdPos = new HashMap<>();
        int p = 0;
        for (Marker m: MarkerManager.getAllMarker())
        {
            markerList.add(m.code);
            markerIdList.add(m.id);
            markerIdPos.put(m.id,p);
            p++;
        }
        markerIds =  markerIdList.toArray(new Integer[]{});
        return new ArrayAdapter<String>(mBa,android.R.layout.simple_list_item_1, markerList){};
    }
    private void markerSpinnerAddSelectListener() {
        getMarkerSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spMarkerCheck++;
                if(spMarkerCheck > 1)
                {
                    Marker marker = Marker.findByPk(markerIds[position]);
                    BoniturSafe.CURRENT_MARKER = marker.id;
                    mBa.fillView(StandortManager.sameStandort(marker));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //-----------Ende Marker Spinner----------------------------------------------------------------

    //--------------- Akzession Spinner-------------------------------------------------------------
    private Spinner spAkzession = null;
    public Integer[] akzessionsIds;
    public HashMap<Integer, Integer> akzesionIdPos;
    public int spAkzessionCheck = 0;
    public Spinner getAkzessionSpinner(){
        if(spAkzession != null) return spAkzession;
        spAkzession = (Spinner) mBa.findViewById(R.id.spAkzession);
        return  spAkzession;
    }
    private ArrayAdapter getAkzessionAdpter(){
        List<String> akzessionList = new ArrayList<>();
        List<Integer> akzessionNrList = new ArrayList<>();
        akzesionIdPos = new HashMap<>();
        akzessionNrList.add(-1);
        akzessionList.add("---");
        akzesionIdPos.put(-1,0);
        int p = 1;
        for (Akzession a: StandortManager.getAllAkzessionen())
        {
            akzesionIdPos.put(a.id,p);
            akzessionNrList.add(a.id);
            akzessionList.add(a.name + " ("+a.nummer+")");
            p++;
        }
        akzessionsIds = akzessionNrList.toArray(new Integer[]{});
        return new ArrayAdapter<String>(mBa,android.R.layout.simple_list_item_1, akzessionList){};
    }
    private void akzessionSpinnerAddSelectListener(){
        getAkzessionSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spAkzessionCheck++;
                if(position > 0 && spAkzessionCheck > 1) {
                    Akzession a = Akzession.findByPk(akzessionsIds[position]);
                    gotoAkzession(a);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void gotoAkzession(Akzession akzession)
    {
        //Todo: Logik für Akzessionswechsel
    }
    //-----------Ende Akzession Spinner-------------------------------------------------------------

    //--------------- Passport Spinner-------------------------------------------------------------
    private Spinner spPassport = null;
    public Integer[] passportIds;
    public HashMap<Integer, Integer> passportIdPos;
    public int spPassportCheck = 0;
    public Spinner getPassportSpinner(){
        if(spPassport != null) return spPassport;
        spPassport = (Spinner) mBa.findViewById(R.id.spPassport);
        return  spPassport;
    }
    private ArrayAdapter getPassportAdpter(){
        List<String> passportList = new ArrayList<>();
        List<Integer> passportNrList = new ArrayList<>();
        passportIdPos = new HashMap<>();
        passportNrList.add(-1);
        passportList.add("---");
        passportIdPos.put(-1,0);
        int p = 1;
        for (Passport pp: StandortManager.getAllPassport())
        {
            passportIdPos.put(pp.id,p);
            passportNrList.add(pp.id);
            passportList.add(pp.leitname + " ("+pp.kennNr+")");
            p++;
        }
        passportIds = passportNrList.toArray(new Integer[]{});
        return new ArrayAdapter<String>(mBa,android.R.layout.simple_list_item_1, passportList){};
    }
    private void passportSpinnerAddSelectListener(){
        getAkzessionSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spAkzessionCheck++;
                if(position > 0 && spAkzessionCheck > 1) {
                    Akzession a = Akzession.findByPk(akzessionsIds[position]);
                    gotoAkzession(a);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void gotoPassport(Passport passport){
        //Todo: Logik für Akzessionswechsel
    }
    //-----------Ende Passport Spinner--------------------------------------------------------------

    //----------------BBCH Stadien Spinner----------------------------------------------------------
    private Spinner spBbchStadium = null;
    public int spBbchStatiumCheck = 0;
    public Spinner getSpBbchStadium() {
        if(spBbchStadium != null) return  spBbchStadium;
        spBbchStadium = (Spinner) mBa.findViewById(R.id.spBbchStadium);
        return  spBbchStadium;
    }
    private void spBbchStadiumAddSelectListener(){
        getSpBbchStadium().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spBbchStatiumCheck++;
                if(spBbchStatiumCheck > 1)
                {
                    int res = 0;
                    int imageRes = 0;
                    switch (position){
                        case 0: return;
                        case 1:
                            res = R.array.bbch_stadien_details_0;
                            imageRes = R.drawable.bbch_0; break;
                        case 2:
                            res = R.array.bbch_stadien_details_1;
                            imageRes = R.drawable.bbch_1; break;
                        case 3:
                            res = R.array.bbch_stadien_details_5;
                            imageRes = R.drawable.bbch_5; break;
                        case 4:
                            res = R.array.bbch_stadien_details_6;
                            imageRes = R.drawable.bbch_6; break;
                        case 5:
                            res = R.array.bbch_stadien_details_7;
                            imageRes = R.drawable.bbch_7; break;
                        case 6:
                            res = R.array.bbch_stadien_details_8;
                            imageRes = R.drawable.bbch_8; break;
                        case 7:
                            res = R.array.bbch_stadien_details_9;
                            imageRes = R.drawable.bbch_9; break;
                    }

                    if(res == 0 ) return;
                    getIvBild().setImageResource(imageRes);
                    updateSpinner(getSpBbchDetail(), res);
                    if(setBbchDetail != -1 && setBbchDetail != getSpBbchDetail().getSelectedItemPosition()){
                        if(getSpBbchDetail().getCount() < setBbchDetail)
                           return;
                        spBbchDetailCheck = 0;
                        getSpBbchDetail().setSelection(setBbchDetail, false);
                    }
                    else
                    {
                        spBbchStatiumCheck++;
                        spBbchDetailCheck++;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //-----------Ende BBCH Stadien Spinner----------------------------------------------------------

    //----------------BBCH Detail Spinner-----------------------------------------------------------
    private Spinner spBbchDetail = null;
    public int spBbchDetailCheck = 0;
    public Spinner getSpBbchDetail() {
        if(spBbchDetail != null) return  spBbchDetail;
        spBbchDetail = (Spinner) mBa.findViewById(R.id.spBbchDetail);
        return  spBbchDetail;
    }
    private void spBbchDetailAddSelectListener(){
        getSpBbchDetail().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spBbchDetailCheck++;
                if(spBbchDetailCheck > 1)
                {
                    String text = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                    //if(ignoreBbchChange) return;
                    if(text.contains(":"))
                    {
                        ((EditText) mBa.findViewById(R.id.etBbchWert)).setText(text.split(":")[0]);
                        //ignoreBbchChange = true;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //------------Ende BBCH Detail Spinner----------------------------------------------------------

    //-----------------Parzellen Spinner------------------------------------------------------------
    private Spinner spParzelle = null;
    public int spParzelleCheck = 0;
    public String[] parzellenNr;
    public HashMap<String, Integer> parzellenNrPos;
    public Spinner getSpParzelle(){
        if(spParzelle != null) return spParzelle;
        spParzelle = (Spinner) mBa.findViewById(R.id.spParzelle);
        return spParzelle;
    }
    private void spParzelleAddSelectListener(){
        getSpParzelle().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spParzelleCheck ++;
                if(spParzelleCheck > 1)
                {
                    setNewPflanzen = true;
                    updateSpReihenValues(parzellenNr[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void init_spParzelleValues() {
        List<String> parzellenList = new ArrayList<>();
        Cursor c = BoniturSafe.db.query(true,
                Standort.TABLE_NAME,
                new String[] {Standort.COLUMN_PARZELLE},
                Standort.COLUMN_VERSUCH+"=?",
                new String[] {""+BoniturSafe.VERSUCH_ID},
                null,
                null,
                Standort.COLUMN_PARZELLE + " ASC",
                null);
        if(c.getCount()>0){
            parzellenNrPos = new HashMap<>();
            parzellenNr = new String [c.getCount()];
            c.moveToNext();
            int p = 0;
            do{
                parzellenNr[p] = c.getString(c.getColumnIndex(Standort.COLUMN_PARZELLE));
                parzellenNrPos.put(parzellenNr[p], p);
                parzellenList.add(parzellenNr[p]);
                p++;
            }while (c.moveToNext());
            getSpParzelle().setAdapter(new ArrayAdapter<String>(mBa,android.R.layout.simple_list_item_1, parzellenList){});
        }
    }
    //------------Ende Parzellen Spinner------------------------------------------------------------

    //-----------------Reihen Spinner---------------------------------------------------------------
    private Spinner spReihe = null;
    public int spReiheCheck = 0;
    public int setSpReihePos = -1;
    public int[] reihenNr;
    public HashMap<Integer, Integer> reihenNrPos;
    public Spinner getSpReihe(){
        if(spReihe != null) return spReihe;
        spReihe = (Spinner) mBa.findViewById(R.id.spReihe);
        return spReihe;
    }
    private void updateSpReihenValues(String parzelle) {
        try {
            List<String> reihenList = new ArrayList<>();
            Cursor c = BoniturSafe.db.query(true,
                    Standort.TABLE_NAME,
                    new String[]{Standort.COLUMN_REIHE},
                    Standort.COLUMN_VERSUCH + "=? AND " + Standort.COLUMN_PARZELLE + " = ?",
                    new String[]{"" + BoniturSafe.VERSUCH_ID, parzelle},
                    null,
                    null,
                    Standort.COLUMN_REIHE + " ASC",
                    null);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mBa, android.R.layout.simple_list_item_1, reihenList) {};
                getSpReihe().setAdapter(adapter);
                if(setCurrentPosition){
                    spReiheCheck = 0;
                    getSpReihe().setSelection(reihenNrPos.get(BoniturSafe.CURRENT_REIHE),false);
                    updateSpPflanzenValues(BoniturSafe.CURRENT_REIHE);
                }
                if(setNewPflanzen)
                {
                    updateSpPflanzenValues(reihenNr[0]);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void spReiheAddSelectListener(){
        getSpReihe().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spReiheCheck++;
                if(spReiheCheck > 1)
                {
                    updateSpPflanzenValues(reihenNr[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //------------Ende Reihen Spinner---------------------------------------------------------------

    //-----------------Pflanzen Spinner-------------------------------------------------------------
    private boolean setNewPflanzen = false;
    private Spinner spPflanzen = null;
    public HashMap<Integer, Integer> pflanzenPos;
    public int[] pflanzen;
    public int spPflanzenCheck = 0;
    public int setSpPflanzenPos = -1;
    public Spinner getSpPflanzen(){
        if(spPflanzen != null) return spPflanzen;
        spPflanzen = (Spinner) mBa.findViewById(R.id.spPflanze);
        return spPflanzen;
    }
    private void updateSpPflanzenValues(int reihe){
        try {
            List<String> pflanzenList = new ArrayList<>();
            Cursor c = BoniturSafe.db.query(true,
                    Standort.TABLE_NAME,
                    new String[]{Standort.COLUMN_PFLANZE},
                    Standort.COLUMN_VERSUCH + "=? AND " + Standort.COLUMN_PARZELLE + " = ? AND " + Standort.COLUMN_REIHE + "=?",
                    new String[]{"" + BoniturSafe.VERSUCH_ID, parzellenNr[getSpParzelle().getSelectedItemPosition()], "" + reihe},
                    null,
                    null,
                    Standort.COLUMN_PFLANZE + " ASC",
                    null);
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
                getSpPflanzen().setAdapter(new ArrayAdapter<String>(mBa, android.R.layout.simple_list_item_1, pflanzenList) {
                });
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void spPflanzenAddSelectListener(){
        getSpPflanzen().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spPflanzenCheck ++;
                if(spPflanzenCheck > 1)
                {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //------------Ende Pflanzen Spinner-------------------------------------------------------------


    private boolean setCurrentPosition = false;
    public void setNewPosition() {
        if (parzellenNr[getSpParzelle().getSelectedItemPosition()].compareTo(BoniturSafe.CURRENT_PARZELLE)==0) {
            if (getSpReihe().getCount() > 0) {
                if(reihenNr[getSpReihe().getSelectedItemPosition()]==BoniturSafe.CURRENT_REIHE)
                {
                    if(pflanzen[getSpPflanzen().getSelectedItemPosition()]==BoniturSafe.CURRENT_PFLANZE)
                    {

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
        }
        else {
            spParzelleCheck = spReiheCheck = spPflanzenCheck =0;
            setCurrentPosition = true;
            getSpParzelle().setSelection(parzellenNrPos.get(BoniturSafe.CURRENT_PARZELLE), false);
            updateSpReihenValues(BoniturSafe.CURRENT_PARZELLE);
        }
    }

    //----------------ImageView---------------------------------------------------------------------
    private ImageView ivBild = null;
    public ImageView getIvBild(){
        if(ivBild != null) return ivBild;
        ivBild = (ImageView) mBa.findViewById(R.id.ivBild);
        return ivBild;
    }

    //-----------Ende ImageView---------------------------------------------------------------------


    //----------------Werte-------------------------------------------------------------------------
    //----------------WerteEingabe------------------------------------------------------------------
    private GridView gvWerte = null;
    public GridView getGvWerte(){
        if(gvWerte != null) return gvWerte;
        gvWerte = (GridView) mBa.findViewById(R.id.gvWerte);
        return gvWerte;
    }

    private RelativeLayout rlDatum = null;
    public RelativeLayout getRlDatum(){
        if(rlDatum != null) return rlDatum;
        rlDatum = (RelativeLayout) mBa.findViewById(R.id.rlDatum);
        return rlDatum;
    }

    private RelativeLayout rlBemerkung = null;
    public RelativeLayout getRlBemerkung(){
        if(rlBemerkung != null) return rlBemerkung;
        rlBemerkung = (RelativeLayout) mBa.findViewById(R.id.rlBemerkung);
        return rlBemerkung;
    }

    private RelativeLayout rlMessen = null;
    public RelativeLayout getRlMessen(){
        if(rlMessen != null) return rlMessen;
        rlMessen = (RelativeLayout) mBa.findViewById(R.id.rlMessen);
        return rlMessen;
    }

    private RelativeLayout rlBBCH = null;
    public RelativeLayout getRlBBCH(){
        if(rlBBCH != null) return rlBBCH;
        rlBBCH = (RelativeLayout) mBa.findViewById(R.id.rlBBCH);
        return rlBBCH;
    }

    public void hideAllEingabeTypen(){
        getGvWerte().setVisibility(View.GONE);
        getRlDatum().setVisibility(View.GONE);
        getRlBemerkung().setVisibility(View.GONE);
        getRlMessen().setVisibility(View.GONE);
        getRlBBCH().setVisibility(View.GONE);
        getIvBild().setVisibility(View.GONE);
    }

    public MarkerWert[] werte;
    public void fillGridView(final MarkerWert[] werte){
        this.werte = werte;
        ArrayAdapter arrayAdapter = new ArrayAdapter (mBa, android.R.layout.simple_list_item_2, android.R.id.text1, werte){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                try {
                    View view = super.getView(position, convertView, parent);

                    ((TextView) view.findViewById(android.R.id.text1)).setText(werte[position].value);
                    ((TextView) view.findViewById(android.R.id.text2)).setText(werte[position].label);

                    String where = VersuchWert.COLUMN_VERSUCH+"=? AND "+ VersuchWert.COLUMN_STANDORT+"=? AND "+VersuchWert.COLUMN_MARKER+"=? AND "+VersuchWert.COLUMN_WERT_ID+"=?";
                    String[] whereArg = new String[] {""+BoniturSafe.VERSUCH_ID, ""+BoniturSafe.CURRENT_STANDORT_ID, ""+mBa.currentMarker.id, ""+werte[position].id};

                    Cursor c = BoniturSafe.db.query(
                            VersuchWert.TABLE_NAME,new String[] {VersuchWert.COLUMN_ID},
                            where, whereArg, null,null,null);

                    if(c.getCount() > 0)
                        view.setBackgroundColor(gvSelected);
                    else
                        view.setBackgroundColor(gvUnSelected);

                    return view;
                }catch (Exception e)
                {
                    int a = 1;
                }
                return null;
            }
        };

        getGvWerte().setAdapter(arrayAdapter);
        gvWerteAddOnClickListener();
    }

    private void updateSpinner(Spinner spin, int res){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mBa,res,android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        //spin.setSelection(Adapter.NO_SELECTION, false);
        //adapter.notifyDataSetChanged();
    }

    private int setBbchDetail = -1;
    public void setBbchWert(String wert){
        spBbchStatiumCheck++;
        spBbchDetailCheck++;
        try{
            boolean setNull = false;
            if(wert == null)
                setNull = true;
            else
            {
                if(wert.length() == 0)
                {
                    setNull = true;
                }
            }

            if(setNull)
            {
                if(getSpBbchStadium().getSelectedItemPosition() == 0) {
                    spBbchStatiumCheck++;
                    return;
                }

                spBbchStatiumCheck = 0;
                setBbchDetail = 0;
                getSpBbchStadium().setSelection(0,false);
                spBbchDetailCheck = 0;
                getSpBbchDetail().setSelection(0,false);
            }
            else {
                int bbch = Integer.parseInt(wert.replaceAll("^0", ""));
                int setSpBbchStatdium = 0;
                switch (bbch / 10) {
                    case 0:
                        switch (bbch%10) {
                            case 0: setBbchDetail = 1; break;
                            case 1: setBbchDetail = 2; break;
                            case 3: setBbchDetail = 3; break;
                            case 5: setBbchDetail = 4; break;
                            case 7: setBbchDetail = 5; break;
                            case 9: setBbchDetail = 6; break;
                        }
                        setSpBbchStatdium = 1;
                        break;
                    case 1:
                        setBbchDetail = bbch%10;
                        setSpBbchStatdium = 2;
                        break;
                    case 5:
                        switch (bbch%10)
                        {
                            case 3: setBbchDetail = 1; break;
                            case 5: setBbchDetail = 2; break;
                            case 7: setBbchDetail = 3; break;
                        }
                        setSpBbchStatdium = 3;
                        break;
                    case 6:
                        setBbchDetail = (bbch%10)+1;
                        setSpBbchStatdium = 4;
                        break;
                    case 7:
                        switch (bbch%10)
                        {
                            case 1: setBbchDetail = 1; break;
                            case 3: setBbchDetail = 2; break;
                            case 5: setBbchDetail = 3; break;
                            case 7: setBbchDetail = 4; break;
                            case 9: setBbchDetail = 5; break;
                        }
                        setSpBbchStatdium = 5;
                        break;
                    case 8:
                        switch (bbch%10)
                        {
                            case 1: setBbchDetail = 1; break;
                            case 3: setBbchDetail = 2; break;
                            case 5: setBbchDetail = 3; break;
                            case 9: setBbchDetail = 4; break;
                        }
                        setSpBbchStatdium = 6;
                        break;
                    case 9:
                        switch (bbch%10)
                        {
                            case 1: setBbchDetail = 1; break;
                            case 2: setBbchDetail = 2; break;
                            case 3: setBbchDetail = 3; break;
                            case 5: setBbchDetail = 4; break;
                            case 7: setBbchDetail = 5; break;
                            case 9: setBbchDetail = 6; break;
                        }
                        setSpBbchStatdium = 7;
                        break;
                }
                if(getSpBbchStadium().getSelectedItemPosition() == setSpBbchStatdium)
                    setSpBbchDetailSelection(setBbchDetail);
                else
                    setSpBbchStadiumSelection(setSpBbchStatdium);

            }
        }catch (Exception e){
            getSpBbchStadium().setSelection(0);
            getSpBbchDetail().setSelection(0);
        }
    }

    private void setSpBbchStadiumSelection(int selection){
        if(getSpBbchStadium().getSelectedItemPosition()==selection)
            spBbchStatiumCheck = 1;
        else {
            spBbchStatiumCheck = 1;
            getSpBbchStadium().setSelection(selection, true);
        }
    }
    private void setSpBbchDetailSelection(int selection){
        if(getSpBbchDetail().getSelectedItemPosition()==selection)
            spBbchDetailCheck = 1;
        else {
            spBbchDetailCheck = 0;
            getSpBbchDetail().setSelection(selection, false);
        }
    }

    //----------------Textfelder--------------------------------------------------------------------
    private EditText etDatumEingabe = null;
    public EditText getEtDatumEingabe(){
        if(etDatumEingabe != null) return  etDatumEingabe;
        etDatumEingabe = (EditText) mBa.findViewById(R.id.etDatumEingabe);
        return etDatumEingabe;
    }

    private EditText etBbchWert = null;
    public EditText getEtBbchWert(){
        if(etBbchWert != null) return  etBbchWert;
        etBbchWert = (EditText) mBa.findViewById(R.id.etBbchWert);
        return etBbchWert;
    }

    private EditText etBemerkungEingabe = null;
    public EditText getEtBemerkungEingabe(){
        if(etBemerkungEingabe != null) return  etBemerkungEingabe;
        etBemerkungEingabe = (EditText) mBa.findViewById(R.id.etBemerkungEingabe);
        return etBemerkungEingabe;
    }

    private EditText etMessenEingabe = null;
    public EditText getEtMessenEingabe(){
        if(etMessenEingabe != null) return  etMessenEingabe;
        etMessenEingabe = (EditText) mBa.findViewById(R.id.etMessenEingabe);
        return etMessenEingabe;
    }
    //-----------Ende Textfelder--------------------------------------------------------------------


    //----------Ende: WerteEingabe------------------------------------------------------------------

    //----------------WerteSpeichern----------------------------------------------------------------
    public void saveValue() {
        switch (mBa.currentMarker.type)
        {
            case Marker.MARKER_TYPE_BONITUR: //im gvWerteAddOnClickListener implementiert
                break;
            case Marker.MARKER_TYPE_MESSEN:
                try {
                    writeValue(VersuchWert.COLUMN_WERT_INT, Integer.parseInt("" + getEtMessenEingabe().getText()));
                }catch (NumberFormatException e){
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
    }

    private void writeValue(String column, Object wert)
    {
        BoniturSafe.db.delete(
                VersuchWert.TABLE_NAME,
                VersuchWert.COLUMN_VERSUCH+"=? AND "+VersuchWert.COLUMN_MARKER+"=? AND "+VersuchWert.COLUMN_STANDORT+"=?",
                new String[] {""+BoniturSafe.VERSUCH_ID, ""+BoniturSafe.CURRENT_MARKER, ""+BoniturSafe.CURRENT_STANDORT_ID});

        ContentValues values = new ContentValues();
        values.put(VersuchWert.COLUMN_VERSUCH, BoniturSafe.VERSUCH_ID);
        values.put(VersuchWert.COLUMN_MARKER, BoniturSafe.CURRENT_MARKER);
        values.put(VersuchWert.COLUMN_STANDORT, BoniturSafe.CURRENT_STANDORT_ID);

        if(wert instanceof String)
            values.put(column, wert.toString());
        else
            values.put(column, (int) wert);

        BoniturSafe.db.insert(VersuchWert.TABLE_NAME,null,values);
    }

    public void gvWerteAddOnClickListener(){
        getGvWerte().setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String where = VersuchWert.COLUMN_VERSUCH+"=? AND "+ VersuchWert.COLUMN_STANDORT+"=? AND "+VersuchWert.COLUMN_MARKER+"=? AND "+VersuchWert.COLUMN_WERT_ID+"=?";
                String[] whereArg = new String[] {""+BoniturSafe.VERSUCH_ID, ""+BoniturSafe.CURRENT_STANDORT_ID, ""+mBa.currentMarker.id, ""+werte[position].id};

                Cursor c = BoniturSafe.db.query(
                        VersuchWert.TABLE_NAME,new String[] {VersuchWert.COLUMN_ID},
                        where, whereArg, null,null,null);

                if(c.getCount() == 0){
                    view.setBackgroundColor(gvSelected);
                    ContentValues values = new ContentValues();
                    values.put(VersuchWert.COLUMN_VERSUCH, BoniturSafe.VERSUCH_ID);
                    values.put(VersuchWert.COLUMN_MARKER, BoniturSafe.CURRENT_MARKER);
                    values.put(VersuchWert.COLUMN_STANDORT, BoniturSafe.CURRENT_STANDORT_ID);
                    values.put(VersuchWert.COLUMN_WERT_ID, werte[position].id);
                    BoniturSafe.db.insert(VersuchWert.TABLE_NAME,null,values);
                }
                else{
                    view.setBackgroundColor(gvUnSelected);
                    BoniturSafe.db.delete(
                            VersuchWert.TABLE_NAME, where, whereArg);
                }

            }
        });
    }

    //----------Ende: WerteSpeichern----------------------------------------------------------------

    //----------------Datum-------------------------------------------------------------------------
    public void setDatumHeute(){
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            String datum = df.format(c.getTime());
            EditText editText = getEtDatumEingabe();
            editText.setText(datum);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //----------Ende: Datum-------------------------------------------------------------------------

    //----------Ende: Werte-------------------------------------------------------------------------
}
