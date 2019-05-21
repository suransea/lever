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

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import top.srsea.lever.Lever;
import top.srsea.lever.graph.Bitmaps;

public class Screens {
    private DisplayMetrics displayMetrics = Lever.getContext().getResources().getDisplayMetrics();

    public static int getWidth() {
        return Singleton.INSTANCE.displayMetrics.widthPixels;
    }

    public static int getHeight() {
        return Singleton.INSTANCE.displayMetrics.heightPixels;
    }

    public static Bitmap snapshot(@NonNull Activity activity) {
        return Bitmaps.from(activity);
    }

    private static class Singleton {
        private static final Screens INSTANCE = new Screens();
    }
}
