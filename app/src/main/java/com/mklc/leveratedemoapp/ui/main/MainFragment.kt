package com.mklc.leveratedemoapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.mklc.leveratedemoapp.R
import com.mklc.leveratedemoapp.databinding.FragmentMainBinding
import com.mklc.leveratedemoapp.ui.main.adapter.MainAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        initialize()
        observeChanges()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadPrices()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopLoading()
    }

    private fun initialize() {
        adapter = MainAdapter()
        binding.recyclerViewTickerList.apply {
            adapter = this@MainFragment.adapter
            itemAnimator = null
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun observeChanges() {
        viewModel.apply {
            liveDataMessage.observe(viewLifecycleOwner) {
                Timber.e(it)
            }
            liveDataTickerViewList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }
}