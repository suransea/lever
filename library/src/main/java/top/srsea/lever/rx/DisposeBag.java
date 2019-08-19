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

package top.srsea.lever.rx;

import io.reactivex.disposables.Disposable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Disposable容器
 */
public class DisposeBag extends ArrayList<WeakReference<Disposable>> {
    public boolean add(Disposable disposable) {
        if (disposable == null || disposable.isDisposed()) return false;
        return super.add(new WeakReference<>(disposable));
    }

    /**
     * 释放全部disposables
     */
    public synchronized void release() {
        for (WeakReference<Disposable> item : this) {
            if (item.get() == null || item.get().isDisposed()) continue;
            item.get().dispose();
        }
        clear();
    }

    @Override
    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }
}