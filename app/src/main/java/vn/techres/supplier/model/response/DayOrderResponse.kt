package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.ListNumberOrder

class DayOrderResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = ListNumberOrder()
}