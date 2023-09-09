package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataReturnsOrder

class DetailMaterialResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataReturnsOrder()
}