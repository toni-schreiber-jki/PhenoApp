package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;
import android.database.SQLException;

import de.bund.jki.jki_bonitur.BoniturSafe;

/**
 * Created by Toni on 10.05.2015.
 */
public abstract class DbModelInterface {

    public String TABLE_NAME;
    public String COLUMN_ID;

    public static String CREATE_TABLE;
    public static String[] ALL_COLUMMNS;

    public int id;

    abstract boolean save();

    protected int saveRow(int id, ContentValues values)
    {
        if(id==-1)
        {
            try {
                this.id = (int) BoniturSafe.db.insertOrThrow(this.TABLE_NAME, null, values);

                return this.id;
            } catch (SQLException e){
                e.printStackTrace();
                return -1;
            }
        }
        else
        {
            if (BoniturSafe.db.update(TABLE_NAME,values,COLUMN_ID+"=?",new String[]{""+id}) == 1)
                return id;
        }

        return -1;
    }

}
