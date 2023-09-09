package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataGroupOrder

class GroupOrderResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataGroupOrder()

}