package com.example.vocabulary.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "vocabulary_items",
    foreignKeys = @ForeignKey(
        entity = VocabularyList.class,
        parentColumns = "id",
        childColumns = "listId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index(value = "listId")}
)
public class VocabularyItem {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long listId;
    private String primaryText;
    private String associationText;
    private int reviewCount;
    private int correctCount;

    public VocabularyItem(long listId, String primaryText, String associationText, int reviewCount, int correctCount) {
        this.listId = listId;
        this.primaryText = primaryText;
        this.associationText = associationText;
        this.reviewCount = reviewCount;
        this.correctCount = correctCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getAssociationText() {
        return associationText;
    }

    public void setAssociationText(String associationText) {
        this.associationText = associationText;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }
}
