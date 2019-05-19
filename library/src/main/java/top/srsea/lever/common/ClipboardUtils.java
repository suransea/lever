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

import androidx.annotation.RequiresApi;
import top.srsea.lever.Lever;

public class ClipboardUtils {

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private static ClipboardManager getClipboardManager() {
        return (ClipboardManager) Lever.getContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static void setContent(String content) {
        ClipboardManager manager = getClipboardManager();
        manager.setPrimaryClip(ClipData.newPlainText(null, content));
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static String getContent() {
        ClipboardManager manager = getClipboardManager();
        if (manager.getPrimaryClip() == null) {
            return "";
        }
        return manager.getPrimaryClip().toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void clear() {
        ClipboardManager manager = getClipboardManager();
        manager.clearPrimaryClip();
    }
}
