package com.list.task.util;

import android.util.Log;

/**
 * Custom logging class
 *
 * @author aleksandrTmk
 */
public class Blog {
    private static final String TAG = "TaskList";
    private static final short V = 0;
    private static final short D = 1;
    private static final short I = 2;
    private static final short W = 3;
    private static final short E = 4;

    private Blog(){
        // no instance
    }

    public static void v(Class clazz, String msg){
        log(V, clazz, msg);
    }

    public static void d(Class clazz, String msg){
        log(D, clazz, msg);
    }

    public static void i(Class clazz, String msg){
        log(I, clazz, msg);
    }

    public static void w(Class clazz, String msg){
        log(W, clazz, msg);
    }

    public static void e(Class clazz, String msg){
        log(E, clazz, msg);
    }

    private static void log(short type, Class clazz, String msg){
        msg = clazz.getSimpleName() + ":   " + msg;

        switch (type){
            case V:
                Log.v(TAG, msg);
                break;
            case D:
                Log.d(TAG, msg);
                break;
            case I:
                Log.i(TAG, msg);
                break;
            case W:
                Log.w(TAG, msg);
                break;
            case E:
                Log.e(TAG, msg);
                break;
        }
    }
}
