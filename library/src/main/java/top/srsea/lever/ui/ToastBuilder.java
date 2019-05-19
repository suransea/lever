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

import android.os.Looper;
import android.widget.Toast;

import top.srsea.lever.Lever;

public class ToastBuilder {
    private String content = "";
    private int duration = Toast.LENGTH_SHORT;

    ToastBuilder content(String content) {
        this.content = content;
        return this;
    }

    private ToastBuilder lengthShort() {
        this.duration = Toast.LENGTH_SHORT;
        return this;
    }

    private ToastBuilder lengthLong() {
        this.duration = Toast.LENGTH_LONG;
        return this;
    }

    public Toast build() {
        return Toast.makeText(Lever.getContext(), content, duration);
    }

    public ToastBuilder prepare() {
        Looper.prepare();
        Looper.loop();
        return this;
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
