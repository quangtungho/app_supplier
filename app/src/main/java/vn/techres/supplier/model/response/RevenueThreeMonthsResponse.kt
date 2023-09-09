package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.ReportRevenueThreeMonths

class RevenueThreeMonthsResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = ReportRevenueThreeMonths()
}