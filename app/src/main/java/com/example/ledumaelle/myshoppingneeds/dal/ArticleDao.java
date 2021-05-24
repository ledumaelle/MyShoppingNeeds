package com.example.ledumaelle.myshoppingneeds.dal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ledumaelle.myshoppingneeds.bo.Article;

import java.util.List;

@Dao
public interface ArticleDao {

    @Query("SELECT * FROM article")
    List<Article> getAll();

    @Query("SELECT * FROM article ORDER BY article.price")
    List<Article> getAllSorted();

    @Query("SELECT * FROM article WHERE id = :id LIMIT 1")
    Article findById(int id);

    @Insert
    void insertAll(Article... articles);
    @Update
    void update(Article article);
    @Delete
    void delete(Article article);

    @Query("DELETE FROM article")
    void clearTable();
}
