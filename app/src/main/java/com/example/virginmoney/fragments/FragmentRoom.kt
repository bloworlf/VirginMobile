package com.example.virginmoney.fragments

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.virginmoney.adapters.EmptyDataObserver
import com.example.virginmoney.adapters.RoomAdapter
import com.example.virginmoney.base.BaseFragment
import com.example.virginmoney.databinding.FragmentRoomsBinding
import com.example.virginmoney.models.room.RoomModel
import com.example.virginmoney.models.view_models.RoomsViewModel
import com.example.virginmoney.utils.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator
import java.util.ArrayList

@AndroidEntryPoint
class FragmentRoom : BaseFragment() {


    private lateinit var bind: FragmentRoomsBinding
    private val viewModel: RoomsViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RoomAdapter
    private lateinit var manager: StaggeredGridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getRooms()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentRoomsBinding.inflate(layoutInflater, container, false)
        return bind.root
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

        viewModel.liveData.observe(viewLifecycleOwner) {
            if (!this@FragmentRoom::adapter.isInitialized) {
                populateRecyclerView(it)
            }
        }
    }

    private fun populateRecyclerView(list: ArrayList<RoomModel>) {
        adapter = RoomAdapter(requireContext(), list, findNavController())
        recyclerView.adapter = adapter

        val emptyDataObserver = EmptyDataObserver(recyclerView, bind.emptyDataParent.root)
        adapter.registerAdapterDataObserver(emptyDataObserver)
    }
}