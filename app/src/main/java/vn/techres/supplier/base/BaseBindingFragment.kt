package vn.techres.supplier.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import vn.techres.supplier.activity.MainActivity
import vn.techres.supplier.helper.CacheManager
import vn.techres.supplier.interfaces.OnBackClick

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseBindingFragment<VB : ViewBinding>(val inflate: Inflate<VB>) : Fragment(),
        OnBackClick {

    val mainActivity: MainActivity?
        get() = activity as MainActivity?

    protected abstract fun onBackPress()

    private var _binding: VB? = null
    val binding get() = _binding
    val cacheManager: CacheManager = CacheManager.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        mainActivity?.setOnBackClick(this)
        return binding?.root
    }

    override fun onBack() {
        onBackPress()
    }

    fun closeKeyboard(edt: EditText?) {
        edt?.requestFocus()
        edt?.isCursorVisible = false
        val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edt?.windowToken, 0)
    }

    fun showKeyboard(edt: EditText?) {
        edt?.requestFocus()
        edt?.isCursorVisible = true
        val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT)
    }


}