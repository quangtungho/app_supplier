package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataGetVersion

class VersionResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataGetVersion()
}