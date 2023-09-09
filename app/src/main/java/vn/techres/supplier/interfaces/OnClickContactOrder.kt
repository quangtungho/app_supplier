package vn.techres.supplier.interfaces

import vn.techres.supplier.model.datamodel.DataMemberOrder


interface OnClickContactOrder {
    fun onClickCall(data: DataMemberOrder, restaurantID: Int)
    fun onClickCallVideo(data: DataMemberOrder, restaurantID: Int)
}