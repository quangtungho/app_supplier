package vn.techres.supplier.fragment.setting

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.adapter.notificationadapter.NotificationAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentNotificationBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.datamodel.DataNotification
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.NotificationResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class NotificationFragment :
    BaseBindingFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {
    val tagName: String = NotificationFragment::class.java.name
    private var adapter: NotificationAdapter? = null
    var dataNotification = DataNotification()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        setAdapter()
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        mainActivity!!.setHeader(getString(R.string.txt_Notification))
        mainActivity!!.setBackClick(true)
        apiListNotification()
        binding!!.swipeRefreshLayout.setOnRefreshListener {
            onSwipeRefreshLayout()
        }
    }
    private fun onSwipeRefreshLayout() {
        apiListNotification()
        binding!!.swipeRefreshLayout.isRefreshing = false
    }
    private fun setAdapter() {
        adapter = mainActivity?.let { NotificationAdapter(it) }
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerView.setHasFixedSize(true)
    }

    private fun apiListNotification() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_LOG
        params.request_url =
            "/api/employee-notification?page=1&limit=20&type=${TechresEnum.ENUM_LOG_NOTIFICATION}"
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, requireActivity()
        )
            .getNotification(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<NotificationResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: NotificationResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        dataNotification = response.data
                        if (dataNotification.list.size == 0) {
                            binding!!.linearDataNull.visibility = View.VISIBLE
                            binding!!.recyclerView.visibility = View.GONE
                        } else {
                            binding!!.linearDataNull.visibility = View.GONE
                            binding!!.recyclerView.visibility = View.VISIBLE
                        }
                        adapter!!.setDataNotification(dataNotification)
                    }else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}