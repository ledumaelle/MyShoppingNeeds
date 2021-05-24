package com.example.ledumaelle.myshoppingneeds.dal;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.ledumaelle.myshoppingneeds.bo.Article;

@Database(entities = {Article.class}, version = 1)
public abstract class AppDatabase  extends RoomDatabase {

    public abstract ArticleDao articleDao();
}
