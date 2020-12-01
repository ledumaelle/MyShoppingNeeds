package com.example.ledumaelle.myshoppingneeds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ledumaelle.myshoppingneeds.bo.Article;
import com.example.ledumaelle.myshoppingneeds.dal.ArticleDao;

import java.util.List;

public class AddArticleActivity extends AppCompatActivity {

    private Article article;
    private ArticleDao articleDao;

    private EditText txtAddArticleName;
    private EditText txtAddArticlePrice;
    private RatingBar ratingAddArticle;
    private EditText txtAddArticleDescription;
    private EditText txtAddArticleUrl;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        intent = getIntent();

        article = new Article();
        articleDao = new ArticleDao(this);
    }

    @Override
    protected void onResume() {
        super.onResume();


        txtAddArticleName = (EditText) findViewById(R.id.txtAddArticleName);
        txtAddArticlePrice = (EditText) findViewById(R.id.txtAddArticlePrice);
        ratingAddArticle = (RatingBar) findViewById(R.id.ratingAddArticle);
        txtAddArticleDescription = (EditText) findViewById(R.id.txtAddArticleDescription);
        txtAddArticleUrl = (EditText) findViewById(R.id.txtAddArticleUrl);

        //Récupérer les préférences si ya dans l'app
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigurationActivity.NOM_FICHIER, MODE_PRIVATE);

        String defaultPrice = sharedPreferences.getString(ConfigurationActivity.CLE_PREFERENCE_DEFAULT_PRICE, "");

        if(!defaultPrice.isEmpty()) {

            txtAddArticlePrice.setText(defaultPrice);
        }
    }

    public void back(View view) {

        onBackPressed();
    }

    public void addArticle(View view) {

        article.setName(txtAddArticleName.getText().toString().trim());
        article.setPrice(Float.parseFloat(txtAddArticlePrice.getText().toString()));
        article.setRate(ratingAddArticle.getRating());
        article.setDescription(txtAddArticleDescription.getText().toString().trim());
        article.setUrl(txtAddArticleUrl.getText().toString().trim());

        article.setState(false);

        List<Article> listArticles = articleDao.get();
        if (!listArticles.contains(article)) {

            if( articleDao.insert(article) > 0) {

                intent.putExtra("result","Article ajouté avec succès !");
                this.setResult(RESULT_OK,intent);

            } else {

                intent.putExtra("result","Échec de l'ajout :(");
                this.setResult(RESULT_CANCELED,intent);
            }

        } else {

            intent.putExtra("result","ERREUR : l'article existe déjà dans la liste des articles");
            this.setResult(RESULT_CANCELED,intent);
        }

        finish();
    }
}