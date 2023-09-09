package vn.techres.supplier.fragment.customermanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.customeradapter.CustomerActiveAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentCustomerBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.interfaces.OnClickCustomerID
import vn.techres.supplier.model.datamodel.Customer
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.CustomerResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.util.*
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class CustomerActiveFragment :
    BaseBindingFragment<FragmentCustomerBinding>(FragmentCustomerBinding::inflate),
    OnClickCustomerID {
    val tagName: String = CustomerActiveFragment::class.java.name
    private var adapter: CustomerActiveAdapter? = null
    private var listData = ArrayList<Customer>()
    private var customerManagerFragment = CustomerProfileFragment()
    var position = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        fragmentCreate()
        setAdapter()
        searchCustomer()
    }
    override fun onResume() {
        super.onResume()
        listData.clear()
        listCustomerAPI()
    }
    private fun fragmentCreate() {
        mainActivity!!.setHeader(getString(R.string.title_customer))
        mainActivity!!.setBackClick(true)

        val animScaleShowTopToBottom =
            AnimationUtils.loadAnimation(context, R.anim.scale_show_top_to_bottom)
        binding!!.recyclerCustomer.startAnimation(animScaleShowTopToBottom)

        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
    }
    private fun setAdapter() {
        binding!!.recyclerCustomer.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = CustomerActiveAdapter(mainActivity!!.baseContext)
        listData.removeAll(listData.toSet())
        binding!!.recyclerCustomer.adapter = adapter
        binding!!.recyclerCustomer.setHasFixedSize(true)
        adapter!!.setClickRecycler(this)

    }
    companion object {
        @JvmStatic
        fun newInstance() = CustomerActiveFragment().apply {
            arguments = Bundle().apply { }
        }
    }
    /**
     * Tim kiem  khach hang
     */
    private fun searchCustomer() {
        binding!!.txtSearchCustomer.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList: ArrayList<Customer> = ArrayList()
                for (key in listData) {
                    if (listData.size > 0) {
                        val name: String = key.restaurant_name
                        if (name.lowercase(Locale.getDefault())
                                .contains(newText!!) || name.contains(newText)
                        )
                            newList.add(key)
                    }
                }
                adapter!!.setDataList(newList)
                binding!!.recyclerCustomer.setHasFixedSize(true)
                binding!!.recyclerCustomer.adapter = adapter

                return false
            }
        })
    }
    override fun onClick(position: Int, id: Int, logo: String) {
        this.position = position
        cacheManager.put(TechresEnum.GET_BY_ID.toString(), id.toString())
        cacheManager.put(TechresEnum.GET_BY_AVATAR.toString(), logo)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.nav_host, CustomerProfileFragment())
                    ?.addToBackStack(customerManagerFragment.tagName)
                    ?.commit()
            }, TechresEnum.TIME_100.toString().toLong()
        )
    }
    /**
     * goi API get danh sach khach hang
     */
    private fun listCustomerAPI() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/restaurants?status=${TechresEnum.CUSTOMER_ACTIVE_MANAGER}&limit=${TechresEnum.LIMIT_100}&page=${TechresEnum.PAGE}"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListCustomer(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CustomerResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("NotifyDataSetChanged")
                override fun onNext(response: CustomerResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        adapter!!.setDataList(response.data!!.list)
                        adapter!!.notifyDataSetChanged()
                        binding!!.txtTotalCustomer.text = response.data!!.total_record.toString()
                    } else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    override fun onBackPress() {
        val animFragmentBack = AnimationUtils.loadAnimation(context, R.anim.slide_right_out)
        binding!!.constrainLayoutMain.startAnimation(animFragmentBack)
        cacheManager.put(
            TechresEnum.KEY_BACK.toString(),
            TechresEnum.ACCOUNTMENU_ID_FRAGMENT.toString()
        )
        mainActivity!!.supportFragmentManager.popBackStack()

    }
}