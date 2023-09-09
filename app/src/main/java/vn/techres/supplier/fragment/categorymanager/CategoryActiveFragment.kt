package vn.techres.supplier.fragment.categorymanager

import android.annotation.SuppressLint
import android.os.Bundle
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
import vn.techres.supplier.adapter.categoryadapter.CategoryActiveAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentCategoryActiveAndPauseBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.datamodel.DataCategory
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.CategoryActiveResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.util.*
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class CategoryActiveFragment :
    BaseBindingFragment<FragmentCategoryActiveAndPauseBinding>(FragmentCategoryActiveAndPauseBinding::inflate) {
    val tagName: String = CategoryActiveFragment::class.java.name
    private var adapter: CategoryActiveAdapter? = null
    private var listData = ArrayList<DataCategory>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        mainActivity!!.setHeader(getString(R.string.txt_category_manager))
        mainActivity!!.setBackClick(true)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        val animScaleShowTopToBottom = AnimationUtils.loadAnimation(context, R.anim.scale_show_top_to_bottom)
        binding!!.recyclerView.startAnimation(animScaleShowTopToBottom)
        setAdapter()
        fragmentCreate()
        searchCategory()
    }
    override fun onResume() {
        super.onResume()
        getAPICategoryActive()
    }
    private fun fragmentCreate() {
        mainActivity!!.setBackClick(true)
        binding!!.txtTotalStatusCategory.text = getString(R.string.txt_total_category_active)
    }
    private fun setAdapter() {
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = CategoryActiveAdapter(mainActivity!!.baseContext)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.setHasFixedSize(true)
    }
    /**
     * Tìm kiếm danh sach danh mục
     */
    private fun searchCategory() {
        binding!!.btnSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList = ArrayList<DataCategory>()
                for (key in listData) {
                    if (listData.size > 0) {
                        val name: String = key.name
                        if (name.lowercase(Locale.getDefault())
                                .contains(newText!!) || name.contains(newText)
                        )
                            newList.add(key)
                    }
                }
                adapter!!.setDataList(newList)
                binding!!.recyclerView.setHasFixedSize(true)
                binding!!.recyclerView.adapter = adapter

                return false
            }
        })
    }
    /**
     * API lấy danh sách danh mục trạng thái đang hoạt động
     * Trạng thái hoạt động: status = 1
     */
    private fun getAPICategoryActive() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/material-categories?status=${TechresEnum.CATEGORY_ACTIVE_MANAGER}"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListCategoryActive(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CategoryActiveResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("NotifyDataSetChanged")
                override fun onNext(response: CategoryActiveResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        listData = response.data
                        adapter!!.setDataList(listData)
                        adapter!!.notifyDataSetChanged()
                        binding!!.txtTotalCategory.text = listData.size.toString()
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