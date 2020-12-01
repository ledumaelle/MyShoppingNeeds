package com.example.ledumaelle.myshoppingneeds.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.ledumaelle.myshoppingneeds.contract.ArticleContract;

public class BddHelper extends SQLiteOpenHelper {

    private static final String TAG = "BDD";
    private static final int VERSION = 4;
    private static final String DATABASE_NAME = "my_shopping_needs_database";

    public BddHelper(@Nullable Context context) {

        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Qd la BdD est crée
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.i(TAG,"Création de la base de données : ");
        sqLiteDatabase.execSQL(ArticleContract.CREATE_TABLE);

    }

    /**
     * Qd une montée de versionn de la BdD
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        Log.i(TAG,"Mise à jour de la base de données : ");

        //PB écrasement des données --> upgrade en THEORIE
        sqLiteDatabase.execSQL(ArticleContract.DROP_TABLE);
        sqLiteDatabase.execSQL(ArticleContract.CREATE_TABLE);
    }
}
