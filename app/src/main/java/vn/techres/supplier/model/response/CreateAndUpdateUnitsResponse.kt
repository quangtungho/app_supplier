package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataUnits

class CreateAndUpdateUnitsResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data: DataUnits? = null
}
