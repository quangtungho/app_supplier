package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataSumMaterialOrder

class SumMaterialOrderResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = ArrayList<DataSumMaterialOrder>()
}