package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
class CreateReceiptsRequest {
    @JsonField(name = ["supplier_addition_fee_reason_id"])
    var supplier_addition_fee_reason_id: Int? = 0

    @JsonField(name = ["fee_month"])
    var fee_month: String? = ""

    @JsonField(name = ["amount"])
    var amount: Float? = 0f

    @JsonField(name = ["note"])
    var note: String? = ""

    @JsonField(name = ["warehouse_session_ids"])
    var warehouse_session_ids = ArrayList<Int>()

}