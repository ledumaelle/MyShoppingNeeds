package com.example.ledumaelle.myshoppingneeds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ledumaelle.myshoppingneeds.bo.Article;
import com.example.ledumaelle.myshoppingneeds.dal.AppDatabase;
import com.example.ledumaelle.myshoppingneeds.dal.ArticleDao;
import com.example.ledumaelle.myshoppingneeds.dal.Connection;

public class DisplayArticleActivity extends AppCompatActivity {

    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_article);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        article = intent.getParcelableExtra("article");

        Toolbar displayArticleToolbar =  findViewById(R.id.myDisplayArticleToolBar);
        setSupportActionBar(displayArticleToolbar);
    }

    /**
     * Si maj de l'article
     */
    @Override
    protected void onResume() {

        super.onResume();

        ChargementArticleTask getArticle = new ChargementArticleTask();
        getArticle.execute();
    }

    private class ChargementArticleTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                //instance de la BdD
                AppDatabase db = Connection.getConnexion(DisplayArticleActivity.this);

                ArticleDao dao = db.articleDao();

                article = dao.findById(article.getId());
            }
            catch(Exception ex) {

                Log.e("ChargementArticleTask","ERREUR tâche asynchrone ChargementArticleTask de DisplayArticleActivity : " + ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(article != null) {

                TextView title = findViewById(R.id.txtTitle);
                title.setText(article.getName());

                TextView price = findViewById(R.id.txtPrice);
                String tmp = article.getPrice() + " €";
                price.setText(tmp);

                TextView description = findViewById(R.id.txtDescription);
                description.setText(String.valueOf(article.getDescription()));

                RatingBar rate = findViewById(R.id.rating);
                rate.setRating(article.getRate());

                ToggleButton state = findViewById(R.id.btnState);
                state.setChecked(article.isState());
            }
        }
    }

    /**
     * Qd je clique sur le bouton planète --> affiche l'URL
     * @param view
     */
    public void displayUrl(View view) {

        if(article != null && !article.getUrl().isEmpty()) {

            Intent intent = new Intent(this, InfoUrlActivity.class);
            intent.putExtra("article", article);
            startActivity(intent);
        }
        else {

            Toast.makeText(this, "Impossible d'afficher l'URL de l'article, veuillez vérifer l'URL enregistré.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Qd je clique sur le bouton acheté / à acheter
     * @param view
     */
    public void changeState(View view) {

        article.setState(((ToggleButton)view).isChecked());
        Toast.makeText(this, "État de l'article : " + (article.isState() ? "Acheté" : "À acheter"), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_article_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:

                Intent intent = new Intent(this, EditArticleActivity.class);
                intent.putExtra("article", article);
                startActivityForResult(intent,14545);

                return true;

            case R.id.action_send:

                Intent intentSend = new Intent(this, SendArticleActivity.class);
                intentSend.putExtra("article", article);
                startActivityForResult(intentSend,14546);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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
            case 14545:

                if(data != null) {

                    Toast.makeText(this,  data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
                }
                break;

            case 14546:
                if(data != null) {

                    Toast.makeText(this,  data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }
}