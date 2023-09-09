package vn.techres.supplier.interfaces

import vn.techres.supplier.model.datamodel.DataListEmployee
import vn.techres.supplier.model.datamodel.DataListOrder

interface PaymentClick {
    fun onItemTag(user: DataListOrder?, position: Int, isTag: Boolean)
    fun onItemTag(id: Int, position: Int, isTag: Boolean)
    fun onUpdate(data: ArrayList<DataListOrder>)
}