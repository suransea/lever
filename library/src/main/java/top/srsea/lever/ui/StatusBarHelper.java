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

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

/**
 * Utilities for a status bar.
 *
 * @author sea
 */
public class StatusBarHelper {
    private final Window window; // the window of this activity
    private final View decorView; // the decor view of this activity

    /**
     * Constructs an instance from an activity.
     *
     * @param activity an activity
     */
    private StatusBarHelper(Activity activity) {
        window = activity.getWindow();
        decorView = window.getDecorView();
    }

    /**
     * Creates a StatusBarHelper with the given activity.
     *
     * @param activity an activity
     * @return StatusBarHelper instance
     */
    public static StatusBarHelper get(Activity activity) {
        return new StatusBarHelper(activity);
    }

    /**
     * Hides the status bar for this window.
     *
     * <p>If there is a navigation bar, the navigation bar will also be hidden.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public StatusBarHelper hide() {
        int flag = View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flag |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        decorView.setSystemUiVisibility(flag);
        return this;
    }

    /**
     * Switches the status bar to dark or light.
     *
     * <p>If there is a navigation bar, and the platform API level is greater or equal to 26,
     * the navigation bar will also be switched.
     *
     * @param light to light or dark
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public StatusBarHelper setBrightness(boolean light) {
        int flag = decorView.getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (light) {
                flag |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                flag &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
        }
        if (light) {
            flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            flag &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        decorView.setSystemUiVisibility(flag);
        return this;
    }

    /**
     * Switches the status bar to light.
     *
     * <p>If there is a navigation bar, and the platform API level is greater or equal to 26,
     * the navigation bar will also be switched.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public StatusBarHelper setLight() {
        return setBrightness(true);
    }

    /**
     * Switches the status bar to dark.
     *
     * <p>If there is a navigation bar, and the platform API level is greater or equal to 26,
     * the navigation bar will also be switched.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public StatusBarHelper setDark() {
        return setBrightness(false);
    }

    /**
     * Sets the status bar to be fully transparent.
     *
     * <p>If there is a navigation bar, and the platform API level is greater or equal to 26,
     * the navigation bar will also be set.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public StatusBarHelper setFullTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
            return this;
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        return this;
    }
}
