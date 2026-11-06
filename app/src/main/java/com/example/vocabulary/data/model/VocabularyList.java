package com.example.vocabulary.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vocabulary_lists")
public class VocabularyList {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private String description;
    private long createdAt;

    public VocabularyList(String name, String description, long createdAt) {
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
