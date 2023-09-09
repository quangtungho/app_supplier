package vn.techres.supplier.model.entity
import vn.techres.supplier.model.chat.data.ReactionItem

class NumberComparator : Comparator<ReactionItem> {
    override fun compare(o1: ReactionItem, o2: ReactionItem): Int {
        return (o1.number).compareTo(o2.number)
    }
}