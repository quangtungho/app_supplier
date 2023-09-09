package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.Branch
import vn.techres.supplier.model.datamodel.DataChangeEmployee
import vn.techres.supplier.model.datamodel.ListPaymentRequest

class PaymentRequestResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = ListPaymentRequest()
}