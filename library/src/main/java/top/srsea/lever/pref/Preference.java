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

package top.srsea.lever.pref;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import top.srsea.lever.Lever;
import top.srsea.torque.value.Property;

import java.util.Set;

/**
 * A wrapper of {@link SharedPreferences}
 *
 * @param <T> type of value
 * @author sea
 * @see SharedPreferences
 * @see Property
 */
public abstract class Preference<T> implements Property<T> {

    /**
     * SharedPreference instance to operate.
     */
    protected SharedPreferences sharedPreferences;

    /**
     * Key of this preference.
     */
    protected String key;

    /**
     * Default value when this value not exists.
     */
    protected T defaultValue;

    /**
     * Constructs an instance with the key and default value.
     *
     * @param key          the key for the preference
     * @param defaultValue the default value for the preference
     */
    public Preference(String key, T defaultValue) {
        Context context = Lever.getContext();
        sharedPreferences = context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        this.key = key;
        this.defaultValue = defaultValue;
    }

    /**
     * Constructs an instance with the key, default value and preference name.
     *
     * @param key            the key for the preference
     * @param defaultValue   the default value for the preference
     * @param preferenceName the preference name of the preference
     */
    public Preference(String key, T defaultValue, String preferenceName) {
        this.sharedPreferences = Lever.getContext().getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        this.key = key;
        this.defaultValue = defaultValue;
    }

    // some convenient factory method provided

    public static Preference<String> create(String key, String defaultValue, String prefName) {
        return new StringPreference(key, defaultValue, prefName);
    }

    public static Preference<Integer> create(String key, int defaultValue, String prefName) {
        return new IntPreference(key, defaultValue, prefName);
    }

    public static Preference<Long> create(String key, long defaultValue, String prefName) {
        return new LongPreference(key, defaultValue, prefName);
    }

    public static Preference<Boolean> create(String key, boolean defaultValue, String prefName) {
        return new BooleanPreference(key, defaultValue, prefName);
    }

    public static Preference<Float> create(String key, float defaultValue, String prefName) {
        return new FloatPreference(key, defaultValue, prefName);
    }

    public static Preference<Set<String>> create(String key, Set<String> defaultValue, String prefName) {
        return new StringSetPreference(key, defaultValue, prefName);
    }

    public static Preference<String> create(String key, String defaultValue) {
        return new StringPreference(key, defaultValue);
    }

    public static Preference<Integer> create(String key, int defaultValue) {
        return new IntPreference(key, defaultValue);
    }

    public static Preference<Long> create(String key, long defaultValue) {
        return new LongPreference(key, defaultValue);
    }

    public static Preference<Boolean> create(String key, boolean defaultValue) {
        return new BooleanPreference(key, defaultValue);
    }

    public static Preference<Float> create(String key, float defaultValue) {
        return new FloatPreference(key, defaultValue);
    }

    public static Preference<Set<String>> create(String key, Set<String> defaultValue) {
        return new StringSetPreference(key, defaultValue);
    }

    // factory method end

    /**
     * Modifies this value use commit.
     *
     * @param value value to set
     * @see SharedPreferences.Editor#commit()
     */
    public abstract void blockingSet(T value);

    /**
     * Removes this value.
     */
    public void remove() {
        sharedPreferences.edit().remove(key).apply();
    }

    /**
     * Removes this value use commit.
     *
     * @see SharedPreferences.Editor#commit()
     */
    @SuppressLint("ApplySharedPref")
    public void blockingRemove() {
        sharedPreferences.edit().remove(key).commit();
    }
}
