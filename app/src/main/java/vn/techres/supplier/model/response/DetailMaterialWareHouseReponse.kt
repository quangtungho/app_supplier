package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataDetailMaterialWareHouse

class DetailMaterialWareHouseReponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataDetailMaterialWareHouse()
}