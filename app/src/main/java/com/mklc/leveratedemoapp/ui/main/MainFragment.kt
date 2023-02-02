package com.mklc.leveratedemoapp.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.mklc.leveratedemoapp.R
import com.mklc.leveratedemoapp.databinding.FragmentMainBinding
import com.mklc.leveratedemoapp.ui.main.adapter.MainAdapter
import com.mklc.leveratedemoapp.ui.main.adapter.MainCarouselAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: MainAdapter
    private lateinit var adapterCarousel: MainCarouselAdapter

    private var shortAnimationDuration: Int = 1500

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
        adapterCarousel = MainCarouselAdapter()
        binding.recyclerViewTickerListCarousel.apply {
            adapter = this@MainFragment.adapterCarousel
            set3DItem(true)
            setAlpha(true)
            setInfinite(true)
            itemAnimator = null
        }
    }

    private fun observeChanges() {
        viewModel.apply {
            liveDataMessage.observe(viewLifecycleOwner) {
                Timber.e(it)
            }
            liveDataTickerViewList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
                adapterCarousel.submitList(it)
            }
            liveDataOrientation.observe(viewLifecycleOwner){
                if(it == RecyclerView.VERTICAL){
                    hide(binding.recyclerViewTickerListCarousel)
                    show(binding.recyclerViewTickerList)
                }else if(it == RecyclerView.HORIZONTAL){
                    hide(binding.recyclerViewTickerList)
                    show(binding.recyclerViewTickerListCarousel)
                }
            }
        }
    }

    private fun hide(view: View){
        view.animate()
            .alpha(0f)
            .setDuration(shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                }
            })
    }

    private fun show(view: View){
        view.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(null)
        }
    }
}