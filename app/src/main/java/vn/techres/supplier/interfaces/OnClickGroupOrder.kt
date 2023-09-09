package vn.techres.supplier.interfaces

import vn.techres.supplier.model.datamodel.DataListOrder


interface OnClickGroupOrder {
    fun onClick(position: Int, data: DataListOrder)
    fun onDetailOrder(position: Int,id :Int ,img:String)
}