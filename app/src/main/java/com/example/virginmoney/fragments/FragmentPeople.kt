package com.example.virginmoney.fragments

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.virginmoney.R
import com.example.virginmoney.adapters.EmptyDataObserver
import com.example.virginmoney.adapters.PeopleAdapter
import com.example.virginmoney.base.BaseFragment
import com.example.virginmoney.models.view_models.PeopleViewModel
import com.example.virginmoney.databinding.FragmentPeopleBinding
import com.example.virginmoney.models.people.PeopleModel
import com.example.virginmoney.utils.GridSpacingItemDecoration
import com.example.virginmoney.utils.Utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator
import java.util.ArrayList

@AndroidEntryPoint
class FragmentPeople : BaseFragment() {

    private lateinit var bind: FragmentPeopleBinding
    private var searchView: SearchView? = null
    private val viewModel: PeopleViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: StaggeredGridLayoutManager
    private lateinit var adapter: PeopleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        viewModel.getPeople()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentPeopleBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.people_menu, menu)
        searchView = (menu.findItem(R.id.search).actionView as SearchView)
        searchView!!.maxWidth = Integer.MAX_VALUE
        searchView!!.queryHint = "Filter"

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.length <= 2) {
                    Toast.makeText(
                        this@FragmentPeople.requireContext(),
                        "Query string too short",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                searchView!!.hideKeyboard()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (this@FragmentPeople::adapter.isInitialized && adapter.list.size > 0) {
                    adapter.filter.filter(query)
                    return true
                }
                return false
            }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (!this::adapter.isInitialized) {
            return
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            manager.spanCount = 2
            recyclerView.removeItemDecorationAt(0)
            recyclerView.addItemDecoration(GridSpacingItemDecoration(2, 10, true))
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
        } else {
            manager.spanCount = 1
            recyclerView.removeItemDecorationAt(0)
            recyclerView.addItemDecoration(GridSpacingItemDecoration(1, 10, true))
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = bind.recyclerView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            recyclerView.itemAnimator = LandingAnimator()
        }
        manager = StaggeredGridLayoutManager(
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                2
            } else {
                1
            }, StaggeredGridLayoutManager.VERTICAL
        )
        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(
                GridSpacingItemDecoration(
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        2
                    } else {
                        1
                    }, 10, true
                )
            )
        }
        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = manager
        }

//        adapter = PeopleAdapter(requireContext(), arrayListOf(), findNavController())
//        recyclerView.adapter = adapter
//
//        val emptyDataObserver = EmptyDataObserver(recyclerView, bind.emptyDataParent.root)
//        adapter.registerAdapterDataObserver(emptyDataObserver)

        viewModel.liveData.observe(viewLifecycleOwner) {
                populateRecyclerView(it)
        }
    }

    private fun populateRecyclerView(list: ArrayList<PeopleModel>) {
//        if (this::adapter.isInitialized) {
//            adapter.notifyItemRangeRemoved(0, adapter.list.size)
//            adapter.list.clear()
//        } else {
//            adapter = PeopleAdapter(requireContext(), list, findNavController())
//            recyclerView.adapter = adapter
//        }
//        adapter.list = list
//        adapter.notifyItemRangeInserted(adapter.list.size - list.size, list.size)
//        if (!this::adapter.isInitialized) {
            adapter = PeopleAdapter(requireContext(), list, findNavController())
            recyclerView.adapter = adapter

            val emptyDataObserver = EmptyDataObserver(recyclerView, bind.emptyDataParent.root)
            adapter.registerAdapterDataObserver(emptyDataObserver)
//        val oldSize = adapter.list.size
//        adapter.list = list
//        adapter.notifyItemRangeInserted(oldSize, list.size)
//        println(adapter.list.size)
//        }
    }
}