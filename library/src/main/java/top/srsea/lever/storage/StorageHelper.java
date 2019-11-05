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

package top.srsea.lever.storage;

import android.content.Context;
import android.os.Environment;
import top.srsea.lever.Lever;

import java.io.File;

public class StorageHelper {

    /**
     * 外部存储是否挂载
     *
     * @return if external storage state is mounted
     */
    public static boolean isExternalMounted() {
        String status = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(status);
    }

    public static File getCacheDir() {
        Context context = Lever.getContext();
        return isExternalMounted() ?
                context.getExternalCacheDir() : context.getCacheDir();
    }
}
