package de.bund.jki.jki_bonitur.db.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import de.bund.jki.jki_bonitur.db.BbchMainStadium;
import de.bund.jki.jki_bonitur.db.BbchStadium;

public class BbchRape extends BbchDataImport {

    public Object[][] getData() {
        return data;
    }

    public static Object[][] data = new Object[][]
        {
            {
                "2",
                "0",
                "Germination",
                "Keimung",
                "",
                new String[][]{
                    {
                        "0",
                        "Dry seed",
                        "Trockener Samen"
                    },
                    {
                        "1",
                        "Beginning of seed imbibition",
                        "Beginn der Samenquellung"
                    },
                    {
                        "3",
                        "Seed imbibition complete (pigeon breast)",
                        "Ende der Samenquellung"
                    },
                    {
                        "5",
                        "Radicle emerged from seed",
                        "Keimwurzel aus dem Samen ausgetreten"
                    },
                    {
                        "7",
                        "Hypocotyl with cotyledons emerged from seed ",
                        "Hypocotyl mit Keimblättern hat Samenschale durchbrochen",
                    },
                    {
                        "8",
                        "Hypocotyl with cotyledons growing towards soil surface ",
                        "Hypocotyl mit Keimblättern wächst zur Erdoberfläche"
                    },
                    {
                        "9",
                        "Emergence: cotyledons emerge through soil surface ",
                        "Auflaufen: Keimblätter durchbrechen Bodenoberfläche"
                    }
                }
            },
            {
                "2",
                "1",
                "Leaf development",
                "Blattentwicklung (Hauptsproß)",
                "1.png",
                new String[][]{
                    {
                        "10",
                        "Cotyledons completely unfolded",
                        "Keimblätter voll entfaltet"
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
                        "9 und mehr Laubblätter entfaltet (Internodien noch nicht gestreckt)"
                    },
                }
            },
            {
                "2",
                "2",
                "Formation of side shoots",
                "Entwicklung von Seitensprossen",
                "",
                new String[][]{
                    {
                        "20",
                        "No side shoots ",
                        "Keine Seitensprosse",
                    },
                    {
                        "21",
                        "Beginning of side shoot development: first side shoot detectable",
                        "Beginn der Seitensproßentwicklung: erster Seitensproß sichtbar"
                    },
                    {
                        "22",
                        "2 side shoots detectable ",
                        "2. Seitensproß sichtbar",
                    },
                    {
                        "23",
                        "3 side shoots detectable ",
                        "3. Seitensproß sichtbar",
                    },
                    {
                        "24",
                        "4 side shoots detectable ",
                        "4. Seitensproß sichtbar",
                    },
                    {
                        "25",
                        "5 side shoots detectable ",
                        "5. Seitensproß sichtbar",
                    },
                    {
                        "26",
                        "6 side shoots detectable ",
                        "6. Seitensproß sichtbar",
                    },
                    {
                        "27",
                        "7 side shoots detectable ",
                        "7. Seitensproß sichtbar",
                    },
                    {
                        "28",
                        "8 side shoots detectable ",
                        "8. Seitensproß sichtbar",
                    },
                    {
                        "29",
                        "End of side shoot development: 9 or more side shoots detectable ",
                        "9 oder mehr Seitensprosse sichtbar"
                    }
                }
            },
            {
                "2",
                "3",
                "Stem elongation",
                "Längenwachstum (Hauptsproß)",
                "3.png",
                new String[][]{
                    {
                        "30",
                        "Beginning of stem elongation: no internodes (“rosette”) ",
                        "Beginn des Längenwachstums",
                    },
                    {
                        "31",
                        "1 visibly extended internode ",
                        "1. sichtbar gestrecktes Internodium",
                    },
                    {
                        "32",
                        "2 visibly extended internodes",
                        "2. sichtbar gestrecktes Internodium"
                    },
                    {
                        "33",
                        "3 visibly extended internodes",
                        "3. sichtbar gestrecktes Internodium"
                    },
                    {
                        "34",
                        "4 visibly extended internodes",
                        "4. sichtbar gestrecktes Internodium"
                    },
                    {
                        "35",
                        "5 visibly extended internodes",
                        "5. sichtbar gestrecktes Internodium"
                    },
                    {
                        "36",
                        "6 visibly extended internodes",
                        "6. sichtbar gestrecktes Internodium"
                    },
                    {
                        "37",
                        "7 visibly extended internodes",
                        "7. sichtbar gestrecktes Internodium"
                    },
                    {
                        "38",
                        "8 visibly extended internodes",
                        "8. sichtbar gestrecktes Internodium"
                    },
                    {
                        "39",
                        "9 or more visibly extended internodes ",
                        "9 und mehr sichtbar gestreckte Internodien"
                    },
                }
            },
            {
                "2",
                "5",
                "Inflorescence emergence",
                "Entwicklung der Blütenanlagen (Hauptsproß);",
                "5.png",
                new String[][]{
                    {
                        "50",
                        "Flower buds present, still enclosed by leaves ",
                        "Hauptinfloreszenz bereits vorhanden, von den obersten Blättern noch dicht umschlossen"
                    },
                    {
                        "51",
                        "Flower buds visible from above (\"green bud\") ",
                        "Hauptinfloreszenz inmitten der obersten Blätter von oben sichtbar"
                    },
                    {
                        "52",
                        "Flower buds free, level with the youngest leaves ",
                        "Hauptinfloreszenz frei; auf gleicher Höhe wie die obersten Blätter"
                    },
                    {
                        "53",
                        "Flower buds raised above the youngest leaves",
                        "Hauptinfloreszenz überragt die obersten Blätter",
                    },
                    {
                        "55",
                        "Individual flower buds (main inflorescence) visible but still closed ",
                        "Einzelblüten der Hauptinfloreszenz sichtbar (geschlossen)",
                    },
                    {
                        "57",
                        "Individual flower buds (secondary inflorescences) visible but still closed",
                        "Einzelblüten der sekundären Infloreszenzen sichtbar (geschlossen)",
                    },
                    {
                        "59",
                        "First petals visible, flower buds still closed (\"yellow bud\") ",
                        "Erste Blütenblätter sichtbar. Blüten noch geschlossen",
                    },
                }
            },
            {
                "2",
                "6",
                "Flowering",
                "Blüte (Hauptsproß)",
                "6.png",
                new String[][]{
                    {
                        "60",
                        "First flowers open ",
                        "Erste offene Blüten",
                    },
                    {
                        "61",
                        "10% of flowers on main raceme open, main raceme elongating ",
                        "ca. 1 O % der Blüten am Haupttrieb offen. Infloreszenzachse verlängert"
                    },
                    {
                        "62",
                        "20% of flowers on main raceme open ",
                        "20% der Blüte am Haupttrieb offen. Inflorenzenzachse verlängert"
                    },
                    {
                        "63",
                        "30% of flowers on main raceme open ",
                        "ca. 30 % der Blüten am Haupttrieb offen",
                    },
                    {
                        "64",
                        "40% of flowers on main raceme open ",
                        "ca. 40 % der Blüten am Haupttrieb offen",
                    },
                    {
                        "65",
                        "Full flowering: 50% flowers on main raceme open, older petals falling ",
                        "Vollblüte: ca. 50 % der Blüten am Haupttrieb offen. Erste Blütenblätter fallen bereits ab",
                    },
                    {
                        "67",
                        "Flowering declining: majority of petals fallen ",
                        "Abgehende Blüte: Mehrzahl der Blütenblätter abgefallen",
                    },
                    {
                        "69",
                        "End of flowering",
                        "Ende der Blüte",
                    }
                }
            },
            {
                "2",
                "7",
                "Development of fruit",
                "Fruchtentwicklung",
                "7.png",
                new String[][]{
                    {
                        "71",
                        "10% of pods have reached final size ",
                        "ca. 1O % der Schoten haben art- bzw. sortenspezifische Größe erreicht"
                    },
                    {
                        "72",
                        "20% of pods have reached final size ",
                        "ca. 2O % der Schoten haben art- bzw. sortenspezifische Größe erreicht"
                    },
                    {
                        "73",
                        "30% of pods have reached final size ",
                        "ca. 3O % der Schoten haben art- bzw. sortenspezifische Größe erreicht"
                    },
                    {
                        "74",
                        "40% of pods have reached final size ",
                        "ca. 4O % der Schoten haben art- bzw. sortenspezifische Größe erreicht"
                    },
                    {
                        "75",
                        "50% of pods have reached final size ",
                        "ca. 5O % der Schoten haben art- bzw. sortenspezifische Größe erreicht"
                    },
                    {
                        "76",
                        "60% of pods have reached final size ",
                        "ca. 6O % der Schoten haben art- bzw. sortenspezifische Größe erreicht"
                    },
                    {
                        "77",
                        "70% of pods have reached final size ",
                        "ca. 7O % der Schoten haben art- bzw. sortenspezifische Größe erreicht"
                    },
                    {
                        "78",
                        "80% of pods have reached final size ",
                        "ca. 8O % der Schoten haben art- bzw. sortenspezifische Größe erreicht"
                    },
                    {
                        "79",
                        "Nearly all pods have reached final size ",
                        "Fast alle Schoten haben art- bzw. sortenspezifische Größe erreicht"
                    },
                }
            },
            {
                "2",
                "8",
                "Ripening",
                "Frucht- und Samenreife",
                "8.png",
                new String[][]{
                    {
                        "80",
                        "Beginning of ripening: seed green, filling pod cavity ",
                        "Beginn der Reife: Samen grün",
                    },
                    {
                        "81",
                        "10% of pods ripe, seeds dark and hard ",
                        "10% der Schoten ausgereift: Samen schwarz und hart",
                    },
                    {
                        "82",
                        "20% of pods ripe, seeds dark and hard ",
                        "20% der Schoten ausgereift: Samen schwarz und hart",
                    },
                    {
                        "83",
                        "30% of pods ripe, seeds dark and hard ",
                        "30% der Schoten ausgereift: Samen schwarz und hart",
                    },
                    {
                        "84",
                        "40% of pods ripe, seeds dark and hard ",
                        "40% der Schoten ausgereift: Samen schwarz und hart",
                    },
                    {
                        "85",
                        "50% of pods ripe, seeds dark and hard ",
                        "50% der Schoten ausgereift: Samen schwarz und hart",
                    },
                    {
                        "86",
                        "60% of pods ripe, seeds dark and hard ",
                        "60% der Schoten ausgereift: Samen schwarz und hart",
                    },
                    {
                        "87",
                        "70% of pods ripe, seeds dark and hard ",
                        "70% der Schoten ausgereift: Samen schwarz und hart",
                    },
                    {
                        "88",
                        "80% of pods ripe, seeds dark and hard ",
                        "80% der Schoten ausgereift: Samen schwarz und hart",
                    },
                    {
                        "89",
                        "Fully ripe: nearly all pods ripe, seeds dark and hard  ",
                        "Vollreife: Fast alle Samen an der gesamten Pflanze schwarz und hart",
                    },
                }
            },
            {
                "2",
                "9",
                "Senescence",
                "Absterben",
                "",
                new String[][]{
                    {
                        "97",
                        "Plant dead and dry  ",
                        "Pflanze abgestorben",
                    },
                    {
                        "99",
                        "Harvested product",
                        "Erntegut"
                    }
                }
            }
        };
}