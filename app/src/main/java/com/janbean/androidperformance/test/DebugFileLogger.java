package com.janbean.androidperformance.test;


import android.os.Environment;
import android.os.Process;
import android.os.SystemClock;
import android.os.storage.StorageManager;
import android.util.Log;

import com.janbean.androidperformance.MyApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class DebugFileLogger extends Thread implements Logger {
    private static final String TAG = "DebugFileLogger";
    private static final int KEEP_DAY = 7;
    private static final String KEY_MOUNTED = "mounted";
    private static List<LogEntry> logs = Collections.synchronizedList(new LinkedList());
    private SimpleDateFormat timeFormatter;
    private SimpleDateFormat dateFormatter;
    private static File sLogDir;
    private static AtomicInteger sCounter = new AtomicInteger(0);
    private static String sToday;
    private static String sFileTag;
    private static boolean sInited;
    private static int sLogLevel = 4;
    private static boolean sPaused = true;
    private static final Object mLock = new Object();
    private static volatile DebugFileLogger sDebugFileLogger;

    /** @deprecated */
    @Deprecated
    public static void setLogLevel(int level) {
        if (level >= 2 && level <= 7) {
            Log.e("DebugFileLogger", "##### set log level->" + level);
            sLogLevel = level;
        } else {
            Log.e("DebugFileLogger", "invalid log level->" + level);
        }

    }

    public static DebugFileLogger getInstance() {
        if (sDebugFileLogger == null) {
            Class var0 = DebugFileLogger.class;
            synchronized(DebugFileLogger.class) {
                if (sDebugFileLogger == null) {
                    DebugFileLogger debugFileLogger = new DebugFileLogger();
                    debugFileLogger.init();
                    sDebugFileLogger = debugFileLogger;
                }
            }
        }

        return sDebugFileLogger;
    }

    private DebugFileLogger() {
        super("debug-logger");
        this.timeFormatter = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    }

    private void init() {
        String tag = "ui";
        File parentDir = MyApplication.Companion.getInstance().getExternalFilesDir((String)null);
        if (parentDir == null) {
            parentDir = MyApplication.Companion.getInstance().getFilesDir();
        }

        File logDir = new File(parentDir, "/log/");
        this.initialize(logDir, tag);
    }

    private void initialize(File logDir, String fileTag) {
        this.initialize(logDir, fileTag, 4);
    }

    private void initialize(File logDir, String fileTag, int logLevel) {
        if (!isExternalStorageExists()) {
            sInited = false;
        } else {
            sInited = true;
            sLogDir = logDir;
            sFileTag = fileTag;
            Log.e("DebugFileLogger", "###init file logger:" + fileTag + "->" + logDir + ",lv:" + sLogLevel);
            this.start();
        }
    }

    public static boolean isExternalStorageExists() {
        return Environment.getExternalStorageState().equalsIgnoreCase(KEY_MOUNTED);
    }

    public void flush() {
    }

    public int v(String tag, String msg) {
        this.log(2, tag, msg);
        return 1;
    }

    public int d(String tag, String msg) {
        this.log(3, tag, msg);
        return 1;
    }

    public int i(String tag, String msg) {
        this.log(4, tag, msg);
        return 1;
    }

    public int w(String tag, String msg) {
        this.log(5, tag, msg);
        return 1;
    }

    public int e(String tag, String msg) {
        this.log(6, tag, msg);
        return 1;
    }

    private void log(int level, String tag, String message) {
        if (sInited) {
            if (level >= sLogLevel) {
                LogEntry entry = new LogEntry();
                entry.counter = sCounter.getAndIncrement();
                entry.rtcTime = System.currentTimeMillis();
                entry.clockTime = SystemClock.elapsedRealtime();
                entry.level = level;
                entry.tag = tag;
                entry.message = message;
                logs.add(entry);
                if (sPaused) {
                    synchronized(mLock) {
                        sPaused = false;
                        mLock.notifyAll();
                    }
                }

            }
        }
    }

    public void run() {
        Process.setThreadPriority(1);

        while(true) {
            final long now = System.currentTimeMillis();
            String today = this.dateFormatter.format(new Date(now));
            if (sLogDir.isDirectory() && !today.equals(sToday)) {
                sToday = today;
                File[] oldFiles = sLogDir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".txt") && filename.startsWith(DebugFileLogger.sFileTag)) {
                            try {
                                long time = DebugFileLogger.this.dateFormatter.parse(filename.substring(DebugFileLogger.sFileTag.length() + 1, filename.length() - 3)).getTime();
                                return now - time > 604800000L;
                            } catch (Exception var5) {
                                Log.w("DebugFileLogger", "parse date failed", var5);
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                });
                if (oldFiles != null) {
                    File[] var5 = oldFiles;
                    int var6 = oldFiles.length;

                    for(int var7 = 0; var7 < var6; ++var7) {
                        File file = var5[var7];
                        file.delete();
                    }
                }
            } else {
                sLogDir.mkdirs();
            }

            String newFileName = sLogDir + File.separator + sFileTag + "_" + today + ".txt";
            FileWriter writer = null;

            label176: {
                try {
                    writer = new FileWriter(newFileName, true);

                    while(true) {
                        if (logs.isEmpty()) {
                            break label176;
                        }

                        LogEntry entry = (LogEntry)logs.get(0);
                        String text = entry.message;
                        String time = this.timeFormatter.format(new Date(entry.rtcTime));
                        writer.write(String.format("[%d][%s][%s(%d)]%s:%s\n", entry.counter, this.getLogLevel(entry.level), time, entry.clockTime, entry.tag, text));
                        if (logs.size() > 0) {
                            logs.remove(0);
                        }
                    }
                } catch (IOException var23) {
                    if (isExternalStorageExists()) {
                        break label176;
                    }

                    logs.clear();
                    sInited = false;
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException var20) {
                        }
                    }

                }

                return;
            }

            synchronized(mLock) {
                sPaused = true;

                try {
                    mLock.wait();
                } catch (InterruptedException var21) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private String getLogLevel(int level) {
        switch (level) {
            case 2:
                return "VERBOSE";
            case 3:
                return "DEBUG";
            case 4:
                return "INFO";
            case 5:
                return "WARN";
            case 6:
                return "ERROR";
            case 7:
                return "ASSERT";
            default:
                return "UNKNOWN";
        }
    }

    private static class LogEntry {
        int counter;
        long rtcTime;
        long clockTime;
        int level;
        String tag;
        String message;

        private LogEntry() {
        }
    }
}
