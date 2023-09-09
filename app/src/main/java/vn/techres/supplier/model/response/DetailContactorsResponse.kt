package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataDetailContactors
import vn.techres.supplier.model.datamodel.DataRestaurantContactors

class DetailContactorsResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataDetailContactors()
}