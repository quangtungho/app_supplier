package vn.techres.supplier.helper.sticker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.DrawableCompat;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.listener.OnStickerActions;
import com.aghajari.emojiview.sticker.Sticker;
import com.aghajari.emojiview.utils.Utils;
import com.aghajari.emojiview.view.AXEmojiLayout;
import com.aghajari.emojiview.view.AXEmojiPager;
import com.aghajari.emojiview.view.AXEmojiView;
import com.aghajari.emojiview.view.AXSingleEmojiView;
import com.aghajari.emojiview.view.AXStickerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import vn.techres.supplier.R;
import vn.techres.supplier.model.eventbussms.EventBusStickerClick;
import vn.techres.supplier.model.chat.data.CategorySticker;

public class UI {

    public static boolean mEmojiView;
    public static boolean mSingleEmojiView;
    public static boolean mStickerView;
    public static boolean mCustomView;
    public static boolean mFooterView;
    public static boolean mCustomFooter;
    public static boolean mWhiteCategory;
    public static List<CategorySticker> categoryStickers = new ArrayList<>();
    public static boolean darkMode;

    public static void loadTheme() {
        // release theme
        darkMode = false;
        AXEmojiManager.resetTheme();

        // set EmojiView Theme
        AXEmojiManager.getEmojiViewTheme().setFooterEnabled(mFooterView && !mCustomFooter);
        AXEmojiManager.getEmojiViewTheme().setSelectionColor(0xffFF4081);
        AXEmojiManager.getEmojiViewTheme().setFooterSelectedItemColor(0xffFF4081);
        AXEmojiManager.getStickerViewTheme().setSelectionColor(0xffFF4081);

        if (mWhiteCategory) {
            AXEmojiManager.getEmojiViewTheme().setSelectionColor(Color.TRANSPARENT);
            AXEmojiManager.getEmojiViewTheme().setSelectedColor(0xffFF4081);
            AXEmojiManager.getEmojiViewTheme().setCategoryColor(Color.WHITE);
            AXEmojiManager.getEmojiViewTheme().setFooterBackgroundColor(Color.WHITE);
            AXEmojiManager.getEmojiViewTheme().setAlwaysShowDivider(true);

            AXEmojiManager.getStickerViewTheme().setSelectedColor(0xffFF4081);
            AXEmojiManager.getStickerViewTheme().setCategoryColor(Color.WHITE);
            AXEmojiManager.getStickerViewTheme().setAlwaysShowDivider(true);
        }
        AXEmojiManager.setBackspaceCategoryEnabled(!mCustomFooter);
    }

    public static void loadDarkTheme() {
        // release theme
        darkMode = true;
        AXEmojiManager.resetTheme();

        // set EmojiView Theme
        AXEmojiManager.getEmojiViewTheme().setFooterEnabled(mFooterView && !mCustomFooter);
        AXEmojiManager.getEmojiViewTheme().setSelectionColor(0xff82ADD9);
        AXEmojiManager.getEmojiViewTheme().setSelectedColor(0xff82ADD9);
        AXEmojiManager.getEmojiViewTheme().setFooterSelectedItemColor(0xff82ADD9);
        AXEmojiManager.getEmojiViewTheme().setBackgroundColor(0xFF1E2632);
        AXEmojiManager.getEmojiViewTheme().setCategoryColor(0xFF1E2632);
        AXEmojiManager.getEmojiViewTheme().setFooterBackgroundColor(0xFF1E2632);
        AXEmojiManager.getEmojiViewTheme().setVariantPopupBackgroundColor(0xFF232D3A);
        AXEmojiManager.getEmojiViewTheme().setVariantDividerEnabled(false);
        AXEmojiManager.getEmojiViewTheme().setDividerColor(0xFF1B242D);
        AXEmojiManager.getEmojiViewTheme().setDefaultColor(0xFF677382);
        AXEmojiManager.getEmojiViewTheme().setTitleColor(0xFF677382);

        AXEmojiManager.getStickerViewTheme().setSelectionColor(0xff82ADD9);
        AXEmojiManager.getStickerViewTheme().setSelectedColor(0xff82ADD9);
        AXEmojiManager.getStickerViewTheme().setBackgroundColor(0xFF1E2632);
        AXEmojiManager.getStickerViewTheme().setCategoryColor(0xFF1E2632);
        AXEmojiManager.getStickerViewTheme().setDividerColor(0xFF1B242D);
        AXEmojiManager.getStickerViewTheme().setDefaultColor(0xFF677382);

        if (mWhiteCategory) {
            AXEmojiManager.getEmojiViewTheme().setSelectionColor(Color.TRANSPARENT);
            AXEmojiManager.getEmojiViewTheme().setCategoryColor(0xFF232D3A);
            AXEmojiManager.getEmojiViewTheme().setFooterBackgroundColor(0xFF232D3A);
            AXEmojiManager.getEmojiViewTheme().setAlwaysShowDivider(true);

            AXEmojiManager.getStickerViewTheme().setCategoryColor(0xFF232D3A);
            AXEmojiManager.getStickerViewTheme().setAlwaysShowDivider(true);
        }
        AXEmojiManager.setBackspaceCategoryEnabled(!mCustomFooter);
    }

    public static void setCategoryStickers(List<CategorySticker> categoryStickers) {
        UI.categoryStickers = categoryStickers;
    }

