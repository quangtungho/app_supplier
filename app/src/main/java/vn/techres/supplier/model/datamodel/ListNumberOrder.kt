package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable

@JsonObject
class ListNumberOrder : Serializable {
    @JsonField(name = ["id"])
    var id: Int = 0

    @JsonField(name = ["report_time"])
    var report_time: String = " "

    @JsonField(name = ["total_count_supplier_order_request"])
    var total_count_supplier_order_request: Int = 0

    @JsonField(name = ["total_count_supplier_order_request_for_supplier"])
    var total_count_supplier_order_request_for_supplier: Int = 0

    @JsonField(name = ["total_count_supplier_order_for_account"])
    var total_count_supplier_order_for_account: Int = 0

    @JsonField(name = ["total_count_supplier_order_for_supplier"])
    var total_count_supplier_order_for_supplier: Int = 0

}