package vn.techres.supplier.helper

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat

class MyValueFormatter(type: Int) : IValueFormatter {
    var type = 0
    private val mFormat: DecimalFormat
    override fun getFormattedValue(
        value: Float,
        entry: Entry,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler
    ): String {
        val format = 0.0
        val a: Float
        a = if (type == 5 || type == 6 || type == 7) {
            value * 1000000000
        } else {
            value * 1000000
        }
        return mFormat.format(a.toDouble())
    }

    init {
        var type = type
        mFormat = DecimalFormat("###,###,###,##0")
        type = type
    }
}