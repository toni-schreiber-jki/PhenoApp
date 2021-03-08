package de.bund.jki.jki_bonitur;

import android.database.Cursor;

import de.bund.jki.jki_bonitur.config.Config;
import de.bund.jki.jki_bonitur.db.Akzession;
import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.Passport;
import de.bund.jki.jki_bonitur.db.Standort;

/**
 * Created by toni.schreiber on 15.06.2015.
 */
public class StandortManager {

    public static int NEXT = 1;
    public static int PREV = 2;

    public static int PFLANZEN_RICHTUNG = 1;
    public static int REIHEN_RICHTUNG   = 2;

    private static String[] columns = {Standort.COLUMN_ID, Standort.COLUMN_PARZELLE, Standort.COLUMN_REIHE, Standort.COLUMN_PFLANZE};

    public static Object[] next() {
        return prevNext(NEXT);
    }

    public static Object[] prev() {
        return prevNext(PREV);
    }

    private static Object[] prevNext(int direction) {
        BoniturSafe.FIRST_EMPTY_COUNT = 1;
        if (BoniturSafe.CURRENT_PFLANZE == - 1)
            return first();

        Object[] marker = direction == NEXT ? MarkerManager.next() : MarkerManager.prev();
        if (((int) marker[1]) == MarkerManager.MARKER_OK)
            return sameStandort(marker[0]);

        if (((int) marker[1]) == MarkerManager.MARKER_NEXT_STANDORT)
            return direction == NEXT ? nextStandort(NEXT, marker[0]) : nextStandort(PREV, marker[0]);

        return null;
    }

    public static Object[] nextStandort(Object marker) {
        return nextStandort(NEXT, marker);
    }

    public static Object[] nextStandort(int direction, Object marker) {
        Cursor c = null;
        try {
            c = BoniturSafe.db.query(
                Standort.TABLE_NAME,
                columns,
                Standort.COLUMN_VERSUCH + "=? AND " + Standort.COLUMN_PARZELLE + "= ? AND " + Standort.COLUMN_REIHE + " = ? AND " + Standort.COLUMN_PFLANZE + " " + (getRealDirection(PFLANZEN_RICHTUNG, direction) == NEXT ? ">" : "<") + "  ?",
                new String[]{"" + BoniturSafe.VERSUCH_ID, "" + BoniturSafe.CURRENT_PARZELLE, "" + BoniturSafe.CURRENT_REIHE, "" + BoniturSafe.CURRENT_PFLANZE},
                null,
                null,
                "CAST(" + Standort.COLUMN_PFLANZE + " AS INTEGER) " + (getRealDirection(PFLANZEN_RICHTUNG, direction) == NEXT ? "ASC" : "DESC"),
                "" + 1
            );
            if (c.getCount() == 1) {
                Object s = getValuesFromStandort(c);
                //c.close();
                return new Object[]{s, marker};
            }
            //c.close();
            return nextReihe(direction, marker);
        } catch (Exception e) {
            //Todo: ggf neue Fehlermeldung einbauen, denn am Parzellenende ist dies eigentlich kein Fehler
            new ErrorLog(e, null);
            e.printStackTrace();
            return new Object[]{null, null};
        } finally {
            if (c != null)
                c.close();
        }
    }

    public static Object[] nextReihe(Object marker) {
        return nextReihe(NEXT, marker);
    }

    public static Object[] nextReihe(int direction, Object marker) {
        Cursor c = null;
        try {
            changeRichtung(PFLANZEN_RICHTUNG);
            c = BoniturSafe.db.query(
                Standort.TABLE_NAME,
                columns,
                Standort.COLUMN_VERSUCH + "=? AND " + Standort.COLUMN_PARZELLE + "= ? AND " + Standort.COLUMN_REIHE + " " + (getRealDirection(REIHEN_RICHTUNG, direction) == NEXT ? ">" : "<") + " ? ",
                new String[]{"" + BoniturSafe.VERSUCH_ID, "" + BoniturSafe.CURRENT_PARZELLE, "" + BoniturSafe.CURRENT_REIHE},
                null,
                null,
                "CAST(" + Standort.COLUMN_REIHE + " AS INTEGER) " + (getRealDirection(REIHEN_RICHTUNG, direction) == NEXT ? "ASC" : "DESC") + ", CAST(" + Standort.COLUMN_PFLANZE + " AS INTEGER) " + (getRealDirection(PFLANZEN_RICHTUNG, direction) == NEXT ? "ASC" : "DESC"),
                "" + 1
            );
            Object s = getValuesFromStandort(c);
            //c.close();
            return new Object[]{s, marker};
        } catch (Exception e) {
            new ErrorLog(e, null);
            return new Object[]{null, null};
        } finally {
            c.close();
        }
    }

