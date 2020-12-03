package com.example.ledumaelle.myshoppingneeds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.ledumaelle.myshoppingneeds.bo.Article;
import com.example.ledumaelle.myshoppingneeds.dal.AppDatabase;
import com.example.ledumaelle.myshoppingneeds.dal.ArticleDao;
import com.example.ledumaelle.myshoppingneeds.dal.Connection;

import java.util.ArrayList;
import java.util.List;

public class AddArticleActivity extends AppCompatActivity {

    private Article article;
    private List<Article> listArticles;

    private EditText txtAddArticleName;
    private EditText txtAddArticlePrice;
    private RatingBar ratingAddArticle;
    private EditText txtAddArticleDescription;
    private EditText txtAddArticleUrl;

    private Intent intent;
    GetListe getArticles = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        intent = getIntent();

        article = new Article();
    }

    @Override
    protected void onResume() {
        super.onResume();

        listArticles = new ArrayList<>();

        getArticles = new GetListe();
        getArticles.execute();

        txtAddArticleName = findViewById(R.id.txtAddArticleName);
        txtAddArticlePrice = findViewById(R.id.txtAddArticlePrice);
        ratingAddArticle = findViewById(R.id.ratingAddArticle);
        txtAddArticleDescription =findViewById(R.id.txtAddArticleDescription);
        txtAddArticleUrl = findViewById(R.id.txtAddArticleUrl);

        //Récupérer les préférences si ya dans l'app
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigurationActivity.NOM_FICHIER, MODE_PRIVATE);

        String defaultPrice = sharedPreferences.getString(ConfigurationActivity.CLE_PREFERENCE_DEFAULT_PRICE, "");

        if(!defaultPrice.isEmpty()) {

            txtAddArticlePrice.setText(defaultPrice);
        }
    }

    public void back(View view) {

        finish();
        //onBackPressed();
    }

    public void addArticle(View view) {

        article.setName(txtAddArticleName.getText().toString().trim());
        article.setPrice(Float.parseFloat(txtAddArticlePrice.getText().toString()));
        article.setRate(ratingAddArticle.getRating());
        article.setDescription(txtAddArticleDescription.getText().toString().trim());
        article.setUrl(txtAddArticleUrl.getText().toString().trim());

        article.setState(false);

        if (!listArticles.contains(article)) {

            Insert insertArticle = new Insert();
            insertArticle.execute();

        } else {

            intent.putExtra("result","ERREUR : l'article existe déjà dans la liste des articles");
            AddArticleActivity.this.setResult(RESULT_CANCELED,intent);
        }

        finish();
    }

    private class GetListe extends AsyncTask<Void, Void, List<Article>> {

        @Override
        protected List<Article> doInBackground(Void... voids) {
            try {

                //instance de la BdD
                AppDatabase db = Connection.getConnexion(AddArticleActivity.this);

                ArticleDao dao = db.articleDao();

                return dao.getAll();
            }
            catch(Exception ex) {

                Log.e("GETListe","ERREUR tâche asynchrone GetListe de AddArticleActivity : " + ex.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            listArticles = articles;
        }
    }

    private class Insert extends AsyncTask<Void, Void, List<Article>> {

        @Override
        protected List<Article> doInBackground(Void... voids) {
            try {

                //instance de la BdD
                AppDatabase db = Connection.getConnexion(AddArticleActivity.this);

                ArticleDao dao = db.articleDao();
                dao.insertAll(article);

                return dao.getAll();
            }
            catch(Exception ex) {

                Log.e("Insert","ERREUR tâche asynchrone Insert de AddArticleActivity : " + ex.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Article> articles) {

            intent.putExtra("result","Article ajouté avec succès !");
            AddArticleActivity.this.setResult(RESULT_OK,intent);
        }
    }
}