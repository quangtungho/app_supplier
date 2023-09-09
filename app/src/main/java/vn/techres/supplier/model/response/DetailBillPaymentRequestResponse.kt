package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.*

class DetailBillPaymentRequestResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = ArrayList<DataListOrder>()
}