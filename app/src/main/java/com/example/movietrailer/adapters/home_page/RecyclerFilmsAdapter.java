package com.example.movietrailer.adapters.home_page;

import static com.example.movietrailer.utils.canditions.ProgressIndicatorColorConditionKt.setProgressIndicatorColor;
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
import com.example.movietrailer.models.discover_model.ResultsItem;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.List;

public class RecyclerFilmsAdapter extends RecyclerView.Adapter<RecyclerFilmsAdapter.ViewHolder> {

    private Context context;
    private List<ResultsItem> films_list;

    public RecyclerFilmsAdapter(Context context) {
        this.context = context;
    }

    public void updateFilmList(List<ResultsItem> list){
        films_list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerFilmsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_recycler_films_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerFilmsAdapter.ViewHolder holder, int position) {

        holder._film_title.setText(films_list.get(position).getTitle());
        holder._film_release_date.setText(films_list.get(position).getReleaseDate());
        int rate = (int) (films_list.get(position).getVoteAverage() * 10);
        holder._film_rate.setText( rate + "%");
        setProgressIndicatorColor(rate, holder._progress_bar, context);
        Glide.with(context).load(IMAGE_BEGIN_URL + films_list.get(position).getPosterPath()).into(holder._film_image);

    }

    @Override
    public int getItemCount() {
        if (films_list != null){
            return films_list.size();
        }

        return 0;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView _film_image;
        private TextView _film_title, _film_release_date, _film_rate;
        private CircularProgressBar _progress_bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _film_image = itemView.findViewById(R.id.film_image);
            _film_title = itemView.findViewById(R.id.film_title);
            _film_release_date = itemView.findViewById(R.id.film_release_date);
            _film_rate = itemView.findViewById(R.id.film_rate);
            _progress_bar = itemView.findViewById(R.id.circularProgressBar);
        }
    }
}
