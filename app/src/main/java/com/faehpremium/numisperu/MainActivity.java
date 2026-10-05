package com.faehpremium.numisperu;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faehpremium.numisperu.adapter.AlbumAdapter;
import com.faehpremium.numisperu.db.BackupManager;
import com.faehpremium.numisperu.db.DatabaseHelper;
import com.faehpremium.numisperu.model.CoinCollection;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private AlbumAdapter adapter;
    private TextView textGlobalOwned;
    private TextView textGlobalQuantity;
    private TextView textGlobalProgress;
    private ProgressBar progressGlobal;
    private ChipGroup chipGroupCategories;

    private String currentCategoryFilter = "Todas";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_albums);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        textGlobalOwned = findViewById(R.id.text_global_owned);
        textGlobalQuantity = findViewById(R.id.text_global_quantity);
        textGlobalProgress = findViewById(R.id.text_global_progress);
        progressGlobal = findViewById(R.id.progress_global);
        chipGroupCategories = findViewById(R.id.chip_group_categories);

        setupCategoryChips();

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CoinPageCreatorActivity.class)));

        loadData();
    }

    private void setupCategoryChips() {
        chipGroupCategories.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chip_commemorative) {
                currentCategoryFilter = "Series Conmemorativas";
            } else if (checkedId == R.id.chip_modern) {
                currentCategoryFilter = "Soles Modernos";
            } else if (checkedId == R.id.chip_historical) {
                currentCategoryFilter = "Soles Históricos";
            } else if (checkedId == R.id.chip_silver) {
                currentCategoryFilter = "Históricas & Plata";
            } else {
                currentCategoryFilter = "Todas";
            }
            loadAlbums();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reorder) {
            startActivity(new Intent(this, ReorderCollectionsActivity.class));
            return true;
        } else if (id == R.id.action_export) {
            if (BackupManager.exportData(this, dbHelper)) {
                Toast.makeText(this, "Copia de seguridad exportada a numisperu_backup.json", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error al exportar la copia de seguridad", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.action_import) {
            if (BackupManager.importData(this, dbHelper)) {
                Toast.makeText(this, "Colecciones importadas exitosamente", Toast.LENGTH_SHORT).show();
                loadData();
            } else {
                Toast.makeText(this, "Error al importar o archivo de respaldo no encontrado", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        final String githubUrl = "https://www.github.com/Francesco091011/com.faehpremium.numisperu";
        final String versionStr = "0.1.2";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acerca de NumisPerú");

        String message = "NumisPerú - Gestor & Catálogo Numismático de Monedas del Perú\n\n"
                + "• Versión: " + versionStr + "\n"
                + "• Desarrollador: Francesco\n"
                + "• Código Fuente:\n" + githubUrl + "\n\n"
                + "Permite llevar un control detallado y digital de álbumes numismáticos, grados de conservación y cantidad de monedas peruanas.";

        builder.setMessage(message);

        builder.setPositiveButton("Abrir GitHub", (dialog, which) -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "No se pudo abrir el navegador", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("Copiar Enlace", (dialog, which) -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("NumisPerú GitHub", githubUrl);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Enlace copiado al portapapeles", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cerrar", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void loadData() {
        updateGlobalStats();
        loadAlbums();
    }

    private void updateGlobalStats() {
        int[] stats = dbHelper.getTotalAppStats();
        int owned = stats[0];
        int total = stats[1];
        int quantity = stats[2];

        int percent = total > 0 ? (int) ((owned * 100.0f) / total) : 0;

        textGlobalOwned.setText(owned + " / " + total);
        textGlobalQuantity.setText(String.valueOf(quantity));
        textGlobalProgress.setText(percent + "%");
        progressGlobal.setProgress(percent);
    }

    private void loadAlbums() {
        List<CoinCollection> collections = dbHelper.getCollectionsByCategory(currentCategoryFilter);
        adapter = new AlbumAdapter(collections, dbHelper, collection -> {
            Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
            intent.putExtra("COLLECTION_ID", collection.getId());
            intent.putExtra("COLLECTION_NAME", collection.getName());
            intent.putExtra("COLLECTION_DESC", collection.getDescription());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}
