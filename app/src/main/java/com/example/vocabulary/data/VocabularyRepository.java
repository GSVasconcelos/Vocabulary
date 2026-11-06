package com.example.vocabulary.data;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.vocabulary.data.dao.VocabularyDao;
import com.example.vocabulary.data.model.VocabularyItem;
import com.example.vocabulary.data.model.VocabularyList;
import com.example.vocabulary.data.model.VocabularyListWithCount;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VocabularyRepository {

    private final VocabularyDao dao;
    private final LiveData<List<VocabularyListWithCount>> allListsWithCount;
    private final ExecutorService executorService;
    private final Handler mainThreadHandler;

    public interface WordsCallback {
        void onWordsLoaded(List<VocabularyItem> words);
    }

    public VocabularyRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.dao = db.vocabularyDao();
        this.allListsWithCount = dao.getAllListsWithCount();
        this.executorService = Executors.newFixedThreadPool(4);
        this.mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public LiveData<List<VocabularyListWithCount>> getAllListsWithCount() {
        return allListsWithCount;
    }

    public LiveData<VocabularyList> getListById(long listId) {
        return dao.getListById(listId);
    }

    public void insertList(String name, String description) {
        executorService.execute(() -> {
            VocabularyList list = new VocabularyList(name.trim(), description.trim(), System.currentTimeMillis());
            dao.insertList(list);
        });
    }

    public void deleteList(long listId) {
        executorService.execute(() -> dao.deleteListById(listId));
    }

    public LiveData<List<VocabularyItem>> getItemsForList(long listId) {
        return dao.getItemsForList(listId);
    }

    public void getItemsForListSync(long listId, WordsCallback callback) {
        executorService.execute(() -> {
            List<VocabularyItem> words = dao.getItemsForListSync(listId);
            mainThreadHandler.post(() -> callback.onWordsLoaded(words));
        });
    }

    public void insertItem(long listId, String primaryText, String associationText) {
        executorService.execute(() -> {
            VocabularyItem item = new VocabularyItem(
                    listId,
                    primaryText.trim(),
                    associationText.trim(),
                    0,
                    0
            );
            dao.insertItem(item);
        });
    }

    public void deleteItem(VocabularyItem item) {
        executorService.execute(() -> dao.deleteItem(item));
    }

    public void recordReviewResult(long itemId, boolean isCorrect) {
        executorService.execute(() -> dao.recordReviewResult(itemId, isCorrect));
    }
}
