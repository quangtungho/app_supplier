package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.ReportRevenueWeek

class RevenueWeekResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = ReportRevenueWeek()
}