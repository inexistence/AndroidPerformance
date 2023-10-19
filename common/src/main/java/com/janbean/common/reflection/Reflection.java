package com.janbean.common.reflection;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

import dalvik.system.DexFile;

public class Reflection {
    private static final byte[] DEX = new byte[]{100, 101, 120, 10, 48, 51, 53, 0, -63, 79, -99, 45, -6, -54, 70, -12, -39, -96, 38, 22, 78, 20, -37, -15, -105, 93, -34, 49, 25, 64, -2, 103, -124, 2, 0, 0, 112, 0, 0, 0, 120, 86, 52, 18, 0, 0, 0, 0, 0, 0, 0, 0, -4, 1, 0, 0, 11, 0, 0, 0, 112, 0, 0, 0, 5, 0, 0, 0, -100, 0, 0, 0, 3, 0, 0, 0, -80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, -44, 0, 0, 0, 1, 0, 0, 0, -4, 0, 0, 0, 104, 1, 0, 0, 28, 1, 0, 0, 34, 1, 0, 0, 36, 1, 0, 0, 44, 1, 0, 0, 47, 1, 0, 0, 52, 1, 0, 0, 79, 1, 0, 0, 99, 1, 0, 0, 102, 1, 0, 0, 106, 1, 0, 0, 127, 1, 0, 0, -117, 1, 0, 0, 3, 0, 0, 0, 4, 0, 0, 0, 5, 0, 0, 0, 6, 0, 0, 0, 8, 0, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 3, 0, 0, 0, 28, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 1, 0, 0, 0, 9, 0, 0, 0, 1, 0, 2, 0, 10, 0, 0, 0, 2, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 17, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0, 0, -20, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 6, 60, 105, 110, 105, 116, 62, 0, 1, 76, 0, 3, 76, 76, 59, 0, 25, 76, 100, 97, 108, 118, 105, 107, 47, 115, 121, 115, 116, 101, 109, 47, 86, 77, 82, 117, 110, 116, 105, 109, 101, 59, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 0, 1, 86, 0, 2, 86, 76, 0, 19, 91, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 0, 10, 103, 101, 116, 82, 117, 110, 116, 105, 109, 101, 0, 22, 115, 101, 116, 72, 105, 100, 100, 101, 110, 65, 112, 105, 69, 120, 101, 109, 112, 116, 105, 111, 110, 115, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 112, 16, 4, 0, 0, 0, 14, 0, 4, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 113, 0, 2, 0, 0, 0, 12, 3, 18, 16, 35, 2, 4, 0, 18, 1, 26, 0, 2, 0, 77, 0, 2, 1, 110, 32, 3, 0, 35, 0, 14, 0, 0, 0, 2, 0, 0, -127, -128, 4, -92, 3, 1, 9, -68, 3, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 11, 0, 0, 0, 112, 0, 0, 0, 2, 0, 0, 0, 5, 0, 0, 0, -100, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, -80, 0, 0, 0, 5, 0, 0, 0, 5, 0, 0, 0, -44, 0, 0, 0, 6, 0, 0, 0, 1, 0, 0, 0, -4, 0, 0, 0, 1, 16, 0, 0, 1, 0, 0, 0, 28, 1, 0, 0, 2, 32, 0, 0, 11, 0, 0, 0, 34, 1, 0, 0, 1, 32, 0, 0, 2, 0, 0, 0, -92, 1, 0, 0, 0, 32, 0, 0, 1, 0, 0, 0, -20, 1, 0, 0, 0, 16, 0, 0, 1, 0, 0, 0, -4, 1, 0, 0};
    private static boolean unseal = false;

    public Reflection() {
    }

    /**
     * 注意，虽然加了方法锁，但是内部的部分系统方法是非线程安全的，多线程可能崩溃
     * @param context
     * @return
     */
    public static synchronized int unseal(Context context) {
        if (VERSION.SDK_INT < 28) {
            return 0;
        } else if (unseal) {
            return 0;
        } else if (unsealByDexFile(context)) {
            unseal = true;
            return 0;
        } else if (BootstrapClass.exemptAll()) {
            unseal = true;
            return 0;
        } else {
            return -1;
        }
    }

    private static boolean unsealByDexFile(Context context) {
        File codeCacheDir = getCodeCacheDir(context);
        if (codeCacheDir == null) {
            return false;
        } else {
            File code = new File(codeCacheDir, System.currentTimeMillis() + ".dex");

            boolean var4;
            try {
                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(code);
                    fos.write(DEX);
                } catch (Exception var14) {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (Exception var13) {
                        }
                    }

                    boolean var5 = false;
                    return var5;
                }

                DexFile dexFile = new DexFile(code);
                Class<?> bootstrapClass = dexFile.loadClass("L", (ClassLoader)null);
                Method exemptAll = bootstrapClass.getDeclaredMethod("L");
                exemptAll.invoke((Object)null);
                boolean var7 = true;
                return var7;
            } catch (Throwable var15) {
                var15.printStackTrace();
                var4 = false;
            } finally {
                if (code.exists()) {
                    code.delete();
                }

            }

            return var4;
        }
    }

    private static File getCodeCacheDir(Context context) {
        if (context != null) {
            if (VERSION.SDK_INT >= 21) {
                return context.getCodeCacheDir();
            } else {
                ApplicationInfo appInfo = context.getApplicationInfo();
                return createFilesDir(new File(appInfo.dataDir, "code_cache"));
            }
        } else {
            String tmpDir = System.getProperty("java.io.tmpdir");
            if (TextUtils.isEmpty(tmpDir)) {
                return null;
            } else {
                File tmp = new File(tmpDir);
                return !tmp.exists() ? null : tmp;
            }
        }
    }

    private static synchronized File createFilesDir(File file) {
        if (!file.exists() && !file.mkdirs()) {
            return file.exists() ? file : null;
        } else {
            return file;
        }
    }
}