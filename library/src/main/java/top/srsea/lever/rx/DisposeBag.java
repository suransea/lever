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
 * Containers of disposables.
 *
 * @author sea
 * @see ArrayList
 */
public class DisposeBag extends ArrayList<WeakReference<Disposable>> {

    /**
     * Adds weak reference of the disposable to this container.
     *
     * @param disposable the disposable to add
     * @return true (as specified by {@link ArrayList#add}),
     * false when disposable is null or disposable has been disposed.
     */
    public boolean add(Disposable disposable) {
        if (disposable == null || disposable.isDisposed()) return false;
        return super.add(new WeakReference<>(disposable));
    }

    /**
     * Disposes all disposables.
     */
    public synchronized void release() {
        for (WeakReference<Disposable> item : this) {
            Disposable disposable = item.get();
            if (disposable == null || disposable.isDisposed()) continue;
            disposable.dispose();
        }
        clear();
    }

    /**
     * When this object is being recycling, disposes all disposables.
     *
     * @throws Throwable any exceptions from {@link Object#finalize()}
     */
    @Override
    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }
}
