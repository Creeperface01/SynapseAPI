package org.itxtech.synapseapi.utils;

import cn.nukkit.utils.Logger;
import cn.nukkit.utils.MainLogger;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @author CreeperFace
 */
public class ThreadUtils {

    public static void dumpThreads() {
        MainLogger logger = MainLogger.getLogger();
        logger.emergency("---------------- All threads ----------------");
        ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);

        for (int i = 0; i < threads.length; ++i) {
            if (i != 0) {
                logger.emergency("------------------------------");
            }

            dumpThread(threads[i], logger);
        }

        logger.emergency("---------------------------------------------");
    }

    private static void dumpThread(ThreadInfo thread, Logger logger) {
        logger.emergency("Current Thread: " + thread.getThreadName());
        logger.emergency("\tPID: " + thread.getThreadId() + " | Suspended: " + thread.isSuspended() + " | Native: " + thread.isInNative() + " | State: " + thread.getThreadState());
        int var4;
        if (thread.getLockedMonitors().length != 0) {
            logger.emergency("\tThread is waiting on monitor(s):");
            MonitorInfo[] var2 = thread.getLockedMonitors();
            int var3 = var2.length;

            for (var4 = 0; var4 < var3; ++var4) {
                MonitorInfo monitor = var2[var4];
                logger.emergency("\t\tLocked on:" + monitor.getLockedStackFrame());
            }
        }

        ThreadMXBean mx = ManagementFactory.getThreadMXBean();
        logger.emergency("\tTotal thread time: " + mx.getThreadCpuTime(thread.getThreadId()));
        logger.emergency("\tThread average CPU usage: " + mx.getThreadCpuTime(thread.getThreadId()));
        logger.emergency("\tStack:");
        StackTraceElement[] var8 = thread.getStackTrace();
        var4 = var8.length;

        for (int var9 = 0; var9 < var4; ++var9) {
            StackTraceElement stack = var8[var9];
            logger.emergency("\t\t" + stack);
        }

    }
}
