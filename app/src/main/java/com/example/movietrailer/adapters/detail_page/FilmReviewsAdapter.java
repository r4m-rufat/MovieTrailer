package com.example.movietrailer.adapters.detail_page;

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
import com.chaek.android.RatingBar;
import com.example.movietrailer.R;
import com.example.movietrailer.models.film_reviews.ResultsItem;

import java.util.List;

public class FilmReviewsAdapter extends RecyclerView.Adapter<FilmReviewsAdapter.ViewHolder> {

    private Context context;
    private List<ResultsItem> reviewList;

    public FilmReviewsAdapter(Context context) {
        this.context = context;
    }

    public void updateReviewList(List<ResultsItem> reviewList){
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilmReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_review_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmReviewsAdapter.ViewHolder holder, int position) {
        String image_url = reviewList.get(position).getAuthorDetails().getAvatarPath();

        /**
         * in api some image urls starts with /https
         * and that's why some configurations should do in url
         */
        if (image_url != null){
            if (image_url.startsWith("/https")){
                StringBuilder stringBuilder = new StringBuilder(image_url);
                stringBuilder.deleteCharAt(0);
                Glide.with(context).load(stringBuilder.toString()).into(holder._profile_image);
            }else{
                Glide.with(context).load(IMAGE_BEGIN_URL + image_url).into(holder._profile_image);
            }
        }else{
            holder._profile_image.setImageResource(R.drawable.ic_person);
        }

        holder._profile_name.setText(reviewList.get(position).getAuthorDetails().getUsername());
        holder._txt_review.setText(reviewList.get(position).getContent());
        holder._txt_review_date.setText(reviewList.get(position).getCreatedAt().substring(0,10));

        double rating = reviewList.get(position).getAuthorDetails().getRating();
        if (rating != 0){
            holder._rating_bar.setScore((float) rating);
        }else{
            holder._rating_bar.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        if (reviewList != null){
            return reviewList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView _profile_image;
        private TextView _profile_name, _txt_review, _txt_review_date;
        private RatingBar _rating_bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _profile_image = itemView.findViewById(R.id.circle_reviewPersonImage);
            _profile_name = itemView.findViewById(R.id.txt_reviewPersonName);
            _txt_review = itemView.findViewById(R.id.txt_review);
            _txt_review_date = itemView.findViewById(R.id.txt_reviewDate);
            _rating_bar = itemView.findViewById(R.id.rating_bar);
        }
    }
}
