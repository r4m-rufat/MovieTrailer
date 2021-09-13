package com.example.movietrailer.fragments.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movietrailer.R
import com.example.movietrailer.adapters.account_page.HistoryAdapter
import com.example.movietrailer.db.Dao
import com.example.movietrailer.db.History
import com.example.movietrailer.db.HistoryDatabase
import com.example.movietrailer.viewmodels.history.HistoryFragmentViewModel
import java.util.regex.Pattern
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.Log
import android.widget.*

import com.example.movietrailer.dialogs.showClearAllHistoryDialog
import com.example.movietrailer.internal_storage.PreferenceManager
import com.example.movietrailer.utils.constants.TAG
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {

    private lateinit var historyRecycler: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var viewModel: HistoryFragmentViewModel
    private lateinit var editSearch: EditText
    private lateinit var icClearAll: ImageView
    private lateinit var icSearch: ImageView
    private lateinit var relSearch: RelativeLayout
    private lateinit var linEmpty: LinearLayout
    private lateinit var progressDialog: KProgressHUD
    private var string: String = ""
    private lateinit var preferenceManager: PreferenceManager

    private lateinit var dao: Dao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hiding Bottom Navigation View
        requireActivity().findViewById<View>(R.id.bottom_navigation_view).visibility =
            View.GONE

        dao = HistoryDatabase.getHistoryDatabase(requireContext()).getDao()!!
        viewModel = ViewModelProvider(this)[HistoryFragmentViewModel::class.java]
        preferenceManager = PreferenceManager(context)
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        initializeWidgets(view)
        setupClearingDialog()
        setupRecyclerView()
        getObservableDataAndSetRecyclerView()
        editSearchTextWatcher()
        clickedIcSearch()
        clickedClearIcon()
        searchIconWhenEditSearchFocusable()

        return view
    }

    private fun initializeWidgets(view: View) {

        historyRecycler = view.findViewById(R.id.recyclerHistory)
        editSearch = view.findViewById(R.id.edit_search)
        icClearAll = view.findViewById(R.id.ic_clearAll)
        icSearch = view.findViewById(R.id.ic_search)
        relSearch = view.findViewById(R.id.rel_search)
        linEmpty = view.findViewById(R.id.lin_layoutEmpty)

    }

    private fun editSearchTextWatcher(){

        editSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                string = s.toString()
                var list = mutableListOf<History>()
                for(item in dao.getAllHistoryList()){
                    if (Pattern.compile(Pattern.quote(s.toString()), Pattern.CASE_INSENSITIVE).matcher(item.filmTitle).find()){
                        list.add(item)
                    }
                }
                if (editSearch.text != null){
                    historyAdapter.updateHistoryList(list)
                    Log.d(TAG, "onTextChanged: 2")
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

    }

    private fun setupRecyclerView() {

        historyAdapter = HistoryAdapter(requireContext())

        historyRecycler.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = historyAdapter
        }

    }

    private fun clickedIcSearch(){
        icSearch.setOnClickListener {
            if (string == ""){
                historyAdapter.updateHistoryList(dao.getAllHistoryList())
            }
            editSearch.onEditorAction(EditorInfo.IME_ACTION_DONE)
            hideKeyboard(it)
        }
    }

    private fun clickedClearIcon(){
        icClearAll.setOnClickListener {
            if (dao.getAllHistoryList().isEmpty()){
                Toast.makeText(context, "List is already empty", Toast.LENGTH_SHORT).show()
            }else{
                showClearAllHistoryDialog(
                    requireContext(),
                    onCLick = {
                        showClearingDialog()
                        CoroutineScope(Main).launch {
                            dao.deleteAllHistory()
                            delay(1000L)
                            viewModel.postHistoryToObservableData()
                            editSearch.text = null
                            editSearch.onEditorAction(EditorInfo.IME_ACTION_DONE)
                            dismissClearingDialog()
                        }
                    }
                )
            }
        }
    }

    private fun setupClearingDialog(){
        progressDialog = KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Clearing...")
            .setCancellable(true)
            .setAnimationSpeed(1)
            .setDimAmount(0.5f)
    }

    private fun showClearingDialog(){
        progressDialog.show()
    }

    private fun dismissClearingDialog(){
        progressDialog.dismiss()
    }

    private fun hideKeyboard(view: View){
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }

    private fun getObservableDataAndSetRecyclerView() {

        viewModel.getHistoryList().observe(viewLifecycleOwner, Observer { historyList ->
            if (historyList.isNotEmpty()){
                linEmpty.visibility = View.GONE
                val controller = AnimationUtils.loadLayoutAnimation(
                    context, R.anim.layout_animation
                )
                historyRecycler.layoutAnimation = controller
            }else{
                linEmpty.visibility = View.VISIBLE
            }
            historyAdapter.updateHistoryList(historyList)
        })

    }

    @SuppressLint("ResourceType")
    private fun searchIconWhenEditSearchFocusable(){
        editSearch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                icSearch.setColorFilter(ContextCompat.getColor(requireContext(), R.color.progress_orange), android.graphics.PorterDuff.Mode.SRC_IN)
                relSearch.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_search_box_focusable)
            }else{
                relSearch.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_search_icon)
                if (preferenceManager.getBoolean("dark_mode")) {
                    icSearch.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
                } else {
                    icSearch.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN)
                }
            }
        }
    }

}