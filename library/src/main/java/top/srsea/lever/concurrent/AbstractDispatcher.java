package top.srsea.lever.concurrent;

import android.os.SystemClock;

import androidx.annotation.NonNull;

public abstract class AbstractDispatcher implements Dispatcher {

    @NonNull
    public Cancellable dispatch(@NonNull Runnable task) {
        return dispatch(0L, task);
    }

    @NonNull
    public Cancellable dispatch(long delayMillis, long periodMillis, @NonNull Runnable task) {
        long startMillis = SystemClock.uptimeMillis();
        PeriodTask periodTask = new PeriodTask(task, startMillis, delayMillis, periodMillis);
        periodTask.cancellable = dispatch(delayMillis, periodTask);
        return periodTask;
    }

    private class PeriodTask implements Runnable, Cancellable {
        final Runnable inner;
        final long startMillis;
        final long delayMillis;
        final long periodMillis;
        int tickCount;
        volatile boolean canceled;
        Cancellable cancellable;

        PeriodTask(@NonNull Runnable inner, long startMillis, long delayMillis, long periodMillis) {
            this.inner = inner;
            this.startMillis = startMillis;
            this.delayMillis = delayMillis;
            this.periodMillis = periodMillis;
        }

        @Override
        public void run() {
            if (canceled) return;
            ++tickCount;
            long nextTickMillis = startMillis + delayMillis + tickCount * periodMillis;
            cancellable = dispatch(nextTickMillis - SystemClock.uptimeMillis(), this);
            inner.run();
        }

        @Override
        public void cancel() {
            canceled = true;
            cancellable.cancel();
        }
    }
}