    public static AXEmojiPager loadView(final Context context, EditText editText) {
        AXEmojiPager emojiPager = new AXEmojiPager(context);

        if (mSingleEmojiView) {
            /**
             * add single emoji view
             */
            AXSingleEmojiView singleEmojiView = new AXSingleEmojiView(context);
            emojiPager.addPage(singleEmojiView, R.drawable.ic_msg_panel_smiles);
        }

        if (mEmojiView) {
            /**
             * add emoji view (with viewpager)
             */
            AXEmojiView emojiView = new AXEmojiView(context);
            emojiPager.addPage(emojiView, R.drawable.ic_msg_panel_smiles);
        }

        if (mStickerView) {
            /**
             * add Sticker View
             */
            AXStickerView stickerView = new AXStickerView(context, "stickers", new WhatsAppProvider(categoryStickers));
            emojiPager.addPage(stickerView, R.drawable.ic_msg_panel_stickers);

            //add sticker click listener
            stickerView.setOnStickerActionsListener(new OnStickerActions() {
                @Override
                public void onClick(View view, Sticker sticker, boolean fromRecent) {
                    EventBus.getDefault().post(new EventBusStickerClick(sticker.toString()));
                }

                @Override
                public boolean onLongClick(View view, Sticker sticker, boolean fromRecent) {
                    return false;
                }
            });
        }

//        if (mCustomView){
//            emojiPager.addPage(new LoadingView(context), R.drawable.msg_round_load_m);
//        }

        // set target emoji edit text to emojiViewPager
        emojiPager.setEditText(editText);

        emojiPager.setSwipeWithFingerEnabled(true);

        if (mCustomFooter) {
            initCustomFooter(context, emojiPager);
        } else {
            emojiPager.setLeftIcon(R.drawable.ic_ab_search);
            emojiPager.setOnFooterItemClicked((view, leftIcon) -> {
                if (leftIcon)
                    Toast.makeText(context, "Search Clicked", Toast.LENGTH_SHORT).show();
            });
        }

        return emojiPager;
    }



    public static AXEmojiPager loadView(final Context context) {
        AXEmojiPager emojiPager = new AXEmojiPager(context);

        if (mSingleEmojiView) {
            /**
             * add single emoji view
             */
            AXSingleEmojiView singleEmojiView = new AXSingleEmojiView(context);
            emojiPager.addPage(singleEmojiView, R.drawable.ic_msg_panel_smiles);
        }

        if (mEmojiView) {
            /**
             * add emoji view (with viewpager)
             */
            AXEmojiView emojiView = new AXEmojiView(context);
            emojiPager.addPage(emojiView, R.drawable.ic_msg_panel_smiles);
        }

        if (mStickerView) {
            /**
             * add Sticker View
             */
            AXStickerView stickerView = new AXStickerView(context, "stickers", new WhatsAppProvider(categoryStickers));
            emojiPager.addPage(stickerView, R.drawable.ic_msg_panel_stickers);

            //add sticker click listener
            stickerView.setOnStickerActionsListener(new OnStickerActions() {
                @Override
                public void onClick(View view, Sticker sticker, boolean fromRecent) {
                    EventBus.getDefault().post(new EventBusStickerClick(sticker.toString()));
                }

                @Override
                public boolean onLongClick(View view, Sticker sticker, boolean fromRecent) {
                    return false;
                }
            });
        }
        emojiPager.setSwipeWithFingerEnabled(true);

        if (mCustomFooter) {
            initCustomFooter(context, emojiPager);
        } else {
            emojiPager.setLeftIcon(R.drawable.ic_ab_search);
            emojiPager.setOnFooterItemClicked((view, leftIcon) -> {
            });
        }

        return emojiPager;
    }

    @SuppressLint({"RtlHardcoded", "UseCompatLoadingForDrawables", "ClickableViewAccessibility"})
    private static void initCustomFooter(final Context context, AXEmojiPager emojiPager) {
        final FrameLayout footer = new FrameLayout(context);
        Drawable drawable = context.getResources().getDrawable(R.drawable.circle_bg);
        if (darkMode) DrawableCompat.setTint(DrawableCompat.wrap(drawable), 0xFF1B242D);
        footer.setBackground(drawable);
        footer.setElevation(Utils.dp(context, 4));

        AXEmojiLayout.LayoutParams lp = new AXEmojiLayout.LayoutParams(Utils.dp(context, 48), Utils.dp(context, 48));
        lp.rightMargin = Utils.dp(context, 12);
        lp.bottomMargin = Utils.dp(context, 12);
        lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        footer.setLayoutParams(lp);
        emojiPager.setCustomFooter(footer, true);

        final AppCompatImageView img = new AppCompatImageView(context);
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(Utils.dp(context, 22), Utils.dp(context, 22));
        lp2.gravity = Gravity.CENTER;
        footer.addView(img, lp2);

        final View.OnClickListener clickListener = view -> Toast.makeText(context, "Search Clicked", Toast.LENGTH_SHORT).show();

        emojiPager.setOnEmojiPageChangedListener((emojiPager1, base, position) -> {
            Drawable drawable1;
            if (AXEmojiManager.isAXEmojiView(base)) {
                drawable1 = context.getResources().getDrawable(R.drawable.emoji_backspace, null);
                Utils.enableBackspaceTouch(footer, emojiPager1.getEditText());
                footer.setOnClickListener(null);
            } else {
                drawable1 = context.getResources().getDrawable(R.drawable.ic_ab_search, null);
                footer.setOnTouchListener(null);
                footer.setOnClickListener(clickListener);
            }
            DrawableCompat.setTint(DrawableCompat.wrap(drawable1), AXEmojiManager.getEmojiViewTheme().getFooterItemColor());
            img.setImageDrawable(drawable1);
        });
    }
}
