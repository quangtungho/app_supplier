package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class UpdatePaymentRequest {
    @JsonField(name = ["restaurant_id"])
    var restaurant_id: Int? = 0

    @JsonField(name = ["restaurant_brand_id"])
    var restaurant_brand_id: Int? = 0

    @JsonField(name = ["branch_id"])
    var branch_id: Int? = 0

    @JsonField(name = ["status"])
    var status: Int? = 0

    @JsonField(name = ["from_date"])
    var from_date: String? = ""

    @JsonField(name = ["to_date"])
    var to_date: String? = ""

    @JsonField(name = ["insert_supplier_order_ids"])
    var insert_supplier_order_ids = ArrayList<Int>()

    @JsonField(name = ["delete_supplier_order_ids"])
    var delete_supplier_order_ids = ArrayList<Int>()


}