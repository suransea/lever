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
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * A context holder for all utilities of this package.
 *
 * @author sea
 */
public class Lever {

    /**
     * An application context weak reference.
     */
    private WeakReference<Context> contextWeakReference;

    /**
     * Initializes with a context.
     *
     * <p>{@link Context#getApplicationContext()} will be used.
     *
     * @param context any valid context
     */
    public static void init(@NonNull Context context) {
        getInstance().contextWeakReference = new WeakReference<>(context.getApplicationContext());
    }

    /**
     * Returns a value if lever has been initialized.
     *
     * @return {@code true} if initialized
     */
    public static boolean hasInitialized() {
        return getInstance().contextWeakReference != null;
    }

    /**
     * Gets instance of lever.
     *
     * @return lever instance
     */
    public static Lever getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Gets the holden context.
     *
     * @return application context
     * @throws RuntimeException if not initialized yet
     */
    public static Context getContext() {
        if (getInstance().contextWeakReference == null) {
            throw new RuntimeException("Please init lever before use it.");
        }
        return getInstance().contextWeakReference.get();
    }

    /**
     * Gets the application context as Application.
     *
     * @return application
     * @throws RuntimeException if not initialized yet
     * @see Application
     */
    public static Application getApplication() {
        return (Application) getContext();
    }

    private static class Singleton {

        // singleton for Lever
        private static final Lever INSTANCE = new Lever();
    }
}
