package vn.techres.supplier.model.eventbussms

class EventBusStickerClick {
    var sticker = ""
    var sticker_id = ""

    constructor(sticker: String) {
        this.sticker = sticker
    }

    
    constructor(sticker: String, sticker_id: String) {
        this.sticker = sticker
        this.sticker_id = sticker_id
    }
}