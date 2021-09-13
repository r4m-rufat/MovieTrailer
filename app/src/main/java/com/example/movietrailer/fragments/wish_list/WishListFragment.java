package com.example.movietrailer.fragments.wish_list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.movietrailer.R;
import com.example.movietrailer.adapters.wish_list.WishListAdapter;
import com.example.movietrailer.internal_storage.PreferenceManager;
import com.example.movietrailer.models.wish_list.WishList;
import com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarItems;
import com.example.movietrailer.viewmodels.wish_list.WishListFragmentViewModel;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

public class WishListFragment extends Fragment {

    private LinearLayout lin_empty;
    private RecyclerView recyclerWishList;
    private WishListFragmentViewModel wishListFragmentViewModel;
    private WishListAdapter wishListAdapter;
    private SpinKitView progressBar;
    private PreferenceManager preferenceManager;
    private Context context;
    private static final String TAG = "WishListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = requireContext();
        wishListFragmentViewModel = new ViewModelProvider(this).get(WishListFragmentViewModel.class);
        preferenceManager = new PreferenceManager(context);
        if (preferenceManager.getBoolean("dark_mode")) {
            context.setTheme(R.style.AppTheme_Base_Night);
        } else {
            context.setTheme(R.style.Theme_MovieTrailer);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // showing Bottom Navigation View
        requireActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        ((MeowBottomNavigation) requireActivity().findViewById(R.id.bottom_navigation_view)).show(BottomNavigationBarItems.WISHLIST.ordinal(), true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);
        getWidgets(view);
        setUpRecyclerView();
        setObservableDataToRecyclerView();
        setProgressBarWhenLoading();

        return view;
    }

    private void getWidgets(View view) {

        recyclerWishList = view.findViewById(R.id.recycler_wishList);
        progressBar = view.findViewById(R.id.progressBar);
        lin_empty = view.findViewById(R.id.lin_layoutEmpty);

    }

    private void setUpRecyclerView() {

        recyclerWishList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerWishList.setHasFixedSize(true);
        wishListAdapter = new WishListAdapter(context);

    }

    private void setObservableDataToRecyclerView() {

        wishListFragmentViewModel.getWishList().observe(getViewLifecycleOwner(), new Observer<List<WishList>>() {
            @Override
            public void onChanged(List<WishList> wishLists) {
                if (!wishLists.isEmpty()){
                    LayoutAnimationController controller =
                            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation);
                    recyclerWishList.setLayoutAnimation(controller);
                    wishListAdapter.updateWishList(wishLists);
                    lin_empty.setVisibility(View.GONE);
                }else{
                    lin_empty.setVisibility(View.VISIBLE);
                }
            }
        });

        recyclerWishList.setAdapter(wishListAdapter);

    }

    private void setProgressBarWhenLoading() {

        wishListFragmentViewModel.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {
                if (loading) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

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