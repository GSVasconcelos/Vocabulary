package com.example.vocabulary.ui.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.vocabulary.R;

public class ThemeManager {

    private static final String PREFS_NAME = "vocabulary_theme_prefs";
    private static final String KEY_THEME = "selected_theme";

    public static final String THEME_LIGHT = "theme_light";
    public static final String THEME_DRACULA = "theme_dracula";
    public static final String THEME_COFFEE = "theme_coffee";

    public static void saveTheme(Context context, String themeKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_THEME, themeKey).apply();
    }

    public static String getSavedTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_THEME, THEME_LIGHT);
    }

    public static int getThemeResId(Context context) {
        String themeKey = getSavedTheme(context);
        switch (themeKey) {
            case THEME_DRACULA:
                return R.style.Theme_Vocabulary_Dracula;
            case THEME_COFFEE:
                return R.style.Theme_Vocabulary_Coffee;
            case THEME_LIGHT:
            default:
                return R.style.Theme_Vocabulary_Light;
        }
    }

    public static void applyTheme(Activity activity) {
        // Desativa a influência do modo escuro/claro do sistema do aparelho
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        activity.setTheme(getThemeResId(activity));
    }
}
