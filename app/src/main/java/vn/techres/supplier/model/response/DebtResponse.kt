package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataDebt

class DebtResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataDebt()
}