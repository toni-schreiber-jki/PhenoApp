package de.bund.jki.jki_bonitur.db.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import de.bund.jki.jki_bonitur.db.BbchMainStadium;
import de.bund.jki.jki_bonitur.db.BbchStadium;

public class BbchRice extends BbchDataImport {

    public Object[][] getData() {
        return data;
    }

    public static Object[][] data = new Object[][]
        {
            {
                "3",
                "0",
                "Germination",
                "Keimung",
                "0.png",
                new String[][]{
                    {
                        "0",
                        "Dry seed (caryopsis)",
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
                        "Radicle emerged from caryopsis",
                        "Keimwurzel aus dem Samen ausgetreten"
                    },
                    {
                        "6",
                        "Radicle elongated, root hairs and/or side roots visible",
                        "Keimwurzel gestreckt; Wurzelhaare und/oder Seitenwurzeln sichtbar"
                    },
                    {
                        "7",
                        "Coleoptile emerged from caryopsis (in water-rice this stage occurs before stage 05)",
                        "Keimscheide (Koleoptile) aus dem Samen ausgetreten (in Wasserreis tritt dieses Stadium vor Stadium 05 auf)"
                    },
                    {
                        "9",
                        "Imperfect leaf emerges (still rolled) at the tip of the coleoptile",
                        "Austritt des \"unvollst??ndigen Blattes\" aus der Koleoptile"
                    }
                }
            },
            {
                "3",
                "1",
                "Leaf development",
                "Blattentwicklung",
                "1.png",
                new String[][]{
                    {
                        "10",
                        "Imperfect leaf unrolled, tip of first true leaf visible",
                        "\"Unvollst??ndiges Blatt\" entfaltet; Spitze des ersten Blattes sichtbar",
                    },
                    {
                        "11",
                        "First leaf unfolded",
                        "1-Blatt-Stadium: 1. Laubblatt entfaltet"
                    },
                    {
                        "12",
                        "2 leaves unfolded",
                        "2-Blatt-Stadium: 2. Laubblatt entfaltet"
                    },
                    {
                        "13",
                        "3 leaves unfolded",
                        "3-Blatt-Stadium: 3. Laubblatt entfaltet"
                    },
                    {
                        "14",
                        "4 leaves unfolded",
                        "4-Blatt-Stadium: 4. Laubblatt entfaltet"
                    },
                    {
                        "15",
                        "5 leaves unfolded",
                        "5-Blatt-Stadium: 5. Laubblatt entfaltet"
                    },
                    {
                        "16",
                        "6 leaves unfolded",
                        "6-Blatt-Stadium: 6. Laubblatt entfaltet"
                    },
                    {
                        "17",
                        "7 leaves unfolded",
                        "7-Blatt-Stadium: 7. Laubblatt entfaltet"
                    },
                    {
                        "18",
                        "8 leaves unfolded",
                        "8-Blatt-Stadium: 8. Laubblatt entfaltet"
                    },
                    {
                        "19",
                        "9 or more leaves unfolded ",
                        "9 und mehr Laubbl??tter entfaltet"
                    },
                }
            },
            {
                "3",
                "2",
                "Tillering",
                "Bestockung",
                "2.png",
                new String[][]{
                    {
                        "21",
                        "Beginning of tillering: first tiller detectable",
                        "Beginn der Bestockung: 1. Bestockungstrieb sichtbar"
                    },
                    {
                        "22",
                        "2 tillers detectable",
                        "2. Bestockungstrieb sichtbar",
                    },
                    {
                        "23",
                        "3 tillers detectable",
                        "3. Bestockungstrieb sichtbar"
                    },
                    {
                        "24",
                        "4 tillers detectable",
                        "4. Bestockungstrieb sichtbar"
                    },
                    {
                        "25",
                        "5 tillers detectable",
                        "5. Bestockungstrieb sichtbar"
                    },
                    {
                        "26",
                        "6 tillers detectable",
                        "6. Bestockungstrieb sichtbar"
                    },
                    {
                        "27",
                        "7 tillers detectable",
                        "7. Bestockungstrieb sichtbar"
                    },
                    {
                        "28",
                        "8 tillers detectable",
                        "8. Bestockungstrieb sichtbar"
                    },
                    {
                        "29",
                        "Max number of tillers detectable",
                        "Ende der Bestockung: Maximale Anzahl der Bestockungstriebe erreicht"
                    },
                }
            },
            {
                "3",
                "3",
                "Stem elongation",
                "Schossen",
                "3.png",
                new String[][]{
                    {
                        "30",
                        "Panicle initiation or green ring stage: cholorophyll accumulates in teh stem tissue, forming a green ring",
                        "Beginn der Rispenanlage: Gr??nringstadium. Chlorophyll akkumuliert im Stengelgewebe und bildet einen gr??nen Ring"
                    },
                    {
                        "32",
                        "Panicle formation: panicle 1-2mm in length",
                        "Rispenbildung: Embryonale Rispe 1 - 2 mm lang"
                    },
                    {
                        "34",
                        "Internode elongation or joining state: internodes begin to elongate, panicle more than 2mm long (variety-dependent)",
                        "Schossen: Internodien strecken sich; Rispe l??nger als 2 mm (sortenabh??ngig)"
                    },
                    {
                        "37",
                        "Flag leaf just visible, still rolled,panicle moving upwards",
                        "Fahnenblatt gerade sichtbar, noch eingerollt"
                    },
                    {
                        "39",
                        "Flag leaf stage: flag leaf unfolded, collar regions (auricle and lingule) of flag leaf and penultimate leaf aligned(pre-boot stage)",
                        "Fahnenblatt-Stadium: Fahnenblatt entfaltet (pre-boot stage)"
                    }
                }
            },
            {
                "3",
                "4",
                "Booting",
                "Rispenschwellen",
                "4.png",
                new String[][]{
                    {
                        "41",
                        "Early boot stage: upper part of stem slightly thickened, sheath of flag leaf about 5 cm out of penultimate leaf sheath ",
                        "Early boot stage: Blattscheide des Fahnenblattes ??berragt vorletzte Blattscheide um ca. 5 cm",
                    },
                    {
                        "43",
                        "Mid boot stage: sheath of flag leaf 5???10 cm out of the penultimate leaf sheath ",
                        "Mid boot stage: Blattscheide des Fahnenblattes ??berragt vorletzte Blattscheide um 5 - 1 O cm"
                    },
                    {
                        "45",
                        "Late boot stage: flag leaf sheath swollen, sheath of flag leaf more than 10 cm out of penultimate leaf sheath ",
                        "Laie boot stage: Blattscheide des Fahnenblattes geschwollen, Blattscheide der Fahnenbl??tter ??berragt vorletzte Blattscheide um 10cm"
                    },
                    {
                        "47",
                        "Flag leaf sheath opening ",
                        "Blattscheide des Fahnenblattes ??ffnet sich"
                    },
                    {
                        "49",
                        "Flag leaf sheath open ",
                        "Fahnenblattscheide ge??ffnet"
                    }
                }
            },
            {
                "3",
                "5",
                "Inflorescence emergence, heading",
                "Entwicklung der Bl??tenanlage; Rispenschieben",
                "5.png",
                new String[][]{
                    {
                        "51",
                        "Beginning of panicle emergence: tip of inflorescence emerged from sheath ",
                        "Beginn des Rispenschiebens: Spitze der Rispe streckt sich aus der Blattscheide",
                    },
                    {
                        "52",
                        "20% of panicle emerged ",
                        "20 % der Rispe ausgetreten",
                    },
                    {
                        "53",
                        "30% of panicle emerged ",
                        "30 % der Rispe ausgetreten",
                    },
                    {
                        "54",
                        "40% of panicle emerged ",
                        "40 % der Rispe ausgetreten",
                    },
                    {
                        "55",
                        "Middle of panicle emergence: neck node still in sheath",
                        "Mitte des Rispenschiebens: Rispenknoten (neck node} noch in der Blattscheide"
                    },
                    {
                        "56",
                        "60% of panicle emerged ",
                        "60 % der Rispe ausgetreten",
                    },
                    {
                        "57",
                        "70% of panicle emerged ",
                        "70 % der Rispe ausgetreten",
                    },
                    {
                        "58",
                        "80% of panicle emerged ",
                        "80 % der Rispe ausgetreten",
                    },
                    {
                        "59",
                        "End of panicle emergence: neck node level with the flag leaf auricle, anthers not yet visible ",
                        "Ende des Rispenschiebens: Rispenknoten auf ??hrchenh??he der Fahnenbl??tter: Staubgef????e noch nicht sichtbar",
                    }
                }
            },
            {
                "3",
                "6",
                "Flowering, anthesis",
                "Bl??te",
                "6.png",
                new String[][]{
                    {
                        "61",
                        "Beginning of flowering: anthers visible at top of panicle",
                        "Beginn der Bl??te: Staubgef????e an der Spitze der Rispe sichtbar",
                    },
                    {
                        "65",
                        "Full flowering: anthers visible on most spikelets ",
                        "Mitte der Bl??te: Staubgef????e an den meisten ??hrchen sichtbar"
                    },
                    {
                        "69",
                        "End of flowering: all spikelets have completed flowering but some dehydrated anthers may remain",
                        "Ende der Bl??te: Alle Ahrchen sind abgebl??ht. Einzelne ausgetrocknete Staubgef????e sind noch sichtbar"
                    }
                }
            },
            {
                "3",
                "7",
                "Development of fruit",
                "Fruchtentwicklung",
                "7-9.png",
                new String[][]{
                    {
                        "71",
                        "Watery ripe: first grains have reached half their final size",
                        "Korninhalt w????rig. Erste K??rner haben die H??lfte der endg??ltigen Gr????e erreicht"
                    },
                    {
                        "73",
                        "Early milk ",
                        "Fr??he Milchreife",
                    },
                    {
                        "75",
                        "Medium milk: grain content milky ",
                        "Mitte Milchreife: Korninhalt milchig. K??rner haben ihre endg??ltige Gr????e erreicht"
                    },
                    {
                        "77",
                        "Late milk ",
                        "Sp??te Milchreife"
                    }
                }
            },
            {
                "3",
                "8",
                "Ripening",
                "Frucht- und Samenreife",
                "7-9.png",
                new String[][]{
                    {
                        "83",
                        "Early dough",
                        "Fr??he Teigreife",
                    },
                    {
                        "85",
                        "Soft dough: grain content soft but dry, fingernail impression not held, grains and glumes still green ",
                        "Teigreife: Korninhalt noch weich aber trocken. Fingernageleindruck reversibel"
                    },
                    {
                        "87",
                        "Hard dough: grain content solid, fingernail impression held ",
                        "Korninhalt fest, Fingernageleindruck irreversibel"
                    },
                    {
                        "89",
                        "Fully ripe: grain hard, difficult to divide with thumbnail",
                        "Vollreife: Korn ist hart, kann nur schwer mit dem Daumennagel gebrochen werden"
                    }
                }
            },
            {
                "3",
                "9",
                "Senescence",
                "Absterben",
                "7-9.png",
                new String[][]{
                    {
                        "92",
                        "Over-ripe: grain very hard, cannot be dented by thumbnail ",
                        "Totreife: Korn kann nicht mehr mit dem Daumennagel eingedr??ckt bzw. nicht mehr gebrochen werden"
                    },
                    {
                        "97",
                        "Plant dead and collapsing ",
                        "Pflanzen abgestorben, Halme brechen zusammen"
                    },
                    {
                        "99",
                        "Harvested product ",
                        "Erntegut"
                    }
                }
            },
        };
}
