package vn.techres.supplier.interfaces

interface OnClickBillOrder {
    fun onClickViewWait(position: Int, id: Int, img: String)
    fun onClickViewDelivery(position: Int, id: Int, img: String)
    fun onClickViewReturn(position: Int, id: Int, img: String)
    fun onClickViewCanceled(position: Int, id: Int, img: String)
    fun onClickViewCompleted(position: Int, id: Int, img: String)

}
