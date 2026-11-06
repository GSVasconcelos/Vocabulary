package com.example.vocabulary.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.vocabulary.R;
import com.example.vocabulary.data.model.VocabularyItem;
import com.example.vocabulary.databinding.ActivityGameBinding;
import com.example.vocabulary.ui.viewmodel.GameViewModel;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_LIST_ID = "extra_list_id";
    public static final String EXTRA_LIST_NAME = "extra_list_name";

    private ActivityGameBinding binding;
    private GameViewModel viewModel;

    private long listId = -1L;
    private String listName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listId = getIntent().getLongExtra(EXTRA_LIST_ID, -1L);
        listName = getIntent().getStringExtra(EXTRA_LIST_NAME);
        if (listName == null) listName = "";

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);

        setupToolbar();
        setupListeners();
        observeViewModel();

        viewModel.loadAndStartGame(listId);
    }

    private void setupToolbar() {
        String title = !listName.isEmpty() ? "Recapitulação: " + listName : "Recapitulação";
        binding.toolbar.setTitle(title);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupListeners() {
        binding.btnCheckAnswer.setOnClickListener(v -> submitAnswer());

        binding.etUserAnswer.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitAnswer();
                return true;
            }
            return false;
        });

        binding.btnNextWord.setOnClickListener(v -> {
            viewModel.nextWord();
            if (binding.etUserAnswer.getText() != null) {
                binding.etUserAnswer.getText().clear();
            }
            binding.etUserAnswer.requestFocus();
        });

        binding.btnPlayAgain.setOnClickListener(v -> {
            if (binding.etUserAnswer.getText() != null) {
                binding.etUserAnswer.getText().clear();
            }
            viewModel.loadAndStartGame(listId);
        });

        binding.btnBackToList.setOnClickListener(v -> finish());
    }

    private void submitAnswer() {
        String answer = binding.etUserAnswer.getText() != null
                ? binding.etUserAnswer.getText().toString()
                : "";
        viewModel.checkAnswer(answer);
    }

    private void observeViewModel() {
        viewModel.getIsGameFinished().observe(this, finished -> {
            if (Boolean.TRUE.equals(finished)) {
                binding.layoutPlaying.setVisibility(View.GONE);
                binding.layoutFinished.setVisibility(View.VISIBLE);
                binding.progressIndicator.setProgress(100);

                int score = viewModel.getScoreLiveData().getValue() != null ? viewModel.getScoreLiveData().getValue() : 0;
                int total = viewModel.getTotalWords().getValue() != null ? viewModel.getTotalWords().getValue() : 0;
                int percentage = total > 0 ? (score * 100) / total : 0;

                binding.tvFinalScore.setText("Você acertou " + score + " de " + total + " palavras! (" + percentage + "%)");
            } else {
                binding.layoutPlaying.setVisibility(View.VISIBLE);
                binding.layoutFinished.setVisibility(View.GONE);
            }
        });

        viewModel.getCurrentWord().observe(this, word -> {
            if (word != null) {
                binding.tvPrimaryWord.setText(word.getPrimaryText());
            }
        });

        viewModel.getCurrentWordNumber().observe(this, currentNum -> updateProgress());
        viewModel.getTotalWords().observe(this, totalNum -> updateProgress());

        viewModel.getStreakLiveData().observe(this, streak -> {
            if (streak != null && streak > 1) {
                binding.tvStreakBadge.setVisibility(View.VISIBLE);
                binding.tvStreakBadge.setText("🔥 " + streak + " seguidos");
            } else {
                binding.tvStreakBadge.setVisibility(View.GONE);
            }
        });

        viewModel.getIsAnswerChecked().observe(this, checked -> {
            if (Boolean.TRUE.equals(checked)) {
                binding.tilAnswer.setEnabled(false);
                binding.btnCheckAnswer.setVisibility(View.GONE);
                binding.cardFeedback.setVisibility(View.VISIBLE);

                boolean isCorrect = Boolean.TRUE.equals(viewModel.getIsCorrect().getValue());
                String correctAnswer = viewModel.getCorrectAnswer().getValue();

                if (isCorrect) {
                    binding.cardFeedback.setCardBackgroundColor(
                            ContextCompat.getColor(GameActivity.this, R.color.correct_green_bg)
                    );
                    binding.cardFeedback.setStrokeColor(
                            ContextCompat.getColor(GameActivity.this, R.color.correct_green)
                    );
                    binding.tvFeedbackTitle.setText("Excelente! Você acertou! 🎉");
                    binding.tvFeedbackTitle.setTextColor(
                            ContextCompat.getColor(GameActivity.this, R.color.correct_green)
                    );
                    binding.tvFeedbackAnswer.setText("Resposta: " + correctAnswer);
                } else {
                    binding.cardFeedback.setCardBackgroundColor(
                            ContextCompat.getColor(GameActivity.this, R.color.wrong_red_bg)
                    );
                    binding.cardFeedback.setStrokeColor(
                            ContextCompat.getColor(GameActivity.this, R.color.wrong_red)
                    );
                    binding.tvFeedbackTitle.setText("Não foi dessa vez! ❌");
                    binding.tvFeedbackTitle.setTextColor(
                            ContextCompat.getColor(GameActivity.this, R.color.wrong_red)
                    );
                    binding.tvFeedbackAnswer.setText("A resposta correta era: " + correctAnswer);
                }

                int current = viewModel.getCurrentWordNumber().getValue() != null ? viewModel.getCurrentWordNumber().getValue() : 0;
                int total = viewModel.getTotalWords().getValue() != null ? viewModel.getTotalWords().getValue() : 0;

                if (current >= total) {
                    binding.btnNextWord.setText("Ver Resultado Final");
                } else {
                    binding.btnNextWord.setText(R.string.next_word);
                }
                binding.btnNextWord.requestFocus();
            } else {
                binding.tilAnswer.setEnabled(true);
                binding.btnCheckAnswer.setVisibility(View.VISIBLE);
                binding.cardFeedback.setVisibility(View.GONE);
            }
        });
    }

    private void updateProgress() {
        int current = viewModel.getCurrentWordNumber().getValue() != null ? viewModel.getCurrentWordNumber().getValue() : 0;
        int total = viewModel.getTotalWords().getValue() != null ? viewModel.getTotalWords().getValue() : 0;

        int progress = total > 0 ? (current * 100) / total : 0;
        binding.progressIndicator.setProgress(progress);
        binding.tvProgressText.setText("Palavra " + current + " de " + total);
    }
}
