package com.janbean.common.reflection;

import android.os.Build.VERSION;
import android.util.Log;

import java.lang.reflect.Method;

class BootstrapClass {
    private static final String TAG = "BootstrapClass";
    private static Object sVmRuntime;
    private static Method setHiddenApiExemptions;

    BootstrapClass() {
    }

    public static boolean exempt(String method) {
        return exempt(method);
    }

    public static boolean exempt(Object... methods) {
        if (sVmRuntime != null && setHiddenApiExemptions != null) {
            try {
                setHiddenApiExemptions.invoke(sVmRuntime, methods);
                return true;
            } catch (Throwable var2) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * enable hidden api be hooked
     * @return
     */
    public static boolean exemptAll() {
        return exempt("L");
    }

    static {
        if (VERSION.SDK_INT >= 28) {
            try {
                Method forName = Class.class.getDeclaredMethod("forName", String.class);
                Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
                Class<?> vmRuntimeClass = (Class)forName.invoke((Object)null, "dalvik.system.VMRuntime");
                Method getRuntime = (Method)getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
                setHiddenApiExemptions = (Method)getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
                sVmRuntime = getRuntime.invoke((Object)null);
            } catch (Throwable var4) {
                Log.w("BootstrapClass", "reflect bootstrap failed:", var4);
            }
        }

    }
}
