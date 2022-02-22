package top.srsea.lever.concurrent;

import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public final class Dispatchers {

    /// For UI tasks
    public static Dispatcher main() {
        return Main.INSTANCE;
    }

    /// For IO intensive tasks
    public static Dispatcher elastic() {
        return Elastic.INSTANCE;
    }

    /// For CPU intensive tasks
    public static Dispatcher parallel() {
        return Parallel.INSTANCE;
    }

    public static Dispatcher single() {
        return Single.INSTANCE;
    }

    public static Dispatcher newSingle() {
        return new ExecutorLooperDispatcher(DispatchExecutors.newSingle(), Looper.getMainLooper());
    }

    public static Dispatcher withLooper(@NonNull Looper looper) {
        return new LooperDispatcher(looper);
    }

    public static Dispatcher withExecutor(@NonNull ExecutorService executor) {
        return new ExecutorDispatcher(executor);
    }

    public static Dispatcher withExecutor(@NonNull ExecutorService executor, @NonNull Looper scheduler) {
        return new ExecutorLooperDispatcher(executor, scheduler);
    }

    public static Dispatcher withScheduledExecutor(@NonNull ScheduledExecutorService executor) {
        return new ScheduledExecutorDispatcher(executor);
    }

    private static class Main {
        private static final Dispatcher INSTANCE = withLooper(Looper.getMainLooper());
    }

    private static class Elastic {
        private static final Dispatcher INSTANCE = withExecutor(DispatchExecutors.elastic(),
                Looper.getMainLooper());
    }

    private static class Parallel {
        private static final Dispatcher INSTANCE = withExecutor(DispatchExecutors.parallel(),
                Looper.getMainLooper());
    }

    private static class Single {
        private static final Dispatcher INSTANCE = newSingle();
    }
}
