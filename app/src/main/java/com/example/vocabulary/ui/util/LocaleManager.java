package com.example.vocabulary.ui.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import java.util.Locale;

public class LocaleManager {

    private static final String PREFS_NAME = "vocabulary_locale_prefs";
    private static final String KEY_LOCALE = "selected_locale";

    public static final String[] SUPPORTED_CODES = {
            "pt", "en", "fr", "es", "de", "zh", "ja"
    };

    public static final String[] SUPPORTED_NAMES = {
            "Português",
            "English",
            "Français",
            "Español",
            "Deutsch",
            "中文 (Chinês)",
            "日本語 (Japonês)"
    };

    public static void init(Context context) {
        LocaleListCompat current = AppCompatDelegate.getApplicationLocales();
        if (current.isEmpty()) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String saved = prefs.getString(KEY_LOCALE, null);
            if (saved != null) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(saved));
            }
        }
    }

    public static void setLocale(Context context, String langCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_LOCALE, langCode).apply();

        // API oficial recomendada do AndroidX para internacionalização por aplicativo
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(langCode);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }

    public static String getSelectedLanguageCode(Context context) {
        LocaleListCompat locales = AppCompatDelegate.getApplicationLocales();
        if (!locales.isEmpty()) {
            Locale locale = locales.get(0);
            if (locale != null) {
                return locale.getLanguage();
            }
        }

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LOCALE, "pt");
    }

    public static String getSelectedLanguageDisplayName(Context context) {
        String code = getSelectedLanguageCode(context);
        for (int i = 0; i < SUPPORTED_CODES.length; i++) {
            if (SUPPORTED_CODES[i].equalsIgnoreCase(code)) {
                return SUPPORTED_NAMES[i];
            }
        }
        return "Português";
    }

    public static int getSelectedLanguageIndex(Context context) {
        String code = getSelectedLanguageCode(context);
        for (int i = 0; i < SUPPORTED_CODES.length; i++) {
            if (SUPPORTED_CODES[i].equalsIgnoreCase(code)) {
                return i;
            }
        }
        return 0;
    }
}
