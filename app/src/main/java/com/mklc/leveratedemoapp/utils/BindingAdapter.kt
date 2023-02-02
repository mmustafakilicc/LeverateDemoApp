package com.mklc.leveratedemoapp.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

@BindingAdapter("android:src")
fun setSrc(imageView: ImageView, @DrawableRes resId: Int){
    imageView.setImageResource(resId)
}