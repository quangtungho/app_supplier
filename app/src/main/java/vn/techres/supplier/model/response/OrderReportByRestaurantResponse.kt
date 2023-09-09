package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataOrderReportByRestaurant

class OrderReportByRestaurantResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataOrderReportByRestaurant()
}