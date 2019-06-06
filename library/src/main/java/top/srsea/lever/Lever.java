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

import android.content.Context;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

public class Lever {
    private WeakReference<Context> contextWeakReference;

    public static void init(@NonNull Context appContext) {
        Singleton.INSTANCE
                .contextWeakReference = new WeakReference<>(appContext.getApplicationContext());
    }

    public static Context getContext() {
        if (Singleton.INSTANCE.contextWeakReference == null) {
            throw new RuntimeException("Please init lever before use it.");
        }
        return Singleton.INSTANCE.contextWeakReference.get();
    }

    private static class Singleton {
        private static final Lever INSTANCE = new Lever();
    }
}
