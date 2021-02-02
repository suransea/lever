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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;

import top.srsea.lever.Lever;

/**
 * Utilities for packages.
 *
 * @author sea
 */
public class PackageHelper {
    private PackageHelper() {
    }

    /**
     * Determines whether the application corresponding to the package name is installed.
     *
     * @param packageName application package name
     * @return {@code true} if installed
     */
    public static boolean isInstalled(@NonNull String packageName) {
        PackageManager packageManager = Lever.getContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_GIDS);
            return packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Opens the app detail settings page.
     *
     * @param packageName application package name
     */
    public static void openAppDetailSetting(@NonNull String packageName) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", packageName, null));
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", packageName);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Lever.getContext().startActivity(intent);
    }

    /**
     * Opens the detail settings page of this application.
     */
    public static void openAppDetailSetting() {
        openAppDetailSetting(Lever.getContext().getPackageName());
    }

    /**
     * Open the application corresponding to the package name.
     *
     * @param packageName the specific package name.
     */
    public static void openApp(String packageName) {
        Context context = Lever.getContext();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) return;
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
