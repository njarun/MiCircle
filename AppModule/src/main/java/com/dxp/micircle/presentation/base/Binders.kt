package com.dxp.micircle.presentation.base

import android.view.View
import android.widget.Toast
import androidx.databinding.BindingAdapter

@BindingAdapter("android:visibility")
fun View.visibility(state: Boolean) {
    visibility = if(state) View.VISIBLE else View.GONE
}

@BindingAdapter("showToast")
fun View.showToast(message: String?) {

    message?.let {

        setOnClickListener {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}