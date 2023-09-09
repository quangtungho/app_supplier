package vn.techres.supplier.fragment.chatmanager.archivemain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.adapter.chatadapter.archiveadapter.LinkTimeLineAdapter
import vn.techres.supplier.databinding.FragmentMediaGroupBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.helper.WriteLog
import vn.techres.supplier.model.chat.data.ListMessageFile
import vn.techres.supplier.model.chat.response.ArchiveByGroupResponse
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class LinkFragment : Fragment() {
    var binding: FragmentMediaGroupBinding? = null
    var adapter: LinkTimeLineAdapter? = null
    var listData = ArrayList<ListMessageFile>()
    private var groupId = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMediaGroupBinding.inflate(layoutInflater)
        onBody()
        return binding!!.root
    }
    private fun onBody() {
        groupId = requireActivity().intent.getStringExtra(TechresEnum.ID_GROUP.toString())!!
        getLinkGroup()
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = context?.let { LinkTimeLineAdapter(it) }
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.setHasFixedSize(true)
    }
    /**
     * goi API get danh sách group link chat
     */
    private fun getLinkGroup() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_CHAT
        params.request_url =
            "/api/message-tms-supplier/get-message-file?limit=2&page=0&group_id=$groupId&type=${TechresEnum.LINK_GROUP}"
        SupplierApplication.context.let { it ->
            ServiceFactory.createRetrofitServiceNode(
                TechResService::class.java, it
            )
                .getArchiveByGroup(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArchiveByGroupResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        e.message?.let { WriteLog.d(getString(R.string.error), it) }
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: ArchiveByGroupResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            listData = response.data.list
                            if (listData.size == 0) {
                                binding!!.linearDataNull.visibility = View.VISIBLE
                                binding!!.recyclerView.visibility = View.GONE
                            } else {
                                binding!!.linearDataNull.visibility = View.GONE
                                binding!!.recyclerView.visibility = View.VISIBLE
                            }
                            adapter!!.setDataSource(listData)

                        } else
                            Toast.makeText(
                                SupplierApplication.context,
                                response.message,
                                Toast.LENGTH_LONG
                            ).show()
                    }
                })
        }
    }
}