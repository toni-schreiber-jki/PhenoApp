package de.bund.jki.jki_bonitur.db.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import de.bund.jki.jki_bonitur.db.BbchMainStadium;
import de.bund.jki.jki_bonitur.db.BbchStadium;
 
public class BbchCereal extends BbchDataImport{

    public Object[][] getData()
    {
        return data;
    };

    public static Object[][] data = new Object[][]
        {
            {
                "1",
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
                "1",
                "1",
                "Leaf development",
                "Blattentwicklung",
                "1.png",
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
                "1",
                "2",
                "Germination",
                "Bestocking",
                "2.png",
                new String[][]{
                    {
                        "20",
                        "No tillers",
                        "Keine Bestockung",
                    },
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
                    "1",
                    "3",
                    "Stem elongation",
                    "Schossen (Haupttrieb)",
                    "3.png",
                    new String[][]{
                        {
                            "30",
                            "Beginning of stem elongation: pseudostem and tillers erect, first internode begins to elongate, top of inflorescence at least 1 cm above tillering node ",
                            "Beginn des Schosses: Haupttrieb und Bestockungstriebe stark aufgerichtet, beginnen sich zu strecken. Ährenspitzen mind. 1 cm vom Bestockungsknoten entfernt",
                        },
                        {
                            "31",
                            "First node at least 1 cm above tillering node ",
                            "1-Knoten-Stadium: 1. Knoten dicht über der Bodenoberfläche wahrnehmbar,mind. 1 cm vom Bestockungsknoten entfernt",
                        },
                        {
                            "32",
                            "Node 2 at least 2 cm above node 1 ",
                            "2-Knoten-Stadium: 2. Knoten wahrnehmbar, mind. 2 cm vom 1. Knoten entfernt",
                        },
                        {
                            "33",
                            "Node 3 at least 2 cm above node 2 ",
                            "3-Knoten-Stadium: 3. Knoten wahrnehmbar, mind. 2 cm vom 1. Knoten entfernt",
                        },
                        {
                            "34",
                            "Node 4 at least 2 cm above node 3 ",
                            "4-Knoten-Stadium: 4. Knoten wahrnehmbar, mind. 2 cm vom 1. Knoten entfernt",
                        },
                        {
                            "35",
                            "Node 5 at least 2 cm above node 4 ",
                            "5-Knoten-Stadium: 5. Knoten wahrnehmbar, mind. 2 cm vom 1. Knoten entfernt",
                        },
                        {
                            "36",
                            "Node 6 at least 2 cm above node 5 ",
                            "6-Knoten-Stadium: 6. Knoten wahrnehmbar, mind. 2 cm vom 1. Knoten entfernt",
                        },
                        {
                            "37",
                            "Flag leaf just visible, still rolled ",
                            "Erscheinen des letzten Blattes (Fahnenblatt); letztes Blatt noch eingerollt"
                        },
                        {
                            "39",
                            "Flag leaf stage: flag leaf fully unrolled, ligule just visible ",
                            "Ligula (Blatthäutchen)-Stadium: Blatthäutchen des Fahnenblattes gerade sichtbar, Fahnenblatt voll entwickelt"
                        }
                    }
                },
                {
                    "1",
                    "4",
                    "Booting",
                    "Ähren-/Rispenschwellen",
                    "4.png",
                    new String[][]{
                        {
                            "41",
                            "Early boot stage: flag leaf sheath extending ",
                            "Blattscheide des Fahnenblattes verlängert sich",
                        },
                        {
                            "43",
                            "Mid boot stage: flag leaf sheath just visibly swollen ",
                            "Ähre/Rispe ist im Halm aufwärts geschoben. Blattscheide des Fahnenblattes beginnt anzuschwellen"
                        },
                        {
                            "45",
                            "Late boot stage: flag leaf sheath swollen ",
                            "Blattscheide des Fahnenblattes geschwollen",
                        },
                        {
                            "47",
                            "Flag leaf sheath opening ",
                            "Blattscheide des Fahnenblattes öffnet sich"
                        },
                        {
                            "49",
                            "First awns visible (in awned forms only) ",
                            "Grannenspitzen: Grannen werden über der Ligula des Fahnenblattes sichtbar"
                        }
                    }
                },
                {
                    "1",
                    "5",
                    "Inflorescence emergence, heading",
                    "Ähren-/Rispenschieben",
                    "5.png",
                    new String[][]{
                        {
                            "51",
                            "Beginning of heading: tip of inflorescence emerged from sheath, first spikelet just visible ",
                            "Beginn des Ähren-/Rispenschiebens: Die Spitze der Ähre/Rispe tritt heraus oder drängt seitlich aus der Blattscheide"
                        },
                        {
                            "52",
                            "20% of inflorescence emerged",
                            "20 % der Ähre/Rispe ausgetreten"
                        },
                        {
                            "53",
                            "30% of inflorescence emerged",
                            "30 % der Ähre/Rispe ausgetreten"
                        },
                        {
                            "54",
                            "40% of inflorescence emerged",
                            "40 % der Ähre/Rispe ausgetreten"
                        },
                        {
                            "55",
                            "Middle of heading: half of inflorescence emerged ",
                            "Mitte des Ähren-/Rispenschiebens: Basis noch in der Blattscheide"
                        },
                        {
                            "56",
                            "60% of inflorescence emerged",
                            "60 % der Ähre/Rispe ausgetreten"
                        },{
                            "57",
                            "70% of inflorescence emerged",
                            "70 % der Ähre/Rispe ausgetreten"
                        },{
                            "58",
                            "80% of inflorescence emerged",
                            "80 % der Ähre/Rispe ausgetreten"
                        },
                        {
                            "59",
                            "End of heading: inflorescence fully emerged ",
                            "Ende des Ähren-/Rispenschiebens: Ähre/Rispe vollständig sichtbar"
                        }
                    }
            },
            {
                "1",
                "6",
                "Flowering, anthesis",
                "Blüte",
                "6.png",
                new String[][]{
                    {
                        "61",
                        "Beginning of flowering",
                        "Beginn der Blüte: Erste Staubgefäße werden sichtbar"
                    },
                    {
                        "65",
                        "Full flowering: 50% of anthers mature ",
                        "Mitte der Blüte: 50 % reife Staubgefäße",
                    },
                    {
                        "69",
                        "End of flowering",
                        "Ende der Blüte",
                    }
                }
            },
            {
            "1",
            "7",
            "Development of fruit",
            "Fruchtentwicklung",
            "7.png",
            new String[][]{
                {
                    "71",
                    "Watery ripe: first grains have reached half their final size",
                    "Korninhalt wäßrig. Erste Körner haben die Hälfte der endgültigen Größe erreicht"
                },
                {
                    "73",
                    "Early milk ",
                    "Frühe Milchreife",
                },
                {
                    "75",
                    "Medium milk: grain content milky ",
                    "Mitte Milchreife: Korninhalt milchig. Körner haben ihre endgültige Größe erreicht"
                },
                {
                    "77",
                    "Late milk ",
                    "Späte Milchreife"
                } 
            }
        },
        {
            "1",
            "8",
            "Ripening",
            "Frucht- und Samenreife",
            "",
            new String[][]{
                {
                    "83",
                    "Early dough",
                    "Frühe Teigreife",
                },
                {
                    "85",
                    "Soft dough: grain content soft but dry, fingernail impression not held, grains and glumes still green ",
                    "Teigreife: Korninhalt noch weich aber trocken. Fingernageleindruck reversibel"
                },
                {
                    "87",
                    "Hard dough: grain content solid, fingernail impression held ",
                    "Gelbreife: Korninhalt noch weich aber trocken. Fingernageleindruck reversibel"
                },
                {
                    "89",
                    "Fully ripe: grain hard, difficult to divide with thumbnail",
                    "Vollreife: Korn ist hart, kann nur schwer mit dem Daumennagel gebrochen werden"
                }
            }
        },
        {
            "1",
            "9",
            "Senescence",
            "Absterben",
            "",
            new String[][]{
                {
                    "92",
                    "Over-ripe: grain very hard, cannot be dented by thumbnail ",
                    "Totreife: Korn kann nicht mehr mit dem Daumennagel eingedrückt bzw. nicht mehr gebrochen werden"
                },
                {
                    "93",
                    "Grains loosening in day-time ",
                    "Körner lockern sich tagsüber"
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
        }
    };
}
