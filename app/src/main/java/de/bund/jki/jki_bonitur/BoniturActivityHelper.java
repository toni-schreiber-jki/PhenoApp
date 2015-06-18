package de.bund.jki.jki_bonitur;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.MarkerWert;
import de.bund.jki.jki_bonitur.db.Versuch;
import de.bund.jki.jki_bonitur.db.VersuchWert;

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
        getMarkerSpinner().setAdapter(getMarkerSpinnerAdpter());
        markerSpinnerAddSelectListener();
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
    private Spinner getMarkerAkzession(){
        if(spAkzession != null) return spAkzession;
        spAkzession = (Spinner) mBa.findViewById(R.id.spAkzession);
        return  spAkzession;
    }
    private ArrayAdapter getMarkerAkzessionAdpter(){
        //TODO: add Logik
        /*List<String> markerList = new ArrayList<>();
        for (Marker m: MarkerManager.getAllMarker())
        {
            markerList.add(m.code);
        }
        return new ArrayAdapter<String>(mBa,android.R.layout.simple_list_item_1, markerList){};*/
        return null;
    }
    private void akzessionSpinnerAddSelectListener(){
        getMarkerAkzession().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO: add Logik
                /*if (ba.ignoreEigenschaftChange) {
                    ba.spEigenschaften.setSelection(ba.bonitur.cur_ovi);
                    ba.ignoreEigenschaftChange = false;
                    return;
                }
                ba.bonitur.cur_ovi = position;
                ba.bonActions.saveValue();
                fillForm(ba.bonitur.getBoniturDaten());*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //-----------Ende Akzession Spinner-------------------------------------------------------------


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

    public void hideAllEingabeTypen(){
        getGvWerte().setVisibility(View.GONE);
        getRlDatum().setVisibility(View.GONE);
        getRlBemerkung().setVisibility(View.GONE);
        getRlMessen().setVisibility(View.GONE);
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
    //----------Ende: WerteEingabe------------------------------------------------------------------

    //----------------WerteSpeichern----------------------------------------------------------------
    public void saveValue() {
        //ToDo: SpeicherLogik erstellen
        switch (mBa.currentMarker.type)
        {
            case Marker.MARKER_TYPE_BONITUR: //im gvWerteAddOnClickListener implementiert
                break;
            case Marker.MARKER_TYPE_MESSEN:
                break;
            case Marker.MARKER_TYPE_DATUM:
                break;
            case Marker.MARKER_TYPE_BEMERKUNG:
                break;
            case Marker.MARKER_TYPE_BBCH:
                break;

        }
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

    //----------Ende: Werte-------------------------------------------------------------------------
}
