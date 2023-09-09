package vn.techres.supplier.model.entity

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import vn.techres.supplier.helper.CacheManager
import vn.techres.supplier.helper.PrefUtils
import vn.techres.supplier.helper.PreferenceHelper
import vn.techres.supplier.helper.TechresEnum


object CurrentUser {
    private var cacheManager: CacheManager? = null

    private var mUserInfo: User? = User()

    fun isLogin(it: Context): Boolean {
        return try {
            val sharedPreference = PreferenceHelper(it)
            val userJson = sharedPreference.getValueString(TechresEnum.CACHE_USER_INFO.toString())

            mUserInfo = Gson().fromJson(userJson, User::class.java)
            mUserInfo!!.id > 0
        } catch (e: Exception) {
            e.stackTrace
            false
        }
    }

    fun initCacheManager(cacheManager: CacheManager) {
        CurrentUser.cacheManager = cacheManager
    }

    fun getAccessToken(context: Context): String {
        val sharedPreference = PreferenceHelper(context)
        val configJava = CurrentConfigJava.getConfigJava(context)
        val userJson = sharedPreference.getValueString(TechresEnum.CACHE_USER_INFO.toString())
        var user = Gson().fromJson<User>(userJson, object :
            TypeToken<User>() {}.type)
        if (user == null) {
            user = User().userDefault
        }
        val accessToken = user.access_token
        return if (accessToken.isNotEmpty()) {
            "Bearer $accessToken"
        } else {
            "Basic " + configJava.api_key + ""
        }
    }

    fun getAccessTokenNodeJs(context: Context): String {
        try {
            val sharedPreference = PreferenceHelper(context)
            val userJson = sharedPreference.getValueString(TechresEnum.CACHE_USER_INFO.toString())
            val user = Gson().fromJson<User>(userJson, object :
                TypeToken<User>() {}.type)
            val nodeAccessToken = user.nodeAccessToken
            return if (user != null) {
                if (nodeAccessToken.isNotEmpty()) {
                    nodeAccessToken
                } else {
                    ""
                }
            } else {
                ""
            }
        }catch (e:Exception){
            return ""
        }
    }

    fun saveUserInfo(ct: Context, userInfo: User?) {
        try {
            val userJson = Gson().toJson(userInfo, User::class.java)
            val sharedPreference = PreferenceHelper(ct)
            sharedPreference.save(TechresEnum.CACHE_USER_INFO.toString(), userJson)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearUserInfo(ct: Context) {
        try {
            val sharedPreference = PreferenceHelper(ct)
            sharedPreference.save(TechresEnum.CACHE_USER_INFO.toString(), "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCurrentUser(ct: Context): User? {
        try {
            val sharedPreference = PreferenceHelper(ct)
            val userJson = sharedPreference.getValueString(TechresEnum.CACHE_USER_INFO.toString())
            mUserInfo = Gson().fromJson(userJson, User::class.java)
        } catch (e: Exception) {
            e.stackTrace
            return User()
        }
        if (mUserInfo == null)
            mUserInfo = User()
        return mUserInfo!!
    }

    fun saveFistRunApp(ct: Context, isFirst: Int) {
        PrefUtils.getInstance(ct)!!.putInt(TechresEnum.KEY_FIRST_RUN_APP.toString(), isFirst)
    }

    fun getFistRunApp(ct: Context): Int {
        return PrefUtils.getInstance(ct)!!.getInt(TechresEnum.KEY_FIRST_RUN_APP.toString(), 0)
    }
}