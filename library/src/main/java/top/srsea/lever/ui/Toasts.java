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

import android.widget.Toast;

import androidx.annotation.StringRes;
import top.srsea.lever.Lever;

public class Toasts {
    public static Builder of(String content) {
        if (content == null) content = "";
        return new Builder().content(content);
    }

    public static Builder of(@StringRes int resId) {
        String content = Lever.getContext().getString(resId);
        return of(content);
    }

    public static class Builder {
        private String content = "";
        private int duration = Toast.LENGTH_SHORT;

        private Builder content(String content) {
            this.content = content;
            return this;
        }

        private Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        private Builder lengthShort() {
            this.duration = Toast.LENGTH_SHORT;
            return this;
        }

        private Builder lengthLong() {
            this.duration = Toast.LENGTH_LONG;
            return this;
        }

        public Toast build() {
            return Toast.makeText(Lever.getContext(), content, duration);
        }

        private void show() {
            build().show();
        }

        public void showShort() {
            lengthShort().show();
        }

        public void showLong() {
            lengthLong().show();
        }
    }
}
