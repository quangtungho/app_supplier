package vn.techres.supplier.fragment.restaurantcontactors

import android.os.Bundle
import android.view.View
import vn.techres.supplier.base.BaseBindingFragment
import vn.techres.supplier.databinding.FragmentDebtRestaurantBinding
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class DebtRestaurantFragment :
    BaseBindingFragment<FragmentDebtRestaurantBinding>(FragmentDebtRestaurantBinding::inflate) {
    val tagName: String = DebtRestaurantFragment::class.java.name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBody()
    }
    private fun onBody() {
    }
    override fun onBackPress() {
        mainActivity!!.supportFragmentManager.popBackStack()
    }
}