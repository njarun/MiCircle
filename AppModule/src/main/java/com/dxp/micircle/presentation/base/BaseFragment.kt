package com.dxp.micircle.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<T> : Fragment() {

    private lateinit var mActivity: BaseActivity<*, *>

    private var viewBinding: ViewBinding? = null
    private var toast: Toast? = null

    abstract fun constructViewBinding(): ViewBinding
    abstract fun onCreated(viewBinding: ViewBinding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as BaseActivity<*, *>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = constructViewBinding()
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let { onCreated(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    fun getViewBinding(): T = viewBinding as T

    protected fun showToast(@StringRes stringRes: Int) {
        mActivity.showToast(stringRes)
    }

    protected fun showToast(string: String) {
        mActivity.showToast(string)
    }

    protected fun hideToast() {
        mActivity.hideToast()
    }
}