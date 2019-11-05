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

import android.util.Log;

public class Logger {
    private String tag;

    private Logger(String tag) {
        this.tag = tag;
    }

    public static Logger getLogger(String tag) {
        return new Logger(tag);
    }

    public static Logger getGlobal() {
        return new Logger("global");
    }

    public void info(String content) {
        Log.i(tag, content);
    }

    public void info(Object object) {
        info(String.valueOf(object));
    }

    public void info(String format, Object... args) {
        info(String.format(format, args));
    }

    public void error(String content) {
        Log.e(tag, content);
    }

    public void error(Object object) {
        error(String.valueOf(object));
    }

    public void error(String format, Object... args) {
        error(String.format(format, args));
    }

    public void debug(String content) {
        Log.d(tag, content);
    }

    public void debug(Object object) {
        debug(String.valueOf(object));
    }

    public void debug(String format, Object... args) {
        debug(String.format(format, args));
    }

    public void verbose(String content) {
        Log.v(tag, content);
    }

    public void verbose(Object object) {
        verbose(String.valueOf(object));
    }

    public void verbose(String format, Object... args) {
        verbose(String.format(format, args));
    }

    public void warn(String content) {
        Log.w(tag, content);
    }

    public void warn(Object object) {
        warn(String.valueOf(object));
    }

    public void warn(String format, Object... args) {
        warn(String.format(format, args));
    }
}
