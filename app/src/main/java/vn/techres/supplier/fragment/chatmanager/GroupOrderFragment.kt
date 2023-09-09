package vn.techres.supplier.fragment.chatmanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.socket.emitter.Emitter
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication.Companion.getSocketInstance
import vn.techres.supplier.adapter.chatadapter.GroupChatAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentGroupOrderBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.SocketChatOnDataEnum
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.chat.data.SocketGroup
import vn.techres.supplier.model.chat.data.SocketRemoveGroup
import vn.techres.supplier.model.datamodel.DataGroupOrder
import vn.techres.supplier.model.datamodel.ListGroupOrder
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.GroupOrderResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.util.*

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class GroupOrderFragment :
    BaseBindingFragment<FragmentGroupOrderBinding>(FragmentGroupOrderBinding::inflate){
    val tagName: String = GroupOrderFragment::class.java.name
    private var adapter: GroupChatAdapter? = null
    private var dataGroup = DataGroupOrder()
    var position = 0
    var listData = ArrayList<ListGroupOrder>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    private fun onBody() {
        mainActivity!!.setLoading(false)
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        val animScaleShowTopToBottom =
            AnimationUtils.loadAnimation(context, R.anim.scale_show_top_to_bottom)
        binding!!.recyclerView.startAnimation(animScaleShowTopToBottom)
        onSearch()
        setAdapter()
        apiGroupChat()
        mainActivity!!.setHeader(getString(R.string.chat))
        binding!!.swipeRefreshLayout.setOnRefreshListener {
            onSwipeRefreshLayout()
        }


        //On Socket
        getSocketInstance()?.on(
            SocketChatOnDataEnum.MESSAGE_NOT_SEEN_BY_ONE_GROUP.toString(),
            onGroup
        )
        getSocketInstance()?.on(
            SocketChatOnDataEnum.MESSAGE_NOT_SEEN_BY_ONE_GROUP_ORDER.toString(),
            onGroup
        )
        getSocketInstance()?.on(
            SocketChatOnDataEnum.RES_REMOVE_USER_OUT_ROOM.toString(),
            onRemoveGroup
        )
        getSocketInstance()?.on(SocketChatOnDataEnum.RES_REMOVE_GROUP.toString(), onRemoveGroup)
        getSocketInstance()?.on(SocketChatOnDataEnum.NEW_GROUP_CREATE.toString(), onCreateGroup)

    }

    /**
     * SOCKET GROUP CHAT
     */
    @SuppressLint("LogNotTimber")
    private val onGroup =
        Emitter.Listener { args: Array<Any> ->
            Thread {
                Log.e("Socket MESSAGE_NOT_SEEN_BY_ONE_GROUP %s", args[0].toString())
                val gson = Gson()
                val socketGroup: SocketGroup = gson.fromJson(
                    args[0].toString(),
                    SocketGroup::class.java
                )
                for (i in listData.indices) {
                    if (listData[i]._id.equals(socketGroup._id)) {
                        listData[i].member.number = socketGroup.number
                        listData[i].last_message = socketGroup.last_message
                        listData[i]
                            .last_message_type = socketGroup.last_message_type
                        listData[i]
                            .user_name_last_message = socketGroup.user_name_last_message
                        listData[i]
                            .status_last_message = socketGroup.status_last_message
                        listData[i]
                            .created_last_message = socketGroup.created_last_message



                        if (i == 0)
                            binding!!.recyclerView.post {  adapter!!.notifyItemChanged(i) }
//                            adapter!!.notifyItemChanged(i) else adapter!!.swap(
//                            listDhata[i],
//                            i
//                        )
                        break
                    }
                }
            }.start()
        }

    @SuppressLint("LogNotTimber")
    private val onRemoveGroup =
        Emitter.Listener { args: Array<Any> ->
            Thread {
                Log.e("Socket RES_REMOVE_GROUP %s", args[0].toString())
                val gson = Gson()
                val socketGroup: SocketRemoveGroup =
                    gson.fromJson(
                        args[0].toString(),
                        SocketRemoveGroup::class.java
                    )
                for (i in listData.indices) {
                    if (listData[i]._id.equals(socketGroup.group_id)) {
                        listData.removeAt(i)
                        adapter!!.notifyItemRemoved(i)
                        break
                    }
                }
            }.start()
        }

    @SuppressLint("LogNotTimber")
    private val onCreateGroup =
        Emitter.Listener { args: Array<Any> ->
            Thread {
                Log.e("Socket NEW_GROUP_CREATE %s", args[0].toString())
                val gson = Gson()
                val socketGroup: ListGroupOrder? =
                    gson.fromJson(
                        args[0].toString(),
                        ListGroupOrder::class.java
                    )
                if (socketGroup != null) {
                    listData.add(0, socketGroup)
                }
                adapter!!.notifyItemInserted(0)
            }.start()
        }

//    override fun onResume() {
//        super.onResume()
//        apiGroupChat()
//    }

    private fun onSwipeRefreshLayout() {
        apiGroupChat()
        binding!!.swipeRefreshLayout.isRefreshing = false
    }

    private fun setAdapter() {
        adapter = mainActivity?.let { GroupChatAdapter(it) }
        binding!!.recyclerView.adapter = adapter

        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)

        binding!!.recyclerView.setHasFixedSize(true)
    }

    private fun onSearch() {
        binding!!.btnSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList: ArrayList<ListGroupOrder> = ArrayList()
                for (key in listData) {
                    if (listData.size > 0) {
                        val name: String = key.restaurant_name!!
                        if (name.lowercase(Locale.getDefault())
                                .contains(newText!!) || name.contains(newText)
                        )
                            newList.add(key)
                    }
                }
                adapter!!.setDataSource(newList)
                binding!!.recyclerView.setHasFixedSize(true)
                binding!!.recyclerView.adapter = adapter
                return false
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = GroupOrderFragment().apply {
            arguments = Bundle().apply { }
        }
    }

    /**
     * goi API get danh sách group  chat
     */
    private fun apiGroupChat() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_CHAT
        params.request_url = "/api/groups-tms-supplier?limit=20&page=1&keyword&app_name=supplier"
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, requireActivity()
        )
            .getGroupOrder(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<GroupOrderResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.onError),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: GroupOrderResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        dataGroup = response.data
                        listData = dataGroup.list
                        if (listData.size == 0) {
                            binding!!.recyclerView.visibility = View.GONE
                            binding!!.linearDataNull.visibility = View.VISIBLE
                           // binding!!.linearSearch.visibility = View.GONE
                        } else {
                            binding!!.btnSearch.visibility = View.VISIBLE
                            binding!!.recyclerView.visibility = View.VISIBLE
                           // binding!!.linearSearch.visibility = View.VISIBLE
                        }
                        adapter?.setDataSource(listData)
                    } else Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                        .show()
                }
            })

    }

    private var doubleBackToExitPressedOnce: Boolean = false
    override fun onBackPress() {
        if (doubleBackToExitPressedOnce) {
            mainActivity!!.finish()
            return
        }
        doubleBackToExitPressedOnce = true
        val snack = Snackbar.make(
            requireView(),
            R.string.toast_outapp,
            Snackbar.LENGTH_LONG
        )
        snack.show()
        Handler(Looper.getMainLooper()).postDelayed(
            {
                doubleBackToExitPressedOnce = false
            }, TechresEnum.TIME_2000.toString().toLong()
        )
    }

}