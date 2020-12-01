package com.example.ledumaelle.myshoppingneeds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ledumaelle.myshoppingneeds.adapter.MyAdapter;
import com.example.ledumaelle.myshoppingneeds.bo.Article;
import com.example.ledumaelle.myshoppingneeds.dal.ArticleDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListeArticlesActivity extends AppCompatActivity implements OnClickItemRecyclerView<Article> {

    private List<Article> listArticles;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArticleDao articleDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_articles);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.myMainToolBar);
        setSupportActionBar(mainToolbar);

        //Récupération d'un objet représentant le Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.listArticles);

        // Permet d’optimiser le chargement dans le cas ou le recycler view ne change pas de taille.
        mRecyclerView.setHasFixedSize(true);

        // Utilisation du linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        articleDao = new ArticleDao(this);
    }

    /**
     * Fonction permettant de définir l'action à réaliser au moment d'un clic sur un élément.
     */
    @Override
    public void onInteraction(Article article) {

        Intent displayArticle = new Intent(this, DisplayArticleActivity.class);
        displayArticle.putExtra("article",article);
        startActivity(displayArticle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:

                Intent intentAddArticle = new Intent(this, AddArticleActivity.class);
                startActivityForResult(intentAddArticle,14540);

                return true;

            case R.id.action_settings:

                Intent intentConfiguration = new Intent(this,ConfigurationActivity.class);
                startActivity(intentConfiguration);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Récupérer les préférences si ya dans l'app
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigurationActivity.NOM_FICHIER, MODE_PRIVATE);

        boolean sortPrice = sharedPreferences.getBoolean(ConfigurationActivity.CLE_PREFERENCE_SORT_PRICE, false);
        String defaultPrice = sharedPreferences.getString(ConfigurationActivity.CLE_PREFERENCE_DEFAULT_PRICE, "");

        if(sortPrice) {

            //Récupérer tous les articles de la BdD
            listArticles = articleDao.get(true);
        } else {

            //Récupérer tous les articles de la BdD
            listArticles = articleDao.get();
        }

        // init de la BdD
        if(listArticles.size() <= 0) {

            Article article;

            article = new Article();
            article.setName("Pain au chocolat");
            article.setPrice(0.95f);
            article.setDescription("Une viennoiserie au beurre et au chocolat");
            article.setRate(1.5f);
            article.setUrl("URL du " + article.getName());
            article.setState(true);
            article.setNameFile("pain_au_chocolat.jpg");
            article.setId((int)articleDao.insert(article));

            article = new Article();
            article.setName("Croissant");
            article.setPrice(0.70f);
            article.setDescription("Une viennoiserie au beurre");
            article.setRate(3.5f);
            article.setUrl("URL du " + article.getName());
            article.setState(false);
            article.setNameFile("croissant.jpg");
            article.setId((int)articleDao.insert(article));

            article = new Article();
            article.setName("Tarte aux fraises");
            article.setPrice(5.99f);
            article.setDescription("Fraises et crème pâtissière vont très bien ensemble...");
            article.setRate(5.0f);
            article.setUrl("URL du " + article.getName());
            article.setState(true);
            article.setNameFile("tarte_aux_fraises.jpg");
            article.setId((int)articleDao.insert(article));
        }

        //Création de l'adapteur
        mAdapter = new MyAdapter(listArticles,this);
        //Lie l’adapter au recycler view
        mRecyclerView.setAdapter(mAdapter);

        Toast.makeText(this,("Tri sur le prix ? " + sortPrice + " Default price : " + defaultPrice),  Toast.LENGTH_LONG).show();
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 14540:

                if(data != null) {

                    Toast.makeText(this, data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }
}