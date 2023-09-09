package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import vn.techres.supplier.model.datamodel.DataMaterialWareHouse

@JsonObject
class CreateWareHouseRequest {
    @JsonField(name = ["type"])
    var type: Int = 0

    @JsonField(name = ["discount_type"])
    var discount_type: Int = 0

    @JsonField(name = ["discount_amount"])
    var discount_amount: Int = 0

    @JsonField(name = ["discount_percent"])
    var discount_percent: Int = 0

    @JsonField(name = ["vat_percent"])
    var vat_percent: Float = 0f

    @JsonField(name = ["note"])
    var note: String = ""

    @JsonField(name = ["material_datas"])
    var material_datas = ArrayList<DataMaterialWareHouse>()
}

