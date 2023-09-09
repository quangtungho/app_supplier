package vn.techres.supplier.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import java.util.*
import java.io.IOException as IOException1

object AppConfig {
    const val SUCCESS_CODE = 200
    const val INTERNAL_SERVER_ERROR = 500
    const val UNAUTHORIZATION = 400
    const val TOKEN_EXPIRED = 410

    const val POST = 1
    const val GET = 0
    const val PROJECT_OAUTH = 8888
    const val PROJECT_ALOLINE = 8082
    const val PROJECT_ID_SUPPLIER = 8087
    const val PROJECT_CHAT = 1484
    const val PROJECT_UPLOAD = 1488
    const val PROJECT_OAUTH_NODE = 9999
    const val PRODUCTION_MODE = 1
    const val PROJECT_LOG = 1487
    const val IS_REQUIRE_UPDATE = 1

    val cacheManager: CacheManager = CacheManager.getInstance()

    var oauthApiEndpoint = "http://oauth.api.techres.vn:8888/"
        internal set

    var oauthApiNodeJs = "http://oauth.node.techres.vn:8888/"
        internal set

    var apiEndpoints = "http://beta.api.techres.vn:8087/"
        internal set

//    var APITEST = "http://beta.api.techres.vn:8082/"
//        internal set

    var project_id = ""

    var client_secret = ""
        internal set

    var defaultToken = ""
        internal set

    var apiADS = "http://beta.ads.api.techres.vn:3002"
        internal set


    var projectIdNode = "cHJvamVjdC51c2VyLmtleSZhYmMkXiYl"
        internal set

    var apiChat = "http://chat.techres.vn:3000"
        internal set

    var apiLuckyWheel = "http://luckywheel.api.techres.vn:3003"
        internal set

    var apiLoginNode = "http://oauth.node.techres.vn:9999"
        internal set

    var productionMode = 0
        internal set

    var baseEndPoint = ""
        internal set

    fun loadConfig(context: Context) {
        val resources = context.resources
        val assetManager = resources.assets




        try {
            val inputStream = assetManager.open("appconfig.properties")
            val properties = Properties()
            properties.load(inputStream)
            oauthApiNodeJs = properties.getProperty("OAUTH_API_NODE_JS")
            apiLoginNode = properties.getProperty("OAUTH_API_NODE_JS")
//            apiEndpoints = properties.getProperty("API_ENDPOINT")
            oauthApiEndpoint = properties.getProperty("API_OAUTH_ENDPOINT")
            projectIdNode = properties.getProperty("PROJECT_ID_NODE")
            project_id = properties.getProperty("PROJECT_ID")
            client_secret = properties.getProperty("CLIENT_SECRET")
            defaultToken = properties.getProperty("DEFAULT_TOKEN")
            baseEndPoint = properties.getProperty("BASE_ENDPOINT")
            productionMode = properties.getProperty("PRODUCTION_MODE").toInt()
        } catch (e: IOException1) {
            System.err.println("Failed to open property file")
            e.printStackTrace()
        }

    }

    fun saveSharePreference(activity: Activity, share_key: String, share_value: String?) {
        val sharedPreference: SharedPreferences =
            activity.getSharedPreferences("PREFERENCE_TECHRES_NAME", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.putString(share_key, share_value)
        editor.putLong("l", 100L)
        editor.apply()
    }


    fun readSharePrefences(activity: Activity, share_key: String) {
        val sharedPreference =
            activity.getSharedPreferences("PREFERENCE_TECHRES_NAME", Context.MODE_PRIVATE)
        sharedPreference.getString(share_key, "")
    }

//    fun getDefaultApiEndpoint(): String {
//
//        return apiEndpoints
//    }

    fun getDefaultOauthApiEndpoint(): String {

        return oauthApiEndpoint
    }

    fun getDefaultProjectIdNode(): String {

        return projectIdNode
    }

    fun getDefaultApiNodeJs(): String {

        return oauthApiNodeJs
    }

    fun getChatNodeJs(): String {

        return apiChat
    }

    fun getDefaultApiLuckyWheel(): String {

        return apiLuckyWheel
    }

    fun getDefaultApiLoginNode(): String {

        return apiLoginNode
    }

    fun getImageNodeJs(): String {

        return apiADS
    }

    fun getBaseURL(): String {
        return baseEndPoint
    }

    fun getProductionModes(): Int {
        return productionMode
    }
}
