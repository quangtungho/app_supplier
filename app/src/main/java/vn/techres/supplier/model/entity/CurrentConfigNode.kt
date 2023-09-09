package vn.techres.supplier.model.entity

import android.content.Context
import com.google.gson.Gson
import vn.techres.supplier.helper.PreferenceHelper
import vn.techres.supplier.helper.TechresEnum


object CurrentConfigNode {
    private var configNodeJs : ConfigNodeJs? = ConfigNodeJs()

    fun saveConfigNode(ct: Context, configNodeJs : ConfigNodeJs?) {
        try {
            val configJSon = Gson().toJson(configNodeJs, ConfigNodeJs::class.java)
            val sharedPreference = PreferenceHelper(ct)
            sharedPreference.save(TechresEnum.CONFIG_NODEJS.toString(), configJSon)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getConfigNode(ct: Context) : ConfigNodeJs {
        try {
            val sharedPreference = PreferenceHelper(ct)
            val configJson = sharedPreference.getValueString(TechresEnum.CONFIG_NODEJS.toString())
            configNodeJs = Gson().fromJson(configJson, ConfigNodeJs::class.java)
        } catch (e: Exception) {
            e.stackTrace
            return ConfigNodeJs()
        }
        if (configNodeJs == null) configNodeJs = ConfigNodeJs()
        return configNodeJs!!
    }
}