package com.dxp.micircle.presentation.base.adapters

interface ItemListener {

    fun onItemSelected(position: Int, itemObj: BaseListItem)

    fun onItemRemoved(position: Int, itemObj: BaseListItem)

    fun onScrolledToEnd(position: Int)
}