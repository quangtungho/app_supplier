package vn.techres.supplier.model.entity

class Base {
    interface Factory<T> {
        fun create(): T
    }

    companion object : Factory<Base> {
        override fun create(): Base = Base()
    }
}