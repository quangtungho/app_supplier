package vn.techres.supplier.interfaces

import vn.techres.supplier.model.datamodel.DataUnitSpecification


interface OnClickTransIdAndModelUnits {
    fun onClick(position: Int, id: Int, model: ArrayList<DataUnitSpecification>)
}