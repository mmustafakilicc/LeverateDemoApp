package com.mklc.leveratedemoapp.ui.main.adapter

import androidx.annotation.DrawableRes

data class TickerView(
    val id: String,
    val name: String,
    val price: String,
    @DrawableRes var resId: Int
)