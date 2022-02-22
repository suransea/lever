package top.srsea.lever.concurrent;

import androidx.annotation.NonNull;

public interface Dispatcher {

    @NonNull
    Cancellable dispatch(@NonNull Runnable task);

    @NonNull
    Cancellable dispatch(long delayMillis, @NonNull Runnable task);

    @NonNull
    Cancellable dispatch(long delayMillis, long periodMillis, @NonNull Runnable task);
}
