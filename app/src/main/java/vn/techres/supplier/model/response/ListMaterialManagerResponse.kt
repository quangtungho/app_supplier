package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.MaterialManager

class ListMaterialManagerResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = ArrayList<MaterialManager>()
}