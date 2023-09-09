
package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataOrderInDay

class OverViewOrderInDayResponse: BaseResponse() {
    @JsonField(name = ["data"])
    var data =  DataOrderInDay()
}