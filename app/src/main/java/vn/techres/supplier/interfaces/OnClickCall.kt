package vn.techres.supplier.interfaces

import vn.techres.supplier.model.chat.data.Members


interface OnClickCall {
    fun onClickCall(data: Members)
    fun onClickCallVideo(data: Members)
}