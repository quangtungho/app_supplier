package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.FileNameImage

class GetLinkImageResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data: FileNameImage? = null
}