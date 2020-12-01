package com.example.ledumaelle.myshoppingneeds;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ledumaelle.myshoppingneeds.adapter.MyContactAdapter;
import com.example.ledumaelle.myshoppingneeds.bo.Article;
import com.example.ledumaelle.myshoppingneeds.bo.Contact;

import java.util.ArrayList;

public class SendArticleActivity extends AppCompatActivity implements OnClickItemRecyclerView<Contact> {

    private Article article;
    private Intent intent;

    private ArrayList<Contact> contactsList;
    private RecyclerView rVContactList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_article);

        intent = getIntent();

        // PERMISSION READ CONTACT
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.SEND_SMS
                },
                14500);

        //Récupération d'un objet représentant le Recycler View
        rVContactList = (RecyclerView) findViewById(R.id.rVContactList);

        // Permet d’optimiser le chargement dans le cas ou le recycler view ne change pas de taille.
        rVContactList.setHasFixedSize(true);

        // Utilisation du linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rVContactList.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();

        article = intent.getParcelableExtra("article");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            //Même code transmis pendant la permission ?
            case 14500:
                //Permission autorisée ?
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getContacts();
                }
                break;

            default:
                break;
        }
    }

    private void getContacts() {

        contactsList = new ArrayList<>();
        ContentResolver cr = getContentResolver();

        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME);

        while (cursor.moveToNext()) {

            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String phoneNumber = "";

            if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                // Query phone here. Covered next
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                while (phones.moveToNext()) {
                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();
            }

            Contact contact = new Contact();
            contact.setId(Integer.parseInt(id));
            contact.setName(name);
            contact.setNumber(phoneNumber);

            contactsList.add(contact);
        }

        //Création de l'adapteur
        mAdapter = new MyContactAdapter(contactsList, this);
        //Lie l’adapter au recycler view
        rVContactList.setAdapter(mAdapter);
    }

    private void sendAnSMS(Contact contact) {

        int MAX_SMS_MESSAGE_LENGTH = 70;
        SmsManager smsManager = SmsManager.getDefault();
        String phoneNumber = "0674859610";

        String message = "Bonjour, je suis en plein TP d'android, je souhaiterais recevoir cet article là pour Noël :) \n" +
                article.getName() + "\nIl est au prix de : " + article.getPrice() + " \nBonne soirée à vous ! <3";

        try {

            if (message.length() > MAX_SMS_MESSAGE_LENGTH) {

                ArrayList<String> messageList = SmsManager.getDefault().divideMessage(message);
                smsManager.sendMultipartTextMessage(phoneNumber, null, messageList, null, null);

            } else {

                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            }

            intent.putExtra("result", "Article envoyé !");
            this.setResult(RESULT_OK, intent);

            finish();

        } catch (Exception e) {

            Log.e("SmsProvider", "" + e);

            intent.putExtra("result", "ERREUR : l'article n'a pas pu être envoyé");
            this.setResult(RESULT_CANCELED, intent);

            finish();
        }
    }

    @Override
    public void onInteraction(Contact contact) {
        sendAnSMS(contact);
    }
}