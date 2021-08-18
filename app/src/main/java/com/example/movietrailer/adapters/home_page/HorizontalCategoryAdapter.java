package com.example.movietrailer.adapters.home_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movietrailer.R;
import com.example.movietrailer.models.discover_model.ResultsItem;
import com.example.movietrailer.utils.default_lists.TopCategoriesItem;
import com.example.movietrailer.viewmodels.ViewModelFilmsFragment;

import java.util.List;

public class HorizontalCategoryAdapter extends RecyclerView.Adapter<HorizontalCategoryAdapter.ViewHolder> {

    private List<String> categoryList;
    private Context context;
    private ViewModelFilmsFragment viewModelFilmsFragment;
    private int positionIndex = -1;
    private OnClickedCategoryItemListener listener;

    public HorizontalCategoryAdapter(List<String> categoryList, Context context, ViewModelFilmsFragment viewModelFilmsFragment, OnClickedCategoryItemListener listener) {
        this.categoryList = categoryList;
        this.context = context;
        this.viewModelFilmsFragment = viewModelFilmsFragment;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HorizontalCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_recycler_categories_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalCategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.category_text.setText(categoryList.get(position));
        holder.category_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryList.get(position).equals(TopCategoriesItem.values()[position].getValue())) {

                    listener.onClickCategoryItem(TopCategoriesItem.values()[position]);

                    positionIndex = position;

                }
            }
        });

        if (position == positionIndex){
            holder.category_text.setTextColor(ContextCompat.getColor(context, R.color.green));
        }else{
            holder.category_text.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView category_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_text = itemView.findViewById(R.id.text_category);
        }
    }

    public interface OnClickedCategoryItemListener {
        void onClickCategoryItem(TopCategoriesItem item);
    }

}