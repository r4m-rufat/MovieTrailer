package com.example.movietrailer.fragments.account

import android.os.Bundle
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
import com.example.movietrailer.viewmodels.history.HistoryFragmentViewModel

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {

    private lateinit var historyRecycler: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var viewModel: HistoryFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HistoryFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        initializeWidgets(view)
        setupRecyclerView()
        getObservableDataAndSetRecyclerView()

        return view
    }

    private fun initializeWidgets(view: View) {

        historyRecycler = view.findViewById(R.id.recyclerHistory)

    }

    private fun setupRecyclerView() {

        historyAdapter = HistoryAdapter(requireContext())

        historyRecycler.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = historyAdapter
        }

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