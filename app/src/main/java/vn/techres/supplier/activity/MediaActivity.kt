package vn.techres.supplier.activity

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import com.zeuskartik.mediaslider.MediaSliderActivity
import vn.techres.supplier.helper.TechresEnum

/**
 * ================================================
 * Created by HOQUANGTUNG on 2021
 * ================================================
 */
class MediaActivity : MediaSliderActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val position = intent.getStringExtra(TechresEnum.POSITION_MEDIA.toString())
        val gson = Gson()
        val userListType = object : TypeToken<ArrayList<String?>?>() {}.type
        val listMedia = gson.fromJson<ArrayList<String>>(
            intent.getStringExtra(TechresEnum.DATA_MEDIA.toString()),
            userListType
        )
        loadMediaSliderView(listMedia, true, position!!.toInt())
        val config = SlidrConfig.Builder()
            .position(SlidrPosition.VERTICAL)
            .build()
        Slidr.attach(this, config)
    }
}