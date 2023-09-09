package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataRoles

open class RolesExchangeResponse: BaseResponse()  {
    ///GETTER
    ///SETTER
    @JsonField(name = ["data"])
    var data: ArrayList<DataRoles>? = null
}
