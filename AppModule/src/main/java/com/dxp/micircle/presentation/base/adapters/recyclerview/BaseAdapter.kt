package com.dxp.micircle.presentation.base.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import com.dxp.micircle.presentation.base.adapters.ItemListener

abstract class BaseAdapter<BINDING : ViewDataBinding, T : BaseListItem,
        itemListener: ItemListener>(var data: List<T>, val listener: ItemListener):
    RecyclerView.Adapter<BaseViewHolder<BINDING>>() {

    @get:LayoutRes
    abstract val layoutId: Int

    abstract fun bind(binding: BINDING, item: T, itemPos: Int)

    override fun getItemCount(): Int = data.size

    fun onScrolledToEnd(pos: Int) {
        listener.onScrolledToEnd(pos)
    }

    fun updateData(list: List<T>) {
        this.data = list
        notifyDataSetChanged()
    }

    fun getItemForPos(position: Int) : BaseListItem? {
        if(data.size > position)
            return data[position]
        else return null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BINDING> {

        val binder = DataBindingUtil.inflate<BINDING>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )

        return BaseViewHolder(binder)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BINDING>, position: Int) {
        bind(holder.binder, data[position], position)
    }
}