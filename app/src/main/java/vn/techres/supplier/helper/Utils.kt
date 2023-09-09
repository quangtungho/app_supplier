package vn.techres.supplier.helper

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.view.View

object Utils {
    fun View.hide(){
        visibility = View.GONE
    }
    fun clamp(value: Double, low: Double, high: Double): Double {
        return Math.min(Math.max(value, low), high)
    }
    fun mapValueFromRangeToRange(
        value: Double,
        fromLow: Double,
        fromHigh: Double,
        toLow: Double,
        toHigh: Double
    ): Double {
        return toLow + (value - fromLow) / (fromHigh - fromLow) * (toHigh - toLow)
    }
    fun View.show(){
        visibility = View.VISIBLE
    }
    fun asActivity(context: Context?): Activity {
        var result: Context? = context
        while (result is ContextWrapper) {
            if (result is Activity) {
                return result
            }
            result = result.baseContext
        }
        throw IllegalArgumentException("The passed Context is not an Activity.")
    }
    val MIN_KEYBOARD_HEIGHT_PX = 150

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }
}