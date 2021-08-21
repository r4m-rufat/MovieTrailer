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
import com.example.movietrailer.models.detail_model.casts.CrewItem;

import java.util.List;

public class HorizontalPersonalStaffAdapter extends RecyclerView.Adapter<HorizontalPersonalStaffAdapter.ViewHolder> {

    private Context context;
    private List<CrewItem> crewList;

    public HorizontalPersonalStaffAdapter(Context context, List<CrewItem> crewList) {
        this.context = context;
        this.crewList = crewList;
    }

    @NonNull
    @Override
    public HorizontalPersonalStaffAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_detail_personal_staff_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalPersonalStaffAdapter.ViewHolder holder, int position) {

        String image_path = crewList.get(position).getProfilePath();

        if (image_path != null){
            Glide.with(context).load(IMAGE_BEGIN_URL + image_path).into(holder._personal_image);
        }else{
            holder._personal_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person));
        }

        holder._person_name.setText(crewList.get(position).getName());
        holder._job_name.setText(crewList.get(position).getJob());

    }

    @Override
    public int getItemCount() {
        return crewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView _personal_image;
        private TextView _person_name, _job_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _personal_image = itemView.findViewById(R.id.personal_staff_image);
            _person_name = itemView.findViewById(R.id.txt_person_name);
            _job_name = itemView.findViewById(R.id.txt_job_inFilm);
        }
    }
}
