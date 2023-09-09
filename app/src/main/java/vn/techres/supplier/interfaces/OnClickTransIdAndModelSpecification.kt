package vn.techres.supplier.interfaces

import vn.techres.supplier.model.datamodel.DataSpecification
import java.util.*
interface OnClickTransIdAndModelSpecification {
    fun onClick(position: Int, id: Int, model: DataSpecification)
}