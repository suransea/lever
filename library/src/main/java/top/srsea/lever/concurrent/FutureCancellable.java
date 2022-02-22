package top.srsea.lever.concurrent;

import androidx.annotation.NonNull;

import java.util.concurrent.Future;

public class FutureCancellable implements Cancellable {
    private final Future<?> future;

    public FutureCancellable(@NonNull Future<?> future) {
        this.future = future;
    }

    @Override
    public void cancel() {
        future.cancel(false);
    }
}
