package com.example.vocabulary.ui.activity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.lifecycle.ViewModelProvider;

import com.example.vocabulary.R;
import com.example.vocabulary.data.model.VocabularyItem;
import com.example.vocabulary.databinding.ActivityGameBinding;
import com.example.vocabulary.ui.util.AccentInputHelper;
import com.example.vocabulary.ui.viewmodel.GameViewModel;

public class GameActivity extends BaseActivity {

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
        String baseTitle = getString(R.string.recap_title);
        String title = !listName.isEmpty() ? baseTitle + ": " + listName : baseTitle;
        binding.toolbar.setTitle(title);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupListeners() {
        // Suporte para digitação de palavras com acentos (Água, Lâmpada, Avião)
        AccentInputHelper.attach(binding.etUserAnswer);

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

    private int getThemeColor(int attrResId) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(attrResId, typedValue, true);
        return typedValue.data;
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

                binding.tvFinalScore.setText(getString(R.string.final_score_result, score, total, percentage));
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
                binding.tvStreakBadge.setText(getString(R.string.streak_badge, streak));
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
                int textPrimary = getThemeColor(R.attr.appTextPrimaryColor);

                if (isCorrect) {
                    int successBg = getThemeColor(R.attr.feedbackSuccessBgColor);
                    int successStroke = getThemeColor(R.attr.feedbackSuccessStrokeColor);
                    int successText = getThemeColor(R.attr.feedbackSuccessTextColor);

                    binding.cardFeedback.setCardBackgroundColor(successBg);
                    binding.cardFeedback.setStrokeColor(successStroke);
                    binding.tvFeedbackTitle.setText(R.string.correct_feedback);
                    binding.tvFeedbackTitle.setTextColor(successText);
                    binding.tvFeedbackAnswer.setText(getString(R.string.expected_answer, correctAnswer));
                    binding.tvFeedbackAnswer.setTextColor(textPrimary);
                } else {
                    int errorBg = getThemeColor(R.attr.feedbackErrorBgColor);
                    int errorStroke = getThemeColor(R.attr.feedbackErrorStrokeColor);
                    int errorText = getThemeColor(R.attr.feedbackErrorTextColor);

                    binding.cardFeedback.setCardBackgroundColor(errorBg);
                    binding.cardFeedback.setStrokeColor(errorStroke);
                    binding.tvFeedbackTitle.setText(R.string.wrong_feedback);
                    binding.tvFeedbackTitle.setTextColor(errorText);
                    binding.tvFeedbackAnswer.setText(getString(R.string.expected_answer, correctAnswer));
                    binding.tvFeedbackAnswer.setTextColor(textPrimary);
                }

                int current = viewModel.getCurrentWordNumber().getValue() != null ? viewModel.getCurrentWordNumber().getValue() : 0;
                int total = viewModel.getTotalWords().getValue() != null ? viewModel.getTotalWords().getValue() : 0;

                if (current >= total) {
                    binding.btnNextWord.setText(R.string.view_final_result);
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
        binding.tvProgressText.setText(getString(R.string.word_progress, current, total));
    }
}
