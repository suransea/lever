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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;

import top.srsea.lever.Lever;

public class Packages {
    public static final String WECHAT = "com.tencent.mm";
    public static final String QQ = "com.tencent.mobileqq";
    public static final String TAOBAO = "com.taobao.taobao";
    public static final String ALIPAY = "com.eg.android.AlipayGphone";


    /**
     * 判断应用是否安装
     *
     * @param packageName 包名
     * @return true if installed
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
     * 打开应用信息页
     *
     * @param packageName 包名
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
     * 打开应用
     *
     * @param packageName 包名
     */
    public static void openApp(String packageName) {
        Context context = Lever.getContext();
        PackageManager packageManager = Lever.getContext().getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) return;
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 打开此应用的应用信息页
     */
    public static void openAppDetailSetting() {
        openAppDetailSetting(Lever.getContext().getPackageName());
    }
}
