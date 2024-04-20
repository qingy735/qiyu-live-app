package org.qiyu.live.msg.provider.config;

import java.util.concurrent.*;

/**
 * @Author: QingY
 * @Date: Created in 19:47 2024-04-19
 * @Description:
 */
public class ThreadPoolManager {

    public static ThreadPoolExecutor commonAsyncPool = new ThreadPoolExecutor(2, 8, 3, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1000), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("commonAsyncPool - " + ThreadLocalRandom.current().nextInt(10000));
            return thread;
        }
    });

}
