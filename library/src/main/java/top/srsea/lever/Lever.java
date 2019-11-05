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

package top.srsea.lever;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

public class Lever {
    private WeakReference<Context> contextWeakReference;

    public static void init(@NonNull Context appContext) {
        getInstance().contextWeakReference = new WeakReference<>(appContext.getApplicationContext());
    }

    public static boolean hasInitialized() {
        return getInstance().contextWeakReference != null;
    }

    public static Lever getInstance() {
        return Singleton.INSTANCE;
    }

    public static Context getContext() {
        if (getInstance().contextWeakReference == null) {
            throw new RuntimeException("Please init lever before use it.");
        }
        return getInstance().contextWeakReference.get();
    }

    public static Application getApplication() {
        return (Application) getContext();
    }

    private static class Singleton {
        private static final Lever INSTANCE = new Lever();
    }
}
