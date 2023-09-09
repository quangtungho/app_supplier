package vn.techres.supplier.activity

import android.view.LayoutInflater
import vn.techres.supplier.base.BaseBindingActivity
import vn.techres.supplier.databinding.ActivityShareMessageGroupBinding
/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class ShareMessageGroupActivity : BaseBindingActivity<ActivityShareMessageGroupBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityShareMessageGroupBinding
        get() = ActivityShareMessageGroupBinding::inflate


    override fun onSetBodyView() {
        binding.imvBack.setOnClickListener {
            onBackPressed()
        }

    }
}