package com.faehpremium.numisperu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faehpremium.numisperu.R;
import com.faehpremium.numisperu.db.DatabaseHelper;
import com.faehpremium.numisperu.model.CoinCollection;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<CoinCollection> collections;
    private DatabaseHelper dbHelper;
    private OnAlbumClickListener listener;

    public interface OnAlbumClickListener {
        void onAlbumClick(CoinCollection collection);
    }

    public AlbumAdapter(List<CoinCollection> collections, DatabaseHelper dbHelper, OnAlbumClickListener listener) {
        this.collections = collections;
        this.dbHelper = dbHelper;
        this.listener = listener;
    }

    public void updateCollections(List<CoinCollection> newCollections) {
        this.collections = newCollections;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_album_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoinCollection collection = collections.get(position);
        holder.textName.setText(collection.getName());
        
        String yearsText = collection.getStartYear() + " - " + collection.getEndYear() + " • Ceca: " + collection.getMintMask();
        holder.textYears.setText(yearsText);
        holder.textDesc.setText(collection.getDescription());
        holder.textCategory.setText(collection.getCategory() != null ? collection.getCategory() : "General");

        if (dbHelper != null) {
            int[] stats = dbHelper.getCollectionStats(collection.getId());
            int owned = stats[0];
            int total = stats[1];
            int totalQuantity = stats[2];

            int percent = total > 0 ? (int) ((owned * 100.0f) / total) : 0;
            holder.progressBar.setProgress(percent);
            
            String statsText = owned + " / " + total + " (" + percent + "%)";
            holder.textStats.setText(statsText);
            
            String qtyText = "Total de ejemplares en tu álbum: " + totalQuantity;
            holder.textQuantity.setText(qtyText);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAlbumClick(collection);
            }
        });
    }

    @Override
    public int getItemCount() {
        return collections != null ? collections.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textYears;
        TextView textDesc;
        TextView textCategory;
        TextView textStats;
        TextView textQuantity;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_album_name);
            textYears = itemView.findViewById(R.id.text_album_years);
            textDesc = itemView.findViewById(R.id.text_album_desc);
            textCategory = itemView.findViewById(R.id.text_album_category);
            textStats = itemView.findViewById(R.id.text_album_stats);
            textQuantity = itemView.findViewById(R.id.text_album_quantity);
            progressBar = itemView.findViewById(R.id.progress_album);
        }
    }
}
