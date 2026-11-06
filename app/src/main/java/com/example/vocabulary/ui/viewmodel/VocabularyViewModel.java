package com.example.vocabulary.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vocabulary.data.VocabularyRepository;
import com.example.vocabulary.data.model.VocabularyItem;
import com.example.vocabulary.data.model.VocabularyList;
import com.example.vocabulary.data.model.VocabularyListWithCount;

import java.util.List;

public class VocabularyViewModel extends AndroidViewModel {

    private final VocabularyRepository repository;
    private final LiveData<List<VocabularyListWithCount>> allLists;

    public VocabularyViewModel(@NonNull Application application) {
        super(application);
        repository = new VocabularyRepository(application);
        allLists = repository.getAllListsWithCount();
    }

    public LiveData<List<VocabularyListWithCount>> getAllLists() {
        return allLists;
    }

    public LiveData<VocabularyList> getList(long listId) {
        return repository.getListById(listId);
    }

    public LiveData<List<VocabularyItem>> getItemsForList(long listId) {
        return repository.getItemsForList(listId);
    }

    public void createList(String name, String description) {
        if (name == null || name.trim().isEmpty()) return;
        repository.insertList(name, description);
    }

    public void deleteList(long listId) {
        repository.deleteList(listId);
    }

    public void addWord(long listId, String primaryText, String associationText) {
        if (primaryText == null || primaryText.trim().isEmpty()) return;
        if (associationText == null || associationText.trim().isEmpty()) return;
        repository.insertItem(listId, primaryText, associationText);
    }

    public void deleteWord(VocabularyItem item) {
        repository.deleteItem(item);
    }
}
