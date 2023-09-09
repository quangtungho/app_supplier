package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataOrderAmount

class ReportOrderAmountResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataOrderAmount()

}