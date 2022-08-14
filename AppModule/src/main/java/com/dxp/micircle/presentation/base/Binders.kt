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
import com.dxp.micircle.presentation.base.adapters.recyclerview.BaseAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

@BindingAdapter(value = ["adapter", "dataSet", "scrollToLast", "associatedFab"], requireAll = false) @Suppress("UNCHECKED_CAST")
fun setRecyclerAdapter(recyclerView: RecyclerView, recyclerviewAdapter: BaseAdapter<*, *, *>?, recyclerviewDataset: List<BaseListItem>?, scrollToLast: Boolean, fab: FloatingActionButton?) {

    var adapter = recyclerviewAdapter as BaseAdapter<ViewDataBinding, BaseListItem, Any>?
    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
    var scrollToItemPos = layoutManager.findFirstCompletelyVisibleItemPosition()

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

                    fab?.let {

                        if (dy > 0) {

                            if (fab.isShown) {
                                fab.hide()
                            }
                        }
                        else if (dy < 0) {

                            if (!fab.isShown) {
                                fab.show()
                            }
                        }
                    }
                }
            })
        }
        else {

            adapter = recyclerView.adapter as BaseAdapter<ViewDataBinding, BaseListItem, Any>
        }

        adapter!!.updateData(recyclerviewDataset ?: listOf())

        if(scrollToLast && scrollToItemPos >= 0) {
            scrollToItemPos = (adapter!!.itemCount - 1)
        }

        if(scrollToItemPos >= 0)
            recyclerView.post{ layoutManager.scrollToPosition(scrollToItemPos) }
    }
}


@BindingAdapter(value = ["imageUrl", "dontScale", "placeholder"], requireAll = false)
fun ImageView.loadImageFromUrlOrPlaceholder(url: String?, dontScale: Boolean?, placeholder: Int?) {

    url?.let {

        if(dontScale != null && dontScale) {

            Glide.with(context).load(url).into(this)
        }
        else {

            Glide.with(context).load(url).centerCrop().into(this)
        }
    } ?: placeholder?.let { Glide.with(context)
        .load(placeholder).into(this) }
}