package vn.techres.supplier.interfaces

import vn.techres.supplier.model.datamodel.DataListEmployee

interface ClickIntentProfile {
    fun onClick(position: Int, id: Int)
    fun onCheck(position: Int, employee : DataListEmployee)

}