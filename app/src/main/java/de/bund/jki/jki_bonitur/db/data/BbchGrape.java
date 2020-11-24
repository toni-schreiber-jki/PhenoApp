package de.bund.jki.jki_bonitur.db.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import de.bund.jki.jki_bonitur.db.BbchMainStadium;
import de.bund.jki.jki_bonitur.db.BbchStadium;

public class BbchGrape extends BbchDataImport{

    public Object[][] getData() {
        return data;
    }

    public static Object[][] data = new Object[][]
        {
            {
                "5",
                "0",
                "Sprouting/Bud development",
                "Austrieb",
                "",
                new String[][]{
                    {
                        "0",
                        "Dormancy: winter buds pointed to rounded, light or dark brown according to cultivar; bud scales more or less closed according to cultivar",
                        "Vegetationsruhe: Winteraugen spitz bis rundbogenförmig, je nach Rebsorte hell- bis dunkelbraun; Knospenschuppen je nach Rebsorte mehr oder weniger geschlossen"
                    },
                    {
                        "1",
                        "Beginning of bud swelling: buds begin to expand inside the bud scales",
                        "Beginn des Knospenschwellens: Augen beginnen sich innerhalb der Knospenschuppen zu vergrössern"
                    },
                    {
                        "3",
                        "End of bud swelling: buds swollen, but not green",
                        "Ende des Knospenschwellens: Knospen geschwollen, aber noch nicht grün"
                    },
                    {
                        "5",
                        "\"Wool stage\": brown wool clearly visible",
                        "\"Wolle-Stadium\": wolleartiger brauner Haarbesatz deutlich sichtbar"
                    },
                    {
                        "7",
                        "Beginning of bud burst: green shoot tips just visible",
                        "Beginn des Knospenaufbruchs: grüne Triebspitzen werden sichtbar"
                    },
                    {
                        "9",
                        "Bud burst: green shoot tips clearly visible",
                        "Knospenaufbruch: grüne Triebspitzen deutlich sichtbar"
                    }
                }
            },
            {
                "5",
                "1",
                "Leaf development",
                "Blattentwicklung",
                "",
                new String[][]{
                    {
                        "11",
                        "First leaf unfolded and spread away from shoot",
                        "1. Laubblatt entfaltet und vom Trieb abgespreizt"
                    },
                    {
                        "12",
                        "2nd leaves unfolded",
                        "2 Laubblätter entfaltet"
                    },
                    {
                        "13",
                        "3rd leaves unfolded",
                        "3 Laubblätter entfaltet"
                    },
                    {
                        "14",
                        "4th leaves unfolded",
                        "4 Laubblätter entfaltet"
                    },
                    {
                        "15",
                        "5th leaves unfolded",
                        "5 Laubblätter entfaltet"
                    },
                    {
                        "16",
                        "6th leaves unfolded",
                        "6 Laubblätter entfaltet"
                    },
                    {
                        "17",
                        "7th leaves unfolded",
                        "7 Laubblätter entfaltet"
                    },
                    {
                        "18",
                        "8th leaves unfolded",
                        "8 Laubblätter entfaltet"
                    },
                    {
                        "19",
                        "9 or more leaves unfolded",
                        "9 oder mehr Laubblätter entfaltet"
                    }
                }
            },
            {
                "5",
                "5",
                "Inflorescence emerge",
                "Entwicklung der Blütenanlagen",
                "",
                new String[][]{
                    {
                        "53",
                        "Inflorescences clearly visible",
                        "Gescheine (Infloreszenzen) deutlich sichtbar"
                    },
                    {
                        "55",
                        "Inflorescences swelling, flowers closely pressed together",
                        "Gescheine (Infloreszenzen) vergrössern sich; Einzelblüten sind dicht zusammengedrängt"
                    },
                    {
                        "57",
                        "Inflorescences fully developed; flowers separating",
                        "Gescheine (Infloreszenzen) sind voll entwickelt; die Einzelblüten spreizen sich"
                    }
                }
            },
            {
                "5",
                "6",
                "Flowering",
                "Blüte",
                "",
                new String[][]{
                    {
                        "60",
                        "First flowerhoods detached from the receptacle",
                        "Erste Blütenkäppchen lösen sich vom Blütenboden"
                    },
                    {
                        "61",
                        "Beginning of flowering: 10% of flowerhoods fallen",
                        "Beginn der Blüte: 10% der Blütenkäppchen abgeworfen"
                    },
                    {
                        "62",
                        "20% of flowerhoods fallen",
                        "20% der Blütenkäppchen abgeworfen"
                    },
                    {
                        "63",
                        "Early flowering: 30% of flowerhoods fallen",
                        "Vorblüte: 30% der Blütenkäppchen abgeworfen"
                    },
                    {
                        "64",
                        "30% of flowerhoods fallen",
                        "40% der Blütenkäppchen abgeworfen"
                    },
                    {
                        "65",
                        "Full flowering: 50% of flowerhoods fallen",
                        "Vollblüte: 50% der Blütenkäppchen abgeworfen"
                    },
                    {
                        "66",
                        "60% of flowerhoods fallen",
                        "60% der Blütenkäppchen abgeworfen"
                    },
                    {
                        "67",
                        "70% of flowerhoods fallen",
                        "70% der Blütenkäppchen abgeworfen"
                    },
                    {
                        "68",
                        "80% of flowerhoods fallen",
                        "80% der Blütenkäppchen abgeworfen"
                    },
                    {
                        "69",
                        "End of flowering",
                        "Ende der Blüte"
                    },
                }
            },
            {
                "5",
                "7",
                "Development of fruits",
                "Fruchtentwicklung",
                "",
                new String[][]{
                    {
                        "71",
                        "Fruit set: young fruits begin to swell, remains of flowers lost",
                        "Fruchtansatz; Fruchtknoten beginnen sich zu vergrössern; «Putzen der Beeren» wird abgeschlossen"
                    },
                    {
                        "73",
                        "Berries groat-sized, bunches begin to hang",
                        "Beeren sind schrotkorngross; Trauben beginnen sich abzusenken"
                    },
                    {
                        "75",
                        "Berries pea-sized, bunches hang",
                        "Beeren sind erbsengross; Trauben hängen"
                    },
                    {
                        "77",
                        "Berries beginning to touch",
                        "Beginn des Traubenschlusses"
                    },
                    {
                        "79",
                        "Majority of berries touching",
                        "Ende des Traubenschlusse"
                    },
                }
            },
            {
                "5",
                "8",
                "Ripening of berries",
                "Fruchtreife",
                "",
                new String[][]{
                    {
                        "81",
                        "Beginning of ripening: berries begin to develop variety-specific colour",
                        "Beginn der Reife, Beeren beginnen hell zu werden (bzw. beginnen sich zu verfärben)"
                    },
                    {
                        "83",
                        "Berries developing colour",
                        "Fortschreiten der Beeren-Aufhellung (bzw. Beerenverfärbung)"
                    },
                    {
                        "85",
                        "Softening of berries",
                        "Weichwerden der Beeren"
                    },
                    {
                        "89",
                        "Berries ripe for harvest<",
                        "Vollreife der Beeren (Lesereife)"
                    },
                }
            },
            {
                "5",
                "9",
                "Senescence",
                "Eintreten der Vegetationsruhe",
                "",
                new String[][]{
                    {
                        "91",
                        "After harvest; end of wood maturation",
                        "Nach der Lese: Abschluss der Holzreife"
                    },
                    {
                        "92",
                        "Beginning of leaf discolouration",
                        "Beginn der Laubblattverfärbung"
                    },
                    {
                        "93",
                        "Beginning of leaf-fall",
                        "Beginn des Laubblattfalls"
                    },
                    {
                        "95",
                        "50% of leaves fallen",
                        "50% der Laubblätter abgefallen"
                    },
                    {
                        "97",
                        "End of leaf-fall",
                        "Ende des Laubblattfalls"
                    },
                    {
                        "99",
                        "Harvested product",
                        "Erntegut /Trauben"
                    },
                }
            },

        };

}
