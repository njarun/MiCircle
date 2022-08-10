package com.dxp.micircle.presentation.base.adapters

interface ItemListener {

    fun onItemClicked(itemObj: BaseListItem)

    fun onScrolled(position: Int)
}