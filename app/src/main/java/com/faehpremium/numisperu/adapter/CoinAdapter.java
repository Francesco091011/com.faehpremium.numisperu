package com.faehpremium.numisperu.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.faehpremium.numisperu.R;
import com.faehpremium.numisperu.model.CoinSlot;

import java.util.List;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.ViewHolder> {

    private List<CoinSlot> coinSlots;
    private OnCoinClickListener listener;

    public interface OnCoinClickListener {
        void onCoinClick(CoinSlot slot);
        void onCoinLongClick(CoinSlot slot);
    }

    public CoinAdapter(List<CoinSlot> coinSlots, OnCoinClickListener listener) {
        this.coinSlots = coinSlots;
        this.listener = listener;
    }

    public void updateList(List<CoinSlot> newList) {
        this.coinSlots = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_coin_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoinSlot slot = coinSlots.get(position);
        holder.textLabel.setText(slot.getLabel());

        String yearMintText = (slot.getYear() > 0 ? slot.getYear() + " • " : "") + slot.getMint();
        holder.textMintYear.setText(yearMintText);

        if (slot.isOwned()) {
            holder.container.setBackgroundColor(Color.parseColor("#E8F5E9")); // Verde tenue numismático
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E8F5E9"));
            holder.textLabel.setTextColor(Color.parseColor("#1B5E20"));
            holder.textMintYear.setTextColor(Color.parseColor("#2E7D32"));
            holder.imgCheck.setVisibility(View.VISIBLE);

            // Quantity badge
            int qty = slot.getQuantity() > 0 ? slot.getQuantity() : 1;
            holder.textQuantityBadge.setText("x" + qty);
            holder.textQuantityBadge.setVisibility(View.VISIBLE);

            // Grade badge
            if (slot.getGrade() != null && !slot.getGrade().equalsIgnoreCase("Sin especificar")) {
                holder.textGradeBadge.setText(slot.getGrade());
                holder.textGradeBadge.setVisibility(View.VISIBLE);
            } else {
                holder.textGradeBadge.setVisibility(View.GONE);
            }
        } else {
            holder.container.setBackgroundColor(Color.parseColor("#FAFAFA")); // Gris claro no poseído
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FAFAFA"));
            holder.textLabel.setTextColor(Color.parseColor("#757575"));
            holder.textMintYear.setTextColor(Color.parseColor("#9E9E9E"));
            holder.imgCheck.setVisibility(View.GONE);
            holder.textQuantityBadge.setVisibility(View.GONE);
            holder.textGradeBadge.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCoinClick(slot);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onCoinLongClick(slot);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return coinSlots != null ? coinSlots.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        RelativeLayout container;
        TextView textLabel;
        TextView textMintYear;
        TextView textQuantityBadge;
        TextView textGradeBadge;
        ImageView imgCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_coin);
            container = itemView.findViewById(R.id.container);
            textLabel = itemView.findViewById(R.id.text_label);
            textMintYear = itemView.findViewById(R.id.text_mint_year);
            textQuantityBadge = itemView.findViewById(R.id.text_quantity_badge);
            textGradeBadge = itemView.findViewById(R.id.text_grade_badge);
            imgCheck = itemView.findViewById(R.id.img_check);
        }
    }
}
