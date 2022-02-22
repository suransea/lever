package top.srsea.lever.concurrent;

import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorDispatcher implements Dispatcher {
    private static final ScheduledExecutorService SCHEDULER =
            new ScheduledThreadPoolExecutor(1);

    private final ExecutorService executor;

    public ExecutorDispatcher(@NonNull ExecutorService executor) {
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
        Task delayedTask = new Task(task);
        delayedTask.futures.add(SCHEDULER.schedule(delayedTask, delayMillis, TimeUnit.MILLISECONDS));
        return delayedTask;
    }

    @NonNull
    @Override
    public Cancellable dispatch(long delayMillis, long periodMillis, @NonNull Runnable task) {
        Task periodTask = new Task(task);
        periodTask.futures.add(SCHEDULER.scheduleAtFixedRate(periodTask,
                delayMillis, periodMillis, TimeUnit.MILLISECONDS));
        return periodTask;
    }

    private class Task implements Runnable, Cancellable {
        final Runnable inner;
        final List<Future<?>> futures = new LinkedList<>();

        Task(@NonNull Runnable inner) {
            this.inner = inner;
        }

        @Override
        public void run() {
            futures.add(executor.submit(inner));
            for (Iterator<Future<?>> it = futures.iterator(); it.hasNext(); ) {
                if (it.next().isDone()) it.remove();
            }
        }

        @Override
        public void cancel() {
            for (Future<?> future : futures) {
                future.cancel(false);
            }
        }
    }
}
