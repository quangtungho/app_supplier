package vn.techres.supplier.helper

import android.os.Debug
import android.util.Log

object WriteLog {
    var debug = true
    fun d(tag: String?, msg: String?) {
        if (!debug) return
        Log.d(tag, msg!!)
    }

    fun e(tag: String?, msg: String?) {
        if (!debug) return
        Log.e(tag, msg!!)
    }

    fun i(tag: String?, msg: String?) {
        if (!debug) return
        Log.i(tag, msg!!)
    }

    fun v(tag: String?, msg: String?) {
        if (!debug) return
        Log.v(tag, msg!!)
    }

    fun w(tag: String?, msg: String?) {
        if (!debug) return
        Log.w(tag, msg!!)
    }

    /**
     *
     * @param str
     * Created by tuan.nguyen on 9/09/2018.
     */
    fun LogMem(str: String?) {
        val usedMegs2 = (Debug.getNativeHeapAllocatedSize() / 1048576L).toInt()
        val useMemKB = (Debug.getNativeHeapAllocatedSize() / 1024L).toInt()
    }
}