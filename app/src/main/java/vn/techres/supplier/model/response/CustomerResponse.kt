package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataCustomer

class CustomerResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data: DataCustomer? = null
}
