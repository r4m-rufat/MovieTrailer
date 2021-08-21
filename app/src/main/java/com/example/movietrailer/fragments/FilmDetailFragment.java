package com.example.movietrailer.fragments;

import static com.example.movietrailer.utils.constants.ConstantsKt.IMAGE_BEGIN_URL;
import static com.example.movietrailer.utils.converters.BudgetConverterKt.convertValueToBudget;
import static com.example.movietrailer.utils.converters.SecondToTimeConverterKt.convertSecondToTime;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movietrailer.R;
import com.example.movietrailer.adapters.detail_page.HorizontalCastAdapter;
import com.example.movietrailer.adapters.detail_page.HorizontalGenreAdapter;
import com.example.movietrailer.adapters.detail_page.HorizontalPersonalStaffAdapter;
import com.example.movietrailer.adapters.detail_page.HorizontalSimilarFilmsAdapter;
import com.example.movietrailer.models.detail_model.casts.CastItem;
import com.example.movietrailer.models.detail_model.casts.CastResponse;
import com.example.movietrailer.models.detail_model.casts.CrewItem;
import com.example.movietrailer.models.detail_model.details.DetailResponse;
import com.example.movietrailer.models.detail_model.details.GenresItem;
import com.example.movietrailer.models.detail_model.similar_films.SimilarItem;
import com.example.movietrailer.models.detail_model.similar_films.SimilarResponse;
import com.example.movietrailer.models.detail_model.video.VideoResponse;
import com.example.movietrailer.viewmodels.FilmDetailFragmentViewModel;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class FilmDetailFragment extends Fragment {

    private static final String TAG = "FilmDetailFragment";
    private FilmDetailFragmentViewModel filmDetailFragmentViewModel;
    private int id;
    private ImageView detailVideoPicture, icPlayVideo, filmBackdropImage;
    private TextView filmTitle, trailerTime, rating, budget, overview, releaseDate;
    private Context context;
    private RecyclerView genreRecyclerView, castsRecyclerView, personalStaffRecyclerView, similarFilmsRecyclerView;
    private HorizontalGenreAdapter horizontalGenreAdapter;
    private HorizontalCastAdapter horizontalCastAdapter;
    private HorizontalPersonalStaffAdapter horizontalPersonalStaffAdapter;
    private HorizontalSimilarFilmsAdapter horizontalSimilarFilmsAdapter;
    private Dialog videoDialog;
    private YouTubePlayerView youTubePlayerView;
    private ImageView closeDialog;

    /**
     * get film id in onCreate and also FilmDetailFragmentViewModel
     *
     * @param savedInstanceState
     */
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
        getCastsLiveData();
        getSimilarFilmsLiveData();
        getVideoLiveData();

        return view;

    }

    /**
     * get film detail list and it is observable
     */
    private void getDetailLiveData() {

        filmDetailFragmentViewModel.getFilmList(id).observe(getActivity(), new Observer<DetailResponse>() {
            @Override
            public void onChanged(DetailResponse detailResponse) {
                setItemsToWidgets(detailResponse);
                setupGenreRecyclerView(detailResponse.getGenres());
            }
        });

    }

    /**
     * get observable casts list
     */
    private void getCastsLiveData() {

        filmDetailFragmentViewModel.getCastsList(id).observe(getActivity(), new Observer<CastResponse>() {
            @Override
            public void onChanged(CastResponse castResponse) {
                setupCastsRecyclerView(castResponse.getCast());
                setupCrewRecyclerView(castResponse.getCrew());
            }
        });

    }

    /**
     * get similar films live data and set it when data came
     */
    private void getSimilarFilmsLiveData() {

        filmDetailFragmentViewModel.getSimilarFilmList(id).observe(getActivity(), new Observer<SimilarResponse>() {
            @Override
            public void onChanged(SimilarResponse similarResponse) {
                setupSimilarFilmsRecyclerView(similarResponse.getResults());
            }
        });

    }


    // initialize widgets
    private void getWidgets(View view) {
        detailVideoPicture = view.findViewById(R.id.detailVideoPicture);
        icPlayVideo = view.findViewById(R.id.ic_play_video);
        filmBackdropImage = view.findViewById(R.id.film_backdrop_image);
        filmTitle = view.findViewById(R.id.txt_film_title);
        trailerTime = view.findViewById(R.id.txt_trailer_time);
        rating = view.findViewById(R.id.txt_rating);
        budget = view.findViewById(R.id.txt_budget);
        overview = view.findViewById(R.id.txt_overview);
        releaseDate = view.findViewById(R.id.txt_release_date);
        genreRecyclerView = view.findViewById(R.id.genreRecyclerView);
        castsRecyclerView = view.findViewById(R.id.castsRecyclerView);
        personalStaffRecyclerView = view.findViewById(R.id.personalStaffRecyclerView);
        similarFilmsRecyclerView = view.findViewById(R.id.similar_filmsRecyclerView);
    }

    /**
     * set all items to ui
     * @param detailResponse
     */
    private void setItemsToWidgets(DetailResponse detailResponse) {

        Glide.with(context).load(IMAGE_BEGIN_URL + detailResponse.getPosterPath()).into(detailVideoPicture);
        Glide.with(context).load(IMAGE_BEGIN_URL + detailResponse.getBackdropPath()).into(filmBackdropImage);

        filmTitle.setText(detailResponse.getTitle());
        trailerTime.setText(convertSecondToTime(detailResponse.getRuntime()));
        rating.setText(String.valueOf(detailResponse.getVoteAverage()));
        budget.setText(convertValueToBudget(detailResponse.getBudget()));
        overview.setText(detailResponse.getOverview());
        releaseDate.setText(detailResponse.getReleaseDate());

    }

    private void setupGenreRecyclerView(List<GenresItem> genreList) {

        horizontalGenreAdapter = new HorizontalGenreAdapter(context, genreList);
        setupHorizontalRecyclerView(genreRecyclerView);
        genreRecyclerView.setAdapter(horizontalGenreAdapter);

    }

    private void setupCastsRecyclerView(List<CastItem> castList) {

        horizontalCastAdapter = new HorizontalCastAdapter(context, castList);
        setupHorizontalRecyclerView(castsRecyclerView);
        castsRecyclerView.setAdapter(horizontalCastAdapter);

    }

    private void setupCrewRecyclerView(List<CrewItem> crewList) {

        horizontalPersonalStaffAdapter = new HorizontalPersonalStaffAdapter(context, crewList);
        setupHorizontalRecyclerView(personalStaffRecyclerView);
        personalStaffRecyclerView.setAdapter(horizontalPersonalStaffAdapter);

    }

    private void setupSimilarFilmsRecyclerView(List<SimilarItem> similarFilmList) {

        horizontalSimilarFilmsAdapter = new HorizontalSimilarFilmsAdapter(context, similarFilmList);
        setupHorizontalRecyclerView(similarFilmsRecyclerView);
        similarFilmsRecyclerView.setAdapter(horizontalSimilarFilmsAdapter);

    }

    private void setupHorizontalRecyclerView(RecyclerView recyclerView) {

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

    }

    private void getVideoLiveData(){

        filmDetailFragmentViewModel.getVideo(id).observe(getActivity(), new Observer<VideoResponse>() {
            @Override
            public void onChanged(VideoResponse videoResponse) {
                // needed only one video id
                playVideo(videoResponse.getResults().get(0).getKey());
            }
        });

    }


    private void playVideo(String videoID){

        icPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoID != null){
                    showVideoDialogue(videoID);
                }else{
                    // because api is not perfect sometimes video id can be null
                    Toast.makeText(getContext(), "Video is not available yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * video id is setted and then is showing with youtube player
     * @param id
     */
    private void showVideoDialogue(String id){

        videoDialog = new Dialog(context, R.style.AppTheme_FullScreenDialog); // custom style in for full screen and also it's background
        videoDialog.setContentView(R.layout.layout_video_dialog);
        youTubePlayerView = videoDialog.findViewById(R.id.youtubeVideo);
        closeDialog = videoDialog.findViewById(R.id.ic_close);
        videoDialog.setCancelable(false);

        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(id, 0);
            }
        });

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerView.release();
                videoDialog.cancel();
            }
        });

        videoDialog.show();

    }
}