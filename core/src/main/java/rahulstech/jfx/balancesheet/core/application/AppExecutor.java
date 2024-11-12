package rahulstech.jfx.balancesheet.core.application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutor {

    private static final int MAX_IO_THREADS = 32;

    private static final int MAX_CPU_THREADS = Runtime.getRuntime().availableProcessors();

    private AppExecutor() {}

    private static ExecutorService ioExecutor;

    public static ExecutorService getIOExecutor() {
        if (null == ioExecutor) {
            ioExecutor = Executors.newFixedThreadPool(MAX_IO_THREADS);
        }
        return ioExecutor;
    }

    private static ExecutorService cpuExecutor;

    public static ExecutorService getCpuExecutor() {
        if (null == cpuExecutor) {
            cpuExecutor = Executors.newFixedThreadPool(MAX_CPU_THREADS);
        }
        return cpuExecutor;
    }
}
