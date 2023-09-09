package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataListEmployee

class EmployeeProfileResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataListEmployee()
}