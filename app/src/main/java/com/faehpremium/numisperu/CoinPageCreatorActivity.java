package com.faehpremium.numisperu;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.faehpremium.numisperu.db.DatabaseHelper;
import com.faehpremium.numisperu.model.CoinSlot;
import com.google.android.material.textfield.TextInputEditText;

public class CoinPageCreatorActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextInputEditText editName, editStartYear, editEndYear, editMint, editDescription;
    private Spinner spinnerCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_page_creator);

        dbHelper = new DatabaseHelper(this);

        editName = findViewById(R.id.edit_name);
        editStartYear = findViewById(R.id.edit_start_year);
        editEndYear = findViewById(R.id.edit_end_year);
        editMint = findViewById(R.id.edit_mint);
        editDescription = findViewById(R.id.edit_description);
        spinnerCategory = findViewById(R.id.spinner_category);

        setupCategorySpinner();

        Button btnCreate = findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(v -> createAlbum());
    }

    private void setupCategorySpinner() {
        String[] categories = new String[]{
                "Series Conmemorativas",
                "Soles Modernos",
                "Soles Históricos",
                "Históricas & Plata",
                "Personalizadas"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void createAlbum() {
        String name = editName.getText() != null ? editName.getText().toString().trim() : "";
        String startStr = editStartYear.getText() != null ? editStartYear.getText().toString().trim() : "";
        String endStr = editEndYear.getText() != null ? editEndYear.getText().toString().trim() : "";
        String mint = editMint.getText() != null ? editMint.getText().toString().trim() : "Lma";
        String description = editDescription.getText() != null ? editDescription.getText().toString().trim() : "";
        String category = spinnerCategory.getSelectedItem() != null ? spinnerCategory.getSelectedItem().toString() : "Personalizadas";

        if (name.isEmpty() || startStr.isEmpty() || endStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos obligatorios (Nombre, Año Inicio y Año Fin)", Toast.LENGTH_SHORT).show();
            return;
        }

        int startYear, endYear;
        try {
            startYear = Integer.parseInt(startStr);
            endYear = Integer.parseInt(endStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Años inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startYear > endYear) {
            Toast.makeText(this, "El año de inicio debe ser menor o igual al año de fin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            description = "Álbum personalizado de monedas peruanas (" + startYear + " - " + endYear + ").";
        }

        long collectionId = dbHelper.createCustomCollection(name, startYear, endYear, description, category);

        int sortIdx = 0;
        for (int y = startYear; y <= endYear; y++) {
            CoinSlot slot = new CoinSlot();
            slot.setCollectionId(collectionId);
            slot.setLabel(name + " (" + y + ")");
            slot.setYear(y);
            slot.setMint(mint.isEmpty() ? "Lma" : mint);
            slot.setOwned(false);
            slot.setQuantity(0);
            slot.setCustomCoin(true);
            slot.setSortOrder(sortIdx++);

            dbHelper.addCoinSlot(slot);
        }

        Toast.makeText(this, "Álbum '" + name + "' creado exitosamente", Toast.LENGTH_SHORT).show();
        finish();
    }
}
