package vn.techres.supplier.interfaces

import vn.techres.supplier.model.datamodel.DataListEmployee

interface RoleClick {
    fun onItemTag(user: DataListEmployee?, position: Int, isTag: Boolean)
    fun onItemTag(id: Int, position: Int, isTag: Boolean)
}