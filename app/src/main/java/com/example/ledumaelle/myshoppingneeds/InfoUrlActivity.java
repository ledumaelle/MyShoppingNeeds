package com.example.ledumaelle.myshoppingneeds;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ledumaelle.myshoppingneeds.bo.Article;

public class InfoUrlActivity extends AppCompatActivity {

    private WebView webViewUrlArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_url);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        Article article = intent.getParcelableExtra("article");

        webViewUrlArticle = findViewById(R.id.webViewUrlArticle);

        webViewUrlArticle.getSettings().setJavaScriptEnabled(true);

        //Pour rester sur l'application sinon on lance un navigateur lors d'un clic sur un lien.
        webViewUrlArticle.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webViewUrlArticle.loadUrl(article.getUrl());
    }

    //Key down --> input détecté par la machine ex: Click sur écran ou bouton retour....
    //Permettre de naviguer dans l'application avec des précédents
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //Est-ce que le code de la clef --> cliquer sur le bouton back ET
        // que c possible de retourner en arrière alors JE BACK (si plus d'historique retour à mon main app)
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webViewUrlArticle.canGoBack()) {

            webViewUrlArticle.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}