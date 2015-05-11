package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;
import android.database.SQLException;

import de.bund.jki.jki_bonitur.BoniturSafe;

/**
 * Created by Toni on 10.05.2015.
 */
public abstract class DbModelInterface {

    public static String TABLE_NAME;
    public static String COLUMN_ID;

    public static String CREATE_TABLE;
    public static String[] ALL_COLUMMNS;

    abstract boolean save();

    protected boolean saveRow(int id, ContentValues values)
    {
        if(id==-1)
        {
            try {
                id = (int) BoniturSafe.db.insertOrThrow(TABLE_NAME, null, values);

                return id==-1 ? false : true;
            } catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }
        else
        {
            if (BoniturSafe.db.update(TABLE_NAME,values,COLUMN_ID+"=?",new String[]{""+id}) == 1)
                return true;
        }

        return false;
    }

}
