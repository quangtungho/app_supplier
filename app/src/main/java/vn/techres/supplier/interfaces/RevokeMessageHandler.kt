package vn.techres.supplier.interfaces

import android.view.View
import vn.techres.supplier.model.chat.data.MessagesByGroup
import vn.techres.supplier.model.chat.data.ReactionItem

interface RevokeMessageHandler {
    fun onRevoke(messagesByGroup: MessagesByGroup?, view: View?)
    fun onRevokeClick(messagesByGroup: MessagesByGroup?, view: View?, view1: View?)
    fun onRevokeEmoji(
        messagesByGroup: MessagesByGroup?,
        view: View?,
        reactionItems: List<ReactionItem?>?,
        y: Int
    )
}