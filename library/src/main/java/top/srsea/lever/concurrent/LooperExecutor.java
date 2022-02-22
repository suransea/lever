package top.srsea.lever.concurrent;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

public class LooperExecutor implements Executor {
    private final Handler handler;

    public LooperExecutor(@NonNull Looper looper) {
        handler = new Handler(looper);
    }

    @Override
    public void execute(Runnable command) {
        handler.post(command);
    }
}
