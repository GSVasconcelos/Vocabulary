package com.example.vocabulary.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vocabulary.data.model.VocabularyItem;
import com.example.vocabulary.databinding.ItemVocabularyWordBinding;

import java.util.ArrayList;
import java.util.List;

public class VocabularyItemAdapter extends RecyclerView.Adapter<VocabularyItemAdapter.ViewHolder> {

    public interface OnWordDeleteClickListener {
        void onDeleteClick(VocabularyItem item);
    }

    private List<VocabularyItem> items = new ArrayList<>();
    private final OnWordDeleteClickListener listener;

    public VocabularyItemAdapter(OnWordDeleteClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<VocabularyItem> newItems) {
        this.items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVocabularyWordBinding binding = ItemVocabularyWordBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemVocabularyWordBinding binding;

        ViewHolder(ItemVocabularyWordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(VocabularyItem item) {
            binding.tvPrimaryWord.setText(item.getPrimaryText());
            binding.tvAssociationWord.setText(item.getAssociationText());

            String stats = "Revisões: " + item.getReviewCount() + " | Acertos: " + item.getCorrectCount();
            binding.tvWordStats.setText(stats);

            binding.btnDeleteWord.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(item);
                }
            });
        }
    }
}
