package top.srsea.lever.concurrent;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

public class LooperDispatcher extends AbstractDispatcher {
    private final Handler handler;

    public LooperDispatcher(@NonNull Looper looper) {
        handler = new Handler(looper);
    }

    @NonNull
    @Override
    public Cancellable dispatch(long delayMillis, @NonNull final Runnable task) {
        handler.postDelayed(task, delayMillis);
        return new Cancellable() {
            @Override
            public void cancel() {
                handler.removeCallbacks(task);
            }
        };
    }
}
