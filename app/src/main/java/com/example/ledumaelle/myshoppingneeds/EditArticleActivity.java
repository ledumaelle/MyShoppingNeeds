package com.example.ledumaelle.myshoppingneeds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.ledumaelle.myshoppingneeds.bo.Article;
import com.example.ledumaelle.myshoppingneeds.dal.ArticleDao;

import java.util.List;

public class EditArticleActivity extends AppCompatActivity {

    private Article article;
    private ArticleDao articleDao;

    private EditText txtEditArticleName;
    private EditText txtEditArticlePrice;
    private RatingBar ratingEditArticle;
    private EditText txtEditArticleDescription;
    private EditText txtEditArticleUrl;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        intent = getIntent();
        articleDao = new ArticleDao(this);
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

        Article articleReload = articleDao.get(article.getId());

        txtEditArticleName = (EditText) findViewById(R.id.txtEditArticleName);
        txtEditArticleName.setText(articleReload.getName().trim());

        txtEditArticlePrice = (EditText) findViewById(R.id.txtEditArticlePrice);
        txtEditArticlePrice.setText(String.valueOf(articleReload.getPrice()));

        txtEditArticleDescription = (EditText) findViewById(R.id.txtEditArticleDescription);
        txtEditArticleDescription.setText(String.valueOf(articleReload.getDescription().trim()));

        ratingEditArticle = (RatingBar) findViewById(R.id.ratingEditArticle);
        ratingEditArticle.setRating(articleReload.getRate());

        txtEditArticleUrl = (EditText) findViewById(R.id.txtEditArticleUrl);
        txtEditArticleUrl.setText(String.valueOf(articleReload.getUrl().trim()));
    }


    public void back(View view) {

        onBackPressed();
    }

    public void editArticle(View view) {

        txtEditArticleName = (EditText) findViewById(R.id.txtEditArticleName);
        article.setName(txtEditArticleName.getText().toString().trim());

        txtEditArticlePrice = (EditText) findViewById(R.id.txtEditArticlePrice);
        article.setPrice(Float.parseFloat(txtEditArticlePrice.getText().toString()));

        ratingEditArticle = (RatingBar) findViewById(R.id.ratingEditArticle);
        article.setRate(ratingEditArticle.getRating());

        txtEditArticleDescription = (EditText) findViewById(R.id.txtEditArticleDescription);
        article.setDescription(txtEditArticleDescription.getText().toString().trim());

        txtEditArticleUrl = (EditText) findViewById(R.id.txtEditArticleUrl);
        article.setUrl(txtEditArticleUrl.getText().toString().trim());

        article.setState(false);

        List<Article> listArticles = articleDao.get();
        if (listArticles.contains(article)) {

            if(articleDao.update(article)) {

                intent.putExtra("result","Article mis à jour avec succès !");
                this.setResult(RESULT_OK,intent);

            } else {

                intent.putExtra("result","Échec de la mise à jour de l'article :( " + article.getName().trim() );
                this.setResult(RESULT_CANCELED,intent);
            }

        } else {

            intent.putExtra("result","ERREUR : l'article n'existe pas dans la liste des articles");
            this.setResult(RESULT_CANCELED,intent);
        }

        finish();
    }
}