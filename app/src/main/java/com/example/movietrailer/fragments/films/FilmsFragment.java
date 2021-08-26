package com.example.movietrailer.fragments.films;

import static com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarSetupKt.setUpBottomNavigationView;
import static com.example.movietrailer.utils.constants.ConstantsKt.PAGE_SIZE;
import static com.example.movietrailer.utils.default_lists.FilmCategoriesListKt.FilmCategoriesList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.movietrailer.R;
import com.example.movietrailer.adapters.home_page.HorizontalCategoryAdapter;
import com.example.movietrailer.adapters.home_page.RecyclerFilmsAdapter;
import com.example.movietrailer.models.discover_model.ResultsItem;
import com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarItems;
import com.example.movietrailer.utils.default_lists.TopCategoriesItem;
import com.example.movietrailer.viewmodels.films.FilmsFragmentViewModel;

import java.util.List;

public class FilmsFragment extends Fragment implements HorizontalCategoryAdapter.OnClickedCategoryItemListener{

    private static final String TAG = "FilmsFragment";
    private RecyclerView categoryRecyclerView, filmsRecyclerView;
    private HorizontalCategoryAdapter horizontalCategoryAdapter;
    private FilmsFragmentViewModel filmsFragmentViewModel;
    private ProgressBar progressBar;
    private EditText editSearch;
    private RecyclerFilmsAdapter recyclerFilmsAdapter;
    private MeowBottomNavigation bottomNavigation;

    // var
    boolean isLoading = false;
    boolean isScrolling = false;
    boolean isLastPage = false;
    private String query = "";
    private boolean clickSearchButton = false;
    private boolean clickCategoryItem = false;
    private TopCategoriesItem item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filmsFragmentViewModel = new ViewModelProvider(this).get(FilmsFragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_films, container, false);

        getWidgets(view);
        setCategoryRecyclerViewSetups();
        setFilmsRecyclerView();

        // when progressbar visible or gone in loading proses
        showOrHideProgressBar();

        clickedEditSearchRightDrawable();

        bottomNavigation.show(BottomNavigationBarItems.FILMS.ordinal(), true);
        setUpBottomNavigationView(bottomNavigation, view);

        return view;
    }

    private void getWidgets(View view) {

        categoryRecyclerView = view.findViewById(R.id.recycler_categories);
        filmsRecyclerView = view.findViewById(R.id.film_recycler_view);
        progressBar = view.findViewById(R.id.circularProgressBar);
        editSearch = view.findViewById(R.id.edit_search);
        bottomNavigation = view.findViewById(R.id.bottom_navigation_view);

    }

    private void setCategoryRecyclerViewSetups() {

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setHasFixedSize(false);
        horizontalCategoryAdapter = new HorizontalCategoryAdapter(FilmCategoriesList(), getActivity(), filmsFragmentViewModel, FilmsFragment.this);
        categoryRecyclerView.setAdapter(horizontalCategoryAdapter);

    }

    private void setFilmsRecyclerView() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        filmsRecyclerView.setLayoutManager(gridLayoutManager);
        filmsRecyclerView.setHasFixedSize(false);
        recyclerFilmsAdapter = new RecyclerFilmsAdapter(getActivity());

        filmsFragmentViewModel.getFilmList().observe(getActivity(), new Observer<List<ResultsItem>>() {
            @Override
            public void onChanged(List<ResultsItem> resultsItems) {
                recyclerFilmsAdapter.updateFilmList(resultsItems);
            }
        });

        filmsRecyclerView.setAdapter(recyclerFilmsAdapter);

        setupPagination(gridLayoutManager);

    }

    private void setupPagination(GridLayoutManager gridLayoutManager) {

        filmsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();

                boolean isNotLoadingAndLastPage = !isLastPage && !isLoading;
                boolean isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount;
                boolean isNotAtBeginning = firstVisibleItemPosition >= 0;
                boolean isTotalMoreThanVisible = totalItemCount >= PAGE_SIZE;

                boolean paginateOrNot = isNotLoadingAndLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling;

                if (paginateOrNot){
                    filmsFragmentViewModel.incrementPageNumber();
                    isScrolling = false;
                    if (clickSearchButton){
                        filmsFragmentViewModel.getSearchResult();
                    }else if (clickCategoryItem){
                        filmsFragmentViewModel.getCategoryFilmListWhenClicked(item);
                    }else{
                        filmsFragmentViewModel.getFilmList();
                    }
                }

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }
        });

    }

    private void showOrHideProgressBar() {

        filmsFragmentViewModel.getLoading().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {
                if (loading) {
                    progressBar.setVisibility(View.VISIBLE);
                    isLoading = true;
                } else {
                    progressBar.setVisibility(View.GONE);
                    isLoading = false;
                }
            }
        });

    }

    private void clickedEditSearchRightDrawable(){

        editSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (editSearch.getRight() - editSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        // edit text string
                        query = editSearch.getText().toString().trim();
                        filmsFragmentViewModel.getQuery().setValue(query);
                        filmsFragmentViewModel.getSearchResult().observe(getActivity(), new Observer<List<ResultsItem>>() {
                            @Override
                            public void onChanged(List<ResultsItem> list) {
                                recyclerFilmsAdapter.updateFilmList(list);
                            }
                        });

                        clickSearchButton = true;
                        clickCategoryItem = false;

                        // when clicked search button, should reset film list
                        filmsFragmentViewModel.resetSearchVariables();

                        return true;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onClickCategoryItem(TopCategoriesItem item) {
        filmsFragmentViewModel.resetSearchVariables();
        filmsFragmentViewModel.getCategorySelectedItem().setValue(item);
        filmsFragmentViewModel.
                getCategoryFilmListWhenClicked(item)
                .observe(getActivity(), new Observer<List<ResultsItem>>() {
                    @Override
                    public void onChanged(List<ResultsItem> list) {
                        recyclerFilmsAdapter.notifyDataSetChanged();
                        horizontalCategoryAdapter.notifyDataSetChanged(); // because change color of top item text
                    }
                });

        /**
         * this setup for pagination, look through setupPagination
          */

        clickCategoryItem = true;
        clickSearchButton = false;
        this.item = item;

    }
}