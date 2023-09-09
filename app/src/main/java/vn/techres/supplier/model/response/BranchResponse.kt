package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.Branch
import vn.techres.supplier.model.datamodel.DataChangeEmployee

class BranchResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = ArrayList<Branch>()
}