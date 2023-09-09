package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import vn.techres.supplier.model.datamodel.DataListMaterial

@JsonObject
class ConfirmBillOrderRequest {
    @JsonField(name = ["vat"])
    var vat: Float? = 0f

    @JsonField(name = ["supplier_order_request_id"])
    var supplier_order_request_id: Int? = null

    @JsonField(name = ["restaurant_material_order_request_id"])
    var restaurant_material_order_request_id: Int? = 0

    @JsonField(name = ["expected_delivery_time"])
    var expected_delivery_time: String? = ""

    @JsonField(name = ["discount_percent"])
    var discount_percent: Float? = 0f

    @JsonField(name = ["list_material"])
    var list_material : List<DataListMaterial>? = ArrayList()


}