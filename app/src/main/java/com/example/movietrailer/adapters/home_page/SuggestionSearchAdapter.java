package com.example.movietrailer.adapters.home_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movietrailer.R;
import com.example.movietrailer.models.discover_model.ResultsItem;

import java.util.List;

public class SuggestionSearchAdapter extends RecyclerView.Adapter<SuggestionSearchAdapter.ViewHolder> {

    private Context context;
    private OnClickSuggestionSearchListener listener;
    private List<ResultsItem> searchList;

    public SuggestionSearchAdapter(Context context, OnClickSuggestionSearchListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void updateSuggestionList(List<ResultsItem> searchList){
        this.searchList = searchList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SuggestionSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_suggestion_search_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull SuggestionSearchAdapter.ViewHolder holder, int position) {
        holder._title.setText(searchList.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickSuggestionItem(searchList.get(position).getTitle());
            }
        });

    }

    @Override
    public int getItemCount() {
        if (searchList != null){
            return searchList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView _title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _title = itemView.findViewById(R.id.txt_suggestion_title);
        }
    }

    public interface OnClickSuggestionSearchListener{
        void onClickSuggestionItem(String film_title);
    }
}
