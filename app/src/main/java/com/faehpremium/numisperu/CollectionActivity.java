package com.faehpremium.numisperu;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faehpremium.numisperu.adapter.CoinAdapter;
import com.faehpremium.numisperu.db.DatabaseHelper;
import com.faehpremium.numisperu.dialog.CoinDetailDialogFragment;
import com.faehpremium.numisperu.model.CoinSlot;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity implements CoinDetailDialogFragment.OnCoinUpdatedListener {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private CoinAdapter adapter;
    private List<CoinSlot> allCoinList = new ArrayList<>();
    private List<CoinSlot> displayedCoinList = new ArrayList<>();
    private long currentCollectionId;

    private TextView textTitle, textStats, textDesc, textTotalQty;
    private ProgressBar progressBar;

    private int currentFilterMode = 0; // 0 = All, 1 = Owned only, 2 = Missing only

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        currentCollectionId = getIntent().getLongExtra("COLLECTION_ID", 1);
        String collectionName = getIntent().getStringExtra("COLLECTION_NAME");
        String collectionDesc = getIntent().getStringExtra("COLLECTION_DESC");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(collectionName != null ? collectionName : "Colección");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);

        textTitle = findViewById(R.id.text_collection_title);
        textStats = findViewById(R.id.text_collection_badge_stats);
        textDesc = findViewById(R.id.text_collection_desc);
        textTotalQty = findViewById(R.id.text_collection_total_qty);
        progressBar = findViewById(R.id.progress_collection);

        if (collectionName != null) textTitle.setText(collectionName);
        if (collectionDesc != null) textDesc.setText(collectionDesc);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collection_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter_all) {
            currentFilterMode = 0;
            applyFilter();
            return true;
        } else if (item.getItemId() == R.id.action_filter_owned) {
            currentFilterMode = 1;
            applyFilter();
            return true;
        } else if (item.getItemId() == R.id.action_filter_missing) {
            currentFilterMode = 2;
            applyFilter();
            return true;
        } else if (item.getItemId() == R.id.action_add_custom_coin) {
            showAddCoinDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadData() {
        allCoinList = dbHelper.getCoinSlots(currentCollectionId);
        updateHeaderStats();
        applyFilter();
    }

    private void updateHeaderStats() {
        int[] stats = dbHelper.getCollectionStats(currentCollectionId);
        int owned = stats[0];
        int total = stats[1];
        int totalQty = stats[2];

        int percent = total > 0 ? (int) ((owned * 100.0f) / total) : 0;

        textStats.setText(owned + " / " + total + " Monedas (" + percent + "%)");
        textTotalQty.setText("Total ejemplares en colección: " + totalQty);
        progressBar.setProgress(percent);
    }

    private void applyFilter() {
        displayedCoinList.clear();
        for (CoinSlot slot : allCoinList) {
            if (currentFilterMode == 1 && !slot.isOwned()) continue;
            if (currentFilterMode == 2 && slot.isOwned()) continue;
            displayedCoinList.add(slot);
        }

        if (adapter == null) {
            adapter = new CoinAdapter(displayedCoinList, new CoinAdapter.OnCoinClickListener() {
                @Override
                public void onCoinClick(CoinSlot slot) {
                    boolean newState = !slot.isOwned();
                    int newQty = newState ? (slot.getQuantity() > 0 ? slot.getQuantity() : 1) : 0;

                    slot.setOwned(newState);
                    slot.setQuantity(newQty);
                    dbHelper.updateOwnership(slot.getId(), newState, newQty);

                    updateHeaderStats();
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCoinLongClick(CoinSlot slot) {
                    CoinDetailDialogFragment dialog = CoinDetailDialogFragment.newInstance(slot.getId());
                    dialog.show(getSupportFragmentManager(), "CoinDetailDialog");
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(displayedCoinList);
        }
    }

    private void showAddCoinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añadir Moneda a este Álbum");

        final EditText input = new EditText(this);
        input.setHint("Nombre / Denominación de la moneda");
        builder.setView(input);

        builder.setPositiveButton("Añadir", (dialog, which) -> {
            String label = input.getText().toString().trim();
            if (!label.isEmpty()) {
                CoinSlot slot = new CoinSlot();
                slot.setCollectionId(currentCollectionId);
                slot.setLabel(label);
                slot.setYear(2025);
                slot.setMint("Lma");
                slot.setOwned(false);
                slot.setQuantity(0);
                slot.setCustomCoin(true);

                dbHelper.addCoinSlot(slot);
                Toast.makeText(CollectionActivity.this, "Moneda añadida correctamente", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onCoinUpdated() {
        loadData();
    }
}