    public static Object[] sameStandort(Object marker) {
        return new Object[]{
            getStandortFromId(BoniturSafe.CURRENT_STANDORT_ID),
            marker
        };
    }

    private static Object[] first() {
        Cursor c = null;
        try {
            c = BoniturSafe.db.query(
                Standort.TABLE_NAME,
                columns,
                Standort.COLUMN_VERSUCH + "=?",
                new String[]{"" + BoniturSafe.VERSUCH_ID},
                null,
                null,
                Standort.COLUMN_PARZELLE + " ASC, " + Standort.COLUMN_REIHE + " ASC, " + Standort.COLUMN_PFLANZE + " ASC",
                "" + 1
            );
            Object[] result = new Object[]{getValuesFromStandort(c), MarkerManager.next()[0]};

//            if(!c.isClosed())
//                c.close();

            return result;
        } catch (Exception e) {
            new ErrorLog(e, null);
            return new Object[]{null, null};
        } finally {
            if (c != null)
                c.close();
        }
    }

    public static Object[] gotoStandort(String parzelle, int reihe, int pflanze, Marker marker) {
        Cursor c = null;
        try {
            c = BoniturSafe.db.query(
                Standort.TABLE_NAME,
                new String[]{Standort.COLUMN_ID},
                Standort.COLUMN_PARZELLE + "=? AND " + Standort.COLUMN_REIHE + "= ? AND " + Standort.COLUMN_PFLANZE + "=? AND " + Standort.COLUMN_VERSUCH + "=?",
                new String[]{parzelle, "" + reihe, "" + pflanze, "" + BoniturSafe.VERSUCH_ID},
                null,
                null,
                null,
                "1"
            );

            if (c.getCount() == 1) {
                c.moveToFirst();
                Object s = getStandortFromId(c.getInt(c.getColumnIndex(Standort.COLUMN_ID)));
                //c.close();
                return new Object[]{
                    s,
                    marker
                };
            }
            //c.close();
            return new Object[]{null, null};
        } catch (Exception e) {
            new ErrorLog(e, null);
            return new Object[]{null, null};
        } finally {
            if (c != null)
                c.close();
        }

    }

