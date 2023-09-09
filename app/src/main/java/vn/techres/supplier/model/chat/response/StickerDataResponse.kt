package vn.techres.supplier.model.chat.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.chat.data.StickerData
import vn.techres.supplier.model.response.BaseResponse

class StickerDataResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data: StickerData = StickerData()
}