package com.example.ledumaelle.myshoppingneeds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ledumaelle.myshoppingneeds.bo.Article;
import com.example.ledumaelle.myshoppingneeds.dal.ArticleDao;

public class DisplayArticleActivity extends AppCompatActivity {

    private Article article;
    private ArticleDao articleDao;

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

        Toolbar displayArticleToolbar = (Toolbar) findViewById(R.id.myDisplayArticleToolBar);
        setSupportActionBar(displayArticleToolbar);

        articleDao = new ArticleDao(this);
    }

    /**
     * Si maj de l'article
     */
    @Override
    protected void onResume() {

        super.onResume();

        Article articleReload = articleDao.get(article.getId());

        if(articleReload != null) {

            TextView title = (TextView)findViewById(R.id.txtTitle);
            title.setText(articleReload.getName());

            TextView price = (TextView)findViewById(R.id.txtPrice);
            String tmp = articleReload.getPrice() + " €";
            price.setText(tmp);

            TextView description = (TextView)findViewById(R.id.txtDescription);
            description.setText(String.valueOf(articleReload.getDescription()));

            RatingBar rate = (RatingBar)findViewById(R.id.rating);
            rate.setRating(articleReload.getRate());

            ToggleButton state = (ToggleButton)findViewById(R.id.btnState);
            state.setChecked(articleReload.isState());
        }

    }

    /**
     * Qd je clique sur le bouton planète --> affiche l'URL
     * @param view
     */
    public void displayUrl(View view) {

        if(article != null) {
            Toast.makeText(this, article.getUrl(), Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, InfoUrlActivity.class);
            intent.putExtra("article", article);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Impossible d'afficher l'URL de l'article", Toast.LENGTH_LONG).show();
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