    private static Object getValuesFromStandort(Cursor c) {
        try {
            if (c.getCount() == 1) {
                c.moveToFirst();
                Object o = getStandortFromId(c.getInt(c.getColumnIndex(Standort.COLUMN_ID)));
                //c.close();
                return o;
            }
            //c.close();
            return null;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private static Object getStandortFromId(int id) {
        Standort s = Standort.findByPk(id);

        if (s != null) {
            BoniturSafe.CURRENT_PARZELLE = s.parzelle;
            BoniturSafe.CURRENT_REIHE = s.reihe;
            BoniturSafe.CURRENT_PFLANZE = s.pflanze;
            BoniturSafe.CURRENT_STANDORT_ID = id;
        }

        return s;
    }

    public static Akzession[] getAllAkzessionen() {
        Cursor c = null;
        try {
            Akzession[] res = new Akzession[0];

            c = BoniturSafe.db.query(
                Akzession.TABLE_NAME,
                new String[]{Akzession.COLUMN_ID},
                Akzession.COLUMN_VERSUCH + "=?",
                new String[]{"" + BoniturSafe.VERSUCH_ID},
                null,
                null,
                Akzession.COLUMN_NAME + " ASC"
            );

            if (c.getCount() > 0) {
                res = new Akzession[c.getCount()];
                int p = 0;
                c.moveToFirst();
                do {
                    res[p] = Akzession.findByPk(c.getInt(c.getColumnIndex(Akzession.COLUMN_ID)));
                    p++;
                } while (c.moveToNext());
            }

            //c.close();

            return res;
        } catch (Exception e) {
            new ErrorLog(e, null);
            return new Akzession[0];
        } finally {
            c.close();
        }
    }

    public static Passport[] getAllPassport() {
        Cursor c = null;
        try {
            Passport[] res = new Passport[0];

            c = BoniturSafe.db.query(
                Passport.TABLE_NAME,
                new String[]{Passport.COLUMN_ID},
                Passport.COLUMN_VERSUCH + "=?",
                new String[]{"" + BoniturSafe.VERSUCH_ID},
                null,
                null,
                Passport.COLUMN_LEITNAME + " ASC"
            );

            if (c.getCount() > 0) {
                res = new Passport[c.getCount()];
                int p = 0;
                c.moveToFirst();
                do {
                    res[p] = Passport.findByPk(c.getInt(c.getColumnIndex(Passport.COLUMN_ID)));
                    p++;
                } while (c.moveToNext());
            }

            //c.close();

            return res;
        } catch (Exception e) {
            new ErrorLog(e, null);
            return new Passport[0];
        } finally {
            if (c != null)
                c.close();
        }
    }

    public static Standort[] getAllStandorte() {
        Cursor c = null;
        try {
            Standort[] res = new Standort[0];


/*
            c = BoniturSafe.db.query(
                    Standort.TABLE_NAME,
                    null, //new String[]{Standort.COLUMN_ID},
                    Standort.COLUMN_VERSUCH + "=?",
                    new String[]{"" + BoniturSafe.VERSUCH_ID},
                    null,
                    null,
                    Standort.COLUMN_PARZELLE + " ASC, CAST(" + Standort.COLUMN_REIHE + " AS INTEGER) ASC, CAST(" + Standort.COLUMN_PFLANZE + " AS INTEGER) ASC"
            );
            */
            c = BoniturSafe.db.rawQuery(
                "SELECT \n" +
                    "standort.* , passport.leitname, passport.kennNr, akzession.nummer, akzession.name,akzession.merkmale as aMerkmale, passport.merkmale as pMerkmale\n" +
                    "FROM standort \n" +
                    "LEFT JOIN akzession ON akzession._id = standort.akzessionId\n" +
                    "LEFT JOIN passport ON passport._id = standort.passportId\n" +
                    "WHERE standort.versuchId = ?\n" +
                    "ORDER BY parzelle ASC, CAST(standort.reihe AS INTEGER) ASC, CAST(standort.pflanze AS INTEGER) ASC",
                new String[]{"" + BoniturSafe.VERSUCH_ID}
            );

            if (c.getCount() > 0) {
                res = new Standort[c.getCount()];
                int p = 0;
                c.moveToFirst();
                do {
                    res[p] = new Standort();
                    res[p].fillElementWithCurcor(c); // = Standort.findByPk(c.getInt(c.getColumnIndex(Standort.COLUMN_ID)));
                    res[p].fillAkzession(c);
                    res[p].fillPassport(c);
                    p++;
                } while (c.moveToNext());
            }

            //c.close();

            return res;
        } catch (Exception e) {
            new ErrorLog(e, null);
            return new Standort[0];
        } finally {
            if (c != null)
                c.close();
        }
    }

    private static int getRealDirection(int art, int richtung) {
        if (! Config.ZICK_ZACK_MODUS)
            return richtung;

        if (art == PFLANZEN_RICHTUNG) {
            if (BoniturSafe.PFLANZEN_RICHTUNG == NEXT)
                return richtung;
            else
                return richtung == NEXT ? PREV : NEXT;

        } else { //REIHEN_RICHTUNG
            if (BoniturSafe.REIHEN_RICHTUNG == NEXT)
                return richtung;
            else
                return richtung == NEXT ? PREV : NEXT;
        }
    }

    public static void changeRichtung(int art) {
        if (! Config.ZICK_ZACK_MODUS) return;

        if (art == PFLANZEN_RICHTUNG) {
            BoniturSafe.PFLANZEN_RICHTUNG = BoniturSafe.PFLANZEN_RICHTUNG == NEXT ? PREV : NEXT;
        } else {
            BoniturSafe.REIHEN_RICHTUNG = BoniturSafe.REIHEN_RICHTUNG == NEXT ? PREV : NEXT;
        }
    }

    public static Object[] gotoFirstEmpty() {
        Cursor c = null;
        try {
            /*Cursor c = BoniturSafe.db.rawQuery(
                    "SELECT standort._id FROM " + Standort.TABLE_NAME + "  LEFT JOIN versuchWert ON " + Standort.TABLE_NAME + "._id = versuchWert.standortId " + "\n" +
                            "WHERE " + Standort.TABLE_NAME + ".versuchId = ? " + "\n" +
                            "GROUP By " + Standort.TABLE_NAME + "._id " + "\n" +
                            "HAVING GROUP_CONCAT(DISTINCT versuchWert.markerId) <> (SELECT DISTINCT GROUP_CONCAT(_id) FROM marker WHERE versuchId=? GROUP BY versuchId ORDER BY _id) OR GROUP_CONCAT(versuchWert.markerId) is null " + "\n" +
                            "ORDER BY " + Standort.TABLE_NAME + ".parzelle ASC, " + Standort.TABLE_NAME + ".reihe ASC, " + Standort.TABLE_NAME + ".pflanze ASC, versuchWert.markerId ASC " + "\n" +
                            "LIMIT 1",
                    new String[]{"" + BoniturSafe.VERSUCH_ID, "" + BoniturSafe.VERSUCH_ID}
            );*/

            String sql = "SELECT \n" +
                "DISTINCT \"standort\".\"_id\" AS standort, \"marker\"._id AS marker\n" +
                "FROM standort , \"marker\" \n" +
                "WHERE \n" +
                "\"standort\".versuchId=? AND \n" +
                "\"marker\".versuchId = ? AND\n" +
                " (SELECT count(*) FROM versuchwert WHERE versuchId=? AND standortId = standort._id AND markerId= marker._id AND ((ifnull(wert_int,\"0\") <> \"0\" )  OR ifnull(wert_text,\"\") <> \"\" OR (ifnull(wert_id,\"0\") <> \"0\"))  ) = 0\n" +
                "ORDER BY \n" +
                "parzelle ASC, reihe ASC, pflanze ASC, marker._id ASC";

            if (BoniturSafe.MARKER_FILTER.size() > 0) {
                sql = "SELECT \n" +
                    "DISTINCT \"standort\".\"_id\" AS standort, \"marker\"._id AS marker\n" +
                    "FROM standort , \"marker\" \n" +
                    "WHERE \n" +
                    "\"standort\".versuchId=? AND \n" +
                    "\"marker\".versuchId = ? AND\n" +
                    "\"marker\".\"_id\" IN (" + getMarkerFilter() + ") AND\n" +
                    " (SELECT count(*) FROM versuchwert WHERE versuchId=? AND standortId = standort._id AND markerId= marker._id AND ((ifnull(wert_int,\"0\") <> \"0\" )  OR ifnull(wert_text,\"\") <> \"\" OR (ifnull(wert_id,\"0\") <> \"0\"))  ) = 0\n" +
                    "ORDER BY \n" +
                    "parzelle ASC, reihe ASC, pflanze ASC, marker._id ASC";

            }

            c = BoniturSafe.db.rawQuery(
                sql,
                new String[]{"" + BoniturSafe.VERSUCH_ID, "" + BoniturSafe.VERSUCH_ID, "" + BoniturSafe.VERSUCH_ID}
            );

            if (c.getCount() >= 1) {
                if (BoniturSafe.FIRST_EMPTY_COUNT < c.getCount()) {
                    c.move(BoniturSafe.FIRST_EMPTY_COUNT);
                } else {
                    c.moveToFirst();
                    BoniturSafe.FIRST_EMPTY_COUNT = 1;
                }
                Standort s = Standort.findByPk(c.getInt(c.getColumnIndex("standort")));
                Marker   m = Marker.findByPk(c.getInt(c.getColumnIndex("marker")));

                BoniturSafe.CURRENT_STANDORT_ID = s.id;
                BoniturSafe.CURRENT_PARZELLE = s.parzelle;
                BoniturSafe.CURRENT_REIHE = s.reihe;
                BoniturSafe.CURRENT_PFLANZE = s.pflanze;

                BoniturSafe.CURRENT_MARKER = m.id;

                BoniturSafe.FIRST_EMPTY_COUNT++;

                //c.close();

                return new Object[]{s, m};

            }
            //c.close();
            return new Object[]{null, null};
        } catch (Exception e) {
            new ErrorLog(e, null);
            return new Object[]{null, null};
        } finally {
            if (c != null)
                c.close();
        }

    }

    /**
     * @return String
     */
    public static String getMarkerFilter() {
        String res = "";
        for (int i = 0; i < BoniturSafe.MARKER_FILTER.size(); i++) {
            if (i > 0)
                res += ",";
            res += BoniturSafe.MARKER_FILTER.get(i);
        }
        return res;
    }

}
