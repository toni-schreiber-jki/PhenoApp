package de.bund.jki.jki_bonitur.dialoge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.bund.jki.jki_bonitur.BoniturActivity;
import de.bund.jki.jki_bonitur.R;
import de.bund.jki.jki_bonitur.db.Standort;

/**
 * Created by toni.schreiber on 19.06.2015.
 */
public class ManyStandorteDialog {
    private Standort[] mStandorte;
    private BoniturActivity mBa;

    public ManyStandorteDialog(BoniturActivity ba, Standort[] standorte) {
        ManyStandortDialogFragment msd = new ManyStandortDialogFragment();
        msd.mBa = ba;
        msd.mStandorte = standorte;
        msd.show(ba.getFragmentManager(),"");
    }

    public static class ManyStandortDialogFragment extends DialogFragment {
        public BoniturActivity mBa;
        public Standort[] mStandorte;

        private View mView;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //return super.onCreateDialog(savedInstanceState);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            mView = inflater.inflate(R.layout.dialog_many_standorte,null);

            builder.setView(mView);

            return builder.create();
        }

        @Override
        public void onResume() {
            super.onResume();
            Typeface typeface = Typeface.createFromAsset(mBa.getAssets(),"fonts/fontawesome.ttf");
            ((TextView) mView.findViewById(R.id.tvFragezeichen)).setTypeface(typeface);
            ((GridView) mView.findViewById(R.id.gvStandort)).setAdapter(getStandortAdapter());
            ((GridView) mView.findViewById(R.id.gvStandort)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mBa.bah.gotoStandort(mStandorte[position]);
                    ManyStandortDialogFragment.this.getDialog().cancel();
                }
            });
        }

        private ArrayAdapter getStandortAdapter() {
            List<String> standortList = new ArrayList<String>();
            for(Standort s: mStandorte)
            {
                standortList.add(s.getName());
            }
            ArrayAdapter<String> result = new ArrayAdapter<String>(mBa,android.R.layout.simple_list_item_1, standortList){

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    view.setBackgroundColor(Color.parseColor("#ffe5e5e5"));
                    return view;
                }
            };

            return result;
        }
    }
}
