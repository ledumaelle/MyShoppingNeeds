package com.example.ledumaelle.myshoppingneeds.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ledumaelle.myshoppingneeds.OnClickItemRecyclerView;
import com.example.ledumaelle.myshoppingneeds.R;
import com.example.ledumaelle.myshoppingneeds.bo.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe permet de créer les views du recycler view.
 * MyAdapter.ViewHolder est définit à l’intérieur de la classe MyAdapter
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    //Permet de stocker les données à afficher.
    private ArrayList<Article> mDataset;

    private OnClickItemRecyclerView action;

    /**
     * Fournit une référence aux vues pour chaque élément de données
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Chaque élément
        public TextView mTextView;
        public RatingBar mRatingBar;
        public ImageView mImageView;

        public ViewHolder(View v) {

            super(v);

            //--> cell_card.xml
            mTextView = v.findViewById(R.id.txtTitleArticle);
            mRatingBar = v.findViewById(R.id.ratingArticle);
            mImageView = v.findViewById(R.id.imgArticle);
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
    public MyAdapter(List<Article> myDataset, OnClickItemRecyclerView activity) {

        mDataset = (ArrayList<Article>) myDataset;
        action = activity;
    }

    /**
     * Créé un ViewHolder qui représente le fichier ligne_recycler_view.xml ou ligne_card.xml
     **/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_card, parent, false);

        //1 viewHolder pour chacune des lignes
        return new ViewHolder(v);
    }

    /**
     * Remplie la vue représentant une ligne
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(((Article) mDataset.get(position)).getName());
        holder.mRatingBar.setRating(((Article) mDataset.get(position)).getRate());
        holder.mImageView.setImageResource(R.drawable.empty);
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
