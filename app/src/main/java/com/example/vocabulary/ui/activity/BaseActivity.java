package com.example.vocabulary.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vocabulary.ui.util.LocaleManager;
import com.example.vocabulary.ui.util.ThemeManager;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);
        LocaleManager.init(this);
        super.onCreate(savedInstanceState);
    }
}
