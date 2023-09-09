package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.ReportRevenueThisMonth

class RevenueThisMonthResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = ReportRevenueThisMonth()
}