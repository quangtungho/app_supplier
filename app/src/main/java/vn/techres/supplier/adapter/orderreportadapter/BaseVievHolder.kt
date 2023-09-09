
package vn.techres.supplier.adapter.orderreportadapter

import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by jiang on 12/3/15.
 */
class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected val mViews: SparseArray<View?>
    var convertView: View
        protected set

    /**
     *
     *
     * @param viewId
     * @return
     */
    fun <T : View?> getView(@IdRes viewId: Int): T? {
        var view = mViews[viewId]
        if (view == null) {
            view = convertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T?
    }

    fun setBgColor(@IdRes resID: Int, color: Int): BaseViewHolder {
        getView<View>(resID)!!.setBackgroundColor(color)
        return this
    }

    fun setBgDrawable(@IdRes resID: Int, drawable: Drawable?): BaseViewHolder {
        getView<View>(resID)!!.background = drawable
        return this
    }

    fun setText(@IdRes resID: Int, text: String?): BaseViewHolder {
        (getView<View>(resID) as TextView).text = text
        return this
    }

    fun setTextSize(@IdRes resID: Int, spSize: Int): BaseViewHolder {
        (getView<View>(resID) as TextView).textSize = spSize.toFloat()
        return this
    }

    fun setVisibility(@IdRes resID: Int, @Visibility visibility: Int): BaseViewHolder {
        when (visibility) {
            VISIBLE -> getView<View>(resID)!!.visibility =
                View.VISIBLE
            INVISIBLE -> getView<View>(resID)!!.visibility =
                View.INVISIBLE
            GONE -> getView<View>(resID)!!.visibility =
                View.GONE
        }
        return this
    }

    fun setTextColor(id: Int, textColor: Int): BaseViewHolder {
        (getView<View>(id) as TextView).setTextColor(textColor)
        return this
    }

    @IntDef(*[VISIBLE, INVISIBLE, GONE])
    @Retention(RetentionPolicy.SOURCE)
    annotation class Visibility
    companion object {
        const val VISIBLE = 0x00000000
        const val INVISIBLE = 0x00000004
        const val GONE = 0x00000008
    }

    init {
        mViews = SparseArray()
        convertView = itemView
    }
}