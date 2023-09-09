package vn.techres.supplier.helper.sticker;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.aghajari.emojiview.sticker.Sticker;
import com.aghajari.emojiview.sticker.StickerCategory;

import vn.techres.supplier.model.chat.data.CategorySticker;

public class WhatsAppStickers implements StickerCategory<String> {

    CategorySticker categorySticker;

    public WhatsAppStickers(CategorySticker categorySticker) {
        this.categorySticker = categorySticker;
    }

    @NonNull
    @Override
    public Sticker[] getStickers() {
        Sticker[] stickers = new Sticker[categorySticker.getStickers().size()];
        for (int i = 0; i < categorySticker.getStickers().size(); i++) {
            stickers[i] = new Sticker<>(categorySticker.getStickers().get(i).getLink_original());
        }
        return stickers;
    }

    @Override
    public String getCategoryData() {
        return categorySticker.getLink_original();
    }

    @Override
    public boolean useCustomView() {
        return false;
    }

    @Override
    public View getView(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void bindView(View view) {
    }

    @Override
    public View getEmptyView(ViewGroup viewGroup) {
        return null;
    }
}
