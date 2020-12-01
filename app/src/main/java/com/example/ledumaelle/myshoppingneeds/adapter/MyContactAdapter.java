package com.example.ledumaelle.myshoppingneeds.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ledumaelle.myshoppingneeds.OnClickItemRecyclerView;
import com.example.ledumaelle.myshoppingneeds.R;
import com.example.ledumaelle.myshoppingneeds.bo.Contact;

import java.util.ArrayList;
import java.util.List;

public class MyContactAdapter extends RecyclerView.Adapter<MyContactAdapter.ViewHolder> {

    //Permet de stocker les données à afficher.
    private ArrayList<Contact> mDataset;

    private OnClickItemRecyclerView action;

    /**
     * Fournit une référence aux vues pour chaque élément de données
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Chaque élément
        public TextView txtSendArticleNameContact;
        public TextView txtSendArticleNumberContact;

        public ViewHolder(View v) {

            super(v);

            //--> cell_card.xml
            txtSendArticleNameContact = v.findViewById(R.id.txtSendArticleNameContact);
            txtSendArticleNumberContact = v.findViewById(R.id.txtSendArticleNumberContact);
            v.setOnClickListener(this);
        }

        /**
         * Action réalisée lors d'un clic sur un élément du RecyclerView.
         */
        @Override
        public void onClick(View v) {
            action.onInteraction(mDataset.get(ViewHolder.this.getAdapterPosition()));
        }
    }

    /**
     * Constructeur qui attend les données à afficher en paramètre
     **/
    public MyContactAdapter(List<Contact> myDataset, OnClickItemRecyclerView activity) {

        mDataset = (ArrayList<Contact>) myDataset;
        action = activity;
    }

    /**
     * Créé un ViewHolder qui représente le fichier ligne_recycler_view.xml ou ligne_card.xml
     **/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_card_contact, parent, false);

        //1 viewHolder pour chacune des lignes
        return new MyContactAdapter.ViewHolder(v);
    }

    /**
     * Remplie la vue représentant une ligne
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtSendArticleNameContact.setText(((Contact) mDataset.get(position)).getName());
        holder.txtSendArticleNumberContact.setText(((Contact) mDataset.get(position)).getNumber());
    }

    /**
     * Retourne le nombre de données à afficher
     *
     * @return le nombre de données à afficher
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}