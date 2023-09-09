package vn.techres.supplier.fragment.paymentrequest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.paymentrequestadapter.PaymentRequestManagerAdapter
import vn.techres.supplier.adapter.unitsadapter.UnitsManagerAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentPaymentRequestManagerBinding
import vn.techres.supplier.databinding.FragmentSupportBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.params.FeedBackParams
import vn.techres.supplier.model.response.BaseResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService

/**
 * ================================================
 * Created by HOQUANGTUNG on 2022
 * ================================================
 */
class PaymentRequestManagerFragment :
    BaseBindingFragment<FragmentPaymentRequestManagerBinding>(FragmentPaymentRequestManagerBinding::inflate) {
    val tagName: String = PaymentRequestManagerFragment::class.java.name
    private var adapter: PaymentRequestManagerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        mainActivity!!.setHeader(getString(R.string.btn_payment_request))
        mainActivity!!.setBackClick(true)
        mainActivity!!.setLoading(false)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        adapter = PaymentRequestManagerAdapter(childFragmentManager, mainActivity!!)

        binding!!.viewPager.adapter = adapter
        binding!!.tabLayout.setupWithViewPager(binding!!.viewPager)
    }

    override fun onBackPress() {
    }
}

