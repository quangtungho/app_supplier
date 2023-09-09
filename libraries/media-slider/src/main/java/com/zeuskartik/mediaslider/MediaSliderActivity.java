package com.zeuskartik.mediaslider;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class MediaSliderActivity extends AppCompatActivity {
    private ViewPager mPager;
    private TextView slider_media_number;
    private boolean isMediaCountVisible;
    private ArrayList<String> urlList;
    private int startPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider);
        SlidrConfig config = new SlidrConfig.Builder()
                                .position(SlidrPosition.VERTICAL)
                                .build();
        Slidr.attach(this, config);
    }


    public void loadMediaSliderView(ArrayList<String> mediaUrlList, boolean isMediaCountVisible, int startPosition) {
        this.urlList = mediaUrlList;
        this.isMediaCountVisible = isMediaCountVisible;
        this.startPosition = startPosition;
        initViewsAndSetAdapter();
    }

    private void setStartPosition() {
        if (startPosition >= 0) {
            if (startPosition > urlList.size()) {
                mPager.setCurrentItem((urlList.size() - 1));
                return;
            }
            mPager.setCurrentItem(startPosition);
        } else {
            mPager.setCurrentItem(0);
        }
        mPager.setOffscreenPageLimit(0);
    }



    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        } catch (MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }

    @SuppressLint("SetTextI18n")
    private void initViewsAndSetAdapter() {
        slider_media_number = findViewById(R.id.number);
        mPager = findViewById(R.id.pager);
        ImageView ivDownload = findViewById(R.id.ivDownload);
        ImageView ivClose = findViewById(R.id.ivClose);
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(MediaSliderActivity.this, urlList);
        mPager.setAdapter(pagerAdapter);
        setStartPosition();
        mPager.setPageMargin(10);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        if (isMediaCountVisible) {
            if (urlList.size() == 1) {
                slider_media_number.setVisibility(View.GONE);
            } else
                slider_media_number.setVisibility(View.VISIBLE);
            slider_media_number.setText((mPager.getCurrentItem() + 1) + "/" + urlList.size());
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaSliderActivity.this.onBackPressed();
            }
        });

        ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager downloadmanager = (DownloadManager) MediaSliderActivity.this.getBaseContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(urlList.get(mPager.getCurrentItem()));
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle(getFileNameFromURL(urlList.get(mPager.getCurrentItem())));
                request.setDescription("Tải về");
                request.setDestinationInExternalPublicDir(String.valueOf(Environment.DIRECTORY_DOWNLOADS), getFileNameFromURL(urlList.get(mPager.getCurrentItem())));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                assert downloadmanager != null;
                downloadmanager.enqueue(request);
                Toast.makeText(MediaSliderActivity.this.getBaseContext(), "Đang tải xuống",
                        Toast.LENGTH_LONG).show();
            }
        });

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (Objects.equals(getMimeType(urlList.get(i)), "mp4")) {
                    JZVideoPlayerStandard.goOnPlayOnPause();
                    JZVideoPlayerStandard.releaseAllVideos();
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onPageSelected(int i) {
                slider_media_number.setText((mPager.getCurrentItem() + 1) + "/" + urlList.size());
                if (Objects.equals(getMimeType(urlList.get(i)), "mp4")) {
                    JZVideoPlayerStandard.goOnPlayOnPause();
                    JZVideoPlayerStandard.releaseAllVideos();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    public static String getMimeType(String url) {
        try {
            return url.substring(url.lastIndexOf(".") + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JZVideoPlayerStandard.goOnPlayOnPause();
        JZVideoPlayerStandard.releaseAllVideos();
        JZVideoPlayer.backPress();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayerStandard.goOnPlayOnPause();
        JZVideoPlayerStandard.releaseAllVideos();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.picture_anim_enter, R.anim.picture_anim_exit);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    private static class ScreenSlidePagerAdapter extends PagerAdapter {
        private final Context context;
        private final ArrayList<String> urlList;
        TouchImageView imageView;
        public JZVideoPlayerStandard jzVideoPlayerStandard;


        private ScreenSlidePagerAdapter(Context context, ArrayList<String> urlList) {
            this.context = context;
            this.urlList = urlList;
        }

        public static String getMimeType(String url) {
            try {
                return url.substring(url.lastIndexOf(".") + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            try {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View view;

                if (Objects.equals(getMimeType(urlList.get(position)), "mp4")) {
                    JZVideoPlayerStandard.goOnPlayOnPause();
                    view = inflater.inflate(R.layout.video_item, container, false);

                    jzVideoPlayerStandard = view.findViewById(R.id.videoplayer);

                    jzVideoPlayerStandard.setUp(urlList.get(position),
                            JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                            "");

                    Glide.with(context).load(urlList.get(position))
                            .into(jzVideoPlayerStandard.thumbImageView);
                } else {

                    view = inflater.inflate(R.layout.image_item, container, false);
                    imageView = view.findViewById(R.id.mBigImage);

                    Glide.with(context)
                            .load(urlList.get(position))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder).error(R.drawable.ic_image_placeholder))
                            .into(imageView);
                }
                container.addView(view);
                return view;
            } catch (Exception e) {
                return null;
            }

        }

        @Override
        public int getCount() {
            return urlList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return (view == o);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
            releasePlayer();
        }

        private void releasePlayer() {
            if (JZVideoPlayer.backPress()) {
                return;
            }
            JZVideoPlayerStandard.goOnPlayOnPause();

            if (jzVideoPlayerStandard != null) {
                jzVideoPlayerStandard.release();
            }
            JZVideoPlayerStandard.releaseAllVideos();
        }
    }
}
