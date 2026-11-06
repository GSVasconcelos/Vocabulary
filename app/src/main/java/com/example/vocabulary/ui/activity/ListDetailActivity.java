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
import com.example.vocabulary.databinding.ActivityListDetailBinding;
import com.example.vocabulary.databinding.DialogAddWordBinding;
import com.example.vocabulary.ui.adapter.VocabularyItemAdapter;
import com.example.vocabulary.ui.viewmodel.VocabularyViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ListDetailActivity extends AppCompatActivity {

    public static final String EXTRA_LIST_ID = "extra_list_id";
    public static final String EXTRA_LIST_NAME = "extra_list_name";

    private ActivityListDetailBinding binding;
    private VocabularyViewModel viewModel;
    private VocabularyItemAdapter adapter;

    private long listId = -1L;
    private String listName = "";
    private int currentWordCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listId = getIntent().getLongExtra(EXTRA_LIST_ID, -1L);
        listName = getIntent().getStringExtra(EXTRA_LIST_NAME);
        if (listName == null) listName = "";

        if (listId == -1L) {
            Toast.makeText(this, "Lista inválida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(VocabularyViewModel.class);

        setupToolbar();
        setupRecyclerView();
        setupListeners();
        observeViewModel();
    }

    private void setupToolbar() {
        binding.toolbar.setTitle(listName);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new VocabularyItemAdapter(item -> viewModel.deleteWord(item));
        binding.rvWords.setLayoutManager(new LinearLayoutManager(this));
        binding.rvWords.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.fabAddWord.setOnClickListener(v -> showAddWordDialog());

        binding.btnStartGame.setOnClickListener(v -> {
            if (currentWordCount == 0) {
                Toast.makeText(
                        ListDetailActivity.this,
                        "Adicione pelo menos uma palavra para começar a recapitulação!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            Intent intent = new Intent(ListDetailActivity.this, GameActivity.class);
            intent.putExtra(GameActivity.EXTRA_LIST_ID, listId);
            intent.putExtra(GameActivity.EXTRA_LIST_NAME, listName);
            startActivity(intent);
        });
    }

    private void observeViewModel() {
        viewModel.getList(listId).observe(this, list -> {
            if (list != null) {
                binding.toolbar.setTitle(list.getName());
                if (list.getDescription() != null && !list.getDescription().trim().isEmpty()) {
                    binding.tvListSubtitle.setText(list.getDescription());
                    binding.tvListSubtitle.setVisibility(View.VISIBLE);
                } else {
                    binding.tvListSubtitle.setVisibility(View.GONE);
                }
            }
        });

        viewModel.getItemsForList(listId).observe(this, items -> {
            currentWordCount = items != null ? items.size() : 0;
            adapter.submitList(items);

            if (items == null || items.isEmpty()) {
                binding.llEmptyWords.setVisibility(View.VISIBLE);
                binding.rvWords.setVisibility(View.GONE);
            } else {
                binding.llEmptyWords.setVisibility(View.GONE);
                binding.rvWords.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showAddWordDialog() {
        DialogAddWordBinding dialogBinding = DialogAddWordBinding.inflate(LayoutInflater.from(this));

        new MaterialAlertDialogBuilder(this)
                .setView(dialogBinding.getRoot())
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String primaryText = dialogBinding.etPrimaryText.getText() != null
                            ? dialogBinding.etPrimaryText.getText().toString().trim()
                            : "";
                    String associationText = dialogBinding.etAssociationText.getText() != null
                            ? dialogBinding.etAssociationText.getText().toString().trim()
                            : "";

                    if (!primaryText.isEmpty() && !associationText.isEmpty()) {
                        viewModel.addWord(listId, primaryText, associationText);
                    } else {
                        Toast.makeText(
                                ListDetailActivity.this,
                                "Preencha tanto a palavra principal quanto a associação",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
