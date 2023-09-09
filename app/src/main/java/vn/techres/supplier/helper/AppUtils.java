package vn.techres.supplier.helper;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.listener.SimplePopupAdapter;
import com.aghajari.emojiview.search.AXEmojiSearchView;
import com.aghajari.emojiview.view.AXEmojiPager;
import com.aghajari.emojiview.view.AXEmojiPopupLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureSelectorUIStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.SdkVersionUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.StringTokenizer;

import timber.log.Timber;
import vn.techres.supplier.R;
import vn.techres.supplier.activity.MediaActivity;
import vn.techres.supplier.activity.SupplierApplication;
import vn.techres.supplier.adapter.BranchSpinnerAdapter;
import vn.techres.supplier.adapter.RestaurantSpinnerAdapter;
import vn.techres.supplier.helper.sticker.UI;
import vn.techres.supplier.listener.GlideEngine;
import vn.techres.supplier.model.datamodel.Branch;
import vn.techres.supplier.model.datamodel.Customer;
import vn.techres.supplier.model.entity.CurrentConfigJava;
import vn.techres.supplier.model.chat.data.CategorySticker;
import vn.techres.supplier.model.chat.data.FileChat;
import vn.techres.supplier.model.chat.data.StickerData;
import vn.techres.supplier.model.datamodel.DataListOrderDetail;

