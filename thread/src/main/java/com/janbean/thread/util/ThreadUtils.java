package com.janbean.thread.util;

import android.text.TextUtils;
import android.util.Log;

import com.janbean.thread.KeepThread;

import org.w3c.dom.Text;

@KeepThread
public class ThreadUtils {
    public static String MARK = "\u2008";

    public static String makeThreadName(final String name) {
        return name == null ? "" : name.startsWith(MARK) ? name : (MARK + name);
    }

    public static String makeThreadName(String name, String prefix) {
        Log.i("hjianbin", "makeThreadName "+name+" "+prefix);
        if (!TextUtils.isEmpty(name) && !name.startsWith(MARK) && !TextUtils.isEmpty(prefix)) {
            return (MARK+prefix + "/" + name);
        }
        return TextUtils.isEmpty(name) ? (MARK + prefix) : (name.startsWith(MARK) || TextUtils.isEmpty(prefix) ? (MARK + name) : (MARK + prefix + "/" + name));
    }

    public static Thread setThreadName(final Thread t, final String prefix) {
        Log.i("hjianbin", "setThreadName " + t.getName() + " prefix " + prefix);
        t.setName(makeThreadName(t.getName(), prefix));
        return t;
    }
}
