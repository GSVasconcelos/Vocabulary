package com.example.vocabulary.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.vocabulary.data.VocabularyRepository;
import com.example.vocabulary.data.model.VocabularyItem;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameViewModel extends AndroidViewModel {

    private final VocabularyRepository repository;

    private final List<VocabularyItem> shuffledWords = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;
    private int streak = 0;

    private final MutableLiveData<VocabularyItem> currentWord = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentWordNumber = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> totalWords = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> scoreLiveData = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> streakLiveData = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isAnswerChecked = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isCorrect = new MutableLiveData<>(false);
    private final MutableLiveData<String> correctAnswer = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isGameFinished = new MutableLiveData<>(false);

    public GameViewModel(@NonNull Application application) {
        super(application);
        this.repository = new VocabularyRepository(application);
    }

    public void loadAndStartGame(long listId) {
        repository.getItemsForListSync(listId, words -> {
            shuffledWords.clear();
            if (words == null || words.isEmpty()) {
                isGameFinished.setValue(true);
                totalWords.setValue(0);
                return;
            }

            shuffledWords.addAll(words);
            // Embaralha as palavras da lista
            Collections.shuffle(shuffledWords);

            currentIndex = 0;
            score = 0;
            streak = 0;
            isGameFinished.setValue(false);
            totalWords.setValue(shuffledWords.size());

            showCurrentWord();
        });
    }

    private void showCurrentWord() {
        if (currentIndex < shuffledWords.size()) {
            VocabularyItem word = shuffledWords.get(currentIndex);
            currentWord.setValue(word);
            currentWordNumber.setValue(currentIndex + 1);
            scoreLiveData.setValue(score);
            streakLiveData.setValue(streak);
            isAnswerChecked.setValue(false);
            isCorrect.setValue(false);
            correctAnswer.setValue(word.getAssociationText());
        } else {
            isGameFinished.setValue(true);
        }
    }

    public void checkAnswer(String userInput) {
        VocabularyItem word = currentWord.getValue();
        if (word == null || Boolean.TRUE.equals(isAnswerChecked.getValue())) {
            return;
        }

        String cleanedInput = userInput == null ? "" : userInput.trim().toLowerCase();
        String cleanedTarget = word.getAssociationText() == null ? "" : word.getAssociationText().trim().toLowerCase();

        boolean match = cleanedInput.equals(cleanedTarget);

        // Se não bateu exatamente, verifica se bate ignorando diacríticos (ex: "maca" aceita "maçã", "voce" aceita "você")
        if (!match) {
            String normInput = stripAccents(cleanedInput);
            String normTarget = stripAccents(cleanedTarget);
            if (!normInput.isEmpty() && normInput.equals(normTarget)) {
                match = true;
            }
        }

        if (match) {
            score++;
            streak++;
        } else {
            streak = 0;
        }

        scoreLiveData.setValue(score);
        streakLiveData.setValue(streak);
        isCorrect.setValue(match);
        correctAnswer.setValue(word.getAssociationText());
        isAnswerChecked.setValue(true);

        // Atualiza métrica no banco de dados em segundo plano
        repository.recordReviewResult(word.getId(), match);
    }

    private String stripAccents(String s) {
        if (s == null) return "";
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public void nextWord() {
        currentIndex++;
        showCurrentWord();
    }

    // Getters para a Activity observar
    public LiveData<VocabularyItem> getCurrentWord() { return currentWord; }
    public LiveData<Integer> getCurrentWordNumber() { return currentWordNumber; }
    public LiveData<Integer> getTotalWords() { return totalWords; }
    public LiveData<Integer> getScoreLiveData() { return scoreLiveData; }
    public LiveData<Integer> getStreakLiveData() { return streakLiveData; }
    public LiveData<Boolean> getIsAnswerChecked() { return isAnswerChecked; }
    public LiveData<Boolean> getIsCorrect() { return isCorrect; }
    public LiveData<String> getCorrectAnswer() { return correctAnswer; }
    public LiveData<Boolean> getIsGameFinished() { return isGameFinished; }
}
