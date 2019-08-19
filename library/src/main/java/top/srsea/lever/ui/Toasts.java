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

package top.srsea.lever.ui;

import android.view.View;
import androidx.annotation.StringRes;
import top.srsea.lever.common.Res;

public class Toasts {
    public static ToastBuilder of(String content) {
        if (content == null) content = "";
        return new ToastBuilder().content(content);
    }

    public static ToastBuilder of(@StringRes int resId) {
        String content = Res.string(resId);
        return of(content);
    }

    public static ToastBuilder of(View view) {
        if (view == null) return of("");
        return new ToastBuilder().view(view);
    }
}
