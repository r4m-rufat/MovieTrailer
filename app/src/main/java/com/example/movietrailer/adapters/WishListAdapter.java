package com.example.movietrailer.adapters;

import static com.example.movietrailer.utils.constants.ConstantsKt.IMAGE_BEGIN_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movietrailer.R;
import com.example.movietrailer.models.wish_list.WishList;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    private Context context;
    private List<WishList> wishLists;

    public WishListAdapter(Context context) {
        this.context = context;
    }

    public void updateWishList(List<WishList> wishLists){
        this.wishLists = wishLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_wish_list_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, int position) {

        holder._film_title.setText(wishLists.get(position).getFilmTitle());
        holder._film_genres.setText(wishLists.get(position).getFilmGenres());
        holder._film_rating.setText(String.valueOf(wishLists.get(position).getVoteAverage()));

        Glide.with(context).load(IMAGE_BEGIN_URL + wishLists.get(position).getFilmImage()).into(holder._film_image);

    }

    @Override
    public int getItemCount() {
        if (wishLists != null){
            return wishLists.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView _film_title, _film_genres, _film_rating;
        private ImageView _film_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            _film_title = itemView.findViewById(R.id.txt_film_titleWishList);
            _film_genres = itemView.findViewById(R.id.txt_film_genresWishList);
            _film_rating = itemView.findViewById(R.id.txt_popularity);
            _film_image = itemView.findViewById(R.id.wish_listImage);

        }
    }
}
