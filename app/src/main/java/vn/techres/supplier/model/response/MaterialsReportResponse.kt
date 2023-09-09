package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataMaterialsReport

class MaterialsReportResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data = DataMaterialsReport()
}
