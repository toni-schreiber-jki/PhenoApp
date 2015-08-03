package de.bund.jki.jki_bonitur;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import de.bund.jki.jki_bonitur.config.Config;
import de.bund.jki.jki_bonitur.dialoge.SettingsDialog;


public class VersuchListActivity extends Activity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versuch_list);
        context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showFiles();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_versuch_list, menu);
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

    private void showFiles()
    {
        try {
            Config.load(this);
            String path = Environment.getExternalStorageDirectory().toString() + Config.BaseFolder +  "/in/";
            File folder = new File(path);
            //folder.mkdirs();

            File[] files = folder.listFiles();
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < files.length; ++i) {
                list.add(files[i].getName());
            }

            ListView lv = (ListView) findViewById(R.id.lvDateien);

            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    Intent i = new Intent(context, BoniturActivity.class);
                    i.putExtra("FILE",tv.getText().toString());
                    context.startActivity(i);
                }
            });

        }catch (Exception e){
            new ErrorLog(e,getApplication());
        }
    }

    public void loadSettings()
    {
        Config.load(this);
        showFiles();

    }

    public void onClick(final View v)
    {
        if(v.getId() == R.id.btnSettings){
            new SettingsDialog(this);
        }
    }


}
