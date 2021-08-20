package com.example.movietrailer.fragments;

import static com.example.movietrailer.utils.constants.ConstantsKt.IMAGE_BEGIN_URL;
import static com.example.movietrailer.utils.converters.BudgetConverterKt.convertValueToBudget;
import static com.example.movietrailer.utils.converters.SecondToTimeConverterKt.convertSecondToTime;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movietrailer.R;
import com.example.movietrailer.models.detail_model.details.DetailResponse;
import com.example.movietrailer.models.discover_model.DiscoverResponse;
import com.example.movietrailer.viewmodels.FilmDetailFragmentViewModel;

public class FilmDetailFragment extends Fragment {

    private static final String TAG = "FilmDetailFragment";
    private FilmDetailFragmentViewModel filmDetailFragmentViewModel;
    private int id;
    private ImageView detailVideoPicture, icPlayVideo, filmBackdropImage;
    private TextView filmTitle, trailerTime, rating, budget, overview, releaseDate;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
            Log.d(TAG, "onCreate: Film id is " + id);
            filmDetailFragmentViewModel = new ViewModelProvider(this).get(FilmDetailFragmentViewModel.class);
        }

        context = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_film_detail, container, false);

       getWidgets(view);
       getDetailLiveData();

       return view;

    }

    private void getDetailLiveData(){

        filmDetailFragmentViewModel.getFilmList(id).observe(getActivity(), new Observer<DetailResponse>() {
            @Override
            public void onChanged(DetailResponse detailResponse) {
                setItemsToWidgets(detailResponse);
            }
        });

    }

    private void getWidgets(View view){
        detailVideoPicture = view.findViewById(R.id.detailVideoPicture);
        icPlayVideo = view.findViewById(R.id.ic_play_video);
        filmBackdropImage = view.findViewById(R.id.film_backdrop_image);
        filmTitle = view.findViewById(R.id.txt_film_title);
        trailerTime = view.findViewById(R.id.txt_trailer_time);
        rating = view.findViewById(R.id.txt_rating);
        budget = view.findViewById(R.id.txt_budget);
        overview = view.findViewById(R.id.txt_overview);
        releaseDate = view.findViewById(R.id.txt_release_date);
    }

    private void setItemsToWidgets(DetailResponse detailResponse){

        Glide.with(context).load(IMAGE_BEGIN_URL + detailResponse.getPosterPath()).into(detailVideoPicture);
        Glide.with(context).load(IMAGE_BEGIN_URL + detailResponse.getBackdropPath()).into(filmBackdropImage);

        filmTitle.setText(detailResponse.getTitle());
        trailerTime.setText(convertSecondToTime(detailResponse.getRuntime()));
        rating.setText(String.valueOf(detailResponse.getVoteAverage()));
        budget.setText(convertValueToBudget(detailResponse.getBudget()));
        overview.setText(detailResponse.getOverview());
        releaseDate.setText(detailResponse.getReleaseDate());

    }

}