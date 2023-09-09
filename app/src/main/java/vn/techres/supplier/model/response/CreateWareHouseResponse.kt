
package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField

class CreateWareHouseResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data = null
}