package com.example.vocabulary.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vocabulary.data.model.VocabularyListWithCount;
import com.example.vocabulary.databinding.ItemVocabularyListBinding;

import java.util.ArrayList;
import java.util.List;

public class VocabularyListAdapter extends RecyclerView.Adapter<VocabularyListAdapter.ViewHolder> {

    public interface OnListClickListener {
        void onListClick(VocabularyListWithCount list);
        void onDeleteClick(VocabularyListWithCount list);
    }

    private List<VocabularyListWithCount> items = new ArrayList<>();
    private final OnListClickListener listener;

    public VocabularyListAdapter(OnListClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<VocabularyListWithCount> newItems) {
        this.items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVocabularyListBinding binding = ItemVocabularyListBinding.inflate(
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
        private final ItemVocabularyListBinding binding;

        ViewHolder(ItemVocabularyListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(VocabularyListWithCount item) {
            binding.tvListName.setText(item.getName());

            if (item.getDescription() != null && !item.getDescription().trim().isEmpty()) {
                binding.tvListDescription.setText(item.getDescription());
                binding.tvListDescription.setVisibility(View.VISIBLE);
            } else {
                binding.tvListDescription.setVisibility(View.GONE);
            }

            String countLabel = item.getItemCount() == 1 ? "1 palavra" : item.getItemCount() + " palavras";
            binding.tvItemCount.setText(countLabel);

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onListClick(item);
                }
            });

            binding.btnDeleteList.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(item);
                }
            });
        }
    }
}
