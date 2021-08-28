package com.example.movietrailer.fragments.wish_list;

import static com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarSetupKt.setUpBottomNavigationView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.movietrailer.R;
import com.example.movietrailer.adapters.WishListAdapter;
import com.example.movietrailer.models.wish_list.WishList;
import com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarItems;
import com.example.movietrailer.viewmodels.wish_list.WishListFragmentViewModel;

import java.util.List;

public class WishListFragment extends Fragment {

    private MeowBottomNavigation bottomNavigation;
    private RecyclerView recyclerWishList;
    private WishListFragmentViewModel wishListFragmentViewModel;
    private WishListAdapter wishListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wishListFragmentViewModel = new ViewModelProvider(this).get(WishListFragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);
        getWidgets(view);
        bottomNavigation.show(BottomNavigationBarItems.WISHLIST.ordinal(), true);

        setUpBottomNavigationView(bottomNavigation, view);
        setUpRecyclerView();

        wishListFragmentViewModel.getWishList().observe(getViewLifecycleOwner(), new Observer<List<WishList>>() {
            @Override
            public void onChanged(List<WishList> wishLists) {
                wishListAdapter.updateWishList(wishLists);
            }
        });

        recyclerWishList.setAdapter(wishListAdapter);

        return view;
    }

    private void getWidgets(View view){

        bottomNavigation = view.findViewById(R.id.bottom_navigation_view);
        recyclerWishList = view.findViewById(R.id.recycler_wishList);

    }

    private void setUpRecyclerView(){

        recyclerWishList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerWishList.setHasFixedSize(true);
        wishListAdapter = new WishListAdapter(getContext());

    }
}