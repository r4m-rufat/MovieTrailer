package com.example.movietrailer.adapters.detail_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movietrailer.R;
import com.example.movietrailer.models.detail_model.details.GenresItem;

import java.util.List;

public class HorizontalGenreAdapter extends RecyclerView.Adapter<HorizontalGenreAdapter.ViewHolder> {

    private Context context;
    private List<GenresItem> genresList;

    public HorizontalGenreAdapter(Context context, List<GenresItem> genresList) {
        this.context = context;
        this.genresList = genresList;
    }

    @NonNull
    @Override
    public HorizontalGenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_genre_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalGenreAdapter.ViewHolder holder, int position) {
        holder._genre_text.setText(genresList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return genresList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView _genre_text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _genre_text = itemView.findViewById(R.id.txt_genre);
        }
    }
}
