/*
 * Copyright (C) 2020 - Amir Hossein Aghajari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package com.aghajari.emojiview.adapters;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.listener.OnEmojiActions;
import com.aghajari.emojiview.shared.VariantEmoji;
import com.aghajari.emojiview.utils.Utils;
import com.aghajari.emojiview.view.AXEmojiImageView;

public class AXEmojiRecyclerAdapter extends RecyclerView.Adapter<AXEmojiRecyclerAdapter.ViewHolder> {
    Emoji[] emojis;
    int count;
    OnEmojiActions events;
    VariantEmoji variantEmoji;

    public AXEmojiRecyclerAdapter(Emoji[] emojis, OnEmojiActions events, VariantEmoji variantEmoji) {
        this.emojis = emojis;
        this.count = emojis.length;
        this.events = events;
        this.variantEmoji = variantEmoji;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
        AXEmojiImageView emojiView = new AXEmojiImageView(viewGroup.getContext());
        int cw = Utils.getColumnWidth(viewGroup.getContext());
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(cw, cw));
        frameLayout.addView(emojiView);

        int dp6 = Utils.dpToPx(viewGroup.getContext(), 8);
        emojiView.setPadding(dp6, dp6, dp6, dp6);

        return new ViewHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        FrameLayout frameLayout = (FrameLayout) viewHolder.itemView;
        final AXEmojiImageView emojiView = (AXEmojiImageView) frameLayout.getChildAt(0);

        Emoji emoji = emojis[i];
        emojiView.setEmoji(variantEmoji.getVariant(emoji));
        //ImageLoadingTask currentTask = new ImageLoadingTask(emojiView);
        //currentTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, emoji, null, null);
        emojiView.setOnEmojiActions(events, false);

    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
