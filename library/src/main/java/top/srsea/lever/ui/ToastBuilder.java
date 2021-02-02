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

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import top.srsea.lever.Lever;

/**
 * A builder for {@link Toast}.
 *
 * @author sea
 */
public class ToastBuilder {
    private String content = "";
    private View view;
    private int duration = Toast.LENGTH_SHORT;

    // value "-1" will be ignored when building toast
    private float hMargin = -1f;
    private float vMargin = -1f;
    private int gravity = -1;
    private int xOffset = -1;
    private int yOffset = -1;

    /**
     * Sets the content for this toast.
     * Mutually exclusive with {@link ToastBuilder#view(View)}.
     *
     * @param content the specific content
     * @return current builder
     * @see Toast#setText(CharSequence)
     */
    ToastBuilder content(String content) {
        this.content = content;
        return this;
    }

    /**
     * Sets view for this toast.
     * Mutually exclusive with {@link ToastBuilder#content(String)}.
     *
     * @param view the specific view
     * @return current toast
     * @see Toast#setView(View)
     */
    ToastBuilder view(View view) {
        this.view = view;
        return this;
    }

    /**
     * Sets the duration of this toast to short.
     *
     * @return current toast
     * @see Toast#LENGTH_SHORT
     * @see Toast#setDuration(int)
     */
    private ToastBuilder lengthShort() {
        this.duration = Toast.LENGTH_SHORT;
        return this;
    }

    /**
     * Sets the duration of this toast to long.
     *
     * @return current toast
     * @see Toast#LENGTH_LONG
     * @see Toast#setDuration(int)
     */
    private ToastBuilder lengthLong() {
        this.duration = Toast.LENGTH_LONG;
        return this;
    }

    /**
     * Builds a toast with current arguments.
     *
     * @return a android toast object
     */
    public Toast build() {
        Toast toast = (view == null ? buildText() : buildView());
        if (gravity != -1) {
            toast.setGravity(gravity, xOffset, yOffset);
        }
        if (hMargin > 0 && vMargin > 0) {
            toast.setMargin(hMargin, vMargin);
        }
        return toast;
    }

    /**
     * Builds a text type toast.
     *
     * @return a android toast object
     */
    private Toast buildText() {
        return Toast.makeText(Lever.getContext(), content, duration);
    }

    /**
     * Builds a custom view type toast.
     *
     * @return a android toast object
     */
    private Toast buildView() {
        Toast toast = new Toast(Lever.getContext());
        toast.setDuration(duration);
        toast.setView(view);
        return toast;
    }

    /**
     * Marks this toast to be shown in the main thread.
     *
     * @return current toast
     * @deprecated ToastBuilder#show() now checks thread
     */
    @Deprecated
    public ToastBuilder mainThread() {
        return this;
    }

    /**
     * Sets margins for this toast.
     *
     * @param h horizontal value
     * @param v vertical value
     * @return current toast
     * @see Toast#setMargin(float, float)
     */
    public ToastBuilder margin(float h, float v) {
        hMargin = h;
        vMargin = v;
        return this;
    }

    /**
     * Sets gravities for this toast.
     *
     * @param gravity the gravity to set
     * @param xOffset offset in x
     * @param yOffset offset in y
     * @return current toast
     * @see Toast#setGravity(int, int, int)
     * @see Gravity
     */
    public ToastBuilder gravity(int gravity, int xOffset, int yOffset) {
        this.gravity = gravity;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        return this;
    }

    /**
     * Shows this toast.
     *
     * @see Toast#show()
     */
    private void show() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    build().show();
                }
            });
            return;
        }
        build().show();
    }

    /**
     * Shows this toast for short duration.
     *
     * @see Toast#show()
     */
    public void showShort() {
        lengthShort().show();
    }

    /**
     * Shows this toast for long duration.
     *
     * @see Toast#show()
     */
    public void showLong() {
        lengthLong().show();
    }
}
