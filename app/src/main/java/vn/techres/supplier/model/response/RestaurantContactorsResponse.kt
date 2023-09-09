package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataRestaurantContactors

class RestaurantContactorsResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = ArrayList<DataRestaurantContactors>()
}