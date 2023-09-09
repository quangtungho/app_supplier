package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataChangeEmployee

class ChangeResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataChangeEmployee()
}