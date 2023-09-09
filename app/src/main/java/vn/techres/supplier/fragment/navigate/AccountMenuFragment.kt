package vn.techres.supplier.fragment.navigate

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.transition.TransitionManager
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.BuildConfig
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.DialogChangePasswordBinding
import vn.techres.supplier.databinding.FragmentAccountMenuBinding
import vn.techres.supplier.fragment.categorymanager.CategoryActiveFragment
import vn.techres.supplier.fragment.collectionfragment.IntroduceFragment
import vn.techres.supplier.fragment.customermanager.CustomerActiveFragment
import vn.techres.supplier.fragment.employeemanager.EmployeeManagerFragment
import vn.techres.supplier.fragment.forgotpassword.LoginFragment
import vn.techres.supplier.fragment.inventorymanagement.InventoryManagementFragment
import vn.techres.supplier.fragment.materialmanager.MaterialManagerFragment
import vn.techres.supplier.fragment.paymentrequest.PaymentRequestManagerFragment
import vn.techres.supplier.fragment.profile.ProfileAccountFragment
import vn.techres.supplier.fragment.reportmanager.*
import vn.techres.supplier.fragment.revenuemanagement.genusitems.check.InCheckFragment
import vn.techres.supplier.fragment.revenuemanagement.genusitems.genus.GenusItemsFragment
import vn.techres.supplier.fragment.revenuemanagement.genusitems.receipts.ReceiptsManagerFragment
import vn.techres.supplier.fragment.setting.NotificationFragment
import vn.techres.supplier.fragment.setting.SettingFragment
import vn.techres.supplier.fragment.specification.SpecificationManagerFragment
import vn.techres.supplier.fragment.support.SupportFragment
import vn.techres.supplier.fragment.units.UnitsManagerFragment
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.AppUtils
import vn.techres.supplier.helper.PreferenceHelper
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.datamodel.ListNumberOrder
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.entity.User
import vn.techres.supplier.model.params.*
import vn.techres.supplier.model.response.BaseResponse
import vn.techres.supplier.model.response.UserNodeResponse
import vn.techres.supplier.model.response.UserResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class AccountMenuFragment :
    BaseBindingFragment<FragmentAccountMenuBinding>(FragmentAccountMenuBinding::inflate) {
    val tagName: String = AccountMenuFragment::class.java.name
    private var user = User()
    private var dateTo: String? = ""
    private var password: String? = ""
    private var passwordNew: String? = ""
    private var dialogChangePass: Dialog? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }

    @SuppressLint("SetTextI18n")
    private fun onBody() {
        val animFragmentNext = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        binding!!.constrainLayoutMain.startAnimation(animFragmentNext)
        mainActivity!!.setLoading(false)
        //onCreate
        fragmentCreate()
        //Intent ProfileAccount fragment
        onClickFragmentProfileAccount()

        //Intent Ordermanagement fragment
        onClickFragmentOrderManagement()
        binding!!.linearRevenueExpenditureAccountmenu.setOnClickListener {
            onClickFragmentRevenueExpenditure()
        }
        //Intent Product fragment
        onClickFragmentMaterialManager()

        onClickFragmentOrderCancelReturnReport()
        //Intent Employees fragment
        onClickFragmentEmployees()

        //Intent Debt fragment
        onClickFragmentDebt()

        //Intent Customer fragment
        onClickFragmentCustomer()

        //Intent Setting fragment
        onClickFragmentSetting()

        //Intent Support fragment
        onClickFragmentSupport()

        //Intent Inventory fragment
        onClickFragmentInventoryManagementFragment()

        //Intent Material Units Manager fragment
        onClickFragmentUnitsManager()

        //Intent Specification Manager fragment
        onClickFragmentSpecificationManager()

        //Intent Category Manager fragment
        onClickFragmentCategoryManager()
        //Intent AppReviews
        onClickOpenAppReviews()
        //Intent Entertainment
        onClickFragmentEntertainment()
        //Intent ChangePass
        onClickFragmentChangePass()
        //Intent logout
        onClickFragmentLogout()
        //Intent OverviewAccoutMenu
        onClickFragmentOverviewAccoutMenu()
        //Intent OrderReport
        onClickFragmentOrderReport()
        //Intent OrderReportByRestaurant
        onClickFragmentOrderReportByRestaurant()
        //  //Intent OrderReportAmount
        onClickFragmentOrderAmountReport()
        //Intent OrderTimeReport
        onClickFragmentOrderTimeReport()
        //Intent MaterialReport
        onClickFragmentMaterialReport()
        onClickFragmentNotify()
        onClickFragmentWareHouseReport()
        onClickInformationProducts()
        onClickFragmentDebtReport()
        onClickFragmentPaymentRequest()
        onClickFragmentGenusItems()
        onClickFragmentReceipts()
        onClickFragmentCheck()
        dataToOrder()
        binding!!.txtVersion.text = getString(R.string.version) + " " + BuildConfig.VERSION_NAME
        onPermission()
    }

    private fun onPermission() {
        when (CurrentUser.getCurrentUser(requireActivity())!!.supplier_role_id) {
            1 -> {

            }
            2 -> {
                binding!!.linearEmployeesAccountmenu.visibility = View.GONE
            }
            3 -> {
                binding!!.linearEmployeesAccountmenu.visibility = View.GONE
                binding!!.linearOrdermanagementAccountmenu.visibility = View.GONE
                binding!!.linearMaterialManager.visibility = View.GONE
                binding!!.linearCategoryManager.visibility = View.GONE
                binding!!.linearDebtAccountmenu.visibility = View.GONE
                binding!!.linearRevenueExpenditureAccountmenu.visibility = View.GONE
                binding!!.linearInventoryManagement.visibility = View.GONE
                binding!!.linearUnitsManager.visibility = View.GONE
                binding!!.linearSpecification.visibility = View.GONE

            }
            4 -> {
                binding!!.txtManager.visibility = View.GONE
                binding!!.linearEmployeesAccountmenu.visibility = View.GONE
                binding!!.linearOrdermanagementAccountmenu.visibility = View.GONE
                binding!!.linearMaterialManager.visibility = View.GONE
                binding!!.linearCategoryManager.visibility = View.GONE
                binding!!.linearDebtAccountmenu.visibility = View.GONE
                binding!!.linearRevenueExpenditureAccountmenu.visibility = View.GONE
                binding!!.linearInventoryManagement.visibility = View.GONE
                binding!!.linearCustomerAccountmenu.visibility = View.GONE
                binding!!.linearUnitsManager.visibility = View.GONE
                binding!!.linearSpecification.visibility = View.GONE
            }
        }
    }

    private fun fragmentCreate() {
        val animFragment = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding!!.constrainLayoutMain.startAnimation(animFragment)
        mainActivity!!.setBackClick(false)
        mainActivity!!.setHideHeader(false)

        //Set data fragment
        user = CurrentUser.getCurrentUser(mainActivity!!.baseContext)!!

        AppUtils.callPhotoAvatar(user.avatar, binding!!.imgAccount)
        binding!!.nameUserAccountmenu.text = user.name
        binding!!.txtSDTAccountmenu.text = user.phone
        binding!!.txtEmailAccountmenu.text = user.email
        binding!!.txtEmailAccountmenu.ellipsize = TextUtils.TruncateAt.MARQUEE
        binding!!.txtEmailAccountmenu.marqueeRepeatLimit = -1
        binding!!.txtEmailAccountmenu.isSelected = true
    }

    /**
     * Bao cao don hang
     */
    private fun onClickFragmentOrderReport() {
        val orderReportFragment = OrderReportFragment()


        binding!!.linearOrderReport.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, OrderReportFragment())
                        ?.addToBackStack(orderReportFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Bao cao kho hang
     */
    private fun onClickFragmentWareHouseReport() {
        val overViewWareHouseFragment = OverViewWareHouseFragment()


        binding!!.linearWarehouseReport.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, OverViewWareHouseFragment())
                        ?.addToBackStack(overViewWareHouseFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Thong bao
     */
    private fun onClickFragmentNotify() {
        val notificationFragment = NotificationFragment()


        binding!!.linearNotifyAccountmenu.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, NotificationFragment())
                        ?.addToBackStack(notificationFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Bao cao don hang theo nha hang
     */
    private fun onClickFragmentOrderReportByRestaurant() {
        val orderReportByRestaurantFragment = OrderReportByRestaurantFragment()

        binding!!.linearOrderReportByRestaurant.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, OrderReportByRestaurantFragment())
                        ?.addToBackStack(orderReportByRestaurantFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Bao cao don hang theo thoi gian
     */
    private fun onClickFragmentOrderTimeReport() {
        val orderTimeReportFragment = OrderTimeReportFragment()


        binding!!.linearOrderTimeReport.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, OrderTimeReportFragment())
                        ?.addToBackStack(orderTimeReportFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Bao cao huy tra don hang
     */
    private fun onClickFragmentOrderCancelReturnReport() {
        val reportCancelReturnFragment = ReportCancelReturnFragment()


        binding!!.linearOrderCancelReturn.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, ReportCancelReturnFragment())
                        ?.addToBackStack(reportCancelReturnFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Bao cao nguyen lieu
     */
    private fun onClickFragmentMaterialReport() {
        val materialReportFragment = MaterialReportFragment()


        binding!!.linearMaterialReport.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, MaterialReportFragment())
                        ?.addToBackStack(materialReportFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Quan ly tai khoan
     */
    private fun onClickFragmentProfileAccount() {
        val profileAccountFragment = ProfileAccountFragment()
        val animFragmentHide = AnimationUtils.loadAnimation(context, R.anim.fragment_alpha_hide)

        //onClick avatar intent fragment profile
        binding!!.linearAvatar.setOnClickListener {
            binding!!.constrainLayoutMain.startAnimation(animFragmentHide)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, ProfileAccountFragment())
                        ?.addToBackStack(profileAccountFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_50.toString().toLong()
            )
        }

        //onClick name user intent fragment profile
        binding!!.nameUserAccountmenu.setOnClickListener {
            binding!!.constrainLayoutMain.startAnimation(animFragmentHide)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, ProfileAccountFragment())
                        ?.addToBackStack(profileAccountFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_50.toString().toLong()
            )
        }

        //onClick phone intent fragment profile
        binding!!.linearPhoneAccountmenu.setOnClickListener {
            binding!!.constrainLayoutMain.startAnimation(animFragmentHide)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, ProfileAccountFragment())
                        ?.addToBackStack(profileAccountFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_50.toString().toLong()
            )
        }

        //onClick email intent fragment profile
        binding!!.linearEmailAccountmenu.setOnClickListener {
            binding!!.constrainLayoutMain.startAnimation(animFragmentHide)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, ProfileAccountFragment())
                        ?.addToBackStack(profileAccountFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_50.toString().toLong()
            )
        }
    }

    /**
     * Quan ly don hang
     */
    private fun onClickFragmentOrderManagement() {
        binding!!.linearOrdermanagementAccountmenu.setOnClickListener {
            val animFragmentBack = AnimationUtils.loadAnimation(context, R.anim.slide_right_out)
            binding!!.constrainLayoutMain.startAnimation(animFragmentBack)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    cacheManager.put(
                        TechresEnum.KEY_BACK.toString(),
                        TechresEnum.ORDERMANAGEMENT_ID_FRAGMENT.toString()
                    )
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.nav_host, MainFragment())?.commitNow()
                }, TechresEnum.TIME_50.toString().toLong()
            )
        }
    }

    /**
     * Quan ly nguyen lieu
     */
    private fun onClickFragmentMaterialManager() {
        val materialManagerFragment = MaterialManagerFragment()

        binding!!.linearMaterialManager.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, MaterialManagerFragment())
                        ?.addToBackStack(materialManagerFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Quan ly danh muc
     */
    private fun onClickFragmentCategoryManager() {
        val categoryActiveFragment = CategoryActiveFragment()

        binding!!.linearCategoryManager.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, CategoryActiveFragment())
                        ?.addToBackStack(categoryActiveFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Quan ly  tong thu chi
     */
    private fun onClickFragmentRevenueExpenditure() {

        if (binding!!.linearRevenueExpenditure.visibility == View.GONE) {
            TransitionManager.beginDelayedTransition(binding!!.linearRevenueExpenditure)
            binding!!.linearRevenueExpenditure.visibility = View.VISIBLE
            binding!!.imgRevenue.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)

        } else {
            TransitionManager.beginDelayedTransition(binding!!.linearRevenueExpenditure)
            binding!!.linearRevenueExpenditure.visibility = View.GONE
            binding!!.imgRevenue.setImageResource(R.drawable.ic_baseline_navigate_next_24)
        }
    }

    /**
     * Quan lý ly do thu chi
     */
    private fun onClickFragmentGenusItems() {
        val genusItemsFragment = GenusItemsFragment()
        binding!!.linearGenusItems.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, GenusItemsFragment())
                        ?.addToBackStack(genusItemsFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Quan ly  hang muc thu
     */
    private fun onClickFragmentReceipts() {
        val receiptsManagerFragment = ReceiptsManagerFragment()
        binding!!.linearReceipts.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, ReceiptsManagerFragment())
                        ?.addToBackStack(receiptsManagerFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Quan ly  hang muc chi
     */
    private fun onClickFragmentCheck() {
        val inCheckFragment = InCheckFragment()
        binding!!.linearCheck.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, InCheckFragment())
                        ?.addToBackStack(inCheckFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Quan ly nhan vien supplier
     */
    private fun onClickFragmentEmployees() {
        val employeeManagerFragment = EmployeeManagerFragment()
        binding!!.linearEmployeesAccountmenu.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, EmployeeManagerFragment())
                        ?.addToBackStack(employeeManagerFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Tong quan
     */
    private fun onClickFragmentOverviewAccoutMenu() {
        binding!!.linearOverviewManagementAccoutmenu.setOnClickListener {
            val animFragmentBack = AnimationUtils.loadAnimation(context, R.anim.slide_right_out)
            binding!!.constrainLayoutMain.startAnimation(animFragmentBack)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    cacheManager.put(
                        TechresEnum.KEY_BACK.toString(),
                        TechresEnum.OVERVIEW_ID_FRAGMENT.toString()
                    )
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.nav_host, MainFragment())?.commitNow()
                }, TechresEnum.TIME_50.toString().toLong()
            )
        }
    }

    /**
     * Ung dung lien quan
     */
    private fun onClickFragmentEntertainment() {
        val entertainmentFragment = IntroduceFragment()
        binding!!.linearEntertainmentAccountmenu.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, IntroduceFragment())
                        ?.addToBackStack(entertainmentFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Quan ly don vi
     */
    private fun onClickFragmentUnitsManager() {
        val unitsManagerFragment = UnitsManagerFragment()
        binding!!.linearUnitsManager.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, UnitsManagerFragment())
                        ?.addToBackStack(unitsManagerFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Quan ly kho hang
     */
    private fun onClickFragmentInventoryManagementFragment() {
        val inventoryManagementFragment = InventoryManagementFragment()

        binding!!.linearInventoryManagement.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, InventoryManagementFragment())
                        ?.addToBackStack(inventoryManagementFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Quan ly quy cach
     */
    private fun onClickFragmentSpecificationManager() {
        val specificationManagerFragment = SpecificationManagerFragment()

        binding!!.linearSpecification.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, SpecificationManagerFragment())
                        ?.addToBackStack(specificationManagerFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Quan ly cong no
     */
    private fun onClickFragmentDebt() {
        binding!!.linearDebtAccountmenu.setOnClickListener {
            val animFragmentBack = AnimationUtils.loadAnimation(context, R.anim.slide_right_out)
            binding!!.constrainLayoutMain.startAnimation(animFragmentBack)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    cacheManager.put(
                        TechresEnum.KEY_BACK.toString(),
                        TechresEnum.DEBT_ID_FRAGMENT.toString()
                    )
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.nav_host, MainFragment())?.commitNow()
                }, TechresEnum.TIME_50.toString().toLong()
            )
        }
    }

    /**
     * Bao cao huy tra don hang
     */
    private fun onClickFragmentPaymentRequest() {
        val paymentRequestManagerFragment = PaymentRequestManagerFragment()


        binding!!.linearPaymentRequestAccountmenu.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, PaymentRequestManagerFragment())
                        ?.addToBackStack(paymentRequestManagerFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Bao cao cong no
     */
    private fun onClickFragmentDebtReport() {
        val debtReportFragment = DebtReportFragment()
        binding!!.linearDebtReport.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, DebtReportFragment())
                        ?.addToBackStack(debtReportFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Quan ly khach hang
     */
    private fun onClickFragmentCustomer() {
        val customerActiveFragment = CustomerActiveFragment()


        binding!!.linearCustomerAccountmenu.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, CustomerActiveFragment())
                        ?.addToBackStack(customerActiveFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Quan ly tong tien don hang
     */
    private fun onClickFragmentOrderAmountReport() {
        val reportOrderAmountFragment = ReportOrderAmountFragment()


        binding!!.linearOrderAmount.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, ReportOrderAmountFragment())
                        ?.addToBackStack(reportOrderAmountFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Cai dat
     */
    private fun onClickFragmentSetting() {
        val settingFragment = SettingFragment()

        binding!!.linearSettingAccountmenu.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, SettingFragment())
                        ?.addToBackStack(settingFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Gop y nha phat trien
     */
    private fun onClickFragmentSupport() {
        val supportFragment = SupportFragment()


        binding!!.linearSupportAccountmenu.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.nav_host, SupportFragment())
                        ?.addToBackStack(supportFragment.tagName)
                        ?.commit()
                }, TechresEnum.TIME_100.toString().toLong()
            )
        }
    }

    /**
     * Danh gia ung dung
     */
    private fun onClickOpenAppReviews() {
        binding!!.linearAppReviewsAccountmenu.setOnClickListener {
            val uri =
                Uri.parse(getString(R.string.supplier_link_app)) // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)

        }
    }

    /**
     * Thong tin san pham
     */
    private fun onClickInformationProducts() {
        binding!!.linearInformationProductsAccountmenu.setOnClickListener {
            val uri =
                Uri.parse(getString(R.string.supplier_link)) // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)

        }
    }

    /**
     * Intent ChangePass fragment
     */
    private fun onClickFragmentChangePass() {
        binding!!.linearChangepasswordAccountmenu.setOnClickListener {
            openDialogChangePass()
        }
    }

    /**
     * Dialog ChangPass
     */

    private fun openDialogChangePass() {
        val bindingDialog = DialogChangePasswordBinding.inflate(layoutInflater)

        dialogChangePass = Dialog(requireActivity())
        dialogChangePass!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogChangePass!!.setContentView(bindingDialog.root)
        val window = dialogChangePass!!.window
        window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowAttributes = window?.attributes
        windowAttributes?.gravity = Gravity.NO_GRAVITY
        window?.attributes = windowAttributes

        fun checkValidate(): Boolean {
            if (Objects.requireNonNull(bindingDialog.oldPassWord.text).toString().isEmpty()) {
                Toast.makeText(requireActivity(), getString(R.string.empty_old), Toast.LENGTH_SHORT)
                    .show()
                bindingDialog.oldPassWord.isFocusable = true
                return false
            }
            if (Objects.requireNonNull(bindingDialog.newPassWord.text).toString().isEmpty()) {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.empty_pass_new),
                    Toast.LENGTH_SHORT
                )
                    .show()
                bindingDialog.newPassWord.isFocusable = true
                return false
            }
            if (Objects.requireNonNull(bindingDialog.newCofirmPassWord.text).toString().isEmpty()) {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.empty_pass_again),
                    Toast.LENGTH_SHORT
                ).show()
                bindingDialog.newCofirmPassWord.isFocusable = true
                return false
            }
            if (!bindingDialog.newCofirmPassWord.text.toString()
                    .equals(bindingDialog.newPassWord.text.toString())
            ) {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.unmatch_pass),
                    Toast.LENGTH_SHORT
                )
                    .show()
                return false
            }
            return true
        }

        bindingDialog.btnCofirm.setOnClickListener {
            hideKeyboard()
            if (checkValidate())
                changePassword(
                    bindingDialog.oldPassWord.text.toString(),
                    bindingDialog.newPassWord.text.toString()
                )
        }
        dialogChangePass!!.show()
    }

    private fun Fragment.hideKeyboard() {
        val activity = this.activity
        if (activity is AppCompatActivity) {
            activity.hideKeyboard()
        }
    }

    private fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

    }

    /**
     * onClick Logout
     */
    private fun onClickFragmentLogout() {
        binding!!.linearLogOutAccountmenu.setOnClickListener {
            openDialogLogout()
        }
    }

    /**
     * Dialog Logout
     */
    private fun openDialogLogout() {
        val dialog = Dialog(mainActivity!!)
        dialog.setContentView(R.layout.dialog_notify)
        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowAttributes = window?.attributes
        windowAttributes?.gravity = Gravity.NO_GRAVITY
        window?.attributes = windowAttributes

        val btnCancel = dialog.findViewById<Button>(R.id.no)
        val btnConfirm = dialog.findViewById<Button>(R.id.yes)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            logOutToken()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.nav_host, LoginFragment())?.commit()
            context?.let { it1 -> CurrentUser.clearUserInfo(it1) }
            mainActivity!!.setLoading(true)
            val snack = Snackbar.make(
                requireView(),
                getString(R.string.logout),
                Snackbar.LENGTH_LONG
            )
            snack.show()
            dialog.dismiss()
        }
        dialog.show()
    }

    /**
     * logoutToken
     */
    private fun logOutToken() {
        val sharedPreference = PreferenceHelper(requireContext())
        val params = PushOutTokenParams()
        params.http_method = 1 /*post 1/get 0*/
        params.project_id = AppConfig.PROJECT_CHAT
        params.request_url = "/api/notification/log-out-supplier"
        params.params.push_token =
            sharedPreference.getValueString(TechresEnum.PUSH_TOKEN.toString())
        params.params.os_name = "supplier_android"
        params.params.device_uid = generateID()
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, requireActivity()
        )
            .getPushOutToken(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResponse> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: BaseResponse) {
                    if (response.status != AppConfig.SUCCESS_CODE) {
                        Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onError(e: Throwable) {}
            })
    }

    private fun dataToOrder() {
        val sdfTo = SimpleDateFormat(getString(R.string.mmyyyy), Locale.getDefault())
        dateTo = sdfTo.format(Date())
    }


    private fun changePassword(oldPassword: String, newPassword: String) {
        val params = ChangePasswordParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        /**
         *  base 64 oldPassword : mat khau hien tai
         */
        val data: ByteArray
        var base64: String? = ""
        val stringTest: String = oldPassword
        try {
            data = stringTest.toByteArray(charset("UTF-8"))
            password = stringTest
            base64 = Base64.encodeToString(
                data,
                Base64.NO_WRAP or Base64.URL_SAFE
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        /**
         * base 64 newPassword : mat khau moi
         */
        val dataNew: ByteArray
        var baseN64: String? = ""
        val stringTestNew: String = newPassword
        try {
            dataNew = stringTestNew.toByteArray(charset("UTF-8"))
            passwordNew = stringTestNew
            baseN64 = Base64.encodeToString(
                dataNew,
                Base64.NO_WRAP or Base64.URL_SAFE
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        params.params.old_password = base64.toString()
        params.params.new_password = baseN64.toString()
        params.params.node_access_token = CurrentUser.getAccessTokenNodeJs(requireContext())
        params.params.type_user = TechresEnum.TYPE_USER.toString()
        params.request_url =
            "/api/employees/${CurrentUser.getCurrentUser(requireActivity())!!.id}/change-password"
        ServiceFactory.createRetrofitService(TechResService::class.java, requireActivity())
            .getChangePassword(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResponse> {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: BaseResponse) {
                    if (response.status == AppConfig.SUCCESS_CODE) {
                        onLoginJava(
                            CurrentUser.getCurrentUser(requireActivity())!!.username,
                            newPassword
                        )
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.txt_Password_Newcofim),
                            Toast.LENGTH_SHORT
                        ).show()
                        dialogChangePass!!.dismiss()
                    } else {
                        Toast.makeText(requireActivity(), response.message!!, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    private fun onLoginJava(userName: String, password_login: String) {
        val params = UserJavaParams()

        params.http_method = 1
        params.project_id = AppConfig.PROJECT_OAUTH
        params.request_url = "/api/suppliers/login"

        val data: ByteArray
        var base64: String? = ""
        val stringTest: String = password_login
        try {
            data = stringTest.toByteArray(charset("UTF-8"))
            password = stringTest
            base64 = Base64.encodeToString(
                data,
                Base64.NO_WRAP or Base64.URL_SAFE
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        params.params.username = userName
        params.params.password = base64.toString()
        params.params.device_uid = generateID()
        params.params.os_name = "android"

        params.let {
            ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
            )
                .loginJava(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {}

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: UserResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            val user = response.data
                            CurrentUser.saveUserInfo(context!!, user)
                            onLoginNodeJs(password_login)
                            supplierDevice()
                            pushToken()
                            val snack = Snackbar.make(
                                requireView(),
                                getString(R.string.welcome_login_success) + user!!.name,
                                Snackbar.LENGTH_LONG
                            )
                            snack.show()


                        } else {

                            val snack = Snackbar.make(
                                requireView(),
                                response.message!!,
                                Snackbar.LENGTH_LONG
                            )
                            snack.show()
                        }
                    }
                })
        }
    }

    private fun pushToken() {
        val sharedPreference = PreferenceHelper(requireContext())
        val params = PushOutTokenParams()
        params.http_method = 1 /*post 1/get 0*/
        params.project_id = AppConfig.PROJECT_CHAT
        params.request_url = "/api/notification/push-token-supplier"
        params.params.push_token =
            sharedPreference.getValueString(TechresEnum.PUSH_TOKEN.toString())
        params.params.os_name = TechresEnum.OS_NAME.toString()
        params.params.device_uid = generateID()
        ServiceFactory.createRetrofitServiceNode(
            TechResService::class.java, requireActivity()
        )
            .getPushOutToken(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResponse> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: BaseResponse) {
                    if (response.status != AppConfig.SUCCESS_CODE) {
                        Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onError(e: Throwable) {}
            })
    }

    private fun onLoginNodeJs(password_login: String) {
        val params = UserNodeJsParams()
        params.http_method = 1
        params.project_id = AppConfig.PROJECT_OAUTH_NODE
        params.request_url = "/api/oauth-login-nodejs/login"

        val data: ByteArray
        var base64: String? = ""
        val stringTest: String = password_login
        try {
            data = stringTest.toByteArray(charset("UTF-8"))
            password = stringTest
            base64 = Base64.encodeToString(
                data,
                Base64.NO_WRAP or Base64.URL_SAFE
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        params.params.username = CurrentUser.getCurrentUser(mainActivity!!)!!.username
        params.params.password = base64.toString()
        params.params.device_uid = generateID().toString()
        params.params.os_name = "android"
        params.params.type_user = TechresEnum.TYPE_USER.toString().toInt()

        params.params.user_id = CurrentUser.getCurrentUser(mainActivity!!)!!.id
        params.params.supplier_id = CurrentUser.getCurrentUser(mainActivity!!)!!.supplier_id
        params.params.role_name =
            CurrentUser.getCurrentUser(mainActivity!!)!!.supplier_role_name
        params.params.role_id =
            CurrentUser.getCurrentUser(mainActivity!!)!!.supplier_role_id

        params.params.full_name = CurrentUser.getCurrentUser(mainActivity!!)!!.name
        params.params.phone = CurrentUser.getCurrentUser(mainActivity!!)!!.phone
        params.params.avatar = CurrentUser.getCurrentUser(mainActivity!!)!!.avatar
        params.params.device_name = getDeviceName()

        params.let {
            ServiceFactory.createRetrofitLoginNode(
                TechResService::class.java, requireActivity()
            )
                .loginNodeJs(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserNodeResponse> {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: UserNodeResponse) {
                        if (response.status == AppConfig.SUCCESS_CODE) {
                            val user = CurrentUser.getCurrentUser(mainActivity!!)
                            user!!._id = response.data!!._id
                            user.nodeAccessToken = response.data!!.node_access_token
                            CurrentUser.saveUserInfo(context!!, user)
                            SupplierApplication.initializationSocket()
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.nav_host, MainFragment())?.commitNow()
                        } else {
                            mainActivity?.setLoading(false)
                            Toast.makeText(
                                requireActivity(),
                                response.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        }
    }

    private fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else {
            capitalize(manufacturer) + " " + model
        }
    }

    private fun capitalize(s: String?): String {
        if (s == null || s.isEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }

    private fun supplierDevice() {
        val sharedPreference = PreferenceHelper(requireContext())
        val params = DeviceParams()
        params.http_method = 1 /*post 1/get 0*/
        params.project_id = AppConfig.PROJECT_ID_SUPPLIER
        params.request_url = "/api/register-supplier-device"
        params.params.restaurant_id = CurrentUser.getCurrentUser(mainActivity!!)!!.restaurant_id
        params.params.device_uid = generateID()
        params.params.push_token =
            sharedPreference.getValueString(TechresEnum.PUSH_TOKEN.toString())
        params.params.os_name = "Android"
        params.params.customer_id = CurrentUser.getCurrentUser(mainActivity!!)!!.id

        params.let {
            ServiceFactory.createRetrofitService(
                TechResService::class.java, requireActivity()
            )
                .getSupplierDevice(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResponse> {
                    override fun onComplete() {}
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: BaseResponse) {
                        if (response.status != AppConfig.SUCCESS_CODE) {
                            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    override fun onError(e: Throwable) {
                    }
                })
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = AccountMenuFragment().apply {
            arguments = Bundle().apply { }
        }
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
    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    @SuppressLint("HardwareIds")
    fun generateID(): String? {
        @SuppressLint("HardwareIds")
        var deviceId =
                Settings.Secure.getString(
                        requireActivity().contentResolver,
                        Settings.Secure.ANDROID_ID
                )
        if ("9774d56d682e549c" == deviceId || deviceId == null) {
            if (ActivityCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.READ_PHONE_STATE
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
            }
            deviceId =
                    (requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
                            .deviceId
            if (deviceId == null) {
                val tmpRand = Random
                deviceId = java.lang.String.valueOf(tmpRand.nextLong())
            }

            return deviceId
        }
        return deviceId
    }
}