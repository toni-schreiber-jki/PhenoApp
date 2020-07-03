package de.bund.jki.jki_bonitur;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class ImpressumActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressum);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init_typefaces();
        WebView wv = findViewById(R.id.wvImpressum);
        wv.loadUrl("file:///android_asset/html/impressum.html");
    }


    public void init_typefaces() {
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome.ttf");
        int[] buttonIds = new int[]{
                R.id.btnImpressumBack
        };

        for (int id : buttonIds) {
            ((Button) this.findViewById(id)).setTypeface(typeface);
        }
    }

    public void onClick(final View v) {
        try {
            switch (v.getId()) {
                case R.id.btnImpressumBack:
                    onBackPressed();
            }
        } catch (Exception e) {
            new ErrorLog(e, getApplicationContext());
        }
    }
}
