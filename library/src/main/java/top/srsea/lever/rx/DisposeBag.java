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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.OpenHashSet;


public final class DisposeBag implements Disposable, DisposableContainer, LifecycleEventObserver {

    private OpenHashSet<Disposable> resources;

    private volatile boolean disposed;

    public DisposeBag(@NonNull LifecycleOwner lifecycleOwner) {
        this(lifecycleOwner.getLifecycle());
    }

    public DisposeBag(@NonNull Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    @Override
    public void dispose() {
        if (disposed) {
            return;
        }
        OpenHashSet<Disposable> set;
        synchronized (this) {
            if (disposed) {
                return;
            }
            disposed = true;
            set = resources;
            resources = null;
        }

        dispose(set);
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    /**
     * Adds a {@link Disposable} to this container or disposes it if the
     * container has been disposed.
     *
     * @param disposable the {@code Disposable} to add, not {@code null}
     * @return {@code true} if successful, {@code false} if this container has been disposed
     * @throws NullPointerException if {@code disposable} is {@code null}
     */
    @Override
    public boolean add(@NonNull Disposable disposable) {
        ObjectHelper.requireNonNull(disposable, "disposable is null");
        if (!disposed) {
            synchronized (this) {
                if (!disposed) {
                    OpenHashSet<Disposable> set = resources;
                    if (set == null) {
                        set = new OpenHashSet<>();
                        resources = set;
                    }
                    set.add(disposable);
                    return true;
                }
            }
        }
        disposable.dispose();
        return false;
    }

    /**
     * Atomically adds the given array of {@link Disposable}s to the container or
     * disposes them all if the container has been disposed.
     *
     * @param disposables the array of {@code Disposable}s
     * @return {@code true} if the operation was successful, {@code false} if the container has been disposed
     * @throws NullPointerException if {@code disposables} or any of its array items is {@code null}
     */
    public boolean addAll(@NonNull Disposable... disposables) {
        ObjectHelper.requireNonNull(disposables, "disposables is null");
        if (!disposed) {
            synchronized (this) {
                if (!disposed) {
                    OpenHashSet<Disposable> set = resources;
                    if (set == null) {
                        set = new OpenHashSet<>(disposables.length + 1);
                        resources = set;
                    }
                    for (Disposable d : disposables) {
                        ObjectHelper.requireNonNull(d, "A Disposable in the disposables array is null");
                        set.add(d);
                    }
                    return true;
                }
            }
        }
        for (Disposable d : disposables) {
            d.dispose();
        }
        return false;
    }

    /**
     * Removes and disposes the given {@link Disposable} if it is part of this
     * container.
     *
     * @param disposable the disposable to remove and dispose, not {@code null}
     * @return {@code true} if the operation was successful
     * @throws NullPointerException if {@code disposable} is {@code null}
     */
    @Override
    public boolean remove(@NonNull Disposable disposable) {
        if (delete(disposable)) {
            disposable.dispose();
            return true;
        }
        return false;
    }

    /**
     * Removes (but does not dispose) the given {@link Disposable} if it is part of this
     * container.
     *
     * @param disposable the disposable to remove, not {@code null}
     * @return {@code true} if the operation was successful
     * @throws NullPointerException if {@code disposable} is {@code null}
     */
    @Override
    public boolean delete(@NonNull Disposable disposable) {
        ObjectHelper.requireNonNull(disposable, "disposable is null");
        if (disposed) {
            return false;
        }
        synchronized (this) {
            if (disposed) {
                return false;
            }

            OpenHashSet<Disposable> set = resources;
            if (set == null || !set.remove(disposable)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Atomically clears the container, then disposes all the previously contained {@link Disposable}s.
     */
    public void clear() {
        if (disposed) {
            return;
        }
        OpenHashSet<Disposable> set;
        synchronized (this) {
            if (disposed) {
                return;
            }

            set = resources;
            resources = null;
        }

        dispose(set);
    }

    /**
     * Returns the number of currently held {@link Disposable}s.
     *
     * @return the number of currently held {@code Disposable}s
     */
    public int size() {
        if (disposed) {
            return 0;
        }
        synchronized (this) {
            if (disposed) {
                return 0;
            }
            OpenHashSet<Disposable> set = resources;
            return set != null ? set.size() : 0;
        }
    }

    /**
     * Dispose the contents of the {@link OpenHashSet} by suppressing non-fatal
     * {@link Throwable}s till the end.
     *
     * @param set the {@code OpenHashSet} to dispose elements of
     */
    void dispose(@Nullable OpenHashSet<Disposable> set) {
        if (set == null) {
            return;
        }
        List<Throwable> errors = null;
        Object[] array = set.keys();
        for (Object o : array) {
            if (o instanceof Disposable) {
                try {
                    ((Disposable) o).dispose();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    if (errors == null) {
                        errors = new ArrayList<>();
                    }
                    errors.add(ex);
                }
            }
        }
        if (errors != null) {
            if (errors.size() == 1) {
                throw ExceptionHelper.wrapOrThrow(errors.get(0));
            }
            throw new CompositeException(errors);
        }
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner lifecycleOwner, @NonNull Lifecycle.Event event) {
        if (event.getTargetState() == Lifecycle.State.DESTROYED) {
            lifecycleOwner.getLifecycle().removeObserver(this);
            dispose();
        }
    }
}
