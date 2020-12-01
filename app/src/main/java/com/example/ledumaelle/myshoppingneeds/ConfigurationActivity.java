package com.example.ledumaelle.myshoppingneeds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class ConfigurationActivity extends AppCompatActivity {

    public static final String CLE_PREFERENCE_SORT_PRICE = "sort_price";
    public static final String CLE_PREFERENCE_DEFAULT_PRICE = "default_price";
    public static final String NOM_FICHIER = "my_preferences";

    private Switch switchSortPrice;
    private EditText txtDefaultPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        switchSortPrice = (Switch) findViewById(R.id.switchSortPrice);
        txtDefaultPrice = (EditText) findViewById(R.id.txtDefaultPrice);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Récupérer les préférences si ya dans l'app
        SharedPreferences sharedPreferences = getSharedPreferences(NOM_FICHIER, MODE_PRIVATE);

        boolean value = sharedPreferences.getBoolean(CLE_PREFERENCE_SORT_PRICE, false);
        switchSortPrice.setChecked(value);

        txtDefaultPrice.setText(sharedPreferences.getString(CLE_PREFERENCE_DEFAULT_PRICE, ""));
    }

    public void saveConfiguration(View view) {

        SharedPreferences sharedPreferences = getSharedPreferences(NOM_FICHIER, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CLE_PREFERENCE_SORT_PRICE, switchSortPrice.isChecked());
        editor.putString(CLE_PREFERENCE_DEFAULT_PRICE,txtDefaultPrice.getText().toString());
        editor.apply();
    }
}