public class
AppUtils {
    public static boolean on_off_youtube = true;
    private static boolean isShowing = false;
    static public Toast mToast;

    public static void showImageMediaLocalSlider(List<String> photo, int position) {
        String json = new Gson().toJson(photo);
        Intent mediaIntent = new Intent(SupplierApplication.context, MediaActivity.class);
        mediaIntent.putExtra(TechresEnum.DATA_MEDIA.toString(), json);
        mediaIntent.putExtra(TechresEnum.POSITION_MEDIA.toString(), String.valueOf(position));
        SupplierApplication.context.startActivity(mediaIntent);
    }

    public static Drawable getDrawablebyResource(Context context, int rID) {
        return getResources(context).getDrawable(rID);
    }

    public static Resources getResources(Context context) {
        return context.getResources();
    }
    public static String getVideo(String url) {
        String link = "";
        int a;
        int b;
        if (url.contains("youtu.be/")) {
            a = url.indexOf(".be/");
            link = url.substring(a + 4);
        } else if (url.contains("youtube.com/watch?v=")) {
            a = url.indexOf("?v=");
            if (url.contains("&")) {
                b = url.indexOf("&");
                link = url.substring(a + 3, b);
            } else
                link = url.substring(a + 3);
        } else if (url.contains("youtube.com/shorts/")) {
            a = url.indexOf("?");
            b = url.indexOf("shorts/");
            link = url.substring(b + 7, a);
        }
        Timber.d("load link id : ");
        Timber.d(link);
        return link;
    }

    @SuppressLint("ShowToast")
    public static void makeText(String string) {
        if (mToast == null) {
            mToast = Toast.makeText(SupplierApplication.context, string, Toast.LENGTH_SHORT);
        }
        mToast.setText(string);
        mToast.show();
    }

    public static void configRecyclerView(final RecyclerView recyclerView
            , RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Objects.requireNonNull(recyclerView.getItemAnimator()).setChangeDuration(0);
        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        recyclerView.setNestedScrollingEnabled(false);
    }
    public static void getBranchSpinner(Spinner spinner, List<Branch> list) {
            BranchSpinnerAdapter branchAdapter = new BranchSpinnerAdapter(SupplierApplication.context, R.layout.custom_spinner, list);
        spinner.setAdapter(branchAdapter);
    }
    public static void getRestaurantSpinner(Spinner spinner, List<Customer> list) {
        RestaurantSpinnerAdapter restaurantSpinnerAdapter = new RestaurantSpinnerAdapter(SupplierApplication.context, R.layout.custom_spinner, list);
        spinner.setAdapter(restaurantSpinnerAdapter);
    }
    public static void callPhoto(String url, ImageView view) {
        Glide.with(SupplierApplication.context)
                .load(getLinkPhoto(url))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(new RequestOptions().placeholder(R.drawable.picture_image_placeholder).error(R.drawable.picture_image_placeholder))
                .into(view);
    }
    public static void initRecyclerView(RecyclerView view, RecyclerView.Adapter adapter) {
        AppUtils.configRecyclerView(view, new PreCachingLayoutManager(SupplierApplication.context, RecyclerView.VERTICAL, false));
        view.setAdapter(adapter);
    }

    public static void callPhotoAvatar(String url, ImageView view) {
        Glide.with(SupplierApplication.context)
                .asBitmap()
                .load(getLinkPhoto(url))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .apply(new RequestOptions().placeholder(R.drawable.decor_avatar_stranger).error(R.drawable.decor_avatar_stranger))
                .into(view);

    }

    public static void callGroupAvatar(String url, ImageView view) {
        Glide.with(SupplierApplication.context)
                .asBitmap()
                .load(getLinkPhoto(url))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .apply(new RequestOptions().placeholder(R.drawable.avatar_groupchat).error(R.drawable.avatar_groupchat))
                .into(view);

    }

    public static void callPhotoLocal(String url, ImageView view) {
        Glide.with(SupplierApplication.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .apply(new RequestOptions().placeholder(R.drawable.picture_image_placeholder).error(R.drawable.picture_image_placeholder))
                .into(view);
    }

    public static Bitmap resizeBitmap(Bitmap source, int maxLength) {
        try {
            if (source.getHeight() >= source.getWidth()) {
                int targetHeight = maxLength;
                if (source.getHeight() > source.getWidth()) {
                    targetHeight = maxLength + 200;
                }
                if (source.getHeight() <= targetHeight) { // if image already smaller than the required height

                    return source;
                }

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (targetHeight * aspectRatio) + 50;

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                }
                return result;
            } else {
                int targetWidth = maxLength;

                if (source.getWidth() <= targetWidth) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = ((double) source.getHeight()) / ((double) source.getWidth());
                int targetHeight = (int) (targetWidth * aspectRatio) + 20;

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                }
                return result;

            }
        } catch (Exception e) {
            return source;
        }
    }

    public static String getDecimalFormattedString(String value) {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        StringBuilder str3 = new StringBuilder();
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt(-1 + str1.length()) == '.') {
            j--;
            str3 = new StringBuilder(".");
        }
        for (int k = j; ; k--) {
            if (k < 0) {
                if (str2.length() > 0)
                    str3.append(".").append(str2);
                return str3.toString();
            }
            if (i == 3) {
                str3.insert(0, ",");
                i = 0;
            }
            str3.insert(0, str1.charAt(k));
            i++;
        }

    }

    public static void showImageMediaSlider(String photo, int position) {
        String json = new Gson().toJson(getListMedia(photo));
        Intent mediaIntent = new Intent(SupplierApplication.context, MediaActivity.class);
        mediaIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mediaIntent.putExtra(TechresEnum.DATA_MEDIA.toString(), json);
        mediaIntent.putExtra(TechresEnum.POSITION_MEDIA.toString(), String.valueOf(position));
        SupplierApplication.context.startActivity(mediaIntent);
    }

    public static void showImageMediaSlider(List<String> photo, int position) {
        String json = new Gson().toJson(getListMedia(photo));
        Intent mediaIntent = new Intent(SupplierApplication.context, MediaActivity.class);
        mediaIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mediaIntent.putExtra(TechresEnum.DATA_MEDIA.toString(), json);
        mediaIntent.putExtra(TechresEnum.POSITION_MEDIA.toString(), String.valueOf(position));
        SupplierApplication.context.startActivity(mediaIntent);
    }

    public static List<String> getListMedia(String photo) {
        List<String> arrayList = new ArrayList<>();
        arrayList.add(getLinkPhoto(photo));
        return arrayList;
    }

    public static List<String> getListMedia(List<String> photo) {
        List<String> arrayList = new ArrayList<>();
        for (int i = 0; i < photo.size(); i++) {
            arrayList.add(getLinkPhoto(photo.get(i)));
        }
        return arrayList;
    }

    public static String getLinkPhoto(String photo) {
        return String.format("%s%s", CurrentConfigJava.INSTANCE.getConfigJava(SupplierApplication.Companion.applicationContext()).getAds_domain(), photo);
    }

    public static void showImageMediaSliderFileChat(List<FileChat> photo, int position) {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < photo.size(); i++) {
            stringList.add(photo.get(i).getLink_original());
        }
        String json = new Gson().toJson(getListMedia(stringList));
        Intent mediaIntent = new Intent(SupplierApplication.context, MediaActivity.class);
        mediaIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mediaIntent.putExtra(TechresEnum.DATA_MEDIA.toString(), json);
        mediaIntent.putExtra(TechresEnum.POSITION_MEDIA.toString(), String.valueOf(position));
        SupplierApplication.context.startActivity(mediaIntent);
    }

    public static String getMimeType(String url) {
        try {
            return url.substring(url.lastIndexOf(".") + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public static void downloadFile(Activity activity, String file) {
        DownloadManager downloadmanager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(getLinkPhoto(file));
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(getNameFileToPath(getLinkPhoto(file)));
        request.setDescription("Tải về");
        request.setDestinationInExternalPublicDir(String.valueOf(Environment.DIRECTORY_DOWNLOADS), getNameFileToPath(getLinkPhoto(file)));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadmanager.enqueue(request);
        ;
        Toast.makeText(activity, activity.getString(R.string.dow_is), Toast.LENGTH_LONG).show();
    }
    public static String trimCommaOfString(String string) {
        if (string.contains(",")) {
            return string.replace(",", "");
        } else {
            return string;
        }
    }
    public static void downloadFile(Context activity, String file) {
        DownloadManager downloadmanager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(getLinkPhoto(file));
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(getNameFileToPath(getLinkPhoto(file)));
        request.setDescription("Tải về");
        request.setDestinationInExternalPublicDir(String.valueOf(Environment.DIRECTORY_DOWNLOADS), getNameFileToPath(getLinkPhoto(file)));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadmanager.enqueue(request);
        Toast.makeText(activity, activity.getString(R.string.dow_is), Toast.LENGTH_LONG).show();
    }


    public static String getNameFileToPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1).trim();

    }

    public static void emitSocket(String key, Object object) {
        try {
            Gson gson = new Gson();
            Log.e(key, gson.toJson(object));
            SupplierApplication.Companion.getSocketInstance().emit(key, new JSONObject(gson.toJson(object)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void emitSocket(String key, JSONObject object) {
        Log.e(key, String.valueOf(object));
        SupplierApplication.Companion.getSocketInstance().emit(key, object);
    }

    public static void setupEmojiSticker(AppCompatDialog activity, EditText view, ImageView imageViewEmoji, AXEmojiPopupLayout emojiPopupLayout) {
        AXEmojiPager emojiPager = UI.loadView(activity.getContext(), view);
        // create emoji popup
        emojiPopupLayout.initPopupView(emojiPager);

        emojiPopupLayout.setPopupAnimationEnabled(true);
        emojiPopupLayout.setPopupAnimationDuration(250);

        emojiPopupLayout.setSearchViewAnimationEnabled(true);
        emojiPopupLayout.setSearchViewAnimationDuration(250);

        // SearchView
        if (AXEmojiManager.isAXEmojiView(emojiPager.getPage(0))) {
            emojiPopupLayout.setSearchView(new AXEmojiSearchView(activity.getContext(), emojiPager.getPage(0)));
            emojiPager.setOnFooterItemClicked((view1, leftIcon) -> {
                if (leftIcon) emojiPopupLayout.showSearchView();
            });
        }

        emojiPopupLayout.hideAndOpenKeyboard();
        view.setOnClickListener(view1 -> emojiPopupLayout.openKeyboard());

        imageViewEmoji.setOnClickListener(view1 -> {
            if (isShowing) {
                emojiPopupLayout.openKeyboard();
            } else {
                emojiPopupLayout.show();
            }
        });


        emojiPopupLayout.setPopupListener(new SimplePopupAdapter() {
            @Override
            public void onShow() {
                updateButton(true);
            }

            @Override
            public void onDismiss() {
                updateButton(false);
            }

            @Override
            public void onKeyboardOpened(int height) {
                updateButton(false);
            }

            @Override
            public void onKeyboardClosed() {
                updateButton(emojiPopupLayout.isShowing());
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            private void updateButton(boolean emoji) {
                if (isShowing == emoji) return;
                isShowing = emoji;
                if (emoji) {
                    imageViewEmoji.setImageDrawable(imageViewEmoji.getContext().getDrawable(R.drawable.ic_keyboard_chat_message));
                } else {
                    imageViewEmoji.setImageDrawable(imageViewEmoji.getContext().getDrawable(R.drawable.ic_emoji_input));
                }
            }
        });
    }

    public static void setupEmojiSticker(Activity activity, ImageView imageViewEmoji, AXEmojiPopupLayout emojiPopupLayout) {
        AXEmojiPager emojiPager = UI.loadView(activity);
        // create emoji popup
        emojiPopupLayout.initPopupView(emojiPager);

        emojiPopupLayout.setPopupAnimationEnabled(true);
        emojiPopupLayout.setPopupAnimationDuration(250);

        emojiPopupLayout.setSearchViewAnimationEnabled(true);
        emojiPopupLayout.setSearchViewAnimationDuration(250);

        // SearchView
        if (AXEmojiManager.isAXEmojiView(emojiPager.getPage(0))) {
            emojiPopupLayout.setSearchView(new AXEmojiSearchView(activity, emojiPager.getPage(0)));
            emojiPager.setOnFooterItemClicked((view1, leftIcon) -> {
                if (leftIcon) emojiPopupLayout.showSearchView();
            });
        }

        emojiPopupLayout.hideAndOpenKeyboard();

        //view.setOnClickListener(view1 -> emojiPopupLayout.openKeyboard());

        imageViewEmoji.setOnClickListener(view1 -> {
            if (isShowing) {
                emojiPopupLayout.openKeyboard();
            } else {
                emojiPopupLayout.show();
            }
        });


        emojiPopupLayout.setPopupListener(new SimplePopupAdapter() {
            @Override
            public void onShow() {
                updateButton(true);
            }

            @Override
            public void onDismiss() {
                updateButton(false);
            }

            @Override
            public void onKeyboardOpened(int height) {
                updateButton(false);
            }

            @Override
            public void onKeyboardClosed() {
                updateButton(emojiPopupLayout.isShowing());
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            private void updateButton(boolean emoji) {
                if (isShowing == emoji) return;
                isShowing = emoji;
                if (emoji) {
                    imageViewEmoji.setImageDrawable(imageViewEmoji.getContext().getDrawable(R.drawable.ic_keyboard_chat_message));
                } else {
                    imageViewEmoji.setImageDrawable(imageViewEmoji.getContext().getDrawable(R.drawable.ic_emoji_input));
                }
            }
        });
    }

    public static void setupEmojiSticker(AppCompatDialogFragment activity, ImageView imageViewEmoji, AXEmojiPopupLayout emojiPopupLayout) {
        AXEmojiPager emojiPager = UI.loadView(activity.getContext());
        // create emoji popup
        emojiPopupLayout.initPopupView(emojiPager);

        emojiPopupLayout.setPopupAnimationEnabled(true);
        emojiPopupLayout.setPopupAnimationDuration(250);

        emojiPopupLayout.setSearchViewAnimationEnabled(true);
        emojiPopupLayout.setSearchViewAnimationDuration(250);

        // SearchView
        if (AXEmojiManager.isAXEmojiView(emojiPager.getPage(0))) {
            emojiPopupLayout.setSearchView(new AXEmojiSearchView(activity.getContext(), emojiPager.getPage(0)));
            emojiPager.setOnFooterItemClicked((view1, leftIcon) -> {
                if (leftIcon) emojiPopupLayout.showSearchView();
            });
        }

        emojiPopupLayout.hideAndOpenKeyboard();

        //view.setOnClickListener(view1 -> emojiPopupLayout.openKeyboard());

        imageViewEmoji.setOnClickListener(view1 -> {
            if (isShowing) {
                emojiPopupLayout.openKeyboard();
            } else {
                emojiPopupLayout.show();
            }
        });


        emojiPopupLayout.setPopupListener(new SimplePopupAdapter() {
            @Override
            public void onShow() {
                updateButton(true);
            }

            @Override
            public void onDismiss() {
                updateButton(false);
            }

            @Override
            public void onKeyboardOpened(int height) {
                updateButton(false);
            }

            @Override
            public void onKeyboardClosed() {
                updateButton(emojiPopupLayout.isShowing());
            }

            private void updateButton(boolean emoji) {
                if (isShowing == emoji) return;
                isShowing = emoji;
                if (emoji) {
                    imageViewEmoji.setImageDrawable(imageViewEmoji.getContext().getDrawable(R.drawable.ic_keyboard_chat_message));
                } else {
                    imageViewEmoji.setImageDrawable(imageViewEmoji.getContext().getDrawable(R.drawable.ic_emoji_input));
                }
            }
        });
    }

    public static void setupEmojiSticker(Activity activity, EditText view, ImageView imageViewEmoji, AXEmojiPopupLayout emojiPopupLayout, RelativeLayout extention) {
        AXEmojiPager emojiPager = UI.loadView(activity, view);
        // create emoji popup
        emojiPopupLayout.initPopupView(emojiPager);

        emojiPopupLayout.setPopupAnimationEnabled(true);
        emojiPopupLayout.setPopupAnimationDuration(250);

        emojiPopupLayout.setSearchViewAnimationEnabled(true);
        emojiPopupLayout.setSearchViewAnimationDuration(250);

        // SearchView
        if (AXEmojiManager.isAXEmojiView(emojiPager.getPage(0))) {
            emojiPopupLayout.setSearchView(new AXEmojiSearchView(activity, emojiPager.getPage(0)));
            emojiPager.setOnFooterItemClicked((view1, leftIcon) -> {
                if (leftIcon) emojiPopupLayout.showSearchView();
            });
        }

        emojiPopupLayout.hideAndOpenKeyboard();
        view.setOnClickListener(view1 -> {
            if (emojiPopupLayout.getVisibility() == View.GONE) {
                emojiPopupLayout.setVisibility(View.VISIBLE);
            }
            emojiPopupLayout.openKeyboard();
            if (extention.getVisibility() == View.VISIBLE) {
                extention.setVisibility(View.GONE);
            }
        });

        imageViewEmoji.setOnClickListener(view1 -> {
            if (extention.getVisibility() == View.VISIBLE) {
                extention.setVisibility(View.GONE);
            }
            if (emojiPopupLayout.getVisibility() == View.GONE) {
                emojiPopupLayout.setVisibility(View.VISIBLE);
            }
            if (isShowing) {
                emojiPopupLayout.openKeyboard();
            } else {
                emojiPopupLayout.show();
            }
        });


        emojiPopupLayout.setPopupListener(new SimplePopupAdapter() {
            @Override
            public void onShow() {
                updateButton(true);
            }

            @Override
            public void onDismiss() {
                updateButton(false);
            }

            @Override
            public void onKeyboardOpened(int height) {
                updateButton(false);
            }

            @Override
            public void onKeyboardClosed() {
                updateButton(emojiPopupLayout.isShowing());
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            private void updateButton(boolean emoji) {
                if (isShowing == emoji) return;
                isShowing = emoji;
                if (emoji) {
                    imageViewEmoji.setImageDrawable(imageViewEmoji.getContext().getDrawable(R.drawable.ic_keyboard_chat_message));
                } else {
                    imageViewEmoji.setImageDrawable(imageViewEmoji.getContext().getDrawable(R.drawable.ic_emoji_input));
                }
            }
        });
    }

    private static final PictureSelectorUIStyle mSelectorUIStyle = PictureSelectorUIStyle.ofSelectNumberStyle();
    private static final PictureWindowAnimationStyle mWindowAnimationStyle = PictureWindowAnimationStyle.ofDefaultWindowAnimationStyle();


    public static void showImagePickerChat(Activity activity, int chooseMode, int selectionMode, boolean isCamera, List<LocalMedia> selectList) {
        int themeId = R.style.picture_white_style;
        int animationMode = AnimationType.DEFAULT_ANIMATION;
        int language = 7;
        PictureSelector.create(activity)
                .openGallery(chooseMode)
                .imageEngine(GlideEngine.createGlideEngine())
                .setPictureUIStyle(mSelectorUIStyle)
                .theme(themeId)
                //.setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                //.setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                .setPictureWindowAnimationStyle(mWindowAnimationStyle)// 自定义相册启动退出动画
                .isWeChatStyle(false)// 是否开启微信图片选择风格
                .isUseCustomCamera(false)// 是否使用自定义相机
                .setLanguage(language)// 设置语言，默认中文
                .isPageStrategy(false)// 是否开启分页策略 & 每页多少条；默认开启
                .setRecyclerAnimationMode(animationMode)// 列表动画效果
                .isWithVideoImage(false)// 图片和视频是否可以同选,只在ofAll模式下有效
                .isMaxSelectEnabledMask(false)// 选择数到了最大阀值列表是否启用蒙层效果
                //.isAutomaticTitleRecyclerTop(false)// 连续点击标题栏RecyclerView是否自动回到顶部,默认true
                //.loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                //.setOutputCameraPath(createCustomCameraOutPath())// 自定义相机输出目录
                //.setButtonFeatures(CustomCameraView.BUTTON_STATE_BOTH)// 设置自定义相机按钮状态
                .maxSelectNum(30)// 最大图片选择数量
                .minSelectNum(0)// 最小选择数量
                .maxVideoSelectNum(10) // 视频最大选择数量
                //.minVideoSelectNum(1)// 视频最小选择数量
                //.closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 关闭在AndroidQ下获取图片或视频宽高相反自动转换
                .imageSpanCount(4)// 每行显示个数
                .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
                .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 如果视频有旋转角度则对换宽高,默认为false
                .isAndroidQTransform(false)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(true)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义视频播放回调控制，用户可以使用自己的视频播放界面
                //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// 自定义图片预览回调接口
                //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// 提供给用户的一些额外的自定义操作回调
                //.cameraFileName(System.currentTimeMillis() +".jpg")    // 重命名拍照文件名、如果是相册拍照则内部会自动拼上当前时间戳防止重复，注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                //.renameCompressFile(System.currentTimeMillis() +".jpg")// 重命名压缩文件名、 如果是多张压缩则内部会自动拼上当前时间戳防止重复
                //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 如果是多张裁剪则内部会自动拼上当前时间戳防止重复
                .selectionMode(selectionMode)// 多选 or 单选
                .isSingleDirectReturn(false)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true)// 是否可预览图片
                .isPreviewVideo(true)// 是否可预览视频
                //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
                .isEnablePreviewAudio(false) // 是否可播放音频
                .isCamera(isCamera)// 是否显示拍照按钮
                //.isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
                //.isMultipleRecyclerAnimation(false)// 多图裁剪底部列表显示动画效果
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
                .isEnableCrop(false)// 是否裁剪
                //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                .isCompress(true)// 是否压缩
                //.compressQuality(80)// 图片压缩后输出质量 0~ 100
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
                //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
                .isGif(true)// 是否显示gif图片
                //.isWebp(false)// 是否显示webp图片,默认显示
                //.isBmp(false)//是否显示bmp图片,默认显示
                .circleDimmedLayer(false)// 是否圆形裁剪
                //.setCropDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// 设置裁剪背景色值
                //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
                //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
                .isOpenClickSound(true)// 是否开启点击声音
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                //.videoMinSecond(10)// 查询多少秒以内的视频
                //.videoMaxSecond(15)// 查询多少秒以内的视频
                //.recordVideoSecond(10)//录制视频秒数 默认60s
                //.isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(100)// 小于多少kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(false) // 裁剪是否可旋转图片
                //.scaleEnabled(false)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                .selectionData(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    public static boolean checkMimeTypeVideo(String url) {
        return Objects.equals(getMimeType(url), "mp4");
    }

    public static void copyText(Activity activity, String text) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("\"text\"", text);
        clipboard.setPrimaryClip(clip);
    }

    public static List<CategorySticker> readFileSticker(Activity activity) {
        String line;
        StringBuilder fileContent = new StringBuilder();
        if (fileExists(activity, "sticker_data.json")) {
            try {
                FileInputStream fIn = activity.openFileInput("sticker_data.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));

                while ((line = reader.readLine()) != null) {
                    fileContent.append(line);
                }
                Log.d("fileContent", fileContent.toString());
                Gson gson = new Gson();
                return gson.fromJson(fileContent.toString(), StickerData.class).getList_category();
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    public static List<CategorySticker> readFileSticker() {
        String line;
        StringBuilder fileContent = new StringBuilder();
        if (fileExists("sticker_data.json")) {
            try {
                FileInputStream fIn = SupplierApplication.context.openFileInput("sticker_data.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));

                while ((line = reader.readLine()) != null) {
                    fileContent.append(line);
                }
                Log.d("fileContent", fileContent.toString());
                Gson gson = new Gson();
                return gson.fromJson(fileContent.toString(), StickerData.class).getList_category();
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    public static boolean fileExists(Activity context, String filename) {
        File file = context.getFileStreamPath(filename);
        return file != null && file.exists();
    }

    public static boolean fileExists(String filename) {
        File file = SupplierApplication.context.getFileStreamPath(filename);
        return file != null && file.exists();
    }

    public static void writeFileSticker(Activity activity, String json) {
        try {
            @SuppressLint("WrongConstant") FileOutputStream fOut = activity.openFileOutput("sticker_data.json", ParcelFileDescriptor.MODE_CREATE);
            OutputStreamWriter writer = new OutputStreamWriter(fOut);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getToDayMessage(String str_date) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            Date time = format.parse(str_date);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            assert time != null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String currentTime = sdf.format(new Date());
            if (currentTime.equals(dayFormat.format(time))) {
                return String.format("%s%s%s", timeFormat.format(time), ", ", SupplierApplication.context.getString(R.string.to_day));

            } else {
                return String.format("%s%s%s", timeFormat.format(time), ", ", dayFormat.format(time));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String formatFloatToString(final float f) {
        final int i = (int) f;
        if (f == i)
            return Integer.toString(i);
        return Float.toString(f);
    }

    public static String formatMaterial(ArrayList<DataListOrderDetail> list, int size) {
        String listMaterial = "";
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                String material;
                if (i != list.size() - 1) {
                    material = String.format(" %s %s %s,", list.get(i).getSupplier_material_name(), formatFloatToString(list.get(i).getQuantity()), list.get(i).getSupplier_unit_name());
                } else {
                    material = String.format(" %s %s %s", list.get(i).getSupplier_material_name(), formatFloatToString(list.get(i).getQuantity()), list.get(i).getSupplier_unit_name());
                }

                listMaterial = String.format("%s%s", listMaterial, material);

            }
        }
        if (size > 3) {
            listMaterial = String.format("%s và %s mặc hàng khác ", listMaterial, size - list.size());
        }
        return listMaterial;
    }


    static String randomString(int len) {

        final String alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        final String digits = "0123456789";

        Random secRand = new SecureRandom();

        if (len < 2) {
            throw new IllegalArgumentException("randomString - length too short: " + len);
        }

        // Pool of characters to select from.
        String pool = "";

        // Array to hold random characters.
        Character[] result = new Character[len];

        // Index into result.
        int index = 0;

        if (false) {
            // Put letters in pool.
            pool = pool + alpha;

            // Ensure at least one letter.
            result[index] = alpha.charAt(secRand.nextInt(alpha.length()));
            index++;
        }

        if (true) {
            // Put digits in pool.
            pool = pool + digits;

            // Ensure at least one digit.
            result[index] = digits.charAt(secRand.nextInt(digits.length()));
            index++;
        }

        // Fill rest of result array from pool.
        for (; index < len; index++) {
            result[index] = pool.charAt(secRand.nextInt(pool.length()));
        }

        // Shuffle result array with secRand to hide ordering.
        Collections.shuffle(Arrays.asList(result), secRand);

        // Assemble return string.
        StringBuilder sb = new StringBuilder(len);
        for (Character c : result) {
            sb.append(c);
        }
        return sb.toString();

    }  // end randomString()

}
