package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataSpecification

class SpecificationResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data: ArrayList<DataSpecification>? = null
}
