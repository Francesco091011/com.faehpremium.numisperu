package com.faehpremium.numisperu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.faehpremium.numisperu.model.CoinCollection;
import com.faehpremium.numisperu.model.CoinSlot;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "numisperu.db";
    private static final int DATABASE_VERSION = 4;

    // Nombres de Tablas
    public static final String TABLE_COLLECTIONS = "collections";
    public static final String TABLE_COIN_SLOTS = "coin_slots";

    // Columnas comunes
    public static final String KEY_ID = "id";

    // Tabla Colecciones
    public static final String KEY_COLLECTION_NAME = "name";
    public static final String KEY_START_YEAR = "start_year";
    public static final String KEY_END_YEAR = "end_year";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_IS_CUSTOM = "is_custom";
    public static final String KEY_SORT_ORDER = "sort_order";
    public static final String KEY_MINT_MASK = "mint_mask";

    // Tabla Casillas de Monedas (CoinSlot)
    public static final String KEY_COLLECTION_ID = "collection_id";
    public static final String KEY_COIN_IDENTIFIER = "coin_identifier";
    public static final String KEY_LABEL = "label";
    public static final String KEY_YEAR = "year";
    public static final String KEY_MINT = "mint";
    public static final String KEY_IS_OWNED = "is_owned";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_GRADE = "grade";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_FINISH_TYPE = "finish_type";
    public static final String KEY_IS_CUSTOM_COIN = "is_custom_coin";
    public static final String KEY_SLOT_SORT_ORDER = "slot_sort_order";

    // Sentencias de creación
    private static final String CREATE_TABLE_COLLECTIONS = "CREATE TABLE "
            + TABLE_COLLECTIONS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_COLLECTION_NAME + " TEXT,"
            + KEY_START_YEAR + " INTEGER,"
            + KEY_END_YEAR + " INTEGER,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_IS_CUSTOM + " INTEGER DEFAULT 0,"
            + KEY_SORT_ORDER + " INTEGER DEFAULT 0,"
            + KEY_MINT_MASK + " TEXT"
            + ")";

    private static final String CREATE_TABLE_COIN_SLOTS = "CREATE TABLE "
            + TABLE_COIN_SLOTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_COLLECTION_ID + " INTEGER,"
            + KEY_COIN_IDENTIFIER + " TEXT,"
            + KEY_LABEL + " TEXT,"
            + KEY_YEAR + " INTEGER,"
            + KEY_MINT + " TEXT,"
            + KEY_IS_OWNED + " INTEGER DEFAULT 0,"
            + KEY_QUANTITY + " INTEGER DEFAULT 0,"
            + KEY_GRADE + " TEXT,"
            + KEY_NOTES + " TEXT,"
            + KEY_FINISH_TYPE + " TEXT,"
            + KEY_IS_CUSTOM_COIN + " INTEGER DEFAULT 0,"
            + KEY_SLOT_SORT_ORDER + " INTEGER DEFAULT 0"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COLLECTIONS);
        db.execSQL(CREATE_TABLE_COIN_SLOTS);
        seedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COIN_SLOTS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        ensureSchema(db);
    }

    private void ensureSchema(SQLiteDatabase db) {
        try {
            boolean rebuildNeeded = false;

            Cursor cCol = db.rawQuery("PRAGMA table_info(" + TABLE_COLLECTIONS + ")", null);
            boolean hasCategory = false;
            if (cCol != null) {
                while (cCol.moveToNext()) {
                    int nameIdx = cCol.getColumnIndex("name");
                    if (nameIdx != -1) {
                        String name = cCol.getString(nameIdx);
                        if (KEY_CATEGORY.equalsIgnoreCase(name)) {
                            hasCategory = true;
                            break;
                        }
                    }
                }
                cCol.close();
            }

            Cursor cSlot = db.rawQuery("PRAGMA table_info(" + TABLE_COIN_SLOTS + ")", null);
            boolean hasQuantity = false;
            if (cSlot != null) {
                while (cSlot.moveToNext()) {
                    int nameIdx = cSlot.getColumnIndex("name");
                    if (nameIdx != -1) {
                        String name = cSlot.getString(nameIdx);
                        if (KEY_QUANTITY.equalsIgnoreCase(name)) {
                            hasQuantity = true;
                            break;
                        }
                    }
                }
                cSlot.close();
            }

            if (!hasCategory || !hasQuantity) {
                rebuildNeeded = true;
            }

            if (rebuildNeeded) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTIONS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_COIN_SLOTS);
                onCreate(db);
            }
        } catch (Exception e) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COIN_SLOTS);
            onCreate(db);
        }
    }

    private void seedData(SQLiteDatabase db) {
        seedRiquezaYOrgullo(db);
        seedRecursosNaturales(db);
        seedFaunaSilvestre(db);
        seedConstructores(db);
        seedMujeresIndependencia(db);
        seedSeriesConmemorativasBCRP(db);
        seedUnSolCirculacion(db);
        seedMonedasBimetalicas(db);
        seedCentavosModernos(db);
        seedSolesDeOro(db);
        seedIntis(db);
        seedMonedasHistoricasPlata(db);
    }

    private void seedRiquezaYOrgullo(SQLiteDatabase db) {
        long collectionId = insertCollection(db, "Riqueza y Orgullo del Perú", 2010, 2016,
                "Serie numismática conmemorativa de 26 monedas de 1 Sol de aleación Alpaca.", "Series Conmemorativas", "Lma", 1);

        String[] coins = {
            "Tumi de Oro (Lambayeque)", "Sarcófago de Karajía (Amazonas)", "Estela de Raimondi (Ancash)",
            "Chullpas de Sillustani (Puno)", "Monasterio de Santa Catalina (Arequipa)", "Machu Picchu (Cusco)",
            "Gran Pajatén (San Martín)", "Piedra de Saywite (Apurímac)", "Fortaleza del Real Felipe (Callao)",
            "Templo del Sol - Vilcashuamán (Ayacucho)", "Kuntur Wasi (Cajamarca)", "Templo de las Manos Cruzadas - Kotosh (Huánuco)",
            "Complejo Arqueológico de Túcume (Lambayeque)", "Cerámica Vicús (Piura)", "Templo de Huaytará (Huancavelica)",
            "Complejo Arqueológico de Chavín (Ancash)", "Catedral de Lima (Lima)", "Huaca de la Luna (La Libertad)",
            "Antiguo Hotel de Inmigrantes (Callao)", "Canal de Cumbemayo (Cajamarca)", "Complejo Arqueológico de Huarautambo (Pasco)",
            "Complejo Arqueológico de Inka Wasi (Huancavelica)", "Tapiz Paracas (Ica)", "Sitio Arqueológico de Cabeza de Vaca (Tumbes)",
            "Petroglifos de Pusharo (Madre de Dios)", "Arquitectura Moqueguana (Moquegua)"
        };

        int[] years = {
            2010, 2010, 2010, 2011, 2011, 2011, 2011, 2012, 2012, 2012, 2012,
            2013, 2013, 2013, 2013, 2014, 2014, 2014, 2014, 2015, 2015, 2015, 2015,
            2016, 2016, 2016
        };

        for (int i = 0; i < coins.length; i++) {
            insertSlot(db, collectionId, coins[i], years[i], "Lma", "1SOL_RYO_" + (i + 1), "Circulación", i);
        }
    }

    private void seedRecursosNaturales(SQLiteDatabase db) {
        long collectionId = insertCollection(db, "Recursos Naturales del Perú", 2013, 2013,
                "Serie numismática de 3 monedas de 1 Sol destacando recursos emblemáticos.", "Series Conmemorativas", "Lma", 2);

        String[] coins = {"La Anchoveta", "El Cacao", "La Quinua"};
        for (int i = 0; i < coins.length; i++) {
            insertSlot(db, collectionId, coins[i], 2013, "Lma", "1SOL_RN_" + (i + 1), "Circulación", i);
        }
    }

    private void seedFaunaSilvestre(SQLiteDatabase db) {
        long collectionId = insertCollection(db, "Fauna Silvestre Amenazada del Perú", 2017, 2019,
                "Serie numismática de 10 monedas de 1 Sol sensibilizando la conservación ecológica.", "Series Conmemorativas", "Lma", 3);

        String[] coins = {
            "Oso Andino de Anteojos", "Cocodrilo de Tumbes", "Cóndor Andino",
            "Tapir Andino", "Pava Aliblanca", "Jaguar", "Suri",
            "Mono Choro de Cola Amarilla", "Gato Andino", "Rana Gigante del Titicaca"
        };
        int[] years = {2017, 2017, 2017, 2018, 2018, 2018, 2018, 2019, 2019, 2019};

        for (int i = 0; i < coins.length; i++) {
            insertSlot(db, collectionId, coins[i], years[i], "Lma", "1SOL_FAUNA_" + (i + 1), "Circulación", i);
        }
    }

    private void seedConstructores(SQLiteDatabase db) {
        long collectionId = insertCollection(db, "Constructores de la República", 2021, 2024,
                "Serie numismática del Bicentenario de la Independencia (9 monedas).", "Series Conmemorativas", "Lma", 4);

        String[] coins = {
            "Juan Pablo Viscardo y Guzmán", "Hipólito Unanue y Pavón", "Toribio Rodríguez de Mendoza",
            "José Baquíjano y Carrillo de Córdoba", "Micaela Bastidas Puyucahua", "José Faustino Sánchez Carrión",
            "Manuel Lorenzo de Vidaurre", "José de la Mar y Cortázar", "José de la Riva-Agüero y Sánchez Boquete"
        };
        int[] years = {2021, 2021, 2021, 2022, 2022, 2022, 2023, 2023, 2024};

        for (int i = 0; i < coins.length; i++) {
            insertSlot(db, collectionId, coins[i], years[i], "Lma", "1SOL_CONST_" + (i + 1), "Circulación", i);
        }
    }

    private void seedMujeresIndependencia(SQLiteDatabase db) {
        long collectionId = insertCollection(db, "La Mujer en el Proceso de Independencia", 2020, 2021,
                "Serie especial rindiendo homenaje a las heroínas de la Patria.", "Series Conmemorativas", "Lma", 5);

        String[] coins = {
            "Heroínas Toledo", "Brígida Silva de Ochoa", "María Parado de Bellido"
        };
        int[] years = {2020, 2020, 2021};

        for (int i = 0; i < coins.length; i++) {
            insertSlot(db, collectionId, coins[i], years[i], "Lma", "1SOL_MUJER_" + (i + 1), "Circulación", i);
        }
    }

    private void seedSeriesConmemorativasBCRP(SQLiteDatabase db) {
        long collectionId = insertCollection(db, "Monedas Conmemorativas y Aniversarios BCRP", 2015, 2026,
                "Emisiones especiales de 1 Sol en homenaje a sucesos históricos.", "Series Conmemorativas", "Lma", 6);

        String[] coins = {
            "450 Años de la Casa Nacional de Moneda (2015)",
            "200 Años de la Casa Nacional de Moneda (2021)",
            "Bicentenario de la Marina de Guerra del Perú (2021)",
            "100 Años del Banco Central de Reserva del Perú (2022)",
            "Bicentenario de la Batalla de Junín (2024)",
            "Bicentenario de la Batalla de Ayacucho (2024)",
            "Aniversario Numismático Nacional (2025)"
        };
        int[] years = {2015, 2021, 2021, 2022, 2024, 2024, 2025};

        for (int i = 0; i < coins.length; i++) {
            insertSlot(db, collectionId, coins[i], years[i], "Lma", "1SOL_CONM_" + (i + 1), "Circulación / Unc", i);
        }
    }

    private void seedUnSolCirculacion(SQLiteDatabase db) {
        long collectionId = insertCollection(db, "1 Sol Regular de Circulación", 1991, 2026,
                "Monedas de 1 Sol / 1 Nuevo Sol ordenadas por año y variantes de acuñación.", "Soles Modernos", "Lma", 7);

        int index = 0;
        // Variantes emblemáticas
        insertSlot(db, collectionId, "1 Nuevo Sol (Con Sistema Braille)", 1991, "Lma", "1SOL_1991_BRAILLE", "Circulación", index++);
        insertSlot(db, collectionId, "1 Nuevo Sol (Sin Braille)", 1991, "Lma", "1SOL_1991_STD", "Circulación", index++);
        insertSlot(db, collectionId, "1 Nuevo Sol (Firma de Diseñador)", 1994, "Lma", "1SOL_1994_FIRMA", "Circulación", index++);

        for (int year = 1992; year <= 2026; year++) {
            if (year == 1994) continue;
            String label = (year >= 2016) ? "1 Sol (" + year + ")" : "1 Nuevo Sol (" + year + ")";
            insertSlot(db, collectionId, label, year, "Lma", "1SOL_" + year, "Circulación", index++);
        }
    }

    private void seedMonedasBimetalicas(SQLiteDatabase db) {
        long collectionId = insertCollection(db, "2 y 5 Soles Bimetálicas", 1994, 2026,
                "Serie de monedas bimetálicas con los Colibríes y Guacamayos de Nasca y el Ave Fragata.", "Soles Modernos", "Lma", 8);

        int index = 0;
        for (int year = 1994; year <= 2026; year++) {
            insertSlot(db, collectionId, "2 Soles (" + year + " - Nasca)", year, "Lma", "2SOL_" + year, "Circulación", index++);
            insertSlot(db, collectionId, "5 Soles (" + year + " - Ave Fragata)", year, "Lma", "5SOL_" + year, "Circulación", index++);
        }
    }

    private void seedCentavosModernos(SQLiteDatabase db) {
        long collectionId = insertCollection(db, "Centavos de Sol / Nuevo Sol", 1991, 2026,
                "Fracciones de 1, 5, 10, 20 y 50 Centavos de Sol.", "Soles Modernos", "Lma", 9);

        int index = 0;
        for (int y = 1991; y <= 2011; y++) {
            insertSlot(db, collectionId, "1 Centavo (" + y + ")", y, "Lma", "1CENT_" + y, "Circulación", index++);
        }
        for (int y = 1991; y <= 2026; y += 2) {
            insertSlot(db, collectionId, "10 Centavos (" + y + ")", y, "Lma", "10CENT_" + y, "Circulación", index++);
            insertSlot(db, collectionId, "20 Centavos (" + y + ")", y, "Lma", "20CENT_" + y, "Circulación", index++);
            insertSlot(db, collectionId, "50 Centavos (" + y + ")", y, "Lma", "50CENT_" + y, "Circulación", index++);
        }
    }

    private void seedSolesDeOro(SQLiteDatabase db) {
        long collectionId = insertCollection(db, "Soles de Oro", 1930, 1985,
                "Monedas históricas del Perú de la era del Sol de Oro.", "Soles Históricos", "Lma", 10);

        int index = 0;
        String[] denoms = {"1/2 Sol de Oro", "1 Sol de Oro Latón", "1 Sol de Oro Cuproníquel", "5 Soles de Oro", "10 Soles de Oro", "50 Soles de Oro", "100 Soles de Oro"};
        int[] sampleYears = {1935, 1943, 1966, 1970, 1976, 1980, 1982};

        for (int i = 0; i < denoms.length; i++) {
            insertSlot(db, collectionId, denoms[i] + " (" + sampleYears[i] + ")", sampleYears[i], "Lma", "SOL_ORO_" + (i + 1), "Circulación", index++);
        }
    }

    private void seedIntis(SQLiteDatabase db) {
        long collectionId = insertCollection(db, "Intis", 1985, 1991,
                "Catálogo de monedas de la unidad monetaria Inti.", "Soles Históricos", "Lma", 11);

        int index = 0;
        String[] denoms = {"1/2 Inti (1985)", "1 Inti (1986)", "5 Intis (1987)", "10 Intis (1988)", "50 Intis (1989)", "100 Intis (1990)", "500 Intis (1990)"};
        int[] years = {1985, 1986, 1987, 1988, 1989, 1990, 1990};

        for (int i = 0; i < denoms.length; i++) {
            insertSlot(db, collectionId, denoms[i], years[i], "Lma", "INTI_" + (i + 1), "Circulación", index++);
        }
    }

    private void seedMonedasHistoricasPlata(SQLiteDatabase db) {
        long collectionId = insertCollection(db, "Monedas Históricas de Plata y República", 1863, 1935,
                "Monedas coloniales e históricas de la República del Perú (1 Sol Plata, Dinero, Quinto).", "Históricas & Plata", "Lma / Cuzco", 12);

        int index = 0;
        insertSlot(db, collectionId, "1 Sol Plata ('Firme y Feliz por la Unión')", 1869, "Lma", "PLATA_1SOL_1869", "Plata (.900)", index++);
        insertSlot(db, collectionId, "1 Sol Plata ('Firme y Feliz por la Unión')", 1893, "Lma", "PLATA_1SOL_1893", "Plata (.900)", index++);
        insertSlot(db, collectionId, "1 Dinero - 10 Centavos Plata", 1890, "Lma", "PLATA_DINERO_1890", "Plata (.900)", index++);
        insertSlot(db, collectionId, "1/2 Dinero - 5 Centavos Plata", 1895, "Lma", "PLATA_MEDIO_DINERO", "Plata (.900)", index++);
        insertSlot(db, collectionId, "1/5 Sol Plata (Quinto)", 1905, "Lma", "PLATA_QUINTO_1905", "Plata (.900)", index++);
        insertSlot(db, collectionId, "Moneda Histórica Cuzco 1/2 Real", 1838, "Cuzco", "PLATA_CUZCO_1838", "Plata Histórica", index++);
        insertSlot(db, collectionId, "Arequipa 1/5 Sol Plata", 1885, "Arequipa", "PLATA_AREQ_1885", "Plata Histórica", index++);
    }

    private long insertCollection(SQLiteDatabase db, String name, int startYear, int endYear, String description, String category, String mintMask, int sortOrder) {
        ContentValues values = new ContentValues();
        values.put(KEY_COLLECTION_NAME, name);
        values.put(KEY_START_YEAR, startYear);
        values.put(KEY_END_YEAR, endYear);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_CATEGORY, category);
        values.put(KEY_IS_CUSTOM, 0);
        values.put(KEY_SORT_ORDER, sortOrder);
        values.put(KEY_MINT_MASK, mintMask);
        return db.insert(TABLE_COLLECTIONS, null, values);
    }

    private void insertSlot(SQLiteDatabase db, long collectionId, String label, int year, String mint, String identifier, String finishType, int sortOrder) {
        ContentValues slot = new ContentValues();
        slot.put(KEY_COLLECTION_ID, collectionId);
        slot.put(KEY_LABEL, label);
        slot.put(KEY_YEAR, year);
        slot.put(KEY_MINT, mint);
        slot.put(KEY_COIN_IDENTIFIER, identifier);
        slot.put(KEY_IS_OWNED, 0);
        slot.put(KEY_QUANTITY, 0);
        slot.put(KEY_GRADE, "Sin especificar");
        slot.put(KEY_NOTES, "");
        slot.put(KEY_FINISH_TYPE, finishType);
        slot.put(KEY_IS_CUSTOM_COIN, 0);
        slot.put(KEY_SLOT_SORT_ORDER, sortOrder);
        db.insert(TABLE_COIN_SLOTS, null, slot);
    }

    public List<CoinCollection> getAllCollections() {
        return getCollectionsByCategory(null);
    }

    public List<CoinCollection> getCollectionsByCategory(String category) {
        List<CoinCollection> collections = new ArrayList<>();
        String query;
        String[] args = null;

        if (category == null || category.isEmpty() || category.equalsIgnoreCase("Todas")) {
            query = "SELECT * FROM " + TABLE_COLLECTIONS + " ORDER BY " + KEY_SORT_ORDER + " ASC, " + KEY_ID + " ASC";
        } else {
            query = "SELECT * FROM " + TABLE_COLLECTIONS + " WHERE " + KEY_CATEGORY + " = ? ORDER BY " + KEY_SORT_ORDER + " ASC, " + KEY_ID + " ASC";
            args = new String[]{category};
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, args);

        if (c.moveToFirst()) {
            int idIndex = c.getColumnIndex(KEY_ID);
            int nameIndex = c.getColumnIndex(KEY_COLLECTION_NAME);
            int startYearIndex = c.getColumnIndex(KEY_START_YEAR);
            int endYearIndex = c.getColumnIndex(KEY_END_YEAR);
            int descIndex = c.getColumnIndex(KEY_DESCRIPTION);
            int categoryIndex = c.getColumnIndex(KEY_CATEGORY);
            int customIndex = c.getColumnIndex(KEY_IS_CUSTOM);
            int sortIndex = c.getColumnIndex(KEY_SORT_ORDER);
            int mintIndex = c.getColumnIndex(KEY_MINT_MASK);

            do {
                CoinCollection col = new CoinCollection();
                if (idIndex != -1) col.setId(c.getLong(idIndex));
                if (nameIndex != -1) col.setName(c.getString(nameIndex));
                if (startYearIndex != -1) col.setStartYear(c.getInt(startYearIndex));
                if (endYearIndex != -1) col.setEndYear(c.getInt(endYearIndex));
                if (descIndex != -1) col.setDescription(c.getString(descIndex));
                if (categoryIndex != -1) col.setCategory(c.getString(categoryIndex));
                if (customIndex != -1) col.setCustom(c.getInt(customIndex) == 1);
                if (sortIndex != -1) col.setSortOrder(c.getInt(sortIndex));
                if (mintIndex != -1) col.setMintMask(c.getString(mintIndex));
                collections.add(col);
            } while (c.moveToNext());
        }
        c.close();
        return collections;
    }

    public List<CoinSlot> getCoinSlots(long collectionId) {
        List<CoinSlot> slots = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_COIN_SLOTS + " WHERE " + KEY_COLLECTION_ID + " = ? ORDER BY " + KEY_SLOT_SORT_ORDER + " ASC, " + KEY_ID + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{String.valueOf(collectionId)});

        if (c.moveToFirst()) {
            int idIndex = c.getColumnIndex(KEY_ID);
            int collIdIndex = c.getColumnIndex(KEY_COLLECTION_ID);
            int identifierIndex = c.getColumnIndex(KEY_COIN_IDENTIFIER);
            int yearIndex = c.getColumnIndex(KEY_YEAR);
            int mintIndex = c.getColumnIndex(KEY_MINT);
            int ownedIndex = c.getColumnIndex(KEY_IS_OWNED);
            int qtyIndex = c.getColumnIndex(KEY_QUANTITY);
            int gradeIndex = c.getColumnIndex(KEY_GRADE);
            int notesIndex = c.getColumnIndex(KEY_NOTES);
            int labelIndex = c.getColumnIndex(KEY_LABEL);
            int finishIndex = c.getColumnIndex(KEY_FINISH_TYPE);
            int customCoinIndex = c.getColumnIndex(KEY_IS_CUSTOM_COIN);
            int sortOrderIndex = c.getColumnIndex(KEY_SLOT_SORT_ORDER);

            do {
                CoinSlot slot = new CoinSlot();
                if (idIndex != -1) slot.setId(c.getLong(idIndex));
                if (collIdIndex != -1) slot.setCollectionId(c.getLong(collIdIndex));
                if (identifierIndex != -1) slot.setCoinIdentifier(c.getString(identifierIndex));
                if (yearIndex != -1) slot.setYear(c.getInt(yearIndex));
                if (mintIndex != -1) slot.setMint(c.getString(mintIndex));
                if (ownedIndex != -1) slot.setOwned(c.getInt(ownedIndex) == 1);
                if (qtyIndex != -1) slot.setQuantity(c.getInt(qtyIndex));
                if (gradeIndex != -1) slot.setGrade(c.getString(gradeIndex));
                if (notesIndex != -1) slot.setNotes(c.getString(notesIndex));
                if (labelIndex != -1) slot.setLabel(c.getString(labelIndex));
                if (finishIndex != -1) slot.setFinishType(c.getString(finishIndex));
                if (customCoinIndex != -1) slot.setCustomCoin(c.getInt(customCoinIndex) == 1);
                if (sortOrderIndex != -1) slot.setSortOrder(c.getInt(sortOrderIndex));
                slots.add(slot);
            } while (c.moveToNext());
        }
        c.close();
        return slots;
    }

    public CoinSlot getCoinSlotById(long slotId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_COIN_SLOTS + " WHERE " + KEY_ID + " = ?", new String[]{String.valueOf(slotId)});
        CoinSlot slot = null;
        if (c.moveToFirst()) {
            slot = new CoinSlot();
            int idIdx = c.getColumnIndex(KEY_ID);
            int collIdIdx = c.getColumnIndex(KEY_COLLECTION_ID);
            int identIdx = c.getColumnIndex(KEY_COIN_IDENTIFIER);
            int yearIdx = c.getColumnIndex(KEY_YEAR);
            int mintIdx = c.getColumnIndex(KEY_MINT);
            int ownedIdx = c.getColumnIndex(KEY_IS_OWNED);
            int qtyIdx = c.getColumnIndex(KEY_QUANTITY);
            int gradeIdx = c.getColumnIndex(KEY_GRADE);
            int notesIdx = c.getColumnIndex(KEY_NOTES);
            int labelIdx = c.getColumnIndex(KEY_LABEL);
            int finishIdx = c.getColumnIndex(KEY_FINISH_TYPE);
            int customIdx = c.getColumnIndex(KEY_IS_CUSTOM_COIN);
            int sortIdx = c.getColumnIndex(KEY_SLOT_SORT_ORDER);

            if (idIdx != -1) slot.setId(c.getLong(idIdx));
            if (collIdIdx != -1) slot.setCollectionId(c.getLong(collIdIdx));
            if (identIdx != -1) slot.setCoinIdentifier(c.getString(identIdx));
            if (yearIdx != -1) slot.setYear(c.getInt(yearIdx));
            if (mintIdx != -1) slot.setMint(c.getString(mintIdx));
            if (ownedIdx != -1) slot.setOwned(c.getInt(ownedIdx) == 1);
            if (qtyIdx != -1) slot.setQuantity(c.getInt(qtyIdx));
            if (gradeIdx != -1) slot.setGrade(c.getString(gradeIdx));
            if (notesIdx != -1) slot.setNotes(c.getString(notesIdx));
            if (labelIdx != -1) slot.setLabel(c.getString(labelIdx));
            if (finishIdx != -1) slot.setFinishType(c.getString(finishIdx));
            if (customIdx != -1) slot.setCustomCoin(c.getInt(customIdx) == 1);
            if (sortIdx != -1) slot.setSortOrder(c.getInt(sortIdx));
        }
        c.close();
        return slot;
    }

    public void updateCoinSlot(CoinSlot slot) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IS_OWNED, slot.isOwned() ? 1 : 0);
        values.put(KEY_QUANTITY, slot.getQuantity());
        values.put(KEY_GRADE, slot.getGrade());
        values.put(KEY_NOTES, slot.getNotes());
        values.put(KEY_FINISH_TYPE, slot.getFinishType());
        values.put(KEY_LABEL, slot.getLabel());
        values.put(KEY_MINT, slot.getMint());
        values.put(KEY_YEAR, slot.getYear());

        db.update(TABLE_COIN_SLOTS, values, KEY_ID + " = ?", new String[]{String.valueOf(slot.getId())});
    }

    public void updateOwnership(long slotId, boolean isOwned, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IS_OWNED, isOwned ? 1 : 0);
        values.put(KEY_QUANTITY, quantity);
        db.update(TABLE_COIN_SLOTS, values, KEY_ID + " = ?", new String[]{String.valueOf(slotId)});
    }

    public long createCustomCollection(String name, int startYear, int endYear, String description, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COLLECTION_NAME, name);
        values.put(KEY_START_YEAR, startYear);
        values.put(KEY_END_YEAR, endYear);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_CATEGORY, (category != null && !category.isEmpty()) ? category : "Personalizadas");
        values.put(KEY_IS_CUSTOM, 1);
        values.put(KEY_SORT_ORDER, 99);
        values.put(KEY_MINT_MASK, "Lma");
        return db.insert(TABLE_COLLECTIONS, null, values);
    }

    public void addCoinSlot(CoinSlot slot) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COLLECTION_ID, slot.getCollectionId());
        values.put(KEY_COIN_IDENTIFIER, slot.getCoinIdentifier() != null ? slot.getCoinIdentifier() : "CUSTOM_" + System.currentTimeMillis());
        values.put(KEY_YEAR, slot.getYear());
        values.put(KEY_MINT, slot.getMint());
        values.put(KEY_LABEL, slot.getLabel());
        values.put(KEY_IS_OWNED, slot.isOwned() ? 1 : 0);
        values.put(KEY_QUANTITY, slot.getQuantity());
        values.put(KEY_GRADE, slot.getGrade());
        values.put(KEY_NOTES, slot.getNotes());
        values.put(KEY_FINISH_TYPE, slot.getFinishType());
        values.put(KEY_IS_CUSTOM_COIN, slot.isCustomCoin() ? 1 : 0);
        values.put(KEY_SLOT_SORT_ORDER, slot.getSortOrder());
        db.insert(TABLE_COIN_SLOTS, null, values);
    }

    public void deleteCollection(long collectionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COIN_SLOTS, KEY_COLLECTION_ID + " = ?", new String[]{String.valueOf(collectionId)});
        db.delete(TABLE_COLLECTIONS, KEY_ID + " = ?", new String[]{String.valueOf(collectionId)});
    }

    public void updateCollectionOrder(List<CoinCollection> collections) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < collections.size(); i++) {
                CoinCollection col = collections.get(i);
                ContentValues values = new ContentValues();
                values.put(KEY_SORT_ORDER, i);
                db.update(TABLE_COLLECTIONS, values, KEY_ID + " = ?", new String[]{String.valueOf(col.getId())});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public int[] getCollectionStats(long collectionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*), SUM(CASE WHEN " + KEY_IS_OWNED + " = 1 THEN 1 ELSE 0 END), SUM(" + KEY_QUANTITY + ") FROM " + TABLE_COIN_SLOTS + " WHERE " + KEY_COLLECTION_ID + " = ?", new String[]{String.valueOf(collectionId)});
        int totalSlots = 0;
        int ownedSlots = 0;
        int totalQuantity = 0;

        if (c.moveToFirst()) {
            totalSlots = c.getInt(0);
            ownedSlots = c.getInt(1);
            totalQuantity = c.getInt(2);
        }
        c.close();
        return new int[]{ownedSlots, totalSlots, totalQuantity};
    }

    public int[] getTotalAppStats() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*), SUM(CASE WHEN " + KEY_IS_OWNED + " = 1 THEN 1 ELSE 0 END), SUM(" + KEY_QUANTITY + ") FROM " + TABLE_COIN_SLOTS, null);
        int totalSlots = 0;
        int ownedSlots = 0;
        int totalQuantity = 0;

        if (c.moveToFirst()) {
            totalSlots = c.getInt(0);
            ownedSlots = c.getInt(1);
            totalQuantity = c.getInt(2);
        }
        c.close();
        return new int[]{ownedSlots, totalSlots, totalQuantity};
    }
}
