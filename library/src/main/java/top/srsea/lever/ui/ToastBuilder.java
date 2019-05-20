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
import android.view.View;
import android.widget.Toast;

import top.srsea.lever.Lever;

public class ToastBuilder {
    private String content;
    private View view;
    private int duration = Toast.LENGTH_SHORT;
    private boolean preparing = false;
    private float hMargin = -1f;
    private float vMargin = -1f;
    private int gravity = -1;
    private int xOffset = -1;
    private int yOffset = -1;

    ToastBuilder content(String content) {
        this.content = content;
        return this;
    }

    ToastBuilder view(View view) {
        this.view = view;
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
        Toast toast = new Toast(Lever.getContext());
        toast.setDuration(duration);
        if (content != null) {
            toast.setText(content);
        }
        if (view != null) {
            toast.setView(view);
        }
        if (gravity != -1) {
            toast.setGravity(gravity, xOffset, yOffset);
        }
        if (hMargin > 0 && vMargin > 0) {
            toast.setMargin(hMargin, vMargin);
        }
        return toast;
    }

    public ToastBuilder prepare() {
        Looper.prepare();
        preparing = true;
        return this;
    }

    public ToastBuilder margin(float h, float v) {
        hMargin = h;
        vMargin = v;
        return this;
    }

    public ToastBuilder gravity(int gravity, int xOffset, int yOffset) {
        this.gravity = gravity;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        return this;
    }

    private void show() {
        build().show();
        if (!preparing) return;
        Looper.loop();
        preparing = false;
    }

    public void showShort() {
        lengthShort().show();
    }

    public void showLong() {
        lengthLong().show();
    }
}
