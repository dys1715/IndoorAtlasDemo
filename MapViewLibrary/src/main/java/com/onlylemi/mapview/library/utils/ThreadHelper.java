package com.onlylemi.mapview.library.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by dys on 2016/5/23 0023.
 */
public class ThreadHelper {
    private static ExecutorService threadPool;

    static {
//        threadPool = Executors.newFixedThreadPool(10);
//        threadPool = Executors.newCachedThreadPool();
        threadPool = Executors.newSingleThreadExecutor();
    }

    public static ExecutorService getThreadPool() {
        return threadPool;
    }

}