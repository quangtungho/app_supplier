package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataOrderReport

class OrderReportResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataOrderReport()
}