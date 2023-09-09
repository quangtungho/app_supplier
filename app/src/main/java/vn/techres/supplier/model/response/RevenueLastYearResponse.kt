package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.ReportRevenueLastYear

class RevenueLastYearResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = ReportRevenueLastYear()
}