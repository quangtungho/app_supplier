package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataGenusItems

class GenusItemsResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = ArrayList<DataGenusItems>()
}