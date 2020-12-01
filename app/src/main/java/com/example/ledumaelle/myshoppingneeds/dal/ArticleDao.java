package com.example.ledumaelle.myshoppingneeds.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ledumaelle.myshoppingneeds.bo.Article;
import com.example.ledumaelle.myshoppingneeds.contract.ArticleContract;

import java.util.ArrayList;
import java.util.List;

public class ArticleDao {

    //permet d'exécuter une requête SQL
    private final SQLiteDatabase db;
    private final BddHelper helper;

    public ArticleDao(Context context) {

        helper = new BddHelper(context);
        db = helper.getWritableDatabase();
    }

    private ContentValues putArticle(Article article) {

        //Dictionnaire de clef/valeur
        ContentValues values = new ContentValues();
        values.put(ArticleContract.COL_NAME,article.getName());
        values.put(ArticleContract.COL_DESCRIPTION,article.getDescription());
        values.put(ArticleContract.COL_PRICE,article.getPrice());
        values.put(ArticleContract.COL_RATE,article.getRate());
        values.put(ArticleContract.COL_URL,article.getUrl());
        values.put(ArticleContract.COL_STATE,article.isState());
        values.put(ArticleContract.COL_NAME_FILE,article.getNameFile());

        return values;
    }

    private Article getArticle(Cursor cursor) {

        Article article = new Article();
        article.setId(cursor.getInt(cursor.getColumnIndex(ArticleContract.COL_ID.trim())));
        article.setName(cursor.getString(cursor.getColumnIndex(ArticleContract.COL_NAME.trim())));
        article.setPrice(cursor.getFloat(cursor.getColumnIndex(ArticleContract.COL_PRICE.trim())));
        article.setDescription(cursor.getString(cursor.getColumnIndex(ArticleContract.COL_DESCRIPTION.trim())));
        article.setRate(cursor.getFloat(cursor.getColumnIndex(ArticleContract.COL_RATE.trim())));
        article.setUrl(cursor.getString(cursor.getColumnIndex(ArticleContract.COL_URL.trim())));
        article.setState(cursor.getInt(cursor.getColumnIndex(ArticleContract.COL_STATE.trim())) == 1);
        article.setNameFile(cursor.getString(cursor.getColumnIndex(ArticleContract.COL_NAME_FILE.trim())));

        return article;
    }

    public long insert(Article article) {

        ContentValues values = putArticle(article);

        //nullColumnHack --> colonnes où je mets null

        //return l'id de la personne concernée
        return db.insert(ArticleContract.TABLE_NAME,null, values);
    }

    public Article get(long id) {

        Article article = null;

        Cursor cursor = db.query(
                ArticleContract.TABLE_NAME,
                //null pour avoir toutes les colonnes de la table
                new String[]{
                        ArticleContract.COL_ID,
                        ArticleContract.COL_NAME,
                        ArticleContract.COL_PRICE,
                        ArticleContract.COL_DESCRIPTION,
                        ArticleContract.COL_RATE,
                        ArticleContract.COL_URL,
                        ArticleContract.COL_STATE,
                        ArticleContract.COL_NAME_FILE
                },
                ArticleContract.COL_ID + " =?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        if(cursor.moveToNext()) {

            article = getArticle(cursor);
        }
        return article;
    }

    public List<Article> get() {

       return get(false);
    }

    public List<Article> get(boolean sortPrice) {

        List<Article> resultat = new ArrayList<>();

        Cursor cursor = db.query(
                ArticleContract.TABLE_NAME,
                new String[]{
                        ArticleContract.COL_ID,
                        ArticleContract.COL_NAME,
                        ArticleContract.COL_PRICE,
                        ArticleContract.COL_DESCRIPTION,
                        ArticleContract.COL_RATE,
                        ArticleContract.COL_URL,
                        ArticleContract.COL_STATE,
                        ArticleContract.COL_NAME_FILE
                },
                null,
                null,
                null,
                null,
                (sortPrice ? ArticleContract.COL_PRICE : null));

        while(cursor.moveToNext()) {

            Article article = getArticle(cursor);
            resultat.add(article);
        }
        return resultat;
    }

    public boolean update(Article article) {

        Log.i("ACOS","Entrée dans update avec " + article.toString());


        ContentValues values = putArticle(article);

        //si nb de lignes > 0 (update a retourné une maj)
        return db.update(ArticleContract.TABLE_NAME,
                values,
                ArticleContract.COL_ID + "=?",
                new String[]{String.valueOf(article.getId())})
                >0;
    }

    public boolean delete(Article article) {

        //si nb de lignes > 0 (delete a bien supprimé une ligne)
        return db.delete(ArticleContract.TABLE_NAME,
                ArticleContract.COL_ID + " =?",
                new String[]{String.valueOf(article.getId())})
                >0;
    }
}
