package com.example.vocabulary.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.vocabulary.R;
import com.example.vocabulary.data.model.VocabularyListWithCount;
import com.example.vocabulary.databinding.ActivityMainBinding;
import com.example.vocabulary.databinding.DialogAddListBinding;
import com.example.vocabulary.ui.adapter.VocabularyListAdapter;
import com.example.vocabulary.ui.viewmodel.VocabularyViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private VocabularyViewModel viewModel;
    private VocabularyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(VocabularyViewModel.class);

        setupRecyclerView();
        setupListeners();
        observeViewModel();
    }

    private void setupRecyclerView() {
        adapter = new VocabularyListAdapter(new VocabularyListAdapter.OnListClickListener() {
            @Override
            public void onListClick(VocabularyListWithCount list) {
                openListDetail(list);
            }

            @Override
            public void onDeleteClick(VocabularyListWithCount list) {
                confirmDeleteList(list);
            }
        });

        binding.rvLists.setLayoutManager(new LinearLayoutManager(this));
        binding.rvLists.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.fabAddList.setOnClickListener(v -> showAddListDialog());
    }

    private void observeViewModel() {
        viewModel.getAllLists().observe(this, lists -> {
            adapter.submitList(lists);
            if (lists == null || lists.isEmpty()) {
                binding.llEmptyState.setVisibility(View.VISIBLE);
                binding.rvLists.setVisibility(View.GONE);
            } else {
                binding.llEmptyState.setVisibility(View.GONE);
                binding.rvLists.setVisibility(View.VISIBLE);
            }
        });
    }

    private void openListDetail(VocabularyListWithCount list) {
        Intent intent = new Intent(this, ListDetailActivity.class);
        intent.putExtra(ListDetailActivity.EXTRA_LIST_ID, list.getId());
        intent.putExtra(ListDetailActivity.EXTRA_LIST_NAME, list.getName());
        startActivity(intent);
    }

    private void showAddListDialog() {
        DialogAddListBinding dialogBinding = DialogAddListBinding.inflate(LayoutInflater.from(this));

        new MaterialAlertDialogBuilder(this)
                .setView(dialogBinding.getRoot())
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String name = dialogBinding.etListName.getText() != null
                            ? dialogBinding.etListName.getText().toString().trim()
                            : "";
                    String description = dialogBinding.etListDescription.getText() != null
                            ? dialogBinding.etListDescription.getText().toString().trim()
                            : "";

                    if (!name.isEmpty()) {
                        viewModel.createList(name, description);
                    } else {
                        Toast.makeText(MainActivity.this, "O nome da lista não pode ficar em branco", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void confirmDeleteList(VocabularyListWithCount list) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.confirm_delete_list)
                .setMessage("Deseja apagar a lista \"" + list.getName() + "\"? Todas as palavras dela serão perdidas.")
                .setPositiveButton(R.string.delete, (dialog, which) -> viewModel.deleteList(list.getId()))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
