package com.faehpremium.numisperu;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faehpremium.numisperu.adapter.ReorderAdapter;
import com.faehpremium.numisperu.db.DatabaseHelper;
import com.faehpremium.numisperu.model.CoinCollection;

import java.util.List;

public class ReorderCollectionsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ReorderAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reorder_collections);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Reordenar Colecciones");
        }

        dbHelper = new DatabaseHelper(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_reorder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<CoinCollection> collections = dbHelper.getAllCollections();
        adapter = new ReorderAdapter(collections);
        recyclerView.setAdapter(adapter);

        Button btnSave = findViewById(R.id.btn_save_order);
        btnSave.setOnClickListener(v -> {
            dbHelper.updateCollectionOrder(adapter.getCollections());
            Toast.makeText(this, "Orden de colecciones guardado", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
