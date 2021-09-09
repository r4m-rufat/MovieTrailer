package com.example.movietrailer.repository.account_page

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.movietrailer.R
import com.example.movietrailer.models.authentication.User
import com.example.movietrailer.models.wish_list.WishList
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.Exception

class AccountPageRepository {

    companion object{
        private var INSTANCE: AccountPageRepository? = null
        private const val TAG = "AccountPageRepository"
        fun instance(): AccountPageRepository? {

            if (INSTANCE == null){
                INSTANCE = AccountPageRepository()
            }
            return INSTANCE
        }
    }

    fun getAccountInfo(
        userData: MutableLiveData<User>
    ): MutableLiveData<User>{

        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        CoroutineScope(IO).launch {

            Log.d(TAG, "getAccountInfo: ${user!!.uid}")

            val source = Source.CACHE
            db.collection("users")
                .document(user.uid)
                .get(source)
                .addOnSuccessListener { document ->
                    val email = document.getString("email")
                    val username = document.getString("username")
                    val password = document.getString("password")
                    val uID = document.getString("uID")
                    val color = document.getString("color")
                    userData.postValue(User(uID, email, password, username, color))
                    Log.d(TAG, "getAccountInfo: Successfully comes")
                }
                .addOnFailureListener {
                    Log.d(TAG, "getAccountInfo: Reason -> ${it.message}")
                }

        }

        return userData

    }

    fun getListSize(loading: MutableLiveData<Boolean>): MutableLiveData<Int>{
        var size: MutableLiveData<Int> = MutableLiveData(0)
        FirebaseDatabase.getInstance().reference.child("user_wish_list")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (single_snapshot in snapshot.children) {
                            size.value = size.value!!.plus(1)
                        }
                    }
                    loading.postValue(false)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(com.example.movietrailer.utils.constants.TAG, "onCancelled: Error is ${error.message}")
                    loading.postValue(false)
                }

            })
        return size
    }

}