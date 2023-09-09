package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataRevenueDetail

class RevenueDetailResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataRevenueDetail()
}
