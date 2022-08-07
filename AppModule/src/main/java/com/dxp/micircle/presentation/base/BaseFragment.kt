package com.dxp.micircle.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T> : Fragment() {

    private var viewBinding: ViewBinding? = null
    private var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = constructViewBinding()
        viewBinding?.let { init(it) }
        return viewBinding?.root
    }

    @Suppress("UNCHECKED_CAST")
    fun getViewBinding(): T = viewBinding as T

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    abstract fun constructViewBinding(): ViewBinding
    abstract fun init(viewBinding: ViewBinding)

    override fun onPause() {
        super.onPause()
        hideToast()
    }

    protected fun showToast(@StringRes stringRes: Int) {
        showToast(getString(stringRes))
    }

    protected fun showToast(message: String) {

        hideToast()

        toast = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
        toast!!.show()
    }

    protected fun hideToast() {

        if (toast != null) {

            toast!!.cancel()
            toast = null
        }
    }
}