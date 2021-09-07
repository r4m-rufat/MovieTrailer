package com.example.movietrailer.fragments.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
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
import com.example.movietrailer.models.discover_model.ResultsItem
import com.example.movietrailer.utils.check_connection.CheckConnectionAsynchronously
import com.example.movietrailer.viewmodels.history.HistoryFragmentViewModel
import java.util.regex.Pattern
import android.app.Activity
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.movietrailer.activities.home.HomeActivity
import android.content.Context.INPUT_METHOD_SERVICE
import androidx.appcompat.app.AppCompatDelegate

import androidx.core.content.ContextCompat.getSystemService
import com.example.movietrailer.dialogs.showClearAllHistoryDialog
import com.example.movietrailer.internal_storage.PreferenceManager
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
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
    private lateinit var progressDialog: KProgressHUD
    private var string: String = ""
    private lateinit var preferenceManager: PreferenceManager

    private lateinit var dao: Dao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dao = HistoryDatabase.getHistoryDatabase(requireContext()).getDao()!!
        viewModel = ViewModelProvider(this)[HistoryFragmentViewModel::class.java]
        preferenceManager = PreferenceManager(context);
        if (preferenceManager.getBoolean("dark_mode")) {
            requireContext().setTheme(R.style.AppTheme_Base_Night);
        } else {
            requireContext().setTheme(R.style.AppTheme);
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

        return view
    }

    private fun initializeWidgets(view: View) {

        historyRecycler = view.findViewById(R.id.recyclerHistory)
        editSearch = view.findViewById(R.id.edit_search)
        icClearAll = view.findViewById(R.id.ic_clearAll)
        icSearch = view.findViewById(R.id.ic_search)

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
                if (s.toString() == ""){
                    historyAdapter.updateHistoryList(dao.getAllHistoryList())
                }
                historyAdapter.updateHistoryList(list)
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
                            delay(1000L)
                            dao.deleteAllHistory()
                            historyAdapter.updateHistoryList(listOf())
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
            val controller = AnimationUtils.loadLayoutAnimation(
                context, R.anim.layout_animation
            )
            historyRecycler.layoutAnimation = controller
            historyAdapter.updateHistoryList(historyList)

        })

    }

}