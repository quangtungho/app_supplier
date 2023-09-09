package vn.techres.supplier.model.chat.data

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import vn.techres.supplier.model.datamodel.DataMemberOrder

@JsonObject
class GetInfoGroup {
    @JsonField(name = ["group_id_tms_supplier"])
    var group_id_tms_supplier = ""

    @JsonField(name = ["group_id_order"])
    var group_id_order = ""

    @JsonField(name = ["list_member"])
    var list_member = ArrayList<DataMemberOrder>()
}