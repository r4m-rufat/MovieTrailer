package com.example.movietrailer.adapters.detail_page;

import static com.example.movietrailer.utils.canditions.ProgressIndicatorColorConditionKt.setProgressIndicatorColor;
import static com.example.movietrailer.utils.constants.ConstantsKt.IMAGE_BEGIN_URL;
import static com.example.movietrailer.utils.icon_setup.SetDefaultMovieIconKt.setDefaultMovieIcon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movietrailer.R;
import com.example.movietrailer.models.detail_model.similar_films.SimilarItem;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.List;

public class HorizontalSimilarFilmsAdapter extends RecyclerView.Adapter<HorizontalSimilarFilmsAdapter.ViewHolder> {

    private Context context;
    private List<SimilarItem> similarFilmList;

    public HorizontalSimilarFilmsAdapter(Context context, List<SimilarItem> similarFilmList) {
        this.context = context;
        this.similarFilmList = similarFilmList;
    }

    @NonNull
    @Override
    public HorizontalSimilarFilmsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_similar_films_recycler_view_item, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalSimilarFilmsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String image_path = similarFilmList.get(position).getPosterPath();
        if (image_path != null){
            holder._film_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(context).load(IMAGE_BEGIN_URL + image_path).into(holder._film_image);
        }else{
            setDefaultMovieIcon(context, holder._film_image);
        }
        holder._film_title.setText(similarFilmList.get(position).getTitle());
        int rate = (int) (similarFilmList.get(position).getVoteAverage() * 10);
        holder._film_rating.setText(rate + "%");
        setProgressIndicatorColor(rate, holder.circularProgressBar, context);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", similarFilmList.get(position).getId());
                Navigation.findNavController(v).navigate(R.id.viewFilmDetailFragment, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return similarFilmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView _film_title, _film_rating;
        private  ImageView _film_image;
        private CircularProgressBar circularProgressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _film_title = itemView.findViewById(R.id.txt_film_title);
            _film_rating = itemView.findViewById(R.id.similar_film_rate);
            _film_image = itemView.findViewById(R.id.similar_film_image);
            circularProgressBar = itemView.findViewById(R.id.similar_circularProgressBar);
        }
    }
}
