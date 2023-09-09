package vn.techres.supplier.fragment.restaurantcontactors

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.restaurantcontactor.ContactorAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentContactorsBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.datamodel.DataRestaurantContactors
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.RestaurantContactorsResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class ContactorsFragment :
    BaseBindingFragment<FragmentContactorsBinding>(FragmentContactorsBinding::inflate) {
    val tagName: String = ContactorsFragment::class.java.name
    private var adapterContactor: ContactorAdapter? = null
    private var contactList = ArrayList<DataRestaurantContactors>()
    var position = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        setAdapter()
        listContactors()
    }
    override fun onResume() {
        super.onResume()
        contactList.clear()
        listContactors()

    }
    private fun setAdapter() {
        adapterContactor = mainActivity?.let { ContactorAdapter(it) }
        contactList.removeAll(contactList)
        binding!!.rcViewContact.adapter = adapterContactor
        binding!!.rcViewContact.layoutManager = LinearLayoutManager(context)
        binding!!.rcViewContact.setHasFixedSize(true)
    }
    private fun listContactors() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-restaurant-contactors?restaurant_id=" + cacheManager.get(TechresEnum.GET_BY_ID.toString())
        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getRestaurantContactors(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<RestaurantContactorsResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @android.annotation.SuppressLint("NotifyDataSetChanged")
                override fun onNext(response: RestaurantContactorsResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        contactList = response.data
                        adapterContactor!!.setDataSource(contactList)
                        adapterContactor!!.notifyDataSetChanged()
                    } else Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                        .show()
                }
            })

    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}