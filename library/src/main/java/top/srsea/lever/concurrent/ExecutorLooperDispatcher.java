package top.srsea.lever.concurrent;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ExecutorLooperDispatcher extends AbstractDispatcher {
    private final ExecutorService executor;
    private final Handler handler;

    public ExecutorLooperDispatcher(@NonNull ExecutorService executor, @NonNull Looper looper) {
        this.executor = executor;
        handler = new Handler(looper);
    }

    @NonNull
    @Override
    public Cancellable dispatch(@NonNull Runnable task) {
        return new FutureCancellable(executor.submit(task));
    }

    @NonNull
    @Override
    public Cancellable dispatch(long delayMillis, @NonNull Runnable task) {
        DelayedTask delayedTask = new DelayedTask(task);
        handler.postDelayed(delayedTask, delayMillis);
        return delayedTask;
    }

    private class DelayedTask implements Runnable, Cancellable {
        final Runnable inner;
        @Nullable
        Future<?> future;

        DelayedTask(@NonNull Runnable inner) {
            this.inner = inner;
        }

        @Override
        public void run() {
            future = executor.submit(inner);
        }

        @Override
        public void cancel() {
            if (future != null) {
                future.cancel(false);
            } else {
                handler.removeCallbacks(this);
            }
        }
    }
}
