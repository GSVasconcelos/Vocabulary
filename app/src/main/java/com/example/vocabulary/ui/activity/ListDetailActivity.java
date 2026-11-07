package com.example.vocabulary.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.vocabulary.R;
import com.example.vocabulary.data.model.VocabularyItem;
import com.example.vocabulary.databinding.ActivityListDetailBinding;
import com.example.vocabulary.databinding.DialogAddWordBinding;
import com.example.vocabulary.ui.adapter.VocabularyItemAdapter;
import com.example.vocabulary.ui.util.AccentInputHelper;
import com.example.vocabulary.ui.viewmodel.VocabularyViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ListDetailActivity extends BaseActivity {

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
            Toast.makeText(this, R.string.toast_invalid_list, Toast.LENGTH_SHORT).show();
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
        adapter = new VocabularyItemAdapter(item -> confirmDeleteWord(item));
        binding.rvWords.setLayoutManager(new LinearLayoutManager(this));
        binding.rvWords.setAdapter(adapter);
    }

    private void confirmDeleteWord(VocabularyItem item) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_word_title)
                .setMessage(getString(R.string.confirm_delete_word, item.getPrimaryText()))
                .setPositiveButton(R.string.delete, (dialog, which) -> viewModel.deleteWord(item))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void setupListeners() {
        binding.fabAddWord.setOnClickListener(v -> showAddWordDialog());

        binding.btnStartGame.setOnClickListener(v -> {
            if (currentWordCount == 0) {
                Toast.makeText(
                        ListDetailActivity.this,
                        R.string.toast_add_word_first,
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
                    binding.tvListSubtitle.setVisibility(android.view.View.VISIBLE);
                } else {
                    binding.tvListSubtitle.setVisibility(android.view.View.GONE);
                }
            }
        });

        viewModel.getItemsForList(listId).observe(this, items -> {
            currentWordCount = items != null ? items.size() : 0;
            adapter.submitList(items);

            if (items == null || items.isEmpty()) {
                binding.llEmptyWords.setVisibility(android.view.View.VISIBLE);
                binding.rvWords.setVisibility(android.view.View.GONE);
            } else {
                binding.llEmptyWords.setVisibility(android.view.View.GONE);
                binding.rvWords.setVisibility(android.view.View.VISIBLE);
            }
        });
    }

    private void showAddWordDialog() {
        DialogAddWordBinding dialogBinding = DialogAddWordBinding.inflate(LayoutInflater.from(this));

        // Permite digitar palavras com acentos normalmente (Água, Lâmpada, Avião, etc.)
        AccentInputHelper.attach(dialogBinding.etPrimaryText);
        AccentInputHelper.attach(dialogBinding.etAssociationText);

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
                                R.string.toast_fill_both_fields,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
