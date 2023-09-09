package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataSpecification

class ChangeStatusSpecificationResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataSpecification()
}
