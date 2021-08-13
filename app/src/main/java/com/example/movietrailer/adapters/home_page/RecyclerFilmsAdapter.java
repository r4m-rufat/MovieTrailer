package com.example.movietrailer.adapters.home_page;

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

import java.util.List;

public class RecyclerFilmsAdapter extends RecyclerView.Adapter<RecyclerFilmsAdapter.ViewHolder> {

    private Context context;
    private List<ResultsItem> films_list;

    public RecyclerFilmsAdapter(Context context, List<ResultsItem> films_list) {
        this.context = context;
        this.films_list = films_list;
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
        Glide.with(context).load(IMAGE_BEGIN_URL + films_list.get(position).getPosterPath()).into(holder._film_image);

    }

    @Override
    public int getItemCount() {
        return films_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView _film_image;
        private TextView _film_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _film_image = itemView.findViewById(R.id.film_image);
            _film_title = itemView.findViewById(R.id.film_title);
        }
    }
}
