package com.dxp.micircle.presentation.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<T, VM : BaseViewModel> : AppCompatActivity() {

    private var viewBinding: ViewBinding? = null
    private var toast: Toast? = null

    private lateinit var vmOperationsDisposible: Disposable

    protected abstract val viewModel: VM
    abstract fun constructViewBinding(): ViewBinding
    abstract fun onCreated(viewBinding: ViewBinding)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        viewBinding = constructViewBinding()
        setContentView(viewBinding?.root)

        vmOperationsDisposible = viewModel
            .interactions
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handleVMInteractions(it) }

        viewBinding?.let { onCreated(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        vmOperationsDisposible.dispose()
        viewBinding = null
    }

    open fun getViewBinding(): T = viewBinding as T

    open fun handleVMInteractions(interaction: Interactor): Boolean {
        return handleVMInteractions(this, interaction)
    }

    fun showToast(@StringRes stringRes: Int) {
        showToast(getString(stringRes))
    }

    fun showToast(message: String) {

        hideToast()

        toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast!!.show()
    }

    fun hideToast() {

        toast?.let {

            toast!!.cancel()
            toast = null
        }
    }
}