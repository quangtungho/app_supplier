package vn.techres.supplier.helper

import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.components.AxisBase
import java.text.DecimalFormat

class MyAxisValueFormatter(type: Int) : IAxisValueFormatter {
    var s = ""
    private val mFormat: DecimalFormat
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        return mFormat.format(value.toDouble()) + " " + s
    }

    init {
        mFormat = DecimalFormat("###,###,###,##0")
        s = if (type == 1) {
            "Tỷ"
        } else if (type == 2) {
            "%"
        } else if (type == 3) {
            ""
        } else {
            "Triệu"
        }
    }
}