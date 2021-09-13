package com.example.movietrailer.fragments.films;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.movietrailer.converters.GenresIdToStringConverterKt.convertGenreIdsToString;
import static com.example.movietrailer.utils.constants.ConstantsKt.PAGE_SIZE;
import static com.example.movietrailer.utils.default_lists.FilmCategoriesListKt.FilmCategoriesList;
import static com.example.movietrailer.utils.default_lists.FilterGenresListKt.getGenreFilterHashMap;
import static com.example.movietrailer.utils.default_lists.FilterGenresListKt.getGenresFilerList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.movietrailer.R;
import com.example.movietrailer.adapters.home_page.HorizontalCategoryAdapter;
import com.example.movietrailer.adapters.home_page.RecyclerFilmsAdapter;
import com.example.movietrailer.adapters.home_page.RecyclerGenresFilterAdapter;
import com.example.movietrailer.adapters.home_page.SuggestionSearchAdapter;
import com.example.movietrailer.internal_storage.PreferenceManager;
import com.example.movietrailer.models.discover_model.ResultsItem;
import com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarItems;
import com.example.movietrailer.utils.check_connection.CheckConnectionAsynchronously;
import com.example.movietrailer.utils.default_lists.TopCategoriesItem;
import com.example.movietrailer.viewmodels.films.FilmsFragmentViewModel;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ClickableViewAccessibility")
public class FilmsFragment extends Fragment
        implements HorizontalCategoryAdapter.OnClickedCategoryItemListener,
        RecyclerGenresFilterAdapter.OnClickedGenreItemListener,
        SuggestionSearchAdapter.OnClickSuggestionSearchListener {

    private static final String TAG = "FilmsFragment";
    private View view;
    private RecyclerView categoryRecyclerView, filmsRecyclerView, genreFilterRecyclerView;
    private HorizontalCategoryAdapter horizontalCategoryAdapter;
    private FilmsFragmentViewModel filmsFragmentViewModel;
    private SpinKitView progressBar;
    private EditText editSearch;
    private RecyclerFilmsAdapter recyclerFilmsAdapter;
    private Context context;
    private GridLayoutManager gridLayoutManager;
    private RelativeLayout rel_searchBox;
    private ImageView ic_arrowUp, filter, ic_search, ic_logo;
    private LinearLayout layout_filter;
    private NumberPicker vote_averagePicker;
    private RecyclerGenresFilterAdapter genresFilterAdapter;
    private List<Integer> filterList = new ArrayList<>();
    private Spinner sortBySpinner;
    private TextView saveFilter;
    private boolean showFilter = true;
    private PreferenceManager preferenceManager;

    // suggestion search
    private RecyclerView suggestionRecycler;
    private SuggestionSearchAdapter searchAdapter;
    private RelativeLayout relSuggestion;
    private LinearLayout linear_emptyInfo;
    private String clicked_suggestion_item = "";

    // for width
    private WindowManager manager;
    private Display display;

    // var
    boolean isLoading = false;
    boolean isScrolling = false;
    boolean isLastPage = false;
    private String query = "";
    private boolean clickSearchButton = false;
    private boolean clickCategoryItem = false;
    private TopCategoriesItem item = TopCategoriesItem.DISCOVER;

    private String filter_routes[] = {
            "popularity.desc",
            "popularity.asc",
            "release_date.desc",
            "release_date.asc",
            "revenue.desc",
            "revenue.asc",
            "vote_average.desc",
            "vote_average.asc"
    };

    private ArrayAdapter<String> spinnerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = requireContext();
        filmsFragmentViewModel = new ViewModelProvider(this).get(FilmsFragmentViewModel.class);
        preferenceManager = new PreferenceManager(context);
        if (preferenceManager.getBoolean("dark_mode")) {
            context.setTheme(R.style.AppTheme_Base_Night);
        } else {
            context.setTheme(R.style.Theme_MovieTrailer);
        }
        CheckConnectionAsynchronously.INSTANCE.init(context);
        /**
         * only when page is created then okay it should request to the api
         * but pass the another page and back to this page with back button
         * then shouldn't request to the api again
         */
        CheckConnectionAsynchronously.INSTANCE.observe((LifecycleOwner) context, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    filmsFragmentViewModel.getDataSetToMutableLiveData();
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_films, container, false);

        if (requireActivity().findViewById(R.id.bottom_navigation_view) != null){
            // showing Bottom Navigation View
            requireActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
            ((MeowBottomNavigation)requireActivity().findViewById(R.id.bottom_navigation_view)).show(BottomNavigationBarItems.FILMS.ordinal(), true);
        }

        getPhoneWidth();
        getWidgets(view);
        setIc_logo();
        setCategoryRecyclerViewSetups();
        setUpRecyclerView();
        searchBoxWhenEditSearchFocusable();

        setFilmsRecyclerView();

        // when progressbar visible or gone in loading proses
        showOrHideProgressBar();

        clickedSearchIcon();
        clickedArrowUp();

        // filter setups
        saveFilterOptionsWhenLandscapeMode();
        clickedFilterButton();
        setupPicker();
        setupGenreFilterRecyclerView();
        clickedSaveFilter();

        // suggestion search
        setupRecyclerSearchSuggestion();
        searchWatcher();

        return view;
    }

    private void getWidgets(View view) {

        categoryRecyclerView = view.findViewById(R.id.recycler_categories);
        filmsRecyclerView = view.findViewById(R.id.film_recycler_view);
        progressBar = view.findViewById(R.id.circularProgressBar);
        editSearch = view.findViewById(R.id.edit_search);
        ic_arrowUp = view.findViewById(R.id.ic_arrowUp);
        filter = view.findViewById(R.id.ic_filter);
        layout_filter = view.findViewById(R.id.layout_filter);
        vote_averagePicker = view.findViewById(R.id.vote_averageNumber);
        genreFilterRecyclerView = view.findViewById(R.id.recycler_filter_genre);
        sortBySpinner = view.findViewById(R.id.sortBySpinner);
        saveFilter = view.findViewById(R.id.saveFilter);
        suggestionRecycler = view.findViewById(R.id.recyclerSuggestionSearch);
        relSuggestion = view.findViewById(R.id.rel_suggestion);
        linear_emptyInfo = view.findViewById(R.id.linear_emptySuggestionInfo);
        ic_search = view.findViewById(R.id.ic_search);
        rel_searchBox = view.findViewById(R.id.rel_search);
        ic_logo = view.findViewById(R.id.ic_logo);

    }

    private void setCategoryRecyclerViewSetups() {

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setHasFixedSize(false);
        horizontalCategoryAdapter = new HorizontalCategoryAdapter(FilmCategoriesList(), getActivity(), filmsFragmentViewModel, FilmsFragment.this);
        categoryRecyclerView.setAdapter(horizontalCategoryAdapter);

    }

    private void setIc_logo(){
        if (preferenceManager.getBoolean("dark_mode")){
            ic_logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_logo_white));
        }else{
            ic_logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_logo));
        }
    }

    private void setFilmsRecyclerView() {

        filmsFragmentViewModel.getFilmList().observe(getActivity(), new Observer<List<ResultsItem>>() {
            @Override
            public void onChanged(List<ResultsItem> resultsItems) {
                recyclerFilmsAdapter.updateFilmList(resultsItems);
            }
        });

    }

    private void setUpRecyclerView() {

        if (display.getWidth() >= 1344) {
            gridLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        }
        filmsRecyclerView.setLayoutManager(gridLayoutManager);
        filmsRecyclerView.setHasFixedSize(false);
        recyclerFilmsAdapter = new RecyclerFilmsAdapter(getActivity());

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

                if (paginateOrNot) {
                    filmsFragmentViewModel.incrementPageNumber();
                    isScrolling = false;
                    if (clickSearchButton) {
                        filmsFragmentViewModel.getSearchResult();
                    } else if (clickCategoryItem) {
                        filmsFragmentViewModel.getCategoryFilmListWhenClicked(item);
                    } else {
                        filmsFragmentViewModel.getDataSetToMutableLiveData();
                    }
                }

                /**
                 * if item position is more than 1 then arrow up button will be visible
                 */
                if (gridLayoutManager.findFirstVisibleItemPosition() > 1) {
                    ic_arrowUp.setVisibility(View.VISIBLE);
                } else {
                    ic_arrowUp.setVisibility(View.GONE);
                }

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) { // No scrolling
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ic_arrowUp.setVisibility(View.GONE);
                        }
                    }, 7000L); // delay of 7 seconds before hiding the fab
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

    private void clickedSearchIcon() {

        ic_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckConnectionAsynchronously.INSTANCE.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean connection) {
                        if (connection) {
                            // edit text string
                            query = editSearch.getText().toString().trim();
                            if (!query.isEmpty()) {
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
                                filmsFragmentViewModel.resetVariables();
                            }
                        }else{
                            Toast.makeText(context, "Please, check internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // hide keyboard and unfocused search edittext
                editSearch.onEditorAction(EditorInfo.IME_ACTION_DONE);
                hideKeyboard(view);

                // suggestion layout should be gone
                relSuggestion.setVisibility(View.GONE);

                // filter has gone
                layout_filter.setVisibility(View.GONE);
                showFilter = true;

            }
        });

    }

    private void searchBoxWhenEditSearchFocusable() {
        editSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ic_search.setColorFilter(ContextCompat.getColor(requireContext(), R.color.progress_orange), android.graphics.PorterDuff.Mode.SRC_IN);
                    rel_searchBox.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.background_search_box_focusable));
                    layout_filter.setVisibility(View.GONE);
                    showFilter = true;
                } else {
                    rel_searchBox.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.background_search_icon));
                    if (preferenceManager.getBoolean("dark_mode")) {
                        ic_search.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                    } else {
                        ic_search.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                    }
                }
            }
        });
    }

    private void clickedArrowUp() {

        ic_arrowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filmsRecyclerView.smoothScrollToPosition(0);
            }
        });

    }

    @Override
    public void onClickCategoryItem(TopCategoriesItem item) {
        filmsFragmentViewModel.resetVariables();
        filmsFragmentViewModel.getCategorySelectedItem().setValue(item);
        horizontalCategoryAdapter.notifyDataSetChanged(); // because change color of top item text

        CheckConnectionAsynchronously.INSTANCE.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean connection) {
                if (connection) {
                    filmsFragmentViewModel.
                            getCategoryFilmListWhenClicked(filmsFragmentViewModel.getCategorySelectedItem().getValue())
                            .observe(getActivity(), new Observer<List<ResultsItem>>() {
                                @Override
                                public void onChanged(List<ResultsItem> list) {
                                    recyclerFilmsAdapter.notifyDataSetChanged();
                                }
                            });
                }
            }
        });

        /**
         * this setup for pagination, look through setupPagination
         */

        clickCategoryItem = true;
        clickSearchButton = false;
        this.item = item;
        filterVisibility();

    }

    /**
     * filter visibility is only visible in Discover category
     * because api doesn't provide filter option with other categories
     */
    private void filterVisibility() {

        if (item == TopCategoriesItem.DISCOVER) {
            filter.setVisibility(View.VISIBLE);
        } else {
            filter.setVisibility(View.GONE);
        }
    }

    private void clickedFilterButton() {

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showFilter) {
                    showFilter = false;
                    layout_filter.setVisibility(View.VISIBLE);
                } else {
                    showFilter = true;
                    layout_filter.setVisibility(View.GONE);
                }
            }
        });

    }

    private void getPhoneWidth() {
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = manager.getDefaultDisplay();
    }

    private void setupPicker() {

        vote_averagePicker.setMinValue(1);
        vote_averagePicker.setMaxValue(10);
        vote_averagePicker.setValue(filmsFragmentViewModel.getVoteAverage().getValue());

        vote_averagePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                filmsFragmentViewModel.getVoteAverage().setValue(newVal);
            }
        });

    }

    private void setupGenreFilterRecyclerView() {

        genresFilterAdapter = new RecyclerGenresFilterAdapter(context, getGenresFilerList(), filterList, this);
        genreFilterRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        genreFilterRecyclerView.setHasFixedSize(true);
        genreFilterRecyclerView.setAdapter(genresFilterAdapter);

    }

    private void selectedSpinnerItem() {

        spinnerAdapter = new ArrayAdapter(context, R.layout.layout_spinner_color, filter_routes);
        spinnerAdapter.setDropDownViewResource(R.layout.layout_custom_dropdown_spinner);
        sortBySpinner.setAdapter(spinnerAdapter);
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filmsFragmentViewModel.getSort_by().setValue(sortBySpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * these items sets to genreIds which locate in ViewModel
     */
    private void setSelectedGenreRecyclerItemsToMutableLiveData() {
        filmsFragmentViewModel.getGenreIds().setValue(convertGenreIdsToString(filterList));
    }

    private void clickedSaveFilter() {

        selectedSpinnerItem();
        saveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_filter.setVisibility(View.GONE);
                filmsFragmentViewModel.resetVariables();
                setSelectedGenreRecyclerItemsToMutableLiveData();
                checkInternetConnectionAndSetWidgetsItem();
                editSearch.setText("");
                showFilter = true;
            }
        });

    }

    private void checkInternetConnectionAndSetWidgetsItem() {

        CheckConnectionAsynchronously.INSTANCE.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean connection) {
                if (connection) {
                    filmsFragmentViewModel.getDataSetToMutableLiveData();
                } else {
                    Toast.makeText(context, "Check internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void hideKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    private void setupRecyclerSearchSuggestion() {

        searchAdapter = new SuggestionSearchAdapter(context, this);
        suggestionRecycler.setHasFixedSize(true);
        suggestionRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        suggestionRecycler.setAdapter(searchAdapter);

        filmsFragmentViewModel.getSuggestionResult().observe(getViewLifecycleOwner(), new Observer<List<ResultsItem>>() {
            @Override
            public void onChanged(List<ResultsItem> list) {
                if (list != null) {
                    relSuggestion.setVisibility(View.VISIBLE);
                    linear_emptyInfo.setVisibility(View.GONE);
                    if (list.size() > 5) {
                        searchAdapter.updateSuggestionList(list.subList(0, 5));
                    } else {
                        searchAdapter.updateSuggestionList(list);
                    }
                    if (list.size() == 0) {
                        linear_emptyInfo.setVisibility(View.VISIBLE);
                    }
                    if (clicked_suggestion_item.equals(editSearch.getText().toString())) {
                        relSuggestion.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    private void searchWatcher() {

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                relSuggestion.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filmsFragmentViewModel.getQuery().setValue(s.toString());
                CheckConnectionAsynchronously.INSTANCE.observe((LifecycleOwner) context, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean connection) {
                        if (connection) {
                            filmsFragmentViewModel.getSuggestionSearchResult();
                        }
                    }
                });
                relSuggestion.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty() || clicked_suggestion_item.equals(s.toString())) {
                    relSuggestion.setVisibility(View.GONE);
                }
            }
        });

    }

    private void saveFilterOptionsWhenLandscapeMode() {

        filterList = filmsFragmentViewModel.getFilterGenreList().getValue();

    }

    /**
     * we need genre id for api request
     * and when clicked recycler item then it is added to list
     *
     * @param genre
     */
    @Override
    public void onAddGenreItemCallBack(@NonNull String genre) {
        filterList.add(getGenreFilterHashMap().get(genre));
        genresFilterAdapter.updateFilterList(filterList);
        filmsFragmentViewModel.getFilterGenreList().setValue(filterList);
    }

    /**
     * and when clicked selected item then
     * remove this id from list
     *
     * @param genre
     */
    @Override
    public void onDeleteGenreItemCallBack(@NonNull String genre) {
        filterList.remove(getGenreFilterHashMap().get(genre));
        genresFilterAdapter.updateFilterList(filterList);
        filmsFragmentViewModel.getFilterGenreList().setValue(filterList);
    }

    /**
     * when clicked suggestion search item
     * then film title set to search edittext and also to view model query
     * and search it
     *
     * @param film_title
     */
    @Override
    public void onClickSuggestionItem(String film_title) {
        editSearch.setText(film_title);
        clicked_suggestion_item = film_title;
        // close keyboard
        editSearch.onEditorAction(EditorInfo.IME_ACTION_DONE);
        hideKeyboard(view);
        relSuggestion.setVisibility(View.GONE);
        filmsFragmentViewModel.getQuery().setValue(film_title);
        filmsFragmentViewModel.resetVariables();
        filmsFragmentViewModel.getSearchDataSetToMutableLiveData();
    }

    /**
     * when click phone back button then activity is finished
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

}