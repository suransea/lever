package top.srsea.lever.concurrent;

import androidx.annotation.NonNull;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorDispatcher implements Dispatcher {
    private final ScheduledExecutorService executor;

    public ScheduledExecutorDispatcher(@NonNull ScheduledExecutorService executor) {
        this.executor = executor;
    }

    @NonNull
    @Override
    public Cancellable dispatch(@NonNull Runnable task) {
        return new FutureCancellable(executor.submit(task));
    }

    @NonNull
    @Override
    public Cancellable dispatch(long delayMillis, @NonNull Runnable task) {
        return new FutureCancellable(executor.schedule(task, delayMillis, TimeUnit.MILLISECONDS));
    }

    @NonNull
    @Override
    public Cancellable dispatch(long delayMillis, long periodMillis, @NonNull Runnable task) {
        return new FutureCancellable(executor.scheduleAtFixedRate(task, delayMillis, periodMillis,
                TimeUnit.MILLISECONDS));
    }
}
