package com.janbean.androidperformance;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

public class Test2 extends FileProvider {

    class PathStrategy {

    }

    private PathStrategy mStrategy;
    private int mResourceId;
    private String authority;

    @Override
    public void attachInfo(@NonNull Context context, @NonNull ProviderInfo info) {
        if (!info.grantUriPermissions) {
            throw new SecurityException("Provider must grant uri permissions");
        }

        info.grantUriPermissions = false;
        try {
            super.attachInfo(context, info);
            this.authority = info.authority.split(";")[0];
        } catch (Throwable t) {
            // ignore
            t.printStackTrace();
        }
        info.grantUriPermissions = true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable Bundle queryArgs, @Nullable CancellationSignal cancellationSignal) {
        if (mStrategy == null) {
            mStrategy = getPathStrategy(getContext(), authority, mResourceId);
        }
        return super.query(uri, projection, queryArgs, cancellationSignal);
    }

    private PathStrategy getPathStrategy(Context context, String authority, int resourceId) {
        return new PathStrategy();
    }
}
