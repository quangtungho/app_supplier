package vn.techres.supplier.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import com.aghajari.emojiview.emoji.EmojiData.data
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.techres.supplier.R
import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentSplashBinding
import vn.techres.supplier.helper.AppConfig
import vn.techres.supplier.helper.PrefUtils
import vn.techres.supplier.helper.TechresEnum
import vn.techres.supplier.model.entity.ConfigJava
import vn.techres.supplier.model.entity.CurrentConfigJava
import vn.techres.supplier.model.entity.CurrentUser
import vn.techres.supplier.model.params.BaseParams
import vn.techres.supplier.model.response.ConfigJavaResponse
import vn.techres.supplier.services.ServiceFactory
import vn.techres.supplier.services.TechResService

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class SplashFragment : BaseBindingFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    override fun onBackPress() {}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
        mainActivity!!.setLoading(false)
    }

    private fun onBody() {
        mainActivity!!.setHideHeader(true)
        mainActivity!!.setLinearLayoutVersion(true)
        CurrentUser.initCacheManager(cacheManager)
        val keyLogout =
                PrefUtils.getInstance(SupplierApplication.context)
                        ?.getString(TechresEnum.FIRST_TIME_RUN_APP.toString(), "1")
        if (mainActivity != null) {
            if (CurrentUser.isLogin(SupplierApplication.context)) {
                Navigation.findNavController(
                        requireActivity(),
                        R.id.nav_host
                ).navigate(R.id.mainFragment)
            } else {
                if (keyLogout.equals("0")) {
                    Navigation.findNavController(
                            requireActivity(),
                            R.id.nav_host
                    ).navigate(R.id.loginFragment)
                } else if (keyLogout.equals("1")) {
                    PrefUtils.getInstance(requireActivity())!!
                            .putString(TechresEnum.FIRST_TIME_RUN_APP.toString(), "0")
                            .toString()
                    Navigation.findNavController(
                            requireActivity(),
                            R.id.nav_host
                    ).navigate(R.id.advSlideWelcomeFragment)
                }
            }
        }
    }
}

