package de.bund.jki.jki_bonitur.tools;

import android.text.Editable;
import android.text.TextWatcher;

import de.bund.jki.jki_bonitur.BoniturActivity;

/**
 * Created by toni.schreiber on 19.06.2015.
 */
public class BoniturTextWatcher implements TextWatcher
{
    private BoniturActivity mBa;

    public BoniturTextWatcher(BoniturActivity ba)
    {
        mBa = ba;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mBa.bah.saveValue();
    }
}
