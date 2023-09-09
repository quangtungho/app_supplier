package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataMaterialManager

class ChangeStatusMaterialResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataMaterialManager()
}