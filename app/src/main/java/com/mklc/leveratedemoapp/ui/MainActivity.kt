package com.mklc.leveratedemoapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mklc.leveratedemoapp.R
import com.mklc.leveratedemoapp.databinding.ActivityMainBinding
import com.mklc.leveratedemoapp.repository.TickerRepository
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: TickerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        initialize()
        observeChanges()
    }

    private fun initialize(){
        repository = TickerRepository()
    }

    override fun onStart() {
        super.onStart()
        repository.init()
    }

    override fun onStop() {
        super.onStop()
        repository.close()
    }

    private fun observeChanges() {
        repository.getMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ message ->
                Timber.d(message)
            }, {
                Timber.e(it)
            })
    }

}