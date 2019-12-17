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

/**
 * A Logger wrapped from android {@link Log}.
 *
 * @author sea
 * @see Log
 */
public class Logger {

    /**
     * Tag for Android {@link Log}.
     */
    private String tag;

    /**
     * Constructs an instance with the specific tag.
     *
     * @param tag the specific tag
     */
    private Logger(String tag) {
        this.tag = tag;
    }

    /**
     * Creates a Logger with the specific tag.
     * The tag will be used for {@link Log}.
     *
     * @param tag the specific tag
     * @return a Logger with the specific tag
     */
    public static Logger getLogger(String tag) {
        return new Logger(tag);
    }

    /**
     * Creates a Logger with the tag {@code "global"}.
     * The tag will be used for {@link Log}.
     *
     * @return a Logger with the tag {@code "global"}
     */
    public static Logger getGlobal() {
        return new Logger("global");
    }

    /**
     * Sends a {@link Log#INFO} log message.
     *
     * @param msg the message you would like logged
     * @see Log#i(String, String)
     */
    public void info(String msg) {
        Log.i(tag, msg);
    }

    /**
     * Sends a {@link Log#INFO} log message.
     *
     * @param object will be converted to string
     */
    public void info(Object object) {
        info(String.valueOf(object));
    }

    /**
     * Sends a {@link Log#INFO} log message.
     *
     * @param format the specific format
     * @param args   will be converted to string for the specific format
     */
    public void info(String format, Object... args) {
        info(String.format(format, args));
    }

    /**
     * Sends a {@link Log#ERROR} log message.
     *
     * @param msg the message you would like logged
     * @see Log#i(String, String)
     */
    public void error(String msg) {
        Log.e(tag, msg);
    }

    /**
     * Sends a {@link Log#ERROR} log message.
     *
     * @param object will be converted to string
     */
    public void error(Object object) {
        error(String.valueOf(object));
    }

    /**
     * Sends a {@link Log#ERROR} log message.
     *
     * @param format the specific format
     * @param args   will be converted to string according to the specific format
     */
    public void error(String format, Object... args) {
        error(String.format(format, args));
    }

    /**
     * Sends a {@link Log#DEBUG} log message.
     *
     * @param msg the message you would like logged
     * @see Log#i(String, String)
     */
    public void debug(String msg) {
        Log.d(tag, msg);
    }

    /**
     * Sends a {@link Log#DEBUG} log message.
     *
     * @param object will be converted to string
     */
    public void debug(Object object) {
        debug(String.valueOf(object));
    }

    /**
     * Sends a {@link Log#DEBUG} log message.
     *
     * @param format the specific format
     * @param args   will be converted to string according to the specific format
     */
    public void debug(String format, Object... args) {
        debug(String.format(format, args));
    }

    /**
     * Sends a {@link Log#VERBOSE} log message.
     *
     * @param msg the message you would like logged
     * @see Log#i(String, String)
     */
    public void verbose(String msg) {
        Log.v(tag, msg);
    }

    /**
     * Sends a {@link Log#VERBOSE} log message.
     *
     * @param object will be converted to string
     */
    public void verbose(Object object) {
        verbose(String.valueOf(object));
    }

    /**
     * Sends a {@link Log#VERBOSE} log message.
     *
     * @param format the specific format
     * @param args   will be converted to string according to the specific format
     */
    public void verbose(String format, Object... args) {
        verbose(String.format(format, args));
    }

    /**
     * Sends a {@link Log#WARN} log message.
     *
     * @param msg the message you would like logged
     * @see Log#i(String, String)
     */
    public void warn(String msg) {
        Log.w(tag, msg);
    }

    /**
     * Sends a {@link Log#WARN} log message.
     *
     * @param object will be converted to string
     */
    public void warn(Object object) {
        warn(String.valueOf(object));
    }

    /**
     * Sends a {@link Log#WARN} log message.
     *
     * @param format the specific format
     * @param args   will be converted to string according to the specific format
     */
    public void warn(String format, Object... args) {
        warn(String.format(format, args));
    }
}
