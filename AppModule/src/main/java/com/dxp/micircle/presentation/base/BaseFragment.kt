package com.dxp.micircle.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<T, VM : BaseViewModel> : Fragment() {

    private lateinit var mActivity: BaseActivity<*, *>

    private lateinit var vmOperationsDisposible: Disposable

    private var viewBinding: ViewBinding? = null
    private var toast: Toast? = null

    protected abstract val viewModel: VM
    abstract fun constructViewBinding(): ViewBinding
    abstract fun onCreated(viewBinding: ViewBinding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as BaseActivity<*, *>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = constructViewBinding()
        vmOperationsDisposible = viewModel
            .interactions
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handleVMInteractions(it) }
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let { onCreated(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
        vmOperationsDisposible.dispose()
    }

    fun getViewBinding(): T = viewBinding as T

    open fun handleVMInteractions(interaction: Interactor): Boolean {
        return handleVMInteractions(mActivity, interaction)
    }

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