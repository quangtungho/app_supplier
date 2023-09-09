package vn.techres.supplier.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PrefUtils private constructor() {
    private var preferences: SharedPreferences? = null

    /**
     * Get String value from SharedPreferences at 'key'. If key not found, return ""
     *
     * @param key SharedPreferences key
     * @return String value at 'key' or "" (empty String) if key not found
     */
    fun getString(key: String?): String? {
        return if (preferences == null) "" else preferences!!.getString(key, "")
    }

    fun getString(key: String?, defaultValue: String?): String? {
        return if (preferences == null) "" else preferences!!.getString(key, defaultValue)
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        return if (preferences == null) 0 else preferences!!.getInt(key, defaultValue)
    }

    fun getBoolean(key: String?): Boolean {
        return if (preferences == null) false else preferences!!.getBoolean(key, false)
    }

    /**
     * Put String value into SharedPreferences with 'key' and save
     *
     * @param key   SharedPreferences key
     * @param value String value to be added
     */
    fun putString(key: String?, value: String?) {
        checkForNullKey(key)
        checkForNullValue(value)
        preferences!!.edit().putString(key, value).apply()
    }

    fun putInt(key: String?, value: Int) {
        checkForNullKey(key)
        preferences!!.edit().putInt(key, value).apply()
    }

    fun putBoolean(key: String?, value: Boolean?) {
        checkForNullKey(key)
        preferences!!.edit().putBoolean(key, value!!).apply()
    }

    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure
     *
     * @param key pref key
     */
    fun checkForNullKey(key: String?) {
        if (key == null) {
            throw NullPointerException()
        }
    }

    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure
     *
     * @param value pref key
     */
    fun checkForNullValue(value: String?) {
        if (value == null) {
            throw NullPointerException()
        }
    }

    companion object {
        private var prefUtils: PrefUtils? = null
        fun savePreferences(activity: Context, key: String?, value: String?) {
            val sp = PreferenceManager.getDefaultSharedPreferences(activity.applicationContext)
            val editor = sp.edit()
            editor.putString(key, value)
            editor.commit()
        }

        fun readPreferences(activity: Context, key: String?, defaultValue: String?): String? {
            val sp = PreferenceManager.getDefaultSharedPreferences(activity.applicationContext)
            return sp.getString(key, defaultValue)
        }

        fun getInstance(activity: Context): PrefUtils? {
            if (prefUtils == null) {
                prefUtils = PrefUtils()
                prefUtils!!.preferences =
                    PreferenceManager.getDefaultSharedPreferences(activity.applicationContext)
            }
            return prefUtils
        }
    }
}