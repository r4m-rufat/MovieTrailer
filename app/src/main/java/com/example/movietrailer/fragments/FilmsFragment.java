package com.example.movietrailer.fragments;

import static com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarSetupKt.setUpBottomNavigationView;
import static com.example.movietrailer.utils.default_lists.FilmCategoriesListKt.FilmCategoriesList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.movietrailer.R;
import com.example.movietrailer.adapters.home_page.HorizontalCategoryAdapter;
import com.example.movietrailer.adapters.home_page.RecyclerFilmsAdapter;
import com.example.movietrailer.models.discover_model.ResultsItem;
import com.example.movietrailer.viewmodels.ViewModelFilmsFragment;

import java.util.List;

public class FilmsFragment extends Fragment {

    private static final String TAG = "FilmsFragment";

    private MeowBottomNavigation bottomNavigation;
    private RecyclerView categoryRecyclerView, filmsRecyclerView;
    private HorizontalCategoryAdapter horizontalCategoryAdapter;
    private ViewModelFilmsFragment viewModelFilmsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelFilmsFragment = new ViewModelProvider(this).get(ViewModelFilmsFragment.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_films, container, false);

        getWidgets(view);
        setUpBottomNavigationView(bottomNavigation);
        setCategoryRecyclerViewSetups();
        setFilmsRecyclerView();

        return view;
    }

    private void getWidgets(View view){

        bottomNavigation = view.findViewById(R.id.bottom_navigation_view);
        categoryRecyclerView = view.findViewById(R.id.recycler_categories);
        filmsRecyclerView = view.findViewById(R.id.film_recycler_view);

    }

    private void setCategoryRecyclerViewSetups(){

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setHasFixedSize(false);
        horizontalCategoryAdapter = new HorizontalCategoryAdapter(FilmCategoriesList(), getActivity());
        categoryRecyclerView.setAdapter(horizontalCategoryAdapter);

    }

    private void setFilmsRecyclerView(){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        filmsRecyclerView.setLayoutManager(gridLayoutManager);
        filmsRecyclerView.setHasFixedSize(false);
        RecyclerFilmsAdapter recyclerFilmsAdapter = new RecyclerFilmsAdapter(getActivity(), viewModelFilmsFragment.getFilmList().getValue());

        viewModelFilmsFragment.getFilmList().observe(getActivity(), new Observer<List<ResultsItem>>() {
            @Override
            public void onChanged(List<ResultsItem> resultsItems) {
                recyclerFilmsAdapter.updateFilmList(resultsItems);
                filmsRecyclerView.setAdapter(recyclerFilmsAdapter);
            }
        });

    }


}