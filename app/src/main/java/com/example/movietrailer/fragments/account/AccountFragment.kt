package com.example.movietrailer.fragments.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.movietrailer.R
import com.example.movietrailer.activities.registration.LoginActivity
import com.example.movietrailer.models.authentication.User
import com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarItems
import com.example.movietrailer.utils.bottom_navigation.setUpBottomNavigationView
import com.example.movietrailer.utils.check_connection.CheckConnectionAsynchronously
import com.example.movietrailer.utils.constants.TAG
import com.example.movietrailer.utils.dialogs.changePasswordBottomSheetDialog
import com.example.movietrailer.utils.dialogs.editAccountBottomSheetDialog
import com.example.movietrailer.viewmodels.account.AccountFragmentViewModel
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class AccountFragment : Fragment() {

    private lateinit var accountName: TextView
    private lateinit var emailName: TextView
    private lateinit var capitalOfName: TextView
    private lateinit var editAccountName: ImageView
    private lateinit var editPassword: ImageView
    private lateinit var signOut: Button
    private lateinit var circularProgressBar: SpinKitView
    private lateinit var linearLayout: LinearLayout

    private lateinit var db: FirebaseFirestore
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomNavigation: MeowBottomNavigation

    private lateinit var firebaseAuth: FirebaseAuth
    private var viewModel: AccountFragmentViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            if (viewModel == null){
                viewModel = ViewModelProvider(this)[AccountFragmentViewModel::class.java]
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_account, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        CheckConnectionAsynchronously.init(requireContext())

        initializeWidgets(view)
        getAccountInfo()
        clickedSignOutButton()
        clickedEditAccountName()
        clickedChangePasswordButton()

        bottomNavigation.show(BottomNavigationBarItems.ACCOUNT.ordinal, true);
        setUpBottomNavigationView(bottomNavigation, view)

        setProgressBar()

        return view
    }

    private fun initializeWidgets(view: View){

        accountName = view.findViewById(R.id.txt_accountName)
        emailName = view.findViewById(R.id.txt_emailName)
        capitalOfName = view.findViewById(R.id.capitalOfName)
        editAccountName = view.findViewById(R.id.ic_editName)
        editPassword = view.findViewById(R.id.ic_editPassword)
        signOut = view.findViewById(R.id.button_signOut)
        bottomNavigation = view.findViewById(R.id.bottom_navigation_view)
        circularProgressBar = view.findViewById(R.id.circularProgressBar)
        linearLayout = view.findViewById(R.id.linearAccount)
    }

    private fun getAccountInfo(){

        CheckConnectionAsynchronously.observe(viewLifecycleOwner, {

            viewModel!!.getUserInfo().observe(requireActivity(),
                    { user ->
                        user?.let {

                            CoroutineScope(Main).launch {

                                accountName.text = user.username
                                emailName.text = user.email
                                capitalOfName.text = user.username[0].uppercase()
                                Log.d(TAG, "getAccountInfo: user info came")

                            }

                        }
                    })

        })
    }

    private fun clickedSignOutButton(){

        signOut.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            requireActivity().finish()
        }

    }

    private fun clickedEditAccountName(){

        bottomSheetDialog = BottomSheetDialog(requireActivity())
        editAccountName.setOnClickListener {

            firebaseAuth.currentUser?.let { it1 ->
                editAccountBottomSheetDialog(
                    requireContext(),
                    previousUsername = accountName.text.toString(),
                    db,
                    user = it1,
                    bottomSheetDialog = bottomSheetDialog,
                    onCLick = {
                        viewModel!!.getUserInfo()
                    }
                )
            }

        }

    }

    private fun clickedChangePasswordButton(){

        var password = ""
        db.collection("users").document(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener { task ->
                password = task.result.getString("password").toString()
                /**
                 * if password equals to ("") then it means
                 * this user signed in with google and he cannot change password
                 */
                if(password == ""){
                    editPassword.visibility = View.GONE
                }
            }
            .addOnFailureListener {
                password = ""
            }

        editPassword.setOnClickListener {
            changePasswordBottomSheetDialog(
                requireContext(),
                password,
                db,
                firebaseAuth.currentUser!!,
                bottomSheetDialog
            )
        }

    }

    private fun setProgressBar(){
        viewModel!!.loading.observe(viewLifecycleOwner,
            { loading ->
                loading?.let {

                    CoroutineScope(Main).launch {

                        if (!it){
                            circularProgressBar.visibility = View.GONE
                            linearLayout.visibility = View.VISIBLE
                        }else{
                            circularProgressBar.visibility = View.VISIBLE
                            linearLayout.visibility = View.GONE
                        }
                    }

                }
            })

    }

}