package com.example.movietrailer.repository.wish_list;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.movietrailer.models.wish_list.WishList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WishLIstRepository {

    private static WishLIstRepository repository;
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static DatabaseReference reference = db.getReference();
    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static WishLIstRepository getInstance(){

        if (repository == null){
            repository = new WishLIstRepository();
        }
        return repository;
    }

    public MutableLiveData<List<WishList>> getWishList(
            MutableLiveData<Boolean> loading
    ) {

        MutableLiveData<List<WishList>> listMutableLiveData = new MutableLiveData<>();

        reference.child("user_wish_list")
                .child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            List<WishList> wishList = new ArrayList<>();
                            for (DataSnapshot single_snapshot: snapshot.getChildren()){

                                wishList.add(single_snapshot.getValue(WishList.class));
                                /**
                                 * list should reverse because last element which
                                 * is added should show in the top of recycler view
                                 */
                                Collections.reverse(wishList);
                                if (wishList.size() > 40){
                                    wishList.subList(40, (wishList.size())).clear();
                                }
                                loading.setValue(false);

                            }

                            compositeDisposable.add(Observable.just(wishList)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<List<WishList>>() {
                                        @Override
                                        public void accept(List<WishList> wishLists) throws Throwable {
                                            listMutableLiveData.setValue(wishLists);
                                        }
                                    }));

                        }else{
                            loading.setValue(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loading.setValue(false);
                    }
                });

        return listMutableLiveData;

    }

}
