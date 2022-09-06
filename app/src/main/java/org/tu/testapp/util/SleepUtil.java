package org.tu.testapp.util;

import android.util.Log;

import java.util.concurrent.TimeUnit;

public class SleepUtil {

    public static void sleep(long seconds) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
        } catch (Exception e) {
            Log.d("recycler-view-adapter", "sleeping interrupted");
        }
    }
}
