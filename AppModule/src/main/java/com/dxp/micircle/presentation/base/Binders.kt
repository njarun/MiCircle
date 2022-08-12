package com.dxp.micircle.presentation.base

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import com.dxp.micircle.presentation.base.adapters.ItemListener
import com.dxp.micircle.presentation.base.adapters.recyclerview.BaseAdapter

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

@BindingAdapter(value = ["adapter", "dataSet"], requireAll = true) @Suppress("UNCHECKED_CAST")
fun setRecyclerAdapter(recyclerView: RecyclerView, recyclerviewAdapter: BaseAdapter<*, *, *>?, recyclerviewDataset: List<BaseListItem>?) {

    var adapter = recyclerviewAdapter as BaseAdapter<ViewDataBinding, BaseListItem, ItemListener>?
    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
    val firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()

    adapter?.let {

        if(recyclerView.adapter == null) {

            recyclerView.adapter = adapter

            recyclerView.clearOnScrollListeners()
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    val itemCount = recyclerView.adapter?.itemCount ?: 0

                    if (lastVisiblePosition == itemCount - 1) {
                        it.onScrolledToEnd(itemCount - 1)
                    }
                }
            })
        }
        else {

            adapter = recyclerView.adapter as BaseAdapter<ViewDataBinding, BaseListItem, ItemListener>
        }

        adapter!!.updateData(recyclerviewDataset ?: listOf())

        if(firstVisibleItem >= 0)
            recyclerView.scrollToPosition(firstVisibleItem)
    }
}


@BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
fun ImageView.loadImageFromUrlOrPlaceholder(url: String?, placeholder: Int?) {

    url?.let { Glide.with(context).load(url).centerCrop().into(this) } ?: placeholder?.let { Glide.with(context)
        .load(placeholder).into(this) }
}