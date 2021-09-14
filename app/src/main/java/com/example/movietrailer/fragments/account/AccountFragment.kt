package com.example.movietrailer.fragments.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat.setLayerType
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.movietrailer.R
import com.example.movietrailer.activities.registration.LoginActivity
import com.example.movietrailer.adapters.account_page.ProfileBackgroundAdapter
import com.example.movietrailer.db.Dao
import com.example.movietrailer.db.HistoryDatabase
import com.example.movietrailer.dialogs.changePasswordBottomSheetDialog
import com.example.movietrailer.dialogs.editAccountBottomSheetDialog
import com.example.movietrailer.dialogs.signOutDialog
import com.example.movietrailer.internal_storage.PreferenceManager
import com.example.movietrailer.utils.balloons.showEditBalloon
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
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.vimalcvs.switchdn.DayNightSwitch
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
    private lateinit var txtFavoriteListSize: TextView
    private lateinit var txtHistoryListSize: TextView
    private lateinit var editAccountName: ImageView
    private lateinit var editPassword: ImageView
    private lateinit var icPalette: ImageView
    private lateinit var icHistory: ImageView
    private lateinit var icHeart: ImageView
    private lateinit var signOut: Button
    private var circularProgressBar: SpinKitView? = null
    private lateinit var linearLayout: LinearLayout
    private lateinit var backgroundRecycler: RecyclerView
    private lateinit var profileBackgroundAdapter: ProfileBackgroundAdapter
    private lateinit var circularProfileBackground: CircleImageView
    private lateinit var dayNightSwitch: DayNightSwitch

    private lateinit var preferenceManager: PreferenceManager

    private lateinit var db: FirebaseFirestore
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var dbreference: DatabaseReference
    private lateinit var dao: Dao

    private lateinit var firebaseAuth: FirebaseAuth
    private var viewModel: AccountFragmentViewModel? = null

    private var colorInDatabase = ""
    private var showBackgroundAdapter = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dao = HistoryDatabase.getHistoryDatabase(requireContext()).getDao()!!

        viewModel = ViewModelProvider(this)[AccountFragmentViewModel::class.java]
        preferenceManager = PreferenceManager(requireContext())
        if (preferenceManager.getBoolean("dark_mode")) {
            requireContext().setTheme(R.style.AppTheme_Base_Night)
        } else {
            requireContext().setTheme(R.style.Theme_MovieTrailer)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_account, container, false)

        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        // showing Bottom Navigation View
        requireActivity().findViewById<MeowBottomNavigation>(R.id.bottom_navigation_view).show(BottomNavigationBarItems.ACCOUNT.ordinal, true);
        requireActivity().findViewById<MeowBottomNavigation>(R.id.bottom_navigation_view).visibility =
            View.VISIBLE

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        dbreference = FirebaseDatabase.getInstance().reference

        CheckConnectionAsynchronously.init(requireContext())

        initializeWidgets(view)
        setHistorySize()
        setFavoriteSize()
        getAccountInfo()
        clickedSignOutButton()
        clickedEditAccountName()
        clickedChangePasswordButton()
        clickedIconHistory()
        clickedIconFavorite()
        switchedDarkMode()

        // background colors adapter
        setupRecyclerView()
        clickedPaletteIcon()

        setProgressBar()

        return view
    }

    private fun initializeWidgets(view: View) {

        accountName = view.findViewById(R.id.txt_accountName)
        emailName = view.findViewById(R.id.txt_emailName)
        capitalOfName = view.findViewById(R.id.capitalOfName)
        editAccountName = view.findViewById(R.id.ic_editName)
        editPassword = view.findViewById(R.id.ic_editPassword)
        signOut = view.findViewById(R.id.button_signOut)
        circularProgressBar = view.findViewById(R.id.circularProgressBar)
        linearLayout = view.findViewById(R.id.linearAccount)
        circularProfileBackground = view.findViewById(R.id.circleProfileBackground)
        backgroundRecycler = view.findViewById(R.id.recyclerProfileBackgroundColor)
        icHistory = view.findViewById(R.id.ic_history)
        icPalette = view.findViewById(R.id.ic_palette)
        txtFavoriteListSize = view.findViewById(R.id.txt_favoriteListSize)
        txtHistoryListSize = view.findViewById(R.id.txt_historyListSize)
        dayNightSwitch = view.findViewById(R.id.switch_mode)
        icHeart= view.findViewById(R.id.ic_favorite)

    }

    private fun setHistorySize() {

        var size = 0
        try {
            for (i in dao.getAllHistoryList()){
                if (i.uid == firebaseAuth.currentUser!!.uid){
                    size++
                }
            }
        }catch (e: NullPointerException){
            e.printStackTrace()
        }
        txtHistoryListSize.text = size.toString()

    }

    private fun setFavoriteSize() {

        viewModel!!.getFavoriteListSize().observe(viewLifecycleOwner, { size ->
            txtFavoriteListSize.text = size.toString()
        })

    }

    private fun getAccountInfo() {

        CheckConnectionAsynchronously.observe(viewLifecycleOwner, {

            viewModel!!.getUserInfo().observe(viewLifecycleOwner,
                { user ->
                    user?.let {

                        CoroutineScope(Main).launch {

                            accountName.text = user.username
                            emailName.text = user.email
                            try {
                                capitalOfName.text = user.username[0].uppercase()
                            } catch (e: IndexOutOfBoundsException) {
                                e.printStackTrace()
                            }
                            colorInDatabase = user.color
                            circularProfileBackground.setImageResource(
                                getProfileBackgroundColorHashMap()[colorInDatabase]!!
                            )
                            profileBackgroundAdapter.updateSelectedItem(colorInDatabase)
                            Log.d(TAG, "getAccountInfo: user info came")

                        }

                    }
                })

        })
    }

    private fun clickedIconHistory() {
        icHistory.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_to_viewHistoryFragment)
        }
    }

    private fun clickedIconFavorite() {
        icHeart.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_to_wishListFragment)
        }
    }

    private fun clickedSignOutButton() {

        signOut.setOnClickListener {
            signOutDialog(
                requireContext(),
                onClickSignOut = {
                    firebaseAuth.signOut()
                    startActivity(Intent(context, LoginActivity::class.java))
                    requireActivity().finish()
                }
            )
        }

    }

    private fun switchedDarkMode() {
        dayNightSwitch.setOnClickListener {
            val navController: NavController = requireActivity().findNavController(R.id.main_nav_graph)
            if (preferenceManager.getBoolean("dark_mode")){
                preferenceManager.putBoolean("dark_mode", false)
            }else{
                preferenceManager.putBoolean("dark_mode", true)
            }
            navController.apply {
                popBackStack()
                navigate(R.id.viewAccountFragment)
            }
        }

        dayNightSwitch.setIsNight(preferenceManager.getBoolean("dark_mode"))
    }

    private fun clickedEditAccountName() {

        bottomSheetDialog = BottomSheetDialog(requireActivity())
        editAccountName.setOnClickListener {

            showEditBalloon(
                context = requireContext(),
                editAccountName,
                "You can edit your profile name",
                onClick = {
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
            )

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
            showEditBalloon(
                context = requireContext(),
                editPassword,
                "You can change password",
                onClick = {
                    changePasswordBottomSheetDialog(
                        requireContext(),
                        password,
                        db,
                        firebaseAuth.currentUser!!,
                        bottomSheetDialog
                    )
                }
            )
        }

    }

    private fun setProgressBar() {
        viewModel!!.loading.observe(viewLifecycleOwner,
            { loading ->
                loading?.let {

                    CoroutineScope(Main).launch {

                        if (!it) {
                            circularProgressBar?.visibility = View.GONE
                            linearLayout.visibility = View.VISIBLE
                        } else {
                            circularProgressBar?.visibility = View.VISIBLE
                            linearLayout.visibility = View.GONE
                        }
                    }

                }
            })

    }

    private fun clickedPaletteIcon() {

        icPalette.setOnClickListener {
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
            ProfileBackgroundAdapter(
                requireContext(),
                getProfileBackgroundList(),
                colorInDatabase,
                this
            )
        backgroundRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(false)
            adapter = profileBackgroundAdapter
        }
    }

    private fun setBackgroundColorToGlobalDatabase(newColor: String) {
        val hasMap = HashMap<String, Any>()
        hasMap["color"] = newColor

        CheckConnectionAsynchronously.observe(this, { connection ->

            if (connection) {
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
            }
        })
    }

    override fun onClickedItemCallBack(color: String) {
        profileBackgroundAdapter.updateSelectedItem(color)
        circularProfileBackground.setImageResource(getProfileBackgroundColorHashMap()[color]!!)
        setBackgroundColorToGlobalDatabase(color)
    }

    /**
     * when click phone back button then activity is finished
     * @param context
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        circularProgressBar = null
    }
}