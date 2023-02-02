package com.mklc.leveratedemoapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.mklc.leveratedemoapp.R
import com.mklc.leveratedemoapp.data.model.network.Ticker
import com.mklc.leveratedemoapp.repository.TickerRepository
import com.mklc.leveratedemoapp.ui.main.adapter.TickerView
import com.mklc.leveratedemoapp.utils.compareDouble
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

    private val tickerListView = mutableListOf<TickerView>()

    private val _liveDataOrientation = MutableLiveData(0)
    val liveDataOrientation: LiveData<Int>
        get() = _liveDataOrientation

    fun setupConnection() {
        tickerRepository.open()
    }

    fun loadPrices(){
        disposables.add(tickerRepository.isConnected()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                if(it){
                    tickerRepository.start()
                }
            })
        disposables.add(tickerRepository.getMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ ticker ->
                checkList(ticker)
            }, {
                _liveDataError.postValue(it.message)
                Timber.e(it)
            }))
    }

    private fun checkList(ticker: Ticker){
        val oldTickerView = tickerListView.find { it.name == ticker.pair }
        val newTickerView = TickerView(id = ticker.id.toString(), name = ticker.pair, price = ticker.a.bestAskPrice, R.drawable.ic_baseline_linear_scale_24)
        if(oldTickerView == null){
            tickerListView.add(newTickerView)
        }else{
            val comparedResult = newTickerView.price.compareDouble(oldTickerView.price)
            if(comparedResult == 1){
                newTickerView.resId = R.drawable.ic_baseline_keyboard_double_arrow_up_24
            }else if(comparedResult == -1){
                newTickerView.resId = R.drawable.ic_baseline_keyboard_double_arrow_down_24
            }
            val index = tickerListView.indexOf(oldTickerView)
            tickerListView.removeAt(index)
            tickerListView.add(index, newTickerView)
        }
        _liveDataTickerViewList.postValue(tickerListView.toList())
    }

    fun stopLoading(){
        tickerRepository.stop()
        disposables.dispose()
    }

    fun onVerticalClicked(){
        _liveDataOrientation.postValue(RecyclerView.VERTICAL)
    }

    fun onHorizontalClicked(){
        _liveDataOrientation.postValue(RecyclerView.HORIZONTAL)
    }

    override fun onCleared() {
        disposables.dispose()
        tickerRepository.close()
        super.onCleared()
    }
}