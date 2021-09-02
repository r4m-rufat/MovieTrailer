package com.example.movietrailer.adapters.detail_page;

import static com.example.movietrailer.utils.constants.ConstantsKt.IMAGE_BEGIN_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movietrailer.R;
import com.example.movietrailer.models.person.cast_images.CastImagesResponse;

public class HorizontalCastImagesAdapter extends RecyclerView.Adapter<HorizontalCastImagesAdapter.ViewHolder> {

    private Context context;
    private CastImagesResponse castImages;

    public HorizontalCastImagesAdapter(Context context) {
        this.context = context;
    }

    public void updateCastImageList(CastImagesResponse castImages){
        this.castImages = castImages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HorizontalCastImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_cast_person_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalCastImagesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String image_path = castImages.getProfiles().get(position).getFilePath();

        if (image_path != null){
            Glide.with(context).load(IMAGE_BEGIN_URL + image_path).into(holder._cast_image);
        }else{
            holder._cast_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person));
        }

    }

    @Override
    public int getItemCount() {
        if (castImages != null){
            return castImages.getProfiles().size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView _cast_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _cast_image =  itemView.findViewById(R.id.cast_image);
        }
    }
}
