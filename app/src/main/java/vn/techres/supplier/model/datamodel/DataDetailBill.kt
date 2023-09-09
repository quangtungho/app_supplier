package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import vn.techres.supplier.model.WareHouse
import java.io.Serializable

@JsonObject
class DataDetailBill : Serializable {
    @JsonField(name = ["id"])
    var id: Int = 0

    @JsonField(name = ["code"])
    var code: String = ""

    @JsonField(name = ["amount"])
    var amount: Float = 0f

    @JsonField(name = ["type"])
    var type: Int = 0

    @JsonField(name = ["note"])
    var note: String = ""

    @JsonField(name = ["restaurant_name"])
    var restaurant_name: String = ""

    @JsonField(name = ["status"])
    var status: Int = 0

    @JsonField(name = ["supplier_employee_create_id"])
    var supplier_employee_create_id: Int = 0

    @JsonField(name = ["supplier_order_id"])
    var supplier_order_id: Int = 0

    @JsonField(name = ["supplier_warehouse_session_ids"])
    var supplier_warehouse_session_ids = ArrayList<Int>()

    @JsonField(name = ["image_urls"])
    var image_urls = ArrayList<String>()

    @JsonField(name = ["object_type"])
    var object_type: Int = 0

    @JsonField(name = ["supplier_addition_fee_reason_id"])
    var supplier_addition_fee_reason_id: Int = 0

    @JsonField(name = ["supplier_addition_fee_reason_name"])
    var supplier_addition_fee_reason_name: String = ""

    @JsonField(name = ["fee_month"])
    var fee_month: String = ""

    @JsonField(name = ["created_at"])
    var created_at: String = ""

    @JsonField(name = ["updated_at"])
    var updated_at: String = ""

    @JsonField(name = ["supplier_employee_create_full_name"])
    var supplier_employee_create_full_name: String = ""

    @JsonField(name = ["supplier_orders"])
    var supplier_orders = ArrayList<DataSupplierOrder>()

    @JsonField(name = ["supplier_warehouse_session_data"])
    var supplier_warehouse_session_data = ArrayList<WareHouse>()

}