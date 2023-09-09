package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.Customer

class CustomerGetByIdResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data: Customer? = null
}
