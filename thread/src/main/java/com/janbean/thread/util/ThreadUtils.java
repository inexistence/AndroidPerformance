package com.janbean.thread.util;

import android.text.TextUtils;

import com.janbean.thread.KeepThread;

import org.w3c.dom.Text;

@KeepThread
public class ThreadUtils {
    public static String MARK = "\u2008";

    public static String makeThreadName(final String name) {
        return name == null ? "" : name.startsWith(MARK) ? name : (MARK + name);
    }

    public static String makeThreadName(String name, String prefix) {
        return name == null ? prefix : (name.startsWith(MARK) || TextUtils.isEmpty(prefix) ? name : (prefix + "#" + name));
    }

    public static Thread setThreadName(final Thread t, final String prefix) {
        t.setName(makeThreadName(t.getName(), prefix));
        return t;
    }
}
