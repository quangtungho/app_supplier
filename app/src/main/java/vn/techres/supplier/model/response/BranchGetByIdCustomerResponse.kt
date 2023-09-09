package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataBranch

class BranchGetByIdCustomerResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data: ArrayList<DataBranch>? = null
}
