package vn.techres.supplier.fragment.restaurantcontactors

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.restaurantcontactor.BranchCustomerAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentBranchBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.BranchGetByIdCustomerResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class BranchFragment : BaseBindingFragment<FragmentBranchBinding>(FragmentBranchBinding::inflate) {
    val tagName: String = BranchFragment::class.java.name
    private var adapter: BranchCustomerAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        mainActivity!!.setHideHeader(true)
        val animScaleShowTopToBottom =
            AnimationUtils.loadAnimation(context, R.anim.scale_show_top_to_bottom)
        binding!!.recyclerView.startAnimation(animScaleShowTopToBottom)
        setAdapter()
        apiBranchRestaurant()
    }
    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = BranchCustomerAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.setHasFixedSize(true)
    }
    private fun apiBranchRestaurant() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/branches?restaurant_id=" + cacheManager.get(TechresEnum.GET_BY_ID.toString())

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListBranchGetByIdCustomer(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BranchGetByIdCustomerResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: BranchGetByIdCustomerResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        adapter!!.setDataList(response.data!!)
                        binding!!.txtTotalBranch.text = response.data!!.size.toString()
                    } else
                        Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                            .show()
                }
            })
    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}