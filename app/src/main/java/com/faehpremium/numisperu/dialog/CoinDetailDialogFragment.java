package com.faehpremium.numisperu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.faehpremium.numisperu.R;
import com.faehpremium.numisperu.db.DatabaseHelper;
import com.faehpremium.numisperu.model.CoinSlot;
import com.google.android.material.textfield.TextInputEditText;

public class CoinDetailDialogFragment extends DialogFragment {

    private static final String ARG_SLOT_ID = "SLOT_ID";

    private long slotId;
    private CoinSlot slot;
    private DatabaseHelper dbHelper;
    private OnCoinUpdatedListener listener;

    public interface OnCoinUpdatedListener {
        void onCoinUpdated();
    }

    public static CoinDetailDialogFragment newInstance(long slotId) {
        CoinDetailDialogFragment fragment = new CoinDetailDialogFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_SLOT_ID, slotId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCoinUpdatedListener) {
            listener = (OnCoinUpdatedListener) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            slotId = getArguments().getLong(ARG_SLOT_ID);
        }

        dbHelper = new DatabaseHelper(requireContext());
        slot = dbHelper.getCoinSlotById(slotId);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_coin_detail, null);

        TextView textTitle = view.findViewById(R.id.dialog_title);
        TextView textSubtitle = view.findViewById(R.id.dialog_subtitle);
        CheckBox checkOwned = view.findViewById(R.id.check_owned);
        TextInputEditText editQty = view.findViewById(R.id.edit_quantity);
        Button btnMinus = view.findViewById(R.id.btn_qty_minus);
        Button btnPlus = view.findViewById(R.id.btn_qty_plus);
        Spinner spinnerGrade = view.findViewById(R.id.spinner_grade);
        Spinner spinnerFinish = view.findViewById(R.id.spinner_finish);
        TextInputEditText editNotes = view.findViewById(R.id.edit_notes);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnSave = view.findViewById(R.id.btn_save);

        if (slot != null) {
            textTitle.setText(slot.getLabel());
            String subtitle = (slot.getYear() > 0 ? "Año " + slot.getYear() + " • " : "") + "Ceca: " + slot.getMint();
            textSubtitle.setText(subtitle);

            checkOwned.setChecked(slot.isOwned());
            int currentQty = slot.getQuantity() > 0 ? slot.getQuantity() : (slot.isOwned() ? 1 : 0);
            editQty.setText(String.valueOf(currentQty));
            editNotes.setText(slot.getNotes());
        }

        // Grados Sheldon / Estándar
        String[] grades = new String[]{
                "Sin especificar",
                "P-1 / FR-2 (Poor / Fair)",
                "AG-3 / G-4 (Good)",
                "VG-8 / VG-10 (Very Good)",
                "F-12 / F-15 (Fine)",
                "VF-20 / VF-30 (Very Fine)",
                "XF-40 / XF-45 (Extremely Fine)",
                "AU-50 / AU-58 (About Uncirculated)",
                "MS-60 / MS-62 (Uncirculated / Sin Circular)",
                "MS-63 / MS-64 (Choice Uncirculated)",
                "MS-65 Gem Unc (Gema Sin Circular)",
                "MS-67+ Superb Uncirculated",
                "PR-65 / Proof (Acuñación Prueba / BCRP)"
        };
        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, grades);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrade.setAdapter(gradeAdapter);

        if (slot != null && slot.getGrade() != null) {
            for (int i = 0; i < grades.length; i++) {
                if (grades[i].startsWith(slot.getGrade()) || grades[i].equalsIgnoreCase(slot.getGrade())) {
                    spinnerGrade.setSelection(i);
                    break;
                }
            }
        }

        // Tipos de Acabado
        String[] finishes = new String[]{
                "Circulación Regular",
                "Brillante Sin Circular (BU)",
                "Proof / Prueba (BCRP)",
                "Colorizada / Esmaltada",
                "Blister / Estuche Oficial BCRP"
        };
        ArrayAdapter<String> finishAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, finishes);
        finishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFinish.setAdapter(finishAdapter);

        if (slot != null && slot.getFinishType() != null) {
            for (int i = 0; i < finishes.length; i++) {
                if (finishes[i].equalsIgnoreCase(slot.getFinishType())) {
                    spinnerFinish.setSelection(i);
                    break;
                }
            }
        }

        // Listeners para botones +/-
        btnMinus.setOnClickListener(v -> {
            String qStr = editQty.getText() != null ? editQty.getText().toString() : "0";
            int q = 0;
            try { q = Integer.parseInt(qStr); } catch (Exception ignored) {}
            if (q > 0) q--;
            editQty.setText(String.valueOf(q));
            if (q == 0) checkOwned.setChecked(false);
        });

        btnPlus.setOnClickListener(v -> {
            String qStr = editQty.getText() != null ? editQty.getText().toString() : "0";
            int q = 0;
            try { q = Integer.parseInt(qStr); } catch (Exception ignored) {}
            q++;
            editQty.setText(String.valueOf(q));
            if (q > 0) checkOwned.setChecked(true);
        });

        checkOwned.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                String qStr = editQty.getText() != null ? editQty.getText().toString() : "0";
                if ("0".equals(qStr) || qStr.isEmpty()) {
                    editQty.setText("1");
                }
            } else {
                editQty.setText("0");
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());

        btnSave.setOnClickListener(v -> {
            if (slot != null) {
                boolean owned = checkOwned.isChecked();
                String qtyStr = editQty.getText() != null ? editQty.getText().toString() : "0";
                int qty = 0;
                try {
                    qty = Integer.parseInt(qtyStr);
                } catch (Exception ignored) {}

                if (qty > 0 && !owned) {
                    owned = true;
                } else if (qty == 0 && owned) {
                    qty = 1;
                }

                slot.setOwned(owned);
                slot.setQuantity(qty);

                String selectedGrade = spinnerGrade.getSelectedItem() != null ? spinnerGrade.getSelectedItem().toString() : "Sin especificar";
                // Extraer código corto si aplica
                if (selectedGrade.contains(" ")) {
                    String shortGrade = selectedGrade.split(" ")[0];
                    slot.setGrade(shortGrade);
                } else {
                    slot.setGrade(selectedGrade);
                }

                String selectedFinish = spinnerFinish.getSelectedItem() != null ? spinnerFinish.getSelectedItem().toString() : "Circulación Regular";
                slot.setFinishType(selectedFinish);

                String notesStr = editNotes.getText() != null ? editNotes.getText().toString() : "";
                slot.setNotes(notesStr);

                dbHelper.updateCoinSlot(slot);

                Toast.makeText(requireContext(), "Moneda actualizada correctamente", Toast.LENGTH_SHORT).show();
                if (listener != null) {
                    listener.onCoinUpdated();
                }
            }
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }
}
