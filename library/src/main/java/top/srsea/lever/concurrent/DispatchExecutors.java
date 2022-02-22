package top.srsea.lever.concurrent;

import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class DispatchExecutors {

    /// For UI tasks
    public static Executor main() {
        return Main.INSTANCE;
    }

    /// For IO intensive tasks
    public static ExecutorService elastic() {
        return Elastic.INSTANCE;
    }

    /// For CPU intensive tasks
    public static ExecutorService parallel() {
        return Parallel.INSTANCE;
    }

    public static ExecutorService single() {
        return Single.INSTANCE;
    }

    public static ExecutorService newSingle() {
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public static Executor withLooper(@NonNull Looper looper) {
        return new LooperExecutor(looper);
    }

    private static class Main {
        private static final Executor INSTANCE = withLooper(Looper.getMainLooper());
    }

    private static class Elastic {
        private static final ExecutorService INSTANCE = new ThreadPoolExecutor(0,
                Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    private static class Parallel {
        private static final ExecutorService INSTANCE;

        static {
            final int nThreads = Runtime.getRuntime().availableProcessors();
            INSTANCE = new ThreadPoolExecutor(nThreads, nThreads,
                    0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        }
    }

    private static class Single {
        private static final ExecutorService INSTANCE = newSingle();
    }
}
