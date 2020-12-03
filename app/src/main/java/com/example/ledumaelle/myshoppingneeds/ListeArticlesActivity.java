package com.example.ledumaelle.myshoppingneeds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ledumaelle.myshoppingneeds.adapter.MyAdapter;
import com.example.ledumaelle.myshoppingneeds.bo.Article;
import com.example.ledumaelle.myshoppingneeds.dal.AppDatabase;
import com.example.ledumaelle.myshoppingneeds.dal.ArticleDao;
import com.example.ledumaelle.myshoppingneeds.dal.Connection;

import java.util.ArrayList;
import java.util.List;

public class ListeArticlesActivity extends AppCompatActivity implements OnClickItemRecyclerView<Article> {

    private List<Article> listArticles;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBarListArticles;

    private boolean sortPrice = false;
    private String defaultPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_articles);
    }

    private void cleanAll() {

        Delete deleteAll = new Delete();
        deleteAll.execute();
    }

    /**
     * Fonction permettant de définir l'action à réaliser au moment d'un clic sur un élément.
     */
    @Override
    public void onInteraction(Article article) {

        Intent displayArticle = new Intent(this, DisplayArticleActivity.class);
        displayArticle.putExtra("article", article);
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
                startActivityForResult(intentAddArticle, 14540);

                return true;

            case R.id.action_delete_all:

                cleanAll();
                return true;

            case R.id.action_settings:

                Intent intentConfiguration = new Intent(this, ConfigurationActivity.class);
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

        Toolbar mainToolbar = findViewById(R.id.myMainToolBar);
        setSupportActionBar(mainToolbar);

        //Récupération d'un objet représentant le Recycler View
        mRecyclerView = findViewById(R.id.listArticles);
        progressBarListArticles = findViewById(R.id.progressBarListeArticles);

        // Permet d’optimiser le chargement dans le cas ou le recycler view ne change pas de taille.
        mRecyclerView.setHasFixedSize(true);

        // Utilisation du linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        listArticles = new ArrayList<>();

        //Récupérer les préférences si ya dans l'app
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigurationActivity.NOM_FICHIER, MODE_PRIVATE);

        sortPrice = sharedPreferences.getBoolean(ConfigurationActivity.CLE_PREFERENCE_SORT_PRICE, false);
        defaultPrice = sharedPreferences.getString(ConfigurationActivity.CLE_PREFERENCE_DEFAULT_PRICE, "");

        GetListe getArticles = new GetListe();
        getArticles.execute();
    }

    private class GetListe extends AsyncTask<Void, Void, List<Article>> {

        @Override
        protected List<Article> doInBackground(Void... voids) {
            try {

                //instance de la BdD
                AppDatabase db = Connection.getConnexion(ListeArticlesActivity.this);

                ArticleDao dao = db.articleDao();


                return sortPrice ? dao.getAllSorted() : dao.getAll();

            } catch (Exception ex) {

                Log.e("GetListe", "ERREUR tâche asynchrone GetListe de ListeArticleActivity : " + ex.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBarListArticles.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<Article> articles) {

            progressBarListArticles.setVisibility(View.GONE);

            listArticles = articles;

            // init de la BdD
            if (listArticles.size() <= 0) {

                Insert insertArticles = new Insert();
                insertArticles.execute();
            } else {

                bindRecyclerView();
            }
        }
    }

    private class Insert extends AsyncTask<Void, Void, List<Article>> {

        @Override
        protected List<Article> doInBackground(Void... voids) {
            try {

                Article painAuChocolat = new Article();
                painAuChocolat.setName("Pain au chocolat");
                painAuChocolat.setPrice(0.95f);
                painAuChocolat.setDescription("Une viennoiserie au beurre et au chocolat");
                painAuChocolat.setRate(1.5f);
                painAuChocolat.setUrl("URL du " + painAuChocolat.getName());
                painAuChocolat.setState(true);
                painAuChocolat.setNameFile("pain_au_chocolat.jpg");

                Article croissant = new Article();
                croissant.setName("Croissant");
                croissant.setPrice(0.70f);
                croissant.setDescription("Une viennoiserie au beurre");
                croissant.setRate(3.5f);
                croissant.setUrl("URL du " + croissant.getName());
                croissant.setState(false);
                croissant.setNameFile("croissant.jpg");

                Article tarte = new Article();
                tarte.setName("Tarte aux fraises");
                tarte.setPrice(5.99f);
                tarte.setDescription("Fraises et crème pâtissière vont très bien ensemble...");
                tarte.setRate(5.0f);
                tarte.setUrl("URL du " + tarte.getName());
                tarte.setState(true);
                tarte.setNameFile("tarte_aux_fraises.jpg");

                //instance de la BdD
                AppDatabase db = Connection.getConnexion(ListeArticlesActivity.this);

                ArticleDao dao = db.articleDao();
                dao.insertAll(painAuChocolat, croissant, tarte);

                return dao.getAll();
            } catch (Exception ex) {

                Log.e("Insert","ERREUR tâche asynchrone Insert de ListeArticleActivity : " + ex.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBarListArticles.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<Article> articles) {

            progressBarListArticles.setVisibility(View.GONE);

            listArticles = articles;
            bindRecyclerView();
        }
    }

    private void bindRecyclerView() {

        //Création de l'adapteur
        mAdapter = new MyAdapter(listArticles, ListeArticlesActivity.this);
        //Lie l’adapter au recycler view
        mRecyclerView.setAdapter(mAdapter);
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

                if (data != null) {

                    Toast.makeText(this, data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }

    private class Delete extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {

                AppDatabase db = Connection.getConnexion(ListeArticlesActivity.this);

                ArticleDao dao = db.articleDao();

                dao.clearTable();

                return true;
            } catch (Exception ex) {

                Log.e("Delete","ERREUR tâche asynchrone Insert de ListeArticleActivity : " + ex.getMessage());
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBarListArticles.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            progressBarListArticles.setVisibility(View.GONE);

            onResume();
        }
    }

}