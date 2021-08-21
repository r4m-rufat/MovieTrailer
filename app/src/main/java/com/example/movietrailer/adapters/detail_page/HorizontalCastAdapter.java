package com.example.movietrailer.adapters.detail_page;

import static com.example.movietrailer.utils.constants.ConstantsKt.IMAGE_BEGIN_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movietrailer.R;
import com.example.movietrailer.models.detail_model.casts.CastItem;

import java.util.List;

public class HorizontalCastAdapter extends RecyclerView.Adapter<HorizontalCastAdapter.ViewHolder> {

    private Context context;
    private List<CastItem> castList;

    public HorizontalCastAdapter(Context context, List<CastItem> castList) {
        this.context = context;
        this.castList = castList;
    }

    @NonNull
    @Override
    public HorizontalCastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_detail_cast_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalCastAdapter.ViewHolder holder, int position) {

        String image_path = castList.get(position).getProfilePath();

        if (image_path != null){
            Glide.with(context).load(IMAGE_BEGIN_URL + image_path).into(holder._cast_image);
        }else{
            holder._cast_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person));
        }

        holder._cast_name_inReal.setText(castList.get(position).getName());
        holder._cast_name_inFilm.setText(castList.get(position).getCharacter());

    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView _cast_image;
        private TextView _cast_name_inFilm, _cast_name_inReal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _cast_image=  itemView.findViewById(R.id.cast_image);
            _cast_name_inReal =  itemView.findViewById(R.id.txt_cast_name_inReal);
            _cast_name_inFilm =  itemView.findViewById(R.id.txt_cast_name_inFilm);
        }
    }
}
