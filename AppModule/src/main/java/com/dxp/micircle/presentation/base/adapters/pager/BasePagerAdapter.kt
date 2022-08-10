package com.dxp.micircle.presentation.base.adapters.pager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.PagerAdapter
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import com.dxp.micircle.presentation.base.adapters.ItemListener

abstract class BasePagerAdapter<BINDING : ViewDataBinding, T : BaseListItem, itemListener: ItemListener>(var data: List<T>, val listener: ItemListener): PagerAdapter() {

    @get:LayoutRes
    abstract val layoutId: Int

    abstract fun bind(binding: BINDING, item: T, itemPos: String)

    fun onScrolled(pos: Int) {
        listener.onScrolled(pos)
    }

    fun updateData(list: List<T>) {
        this.data = list
        notifyDataSetChanged()
    }

    override fun getCount(): Int = data.size

    override fun instantiateItem(collection: ViewGroup, position: Int) : Any {

        val binder = DataBindingUtil.inflate<BINDING>(LayoutInflater.from(collection.context), layoutId, null, false)

        bind(binder, data[position], "${position+1} / $count")

        collection.addView(binder.root)

        return binder.root
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === (`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}