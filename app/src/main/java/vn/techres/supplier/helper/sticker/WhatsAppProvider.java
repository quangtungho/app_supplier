package vn.techres.supplier.helper.sticker;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.aghajari.emojiview.sticker.Sticker;
import com.aghajari.emojiview.sticker.StickerCategory;
import com.aghajari.emojiview.sticker.StickerLoader;
import com.aghajari.emojiview.sticker.StickerProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import vn.techres.supplier.helper.AppUtils;
import vn.techres.supplier.model.chat.data.CategorySticker;

public class WhatsAppProvider implements StickerProvider {

    List<CategorySticker> data;

    public WhatsAppProvider(List<CategorySticker> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public StickerCategory[] getCategories() {
        StickerCategory[] category = new StickerCategory[data.size()];
        for (int i = 0; i < data.size(); i++) {
            category[i] = new WhatsAppStickers(data.get(i));
        }
        return category;
    }

    @NonNull
    @Override
    public StickerLoader getLoader() {
        return new StickerLoader() {
            @Override
            public void onLoadSticker(View view, Sticker<String> sticker) {
                if (sticker.isInstance(String.class)) {
                    Glide.with(view).load(AppUtils.getLinkPhoto(sticker.getData())).apply(RequestOptions.fitCenterTransform()).into((AppCompatImageView) view);
                }

            }

            @Override
            public void onLoadStickerCategory(View view, StickerCategory stickerCategory, boolean selected) {
                try {
                    Glide.with(view).load(AppUtils.getLinkPhoto((String) stickerCategory.getCategoryData())).apply(RequestOptions.fitCenterTransform()).into((AppCompatImageView) view);
                } catch (Exception ignore) {
                }
            }
        };
    }

    @Override
    public boolean isRecentEnabled() {
        return true;
    }
}
