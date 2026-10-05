package com.faehpremium.numisperu.db;

import android.content.Context;
import android.util.Log;

import com.faehpremium.numisperu.model.CoinCollection;
import com.faehpremium.numisperu.model.CoinSlot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;

public class BackupManager {

    private static final String TAG = "BackupManager";

    public static boolean exportData(Context context, DatabaseHelper dbHelper) {
        try {
            JSONArray collectionsArray = new JSONArray();
            List<CoinCollection> collections = dbHelper.getAllCollections();

            for (CoinCollection col : collections) {
                JSONObject colJson = new JSONObject();
                colJson.put("id", col.getId());
                colJson.put("name", col.getName());
                colJson.put("startYear", col.getStartYear());
                colJson.put("endYear", col.getEndYear());
                colJson.put("description", col.getDescription());
                colJson.put("category", col.getCategory());
                colJson.put("isCustom", col.isCustom());
                colJson.put("sortOrder", col.getSortOrder());
                colJson.put("mintMask", col.getMintMask());

                JSONArray slotsArray = new JSONArray();
                List<CoinSlot> slots = dbHelper.getCoinSlots(col.getId());
                for (CoinSlot slot : slots) {
                    JSONObject slotJson = new JSONObject();
                    slotJson.put("id", slot.getId());
                    slotJson.put("coinIdentifier", slot.getCoinIdentifier());
                    slotJson.put("year", slot.getYear());
                    slotJson.put("mint", slot.getMint());
                    slotJson.put("label", slot.getLabel());
                    slotJson.put("isOwned", slot.isOwned());
                    slotJson.put("quantity", slot.getQuantity());
                    slotJson.put("grade", slot.getGrade());
                    slotJson.put("notes", slot.getNotes());
                    slotJson.put("finishType", slot.getFinishType());
                    slotJson.put("isCustomCoin", slot.isCustomCoin());
                    slotJson.put("sortOrder", slot.getSortOrder());
                    slotsArray.put(slotJson);
                }
                colJson.put("slots", slotsArray);
                collectionsArray.put(colJson);
            }

            File dir = context.getExternalFilesDir(null);
            File file = new File(dir, "numisperu_backup.json");
            FileWriter writer = new FileWriter(file);
            writer.write(collectionsArray.toString(2));
            writer.flush();
            writer.close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Export failed", e);
            return false;
        }
    }

    public static boolean importData(Context context, DatabaseHelper dbHelper) {
        try {
            File dir = context.getExternalFilesDir(null);
            File file = new File(dir, "numisperu_backup.json");
            if (!file.exists()) {
                file = new File(dir, "backup_numisperu.json");
            }
            if (!file.exists()) return false;

            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String json = new String(data, "UTF-8");

            JSONArray collectionsArray = new JSONArray(json);
            for (int i = 0; i < collectionsArray.length(); i++) {
                JSONObject colJson = collectionsArray.getJSONObject(i);
                String name = colJson.optString("name", "Colección Importada");
                int startYear = colJson.optInt("startYear", 2000);
                int endYear = colJson.optInt("endYear", 2025);
                String desc = colJson.optString("description", "");
                String category = colJson.optString("category", "Personalizadas");

                long colId = dbHelper.createCustomCollection(name, startYear, endYear, desc, category);

                if (colJson.has("slots")) {
                    JSONArray slotsArray = colJson.getJSONArray("slots");
                    for (int j = 0; j < slotsArray.length(); j++) {
                        JSONObject slotJson = slotsArray.getJSONObject(j);
                        CoinSlot slot = new CoinSlot();
                        slot.setCollectionId(colId);
                        slot.setCoinIdentifier(slotJson.optString("coinIdentifier", "IMP_" + j));
                        slot.setLabel(slotJson.optString("label", "Moneda"));
                        slot.setYear(slotJson.optInt("year", startYear));
                        slot.setMint(slotJson.optString("mint", "Lma"));
                        slot.setOwned(slotJson.optBoolean("isOwned", false));
                        slot.setQuantity(slotJson.optInt("quantity", slot.isOwned() ? 1 : 0));
                        slot.setGrade(slotJson.optString("grade", "Sin especificar"));
                        slot.setNotes(slotJson.optString("notes", ""));
                        slot.setFinishType(slotJson.optString("finishType", "Circulación"));
                        slot.setCustomCoin(slotJson.optBoolean("isCustomCoin", true));
                        slot.setSortOrder(slotJson.optInt("sortOrder", j));

                        dbHelper.addCoinSlot(slot);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Import failed", e);
            return false;
        }
    }
}
