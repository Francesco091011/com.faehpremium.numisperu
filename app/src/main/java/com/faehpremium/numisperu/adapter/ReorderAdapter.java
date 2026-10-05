package com.faehpremium.numisperu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faehpremium.numisperu.R;
import com.faehpremium.numisperu.model.CoinCollection;

import java.util.Collections;
import java.util.List;

public class ReorderAdapter extends RecyclerView.Adapter<ReorderAdapter.ViewHolder> {

    private List<CoinCollection> collections;

    public ReorderAdapter(List<CoinCollection> collections) {
        this.collections = collections;
    }

    public List<CoinCollection> getCollections() {
        return collections;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_reorder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoinCollection collection = collections.get(position);
        holder.textName.setText(collection.getName());

        holder.btnUp.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos > 0) {
                Collections.swap(collections, pos, pos - 1);
                notifyItemMoved(pos, pos - 1);
            }
        });

        holder.btnDown.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos < collections.size() - 1) {
                Collections.swap(collections, pos, pos + 1);
                notifyItemMoved(pos, pos + 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return collections != null ? collections.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        ImageButton btnUp;
        ImageButton btnDown;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_collection_name);
            btnUp = itemView.findViewById(R.id.btn_move_up);
            btnDown = itemView.findViewById(R.id.btn_move_down);
        }
    }
}
