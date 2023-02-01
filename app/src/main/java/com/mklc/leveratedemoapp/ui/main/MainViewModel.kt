package com.mklc.leveratedemoapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mklc.leveratedemoapp.repository.TickerRepository
import com.mklc.leveratedemoapp.ui.main.adapter.TickerView
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tickerRepository: TickerRepository
) : ViewModel()
{
    private val disposables = CompositeDisposable()

    private val _liveDataError = MutableLiveData<String>()
    val liveDataError: LiveData<String>
        get() = _liveDataError

    private val _liveDataMessage = MutableLiveData<String>()
    val liveDataMessage: LiveData<String>
        get() = _liveDataMessage

    private val _liveDataTickerViewList = MutableLiveData<List<TickerView>>()
    val liveDataTickerViewList: LiveData<List<TickerView>>
        get() = _liveDataTickerViewList

    fun start() {
        tickerRepository.init()
        disposables.add(tickerRepository.getMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ message ->
                _liveDataMessage.postValue(message)
            }, {
                _liveDataError.postValue(it.message)
                Timber.e(it)
            }))
    }

    fun stop() {
        tickerRepository.close()
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}