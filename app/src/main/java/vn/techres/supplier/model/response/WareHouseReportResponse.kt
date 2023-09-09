package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataRestaurantContactors
import vn.techres.supplier.model.datamodel.DataWareHouseReport

class WareHouseReportResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataWareHouseReport()
}