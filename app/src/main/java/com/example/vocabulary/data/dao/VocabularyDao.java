package com.example.vocabulary.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vocabulary.data.model.VocabularyItem;
import com.example.vocabulary.data.model.VocabularyList;
import com.example.vocabulary.data.model.VocabularyListWithCount;

import java.util.List;

@Dao
public interface VocabularyDao {

    // === Listas ===

    @Query("SELECT l.id, l.name, l.description, l.createdAt, COUNT(i.id) AS itemCount " +
           "FROM vocabulary_lists l " +
           "LEFT JOIN vocabulary_items i ON l.id = i.listId " +
           "GROUP BY l.id " +
           "ORDER BY l.createdAt DESC")
    LiveData<List<VocabularyListWithCount>> getAllListsWithCount();

    @Query("SELECT * FROM vocabulary_lists WHERE id = :listId")
    LiveData<VocabularyList> getListById(long listId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertList(VocabularyList list);

    @Update
    void updateList(VocabularyList list);

    @Delete
    void deleteList(VocabularyList list);

    @Query("DELETE FROM vocabulary_lists WHERE id = :listId")
    void deleteListById(long listId);

    // === Palavras / Itens ===

    @Query("SELECT * FROM vocabulary_items WHERE listId = :listId ORDER BY id DESC")
    LiveData<List<VocabularyItem>> getItemsForList(long listId);

    @Query("SELECT * FROM vocabulary_items WHERE listId = :listId")
    List<VocabularyItem> getItemsForListSync(long listId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertItem(VocabularyItem item);

    @Update
    void updateItem(VocabularyItem item);

    @Delete
    void deleteItem(VocabularyItem item);

    @Query("UPDATE vocabulary_items " +
           "SET reviewCount = reviewCount + 1, " +
           "    correctCount = correctCount + CASE WHEN :isCorrect THEN 1 ELSE 0 END " +
           "WHERE id = :itemId")
    void recordReviewResult(long itemId, boolean isCorrect);
}
