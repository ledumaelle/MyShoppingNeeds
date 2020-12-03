package com.example.ledumaelle.myshoppingneeds;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ledumaelle.myshoppingneeds.bo.Article;
import com.example.ledumaelle.myshoppingneeds.dal.AppDatabase;
import com.example.ledumaelle.myshoppingneeds.dal.ArticleDao;
import com.example.ledumaelle.myshoppingneeds.dal.Connection;

import java.util.List;

public class EditArticleActivity extends AppCompatActivity {

    private Article article;
    private List<Article> listArticles;

    private EditText txtEditArticleName;
    private EditText txtEditArticlePrice;
    private RatingBar ratingEditArticle;
    private EditText txtEditArticleDescription;
    private EditText txtEditArticleUrl;

    GetListe getArticles = null;
    GetArticle getArticle = null;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        intent = getIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();

        article = intent.getParcelableExtra("article");
    }

    /**
     * Si maj de l'article
     */
    @Override
    protected void onResume() {

        super.onResume();

        GetArticle getArticle = new GetArticle();
        getArticle.execute();

        GetListe getArticles = new GetListe();
        getArticles.execute();

    }

    private class GetArticle extends AsyncTask<Void, Void, Article> {

        @Override
        protected Article doInBackground(Void... voids) {
            try {

                //instance de la BdD
                AppDatabase db = Connection.getConnexion(EditArticleActivity.this);

                ArticleDao dao = db.articleDao();

                return dao.findById(article.getId());
            } catch (Exception ex) {

                Log.e("GetArticle","ERREUR tâche asynchrone GetArticle de EditArticleActivity : " + ex.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Article article) {

            if (article != null) {

                txtEditArticleName = findViewById(R.id.txtEditArticleName);
                txtEditArticleName.setText(article.getName().trim());

                txtEditArticlePrice = findViewById(R.id.txtEditArticlePrice);
                txtEditArticlePrice.setText(String.valueOf(article.getPrice()));

                txtEditArticleDescription = findViewById(R.id.txtEditArticleDescription);
                txtEditArticleDescription.setText(String.valueOf(article.getDescription().trim()));

                ratingEditArticle = findViewById(R.id.ratingEditArticle);
                ratingEditArticle.setRating(article.getRate());

                txtEditArticleUrl = findViewById(R.id.txtEditArticleUrl);
                txtEditArticleUrl.setText(String.valueOf(article.getUrl().trim()));
            }
        }
    }

    public void back(View view) {

        finish();
    }

    public void editArticle(View view) {

        txtEditArticleName = findViewById(R.id.txtEditArticleName);
        article.setName(txtEditArticleName.getText().toString().trim());

        txtEditArticlePrice = findViewById(R.id.txtEditArticlePrice);
        article.setPrice(Float.parseFloat(txtEditArticlePrice.getText().toString()));

        ratingEditArticle = findViewById(R.id.ratingEditArticle);
        article.setRate(ratingEditArticle.getRating());

        txtEditArticleDescription = findViewById(R.id.txtEditArticleDescription);
        article.setDescription(txtEditArticleDescription.getText().toString().trim());

        txtEditArticleUrl = findViewById(R.id.txtEditArticleUrl);
        article.setUrl(txtEditArticleUrl.getText().toString().trim());

        article.setState(false);

        if (listArticles.contains(article)) {

            Update updateArticle = new Update();
            updateArticle.execute();

        } else {

            intent.putExtra("result", "ERREUR : l'article n'existe pas dans la liste des articles");
            this.setResult(RESULT_CANCELED, intent);

            finish();
        }
    }

    private class GetListe extends AsyncTask<Void, Void, List<Article>> {

        @Override
        protected List<Article> doInBackground(Void... voids) {
            try {

                //instance de la BdD
                AppDatabase db = Connection.getConnexion(EditArticleActivity.this);

                ArticleDao dao = db.articleDao();

                return dao.getAll();
            } catch (Exception ex) {

                Log.e("GETListe","ERREUR tâche asynchrone GetListe de EditArticleActivity : " + ex.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            listArticles = articles;
        }
    }

    private class Update extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {

                //instance de la BdD
                AppDatabase db = Connection.getConnexion(EditArticleActivity.this);

                ArticleDao dao = db.articleDao();
                dao.update(article);

                return true;
            } catch (Exception ex) {

                Log.e("Update","ERREUR tâche asynchrone Update de EditArticleActivity : " + ex.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aboolean) {

            if (aboolean) {

                intent.putExtra("result", "Article mis à jour avec succès !");
                EditArticleActivity.this.setResult(RESULT_OK, intent);
            } else {

                intent.putExtra("result", "ERREUR : l'article n'a pas pu être mis à jour :(");
                EditArticleActivity.this.setResult(RESULT_CANCELED, intent);
            }

            finish();
        }
    }
}