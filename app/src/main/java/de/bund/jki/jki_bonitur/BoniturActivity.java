package de.bund.jki.jki_bonitur;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.bund.jki.jki_bonitur.db.BoniturDatenbank;
import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.Standort;
import de.bund.jki.jki_bonitur.excel.Reader;


public class BoniturActivity extends ActionBarActivity {

    BoniturDatenbank bonDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonitur);
        bonDb = new BoniturDatenbank(this);
        init();
    }

    private void init()
    {
        BoniturSafe.db = bonDb.getWritableDatabase();

        Reader reader = new Reader(getIntent().getStringExtra("FILE"));
        reader.read();

        BoniturSafe.CURRENT_PARZELLE    = "-";
        BoniturSafe.CURRENT_REIHE       = -1;
        BoniturSafe.CURRENT_PFLANZE     = -1;

        BoniturSafe.CURRENT_MARKER      = -1;

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

    public void fillView(Object[] daten)
    {
        if(daten[0] != null ) {
            ((TextView) findViewById(R.id.tvStandort)).setText(((Standort) daten[0]).parzelle + "-" + String.format("%03d-%03d", ((Standort) daten[0]).reihe, ((Standort) daten[0]).pflanze));
            ((TextView) findViewById(R.id.tvSorteZucht)).setText(((Standort) daten[0]).sorte + " / " + ((Standort) daten[0]).zuchtstamm);
            ((TextView) findViewById(R.id.tvEltern)).setText(((Standort) daten[0]).mutter + " / " + ((Standort) daten[0]).vater);
            ((TextView) findViewById(R.id.tvSortiment)).setText(((Standort) daten[0]).sortimentsnummer);
        }

        if(daten[1] != null){
            ((TextView) findViewById(R.id.tvMerkmal)).setText(((Marker) daten[1]).name);
            ((TextView) findViewById(R.id.tvBeschreibung)).setText(((Marker) daten[1]).beschreibung);
        }
    }
}
