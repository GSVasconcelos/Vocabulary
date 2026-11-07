package com.example.vocabulary.ui.util;

import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class AccentInputHelper {

    /**
     * Anexa um listener no EditText para combinar teclas mortas (dead keys)
     * e acentos do teclado físico/emulador (ex: ~ + a = ã, ´ + a = á, ^ + e = ê).
     */
    public static void attach(EditText editText) {
        if (editText == null) return;

        editText.setOnKeyListener(new View.OnKeyListener() {
            private int pendingAccent = 0;

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return false;
                }

                // Se apertar Backspace ou tecla de navegação, limpa qualquer acento pendente
                if (keyCode == KeyEvent.KEYCODE_DEL || keyCode == KeyEvent.KEYCODE_FORWARD_DEL
                        || keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (pendingAccent != 0) {
                        pendingAccent = 0;
                        return true;
                    }
                    return false;
                }

                int unicodeChar = event.getUnicodeChar();

                // 1. Detecta tecla morta nativa do Android (flag COMBINING_ACCENT)
                if ((unicodeChar & KeyCharacterMap.COMBINING_ACCENT) != 0) {
                    pendingAccent = unicodeChar & KeyCharacterMap.COMBINING_ACCENT_MASK;
                    return true;
                }

                // 2. Detecta acento avulso enviado pelo teclado do PC no emulador (~, ´, `, ^, ¨, ')
                if (pendingAccent == 0 && isAccentChar(unicodeChar)) {
                    pendingAccent = normalizeAccent(unicodeChar);
                    return true;
                }

                // 3. Se havia um acento aguardando uma letra
                if (pendingAccent != 0) {
                    // Se o usuário apertar espaço logo após o acento, insere o acento isolado
                    if (unicodeChar == ' ' || keyCode == KeyEvent.KEYCODE_SPACE) {
                        insertText(editText, String.valueOf((char) pendingAccent));
                        pendingAccent = 0;
                        return true;
                    }

                    // Tenta combinar o acento pendente com a letra atual
                    int composed = KeyCharacterMap.getDeadChar(pendingAccent, unicodeChar);

                    // Tratamento especial para ç / Ç caso o teclado do PC envie ' + c
                    if (composed == 0 && (pendingAccent == '\'' || pendingAccent == '´')) {
                        if (unicodeChar == 'c') composed = 'ç';
                        else if (unicodeChar == 'C') composed = 'Ç';
                    }

                    if (composed != 0) {
                        // Combinação bem-sucedida! (ex: á, ã, ê, ç, Á, Â, etc.)
                        insertText(editText, String.valueOf((char) composed));
                        pendingAccent = 0;
                        return true;
                    } else {
                        // Se não combinou (ex: ~ + b), insere o acento e depois o caractere
                        insertText(editText, String.valueOf((char) pendingAccent) + (char) unicodeChar);
                        pendingAccent = 0;
                        return true;
                    }
                }

                return false;
            }

            private boolean isAccentChar(int c) {
                return c == '´' || c == '`' || c == '~' || c == '^' || c == '¨' || c == '\'';
            }

            private int normalizeAccent(int c) {
                switch (c) {
                    case '´':
                    case '\'':
                        return '\'';
                    case '`':
                        return '`';
                    case '~':
                        return '~';
                    case '^':
                        return '^';
                    case '¨':
                        return '¨';
                    default:
                        return c;
                }
            }

            private void insertText(EditText et, String text) {
                int start = Math.max(et.getSelectionStart(), 0);
                int end = Math.max(et.getSelectionEnd(), 0);
                et.getText().replace(Math.min(start, end), Math.max(start, end), text);
            }
        });
    }
}
