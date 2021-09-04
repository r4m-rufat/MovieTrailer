package com.example.movietrailer.fragments.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.movietrailer.R
import com.example.movietrailer.activities.registration.LoginActivity
import com.example.movietrailer.adapters.account_page.ProfileBackgroundAdapter
import com.example.movietrailer.dialogs.changePasswordBottomSheetDialog
import com.example.movietrailer.dialogs.editAccountBottomSheetDialog
import com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarItems
import com.example.movietrailer.utils.bottom_navigation.setUpBottomNavigationView
import com.example.movietrailer.utils.check_connection.CheckConnectionAsynchronously
import com.example.movietrailer.utils.constants.TAG
import com.example.movietrailer.utils.default_lists.getProfileBackgroundColorHashMap
import com.example.movietrailer.utils.default_lists.getProfileBackgroundList
import com.example.movietrailer.viewmodels.account.AccountFragmentViewModel
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class AccountFragment : Fragment(), ProfileBackgroundAdapter.OnClickColorListener {

    private lateinit var accountName: TextView
    private lateinit var emailName: TextView
    private lateinit var capitalOfName: TextView
    private lateinit var editAccountName: ImageView
    private lateinit var editPassword: ImageView
    private lateinit var icHistory: ImageView
    private lateinit var signOut: Button
    private lateinit var circularProgressBar: SpinKitView
    private lateinit var linearLayout: LinearLayout
    private lateinit var backgroundRecycler: RecyclerView
    private lateinit var profileBackgroundAdapter: ProfileBackgroundAdapter
    private lateinit var circularProfileBackground: CircleImageView

    private lateinit var db: FirebaseFirestore
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomNavigation: MeowBottomNavigation

    private lateinit var firebaseAuth: FirebaseAuth
    private var viewModel: AccountFragmentViewModel? = null

    private var colorInDatabase = ""
    private var showBackgroundAdapter = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel == null) {
            viewModel = ViewModelProvider(this)[AccountFragmentViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_account, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        CheckConnectionAsynchronously.init(requireContext())

        initializeWidgets(view)
        getAccountInfo()
        clickedSignOutButton()
        clickedEditAccountName()
        clickedChangePasswordButton()
        clickedIconHistory()

        bottomNavigation.show(BottomNavigationBarItems.ACCOUNT.ordinal, true);
        setUpBottomNavigationView(bottomNavigation, view)

        setProgressBar()

        // background colors adapter
        setupRecyclerView()
        clickedCircularProfile()

        return view
    }

    private fun initializeWidgets(view: View) {

        accountName = view.findViewById(R.id.txt_accountName)
        emailName = view.findViewById(R.id.txt_emailName)
        capitalOfName = view.findViewById(R.id.capitalOfName)
        editAccountName = view.findViewById(R.id.ic_editName)
        editPassword = view.findViewById(R.id.ic_editPassword)
        signOut = view.findViewById(R.id.button_signOut)
        bottomNavigation = view.findViewById(R.id.bottom_navigation_view)
        circularProgressBar = view.findViewById(R.id.circularProgressBar)
        linearLayout = view.findViewById(R.id.linearAccount)
        circularProfileBackground = view.findViewById(R.id.circleProfileBackground)
        backgroundRecycler = view.findViewById(R.id.recyclerProfileBackgroundColor)
        icHistory = view.findViewById(R.id.ic_history)

    }

    private fun getAccountInfo() {

        CheckConnectionAsynchronously.observe(viewLifecycleOwner, {

            viewModel!!.getUserInfo().observe(requireActivity(),
                { user ->
                    user?.let {

                        CoroutineScope(Main).launch {

                            accountName.text = user.username
                            emailName.text = user.email
                            capitalOfName.text = user.username[0].uppercase()
                            colorInDatabase = user.color
                            circularProfileBackground.setImageResource(
                                getProfileBackgroundColorHashMap()[colorInDatabase]!!)
                            profileBackgroundAdapter.updateSelectedItem(colorInDatabase)
                            Log.d(TAG, "getAccountInfo: user info came")

                        }

                    }
                })

        })
    }

    private fun clickedIconHistory(){
        icHistory.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_to_viewHistoryFragment)
        }
    }

    private fun clickedSignOutButton() {

        signOut.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            requireActivity().finish()
        }

    }

    private fun clickedEditAccountName() {

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

    private fun clickedChangePasswordButton() {

        var password = ""
        db.collection("users").document(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener { task ->
                password = task.result.getString("password").toString()
                /**
                 * if password equals to ("") then it means
                 * this user signed in with google and he cannot change password
                 */
                if (password == "") {
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

    private fun setProgressBar() {
        viewModel!!.loading.observe(viewLifecycleOwner,
            { loading ->
                loading?.let {

                    CoroutineScope(Main).launch {

                        if (!it) {
                            circularProgressBar.visibility = View.GONE
                            linearLayout.visibility = View.VISIBLE
                        } else {
                            circularProgressBar.visibility = View.VISIBLE
                            linearLayout.visibility = View.GONE
                        }
                    }

                }
            })

    }

    private fun clickedCircularProfile() {

        circularProfileBackground.setOnClickListener {
            if (showBackgroundAdapter) {
                backgroundRecycler.visibility = View.VISIBLE
                showBackgroundAdapter = false
            } else {
                backgroundRecycler.visibility = View.GONE
                showBackgroundAdapter = true
            }
        }

    }

    private fun setupRecyclerView() {
        profileBackgroundAdapter =
            ProfileBackgroundAdapter(requireContext(), getProfileBackgroundList(), colorInDatabase, this)
        backgroundRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(false)
            adapter = profileBackgroundAdapter
        }
    }
    private fun setBackgroundColorToGlobalDatabase(newColor: String) {
        val hasMap = HashMap<String, Any>()
        hasMap["color"] = newColor

       CheckConnectionAsynchronously.observe(viewLifecycleOwner, { connection ->

           if (connection){
               db.collection("users")
                   .document(firebaseAuth.currentUser!!.uid)
                   .update(hasMap)
                   .addOnSuccessListener {
                       Log.d(
                           TAG,
                           "setBackgroundColorToGlobalDatabase: color successfully added to global database. Color: $newColor"
                       )
                   }.addOnFailureListener {
                       Log.d(
                           TAG,
                           "setBackgroundColorToGlobalDatabase: database error. Reason -> ${it.message}"
                       )
                   }
           }else{
               Toast.makeText(context, "Please, check internet connection!", Toast.LENGTH_SHORT)
                   .show()
           }
       })
    }

    override fun onClickedItemCallBack(color: String) {
        profileBackgroundAdapter.updateSelectedItem(color)
        circularProfileBackground.setImageResource(getProfileBackgroundColorHashMap()[color]!!)
        setBackgroundColorToGlobalDatabase(color)
    }

}