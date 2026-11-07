package com.example.vocabulary.ui.activity;

import android.os.Bundle;

import com.example.vocabulary.R;
import com.example.vocabulary.databinding.ActivitySettingsBinding;
import com.example.vocabulary.ui.util.LocaleManager;
import com.example.vocabulary.ui.util.ThemeManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupViews();
        setupListeners();
    }

    private void setupToolbar() {
        binding.toolbar.setTitle(R.string.settings);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViews() {
        updateThemeSummary();
        updateLanguageSummary();
    }

    private void updateThemeSummary() {
        String currentTheme = ThemeManager.getSavedTheme(this);
        int themeRes;
        if (ThemeManager.THEME_DRACULA.equals(currentTheme)) {
            themeRes = R.string.theme_dracula_short;
        } else if (ThemeManager.THEME_COFFEE.equals(currentTheme)) {
            themeRes = R.string.theme_coffee_short;
        } else {
            themeRes = R.string.theme_light_short;
        }
        binding.tvCurrentTheme.setText(getString(themeRes));
    }

    private void updateLanguageSummary() {
        binding.tvCurrentLanguage.setText(LocaleManager.getSelectedLanguageDisplayName(this));
    }

    private void setupListeners() {
        binding.cardThemeSetting.setOnClickListener(v -> showThemeSelectionDialog());
        binding.cardLanguageSetting.setOnClickListener(v -> showLanguageSelectionDialog());
    }

    private void showThemeSelectionDialog() {
        String currentTheme = ThemeManager.getSavedTheme(this);
        int selectedIndex = 0;
        if (ThemeManager.THEME_DRACULA.equals(currentTheme)) {
            selectedIndex = 1;
        } else if (ThemeManager.THEME_COFFEE.equals(currentTheme)) {
            selectedIndex = 2;
        }

        String[] themes = new String[]{
                getString(R.string.theme_light),
                getString(R.string.theme_dracula),
                getString(R.string.theme_coffee)
        };

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.choose_theme)
                .setSingleChoiceItems(themes, selectedIndex, (dialog, which) -> {
                    String chosenTheme = ThemeManager.THEME_LIGHT;
                    if (which == 1) {
                        chosenTheme = ThemeManager.THEME_DRACULA;
                    } else if (which == 2) {
                        chosenTheme = ThemeManager.THEME_COFFEE;
                    }

                    ThemeManager.saveTheme(SettingsActivity.this, chosenTheme);
                    dialog.dismiss();
                    recreate();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showLanguageSelectionDialog() {
        int selectedIndex = LocaleManager.getSelectedLanguageIndex(this);

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.choose_language)
                .setSingleChoiceItems(LocaleManager.SUPPORTED_NAMES, selectedIndex, (dialog, which) -> {
                    String selectedCode = LocaleManager.SUPPORTED_CODES[which];
                    LocaleManager.setLocale(SettingsActivity.this, selectedCode);
                    dialog.dismiss();
                    recreate();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
