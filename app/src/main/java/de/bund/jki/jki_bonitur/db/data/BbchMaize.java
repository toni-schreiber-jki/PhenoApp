package de.bund.jki.jki_bonitur.db.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import de.bund.jki.jki_bonitur.db.BbchMainStadium;
import de.bund.jki.jki_bonitur.db.BbchStadium;

public class BbchMaize extends BbchDataImport {


    public Object[][] getData() {
        return data;
    }

    public static Object[][] data = new Object[][]{
        {
            "4",
            "0",
            "Germination",
            "Keimung",
            "",
            new String[][]{
                {
                    "0",
                    "Dry seed (caryopsis)",
                    "Trockener Samen"
                },
                {
                    "1",
                    "Beginning of seed imbibition ",
                    "Beginn der Samenquellung"
                },
                {
                    "3",
                    "Seed imbitition complete",
                    "Ende der Samenquellung"
                },
                {
                    "5",
                    "Radicle emerged from caryopsis ",
                    "Keimwurzel aus dem Samen ausgetreten"
                },
                {
                    "6",
                    "Radicle elongated, root hairs and/or side roots visible ",
                    "Keimwurzel gestreckt, Wurzelhaare und/oder Seitenwurzeln sichtbar"
                },
                {
                    "7",
                    "Coleoptile emerged from caryopsis ",
                    "Keimscheide (Koleoptile) aus dem Samen ausgetreten"
                },
                {
                    "9",
                    "Emergence: coleoptile penetrates soil surface (cracking stage)  ",
                    "Auflaufen: Koleoptile durchbricht Bodenoberfläche"
                }
            }
        },
        {
            "4",
            "1",
            "Leaf development",
            "Blattentwicklung (Hauptsproß)",
            "",
            new String[][]{
                {
                    "10",
                    "First leaf through coleoptile",
                    "1. Laubblatt aus der Koleoptile ausgetreten"
                },
                {
                    "11",
                    "First leaf unfolded",
                    "1 . Laubblatt entfaltet"
                },
                {
                    "12",
                    "2 leaves unfolded",
                    "2. Laubblatt entfaltet"
                },
                {
                    "13",
                    "3 leaves unfolded",
                    "3. Laubblatt entfaltet"
                },
                {
                    "14",
                    "4 leaves unfolded",
                    "4. Laubblatt entfaltet"
                },
                {
                    "15",
                    "5 leaves unfolded",
                    "5. Laubblatt entfaltet"
                },
                {
                    "16",
                    "6 leaves unfolded",
                    "6. Laubblatt entfaltet"
                },
                {
                    "17",
                    "7 leaves unfolded",
                    "7. Laubblatt entfaltet"
                },
                {
                    "18",
                    "8 leaves unfolded",
                    "8. Laubblatt entfaltet"
                },
                {
                    "19",
                    "9 or more leaves unfolded ",
                    "9 und mehr Laubblätter entfaltet"
                },
            }
        },
        {
            "4",
            "3",
            "Stem elongation",
            "Längenwachstum (Hauptsproß); Schossen",
            "",
            new String[][]{
                {
                    "30",
                    "Beginning of stem elongation",
                    "Beginn des Längenwachstums"
                },
                {
                    "31",
                    "First node detectable",
                    "Erster Stengelknoten wahrnehmbar"
                },
                {
                    "32",
                    "2 nodes detectable",
                    "2. Stengelknoten wahrnehmbar"
                },
                {
                    "33",
                    "3 nodes detectable",
                    "3. Stengelknoten wahrnehmbar"
                },
                {
                    "34",
                    "4 nodes detectable",
                    "4. Stengelknoten wahrnehmbar"
                },
                {
                    "35",
                    "5 nodes detectable",
                    "5. Stengelknoten wahrnehmbar"
                },
                {
                    "36",
                    "6 nodes detectable",
                    "6. Stengelknoten wahrnehmbar"
                },
                {
                    "37",
                    "7 nodes detectable",
                    "7. Stengelknoten wahrnehmbar"
                },
                {
                    "38",
                    "8 nodes detectable",
                    "8. Stengelknoten wahrnehmbar"
                },
                {
                    "39",
                    "9 or more nodes detectable",
                    "9 und mehr Stengelknoten wahrnehmbar"
                },
            },
        },
        {
            "4",
            "5",
            "Inflorescence emergence, heading ",
            "Entwicklung der Blütenanlagen; Rispenschieben",
            "",
            new String[][]{
                {
                    "51",
                    "Beginning of tassel emergence: tassel detectable at top of stem",
                    "Beginn des Rispenschiebens: Rispe in Tüte gut fühlbar",
                },
                {
                    "53",
                    "Tip of tassel visible",
                    "Spitze der Rispe sichtbar",
                },
                {
                    "55",
                    "Middle of tassel emergence: middle of tassel begins to separate",
                    "Mitte des Rispenschiebens: Rispe voll ausgestreckt, frei von umhüllenden Blättern; Rispenmitteläste entfalten sich",
                },
                {
                    "59",
                    "End of tassel emergence: tassel fully emerged and separated",
                    "Ende des Rispenschiebens: untere Rispenmitteläste voll entfaltet"
                }
            }
        },
        {
            "4",
            "6",
            "Flowering, anthesis ",
            "Blüte",
            "",
            new String[][]{
                {
                    "61",
                    "Male: stammens in middle of tassel visible Female: tip of ear emerging from leaf sheath",
                    "männliche Infloreszenz: Beginn der Blüte; Mitte des Rispenmittelastes blüht, weibliche Infloreszenz: Spitze der Kolbenanlage schiebt aus der Blattscheide"
                },
                {
                    "63",
                    "Male: beginning of pollen shedding Female: tips pf stigmata visible",
                    "männliche Infloreszenz: Pollenschüttung beginnt weibliche Infloreszenz: Spitzen der Narbenfäden sichtbar",
                },
                {
                    "65",
                    "Male: upper and lower parts of tassel in flower Female: stigmata fully emerged",
                    "männliche Infloreszenz: Vollblüte: obere und untere Rispenäste in Blüte, weibliche Infloreszenz: Narbenfäden vollständig geschoben"
                },
                {
                    "67",
                    "Male: flowering completed Female: stigmata drying",
                    "männliche Infloreszenz: Blüte abgeschlossen weibliche Infloreszenz: Narbenfäden beginnen zu vertrocknen"
                },
                {
                    "69",
                    "End of flowering: stigmata completly dry",
                    "Ende der Blüte"
                }
            }
        },
        {

            "4",
            "7",
            "Development of fruit",
            "Fruchtentwicklung",
            "",
            new String[][]{
                {
                    "71",
                    "Beginning of grain development: kernels at blister stage,about 16% dry matter",
                    "Beginn der Kornbildung: Körner sind zu erkennen; Inhalt wässrig; ca. 16 % TS im Korn"
                },
                {
                    "73",
                    "early milk",
                    "Frühe Milchreife"
                },
                {
                    "75",
                    "Kernels in middle of cob yellowish-white (variety-dependent) content milky, about 40% dry matter",
                    "Milchreife: Körner in Kolbenmitte sind weiß-gelblich; Inhalt milchig; ca. 40 % TS im Korn"
                },
                {
                    "79",
                    "Nearly all kernels have reached final size",
                    "Art- bzw. sortenspezifische Korngröße erreicht"
                }
            }
        },
        {
            "4",
            "8",
            "Ripening",
            "Frucht- und Samenreife",
            "",
            new String[][]{
                {
                    "83",
                    "Early dough: kernel content soft, about 45% dry matter",
                    "Frühe Teigreife: Körner teigartig; am Spindelansatz noch feucht; ca. 45 % TS im Korn"
                },
                {
                    "85",
                    "Dough stage: kernels yellowish to yellow (variety dependent), about 55% dry matter",
                    "Teigreife (= Siloreife): Körner gelblich bis gelb (sortenabhängig); teigige Konsistenz; ca. 55 % TS im Korn"
                },
                {
                    "87",
                    "Physiological maturity: black dot/layer visible at base of kernels, about 60% dry matter",
                    "Physiologische Reife: schwarze(r) Punkt/Schicht am Korngrund; ca. 60 % TS im Korn"
                },
                {
                    "89",
                    "Fully ripe: kernels hard and shiny, about 65% dry matter",
                    "Vollreife: Körner durchgehärtet und glänzend; ca. 65 % TS im Korn",
                }
            }
        },
        {
            "4",
            "9",
            "Senescence",
            "Absterben",
            "",
            new String[][]{
                {
                    "97",
                    "Plant dead and collapsing",
                    "Pflanze abgestorben"
                },
                {
                    "99",
                    "Harvested product",
                    "Erntegut"
                },
            }
        }
    };
}