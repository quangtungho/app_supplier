package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.Branch
import vn.techres.supplier.model.datamodel.DataChangeEmployee
import vn.techres.supplier.model.datamodel.PaymentRequest

class DetailPaymentRequestResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = PaymentRequest()
}