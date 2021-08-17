/*
 * Copyright (C) 2019 sea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.srsea.lever.common;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import top.srsea.lever.Lever;
import top.srsea.torque.common.Preconditions;

/**
 * Utilities for android clipboards.
 *
 * @author sea
 * @see Context#CLIPBOARD_SERVICE
 * @see ClipboardManager
 */
public class ClipboardHelper {
    private ClipboardHelper() {
    }

    /**
     * Gets the clipboard manager service.
     *
     * @return clipboard manager service
     * @see Context#CLIPBOARD_SERVICE
     */
    private static ClipboardManager getClipboardManager() {
        return (ClipboardManager) Lever.getContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * Gets the content in the primary clipboard.
     * A {@code ""} returns when having no content.
     *
     * @return the content in the primary clipboard or a {@code ""}.
     */
    public static String getContent() {
        ClipboardManager manager = getClipboardManager();
        if (!manager.hasPrimaryClip()) return "";
        ClipData clipData = manager.getPrimaryClip();
        if (clipData == null) return "";
        if (clipData.getItemCount() <= 0) return "";
        return clipData.getItemAt(0).coerceToText(Lever.getContext()).toString();
    }

    /**
     * Sets the primary clipboard to the specific content.
     * If the content is null, it will be replaced with {@code ""}.
     *
     * @param content the specific content
     */
    public static void setContent(@Nullable String content) {
        ClipboardManager manager = getClipboardManager();
        if (content == null) {
            content = "";
        }
        manager.setPrimaryClip(ClipData.newPlainText(null, content));
    }

    /**
     * Gets the clip data in the primary clipboard.
     * Null returns when having no clip data.
     *
     * @return the clip in the primary clipboard or null.
     */
    @Nullable
    public static ClipData getClipData() {
        ClipboardManager manager = getClipboardManager();
        if (!manager.hasPrimaryClip()) return null;
        return manager.getPrimaryClip();
    }

    /**
     * Sets the primary clipboard to the specific clip data.
     *
     * @param clipData the specific clip data
     * @throws NullPointerException if the {@code clipData} is null
     */
    public static void setClipData(@NonNull ClipData clipData) {
        Preconditions.requireNonNull(clipData);
        ClipboardManager manager = getClipboardManager();
        manager.setPrimaryClip(clipData);
    }

    /**
     * Clears the primary clipboard.
     *
     * @see ClipboardManager#clearPrimaryClip()
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void clear() {
        ClipboardManager manager = getClipboardManager();
        manager.clearPrimaryClip();
    }
}
