package vn.techres.supplier.fragment.navigate

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.socket.emitter.Emitter
import vn.techres.supplier.BuildConfig
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.adapter.chatadapter.GroupChatAdapter
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.DialogUpdateVersionBinding
import vn.techres.supplier.databinding.FragmentMainBinding
import vn.techres.supplier.fragment.chatmanager.GroupOrderFragment
import vn.techres.supplier.fragment.debtmanager.DebtFragment
import vn.techres.supplier.fragment.ordermanager.OrderManagerFragment
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.SocketChatOnDataEnum
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.chat.data.MessageNotSeen
import vn.techres.supplier.model.chat.data.SocketGroup
import vn.techres.supplier.model.chat.data.SocketRemoveGroup
import vn.techres.supplier.model.chat.response.NotSeenGroupChatResponse
import vn.techres.supplier.model.datamodel.DataGetVersion
import vn.techres.supplier.model.datamodel.ListGroupOrder
import vn.techres.supplier.model.datamodel.ListNumberOrder
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.entity.User
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.DayOrderResponse
import vn.techres.supplier.model.response.UserResponse
import vn.techres.supplier.model.response.VersionResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.text.SimpleDateFormat
import java.util.*

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class MainFragment : BaseBindingFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    val tagName: String = MainFragment::class.java.name
    var dataNotSeenGroupChat = MessageNotSeen()

    private var dateTo: String? = ""
    var listData = ArrayList<ListGroupOrder>()
    var dataOrder = ListNumberOrder()
    private var adapter: GroupChatAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
        mainActivity!!.setLoading(false)
        //On Socket
        SupplierApplication.getSocketInstance()?.on(
            SocketChatOnDataEnum.MESSAGE_NOT_SEEN_BY_ONE_GROUP.toString(),
            onGroup
        )
        SupplierApplication.getSocketInstance()?.on(
            SocketChatOnDataEnum.MESSAGE_NOT_SEEN_BY_ONE_GROUP_ORDER.toString(),
            onGroup
        )
        SupplierApplication.getSocketInstance()?.on(
                SocketChatOnDataEnum.RES_REMOVE_USER_OUT_ROOM.toString(),
                onRemoveGroup
        )
        SupplierApplication.getSocketInstance()
            ?.on(SocketChatOnDataEnum.RES_REMOVE_GROUP.toString(), onRemoveGroup)
        SupplierApplication.getSocketInstance()
            ?.on(SocketChatOnDataEnum.NEW_GROUP_CREATE.toString(), onCreateGroup)

        //checkVersionUpdate()

        dataToOrder()
        mainActivity!!.setLinearLayoutVersion(false)
        //API load user
        getUser()
        when {
            cacheManager.get(TechresEnum.KEY_BACK.toString()) == TechresEnum.DEBT_ID_FRAGMENT.toString() -> {
                showiconnavigationDebtfragment()
                binding!!.bottomNavigation.show(TechresEnum.DEBT_ID_FRAGMENT.toString().toInt())

            }
            cacheManager.get(TechresEnum.KEY_BACK.toString()) == TechresEnum.ORDERMANAGEMENT_ID_FRAGMENT.toString() -> {
                showIconNavigationOrderManagementFragment()
                binding!!.bottomNavigation.show(
                    TechresEnum.ORDERMANAGEMENT_ID_FRAGMENT.toString().toInt()
                )

            }
            cacheManager.get(TechresEnum.KEY_BACK.toString()) == TechresEnum.CHAT_ID_FRAGMENT.toString() -> {
                showIconNavigationChatFragment()
                binding!!.bottomNavigation.show(TechresEnum.CHAT_ID_FRAGMENT.toString().toInt())


            }
            cacheManager.get(TechresEnum.KEY_BACK.toString()) == TechresEnum.ACCOUNTMENU_ID_FRAGMENT.toString() -> {
                showIconNavigationAccountMenuFragment()
                binding!!.bottomNavigation.show(
                    TechresEnum.ACCOUNTMENU_ID_FRAGMENT.toString().toInt()
                )

            }
            else -> {
                addFragment(OverviewFragment.newInstance())
                binding!!.bottomNavigation.show(TechresEnum.OVERVIEW_ID_FRAGMENT.toString().toInt())
            }
        }

        addIconIdFragment()

        binding!!.bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                0 -> {
                    showIconNavigationHomeFragment()
                }
                1 -> {
                    showiconnavigationDebtfragment()
                }
                2 -> {
                    showIconNavigationOrderManagementFragment()
                }
                3 -> {
                    showIconNavigationChatFragment()
                }
                4 -> {
                    showIconNavigationAccountMenuFragment()
                }
                else -> {
                    showIconNavigationHomeFragment()
                }
            }
        }
    }
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
                        if (i == 0) adapter!!.notifyItemChanged(i) else adapter!!.swap(
                            listData[i],
                            i
                        )
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
    override fun onResume() {
        super.onResume()
        apiChatNotSeenGroupChat()
        getAPIListCountOrder(dateTo!!)
    }
    private fun getUser() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/employees/" + CurrentUser.getCurrentUser(requireActivity())!!.id

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getUser(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UserResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: UserResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        if (response.data != null) {
                            val user = response.data
                            saveUser(user)
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                response.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }
            })
    }
    private fun dataToOrder() {
        val sdfTo = SimpleDateFormat(getString(R.string.ddmmyyyy), Locale.getDefault())
        dateTo = sdfTo.format(Date())
    }
    private fun getAPIListCountOrder(toDate: String) {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url =
            "/api/supplier-orders/count?time=$toDate"

        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getListDay(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DayOrderResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: DayOrderResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        dataOrder = response.data
                        if (dataOrder.total_count_supplier_order_request == 0 && dataOrder.total_count_supplier_order_for_supplier == 0)
                            binding!!.bottomNavigation.clearFocus()
                        else
                            binding!!.bottomNavigation.setCount(
                                2,
                                dataOrder.total_count_supplier_order_request.toString()
                            )
                    } else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    private fun apiChatNotSeenGroupChat() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_CHAT
        params.request_url = "/api/group-tms-supplier/total-unread-messages"
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, requireActivity()
        )
            .getChatNotSeenGroupChat(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<NotSeenGroupChatResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: NotSeenGroupChatResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        dataNotSeenGroupChat = response.data
                        if (dataNotSeenGroupChat.message_not_seen_all == 0)
                            binding!!.bottomNavigation.clearFocus()
                        else
                            binding!!.bottomNavigation.setCount(
                                3,
                                dataNotSeenGroupChat.message_not_seen_all.toString()
                            )
                    } else Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }
    fun saveUser(userResponse: User?) {
        val user = CurrentUser.getCurrentUser(requireActivity().baseContext)
        user?.let {
            it.name = userResponse?.name + ""
            it.phone = userResponse?.phone + ""
            it.address = userResponse?.address + ""
            it.email = userResponse?.email + ""
            it.avatar = userResponse?.avatar + ""
        }
        CurrentUser.saveUserInfo(requireActivity().baseContext, user)
    }
    private fun showIconNavigationHomeFragment() {
        /**
         * When the icon's overview is showed then these other icon are hided
         * And put on TechresEnum KEY_BACK = "OVERVIEW_ID_FRAGMENT"
         * Keyword OVERVIEW_ID_FRAGMENT = 0
         */

        replaceFragment(OverviewFragment.newInstance())
        cacheManager.put(
            TechresEnum.KEY_BACK.toString(),
            TechresEnum.OVERVIEW_ID_FRAGMENT.toString()
        )

        binding!!.txtOverview.visibility = View.GONE
        binding!!.txtDebt.visibility = View.VISIBLE
        binding!!.txtCart.visibility = View.VISIBLE
        binding!!.txtChat.visibility = View.VISIBLE
        binding!!.txtAccount.visibility = View.VISIBLE
    }
    private fun showiconnavigationDebtfragment() {
        /**
         * When the icon's debt is showed then these other icon are hided
         * And put on TechresEnum KEY_BACK = "DEBT_ID_FRAGMENT"
         * Keyword DEBT_ID_FRAGMENT = 1
         */

        replaceFragment(DebtFragment.newInstance())
        cacheManager.put(TechresEnum.KEY_BACK.toString(), TechresEnum.DEBT_ID_FRAGMENT.toString())

        binding!!.txtOverview.visibility = View.VISIBLE
        binding!!.txtDebt.visibility = View.GONE
        binding!!.txtCart.visibility = View.VISIBLE
        binding!!.txtChat.visibility = View.VISIBLE
        binding!!.txtAccount.visibility = View.VISIBLE
    }
    private fun showIconNavigationOrderManagementFragment() {
        /**
         * When the icon's order management is showed then these other icon are hided.
         * And put on TechresEnum KEY_BACK = "ORDERMANAGEMENT_ID_FRAGMENT"
         * Keyword ORDERMANAGEMENT_ID_FRAGMENT = 2
         */

        replaceFragment(OrderManagerFragment.newInstance())
        cacheManager.put(
            TechresEnum.KEY_BACK.toString(),
            TechresEnum.ORDERMANAGEMENT_ID_FRAGMENT.toString()
        )

        binding!!.txtOverview.visibility = View.VISIBLE
        binding!!.txtDebt.visibility = View.VISIBLE
        binding!!.txtCart.visibility = View.GONE
        binding!!.txtChat.visibility = View.VISIBLE
        binding!!.txtAccount.visibility = View.VISIBLE
    }
    private fun showIconNavigationChatFragment() {
        /**
         * When the icon's customer is showed then these other icon are hided.
         * And put on TechresEnum KEY_BACK = "CHAT_ID_FRAGMENT"
         * Keyword CHAT_ID_FRAGMENT = 3
         */

        replaceFragment(GroupOrderFragment.newInstance())
        cacheManager.put(
            TechresEnum.KEY_BACK.toString(),
            TechresEnum.CHAT_ID_FRAGMENT.toString()
        )

        binding!!.txtOverview.visibility = View.VISIBLE
        binding!!.txtDebt.visibility = View.VISIBLE
        binding!!.txtCart.visibility = View.VISIBLE
        binding!!.txtChat.visibility = View.GONE
        binding!!.txtAccount.visibility = View.VISIBLE

    }
    private fun showIconNavigationAccountMenuFragment() {
        /**
         * When the icon's account is showed then these other icon are hided.
         * And put on TechresEnum KEY_BACK = "ACCOUNTMENU_ID_FRAGMENT"
         * Keyword ACCOUNTMENU_ID_FRAGMENT = 4
         */

        replaceFragment(AccountMenuFragment.newInstance())
        cacheManager.put(
            TechresEnum.KEY_BACK.toString(),
            TechresEnum.ACCOUNTMENU_ID_FRAGMENT.toString()
        )
        binding!!.txtOverview.visibility = View.VISIBLE
        binding!!.txtDebt.visibility = View.VISIBLE
        binding!!.txtCart.visibility = View.VISIBLE
        binding!!.txtChat.visibility = View.VISIBLE
        binding!!.txtAccount.visibility = View.GONE
    }
    companion object {
        @JvmStatic
        fun newInstance() = MainFragment().apply {
            arguments = Bundle().apply { }
        }
    }
    private fun addIconIdFragment() {
        /**
         * Pass the id corresponding to the icon on the bar navigation
         */
        binding!!.bottomNavigation.add(
            MeowBottomNavigation.Model(
                TechresEnum.OVERVIEW_ID_FRAGMENT.toString()
                    .toInt(), R.drawable.icon_overview_navigation
            )
        )
        binding!!.bottomNavigation.add(
            MeowBottomNavigation.Model(
                TechresEnum.DEBT_ID_FRAGMENT.toString()
                    .toInt(), R.drawable.icon_debt_navigation
            )
        )
        binding!!.bottomNavigation.add(
            MeowBottomNavigation.Model(
                TechresEnum.ORDERMANAGEMENT_ID_FRAGMENT.toString()
                    .toInt(), R.drawable.icon_cart_navigation
            )
        )

        binding!!.bottomNavigation.add(
            MeowBottomNavigation.Model(
                TechresEnum.CHAT_ID_FRAGMENT.toString()
                    .toInt(), R.drawable.icon_chat_navigation
            )

        )

        binding!!.bottomNavigation.add(
            MeowBottomNavigation.Model(
                TechresEnum.ACCOUNTMENU_ID_FRAGMENT.toString()
                    .toInt(), R.drawable.icon_account_navigation
            )
        )

    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.frame_container, fragment)
            .addToBackStack(Fragment::class.java.simpleName).commit()

    }
    private fun addFragment(fragment: Fragment) {
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransition.add(R.id.frame_container, fragment)
            .addToBackStack(Fragment::class.java.simpleName).commit()
    }
    private var doubleBackToExitPressedOnce: Boolean = false
    override fun onBackPress() {
        if (doubleBackToExitPressedOnce) {
            requireActivity().finish()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(context, R.string.toast_outapp, Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(
            {
                doubleBackToExitPressedOnce = false
            }, TechresEnum.TIME_2000.toString().toLong()
        )
    }
    private fun checkVersionUpdate() {
        val params = BaseParams()
        params.http_method = 0
        params.project_id = AppConfig.PROJECT_OAUTH
        params.request_url = "/api/check-version?os_name=supplier_android"
        ServiceFactory.createRetrofitService(
            TechResService::class.java, requireActivity()
        )
            .getVersion(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<VersionResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: VersionResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        val dataVersion = response.data

                        if (dataVersion.version != BuildConfig.VERSION_NAME) {
                            if (dataVersion.is_require_update == AppConfig.IS_REQUIRE_UPDATE) {
                                openDialogVersion(dataVersion)
                            }
                        }

                    }
                }
            })
    }
    /**
     *  openDialogVersion
      */
    private fun openDialogVersion(dataVersion: DataGetVersion) {
        val bindingDialog = DialogUpdateVersionBinding.inflate(layoutInflater)
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(bindingDialog.root)
        val animFragment =
            AnimationUtils.loadAnimation(SupplierApplication.context, R.anim.fade_out)
        bindingDialog.linearUpdate.startAnimation(animFragment)
        bindingDialog.cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialog.btnUpdate.setOnClickListener {
            val urlRateApp = getString(R.string.supplier_link)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlRateApp)
            startActivity(intent)
        }
        dialog.setCancelable(false)
        bindingDialog.tvDescription.text = dataVersion.message
        dialog.show()
    }
}