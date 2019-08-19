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

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import top.srsea.lever.Lever;

public class Res {
    public static int color(@ColorRes int id) {
        return ContextCompat.getColor(Lever.getContext(), id);
    }

    public static float dimen(@DimenRes int id) {
        return Lever.getContext().getResources().getDimension(id);
    }

    public static String string(@StringRes int id) {
        return Lever.getContext().getResources().getString(id);
    }

    public static Drawable drawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(Lever.getContext(), id);
    }

    public static ColorStateList colorStateList(@ColorRes int id) {
        return ContextCompat.getColorStateList(Lever.getContext(), id);
    }
}
