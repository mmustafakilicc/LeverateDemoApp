package com.mklc.leveratedemoapp.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mklc.leveratedemoapp.R
import com.mklc.leveratedemoapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.setupConnection()
        observeChanges()
    }

    private fun observeChanges() {
        viewModel.apply {
            liveDataError.observe(this@MainActivity){
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

}