package com.zandroid.tools;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by bstadmin on 2018/6/11.
 * closeIO       : 关闭 IO
 * closeIOQuietly: 安静关闭 IO
 */

public final class CloseUtils {


    private CloseUtils() {

        throw new UnsupportedOperationException("u can't instantiate me...");

    }


    /**
     * Close the io stream.
     *
     * @param closeables closeables
     */

    public static void closeIO(final Closeable... closeables) {

        if (closeables == null) return;

        for (Closeable closeable : closeables) {

            if (closeable != null) {

                try {

                    closeable.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }


    /**
     * Close the io stream quietly.
     *
     * @param closeables closeables
     */

    public static void closeIOQuietly(final Closeable... closeables) {

        if (closeables == null) return;

        for (Closeable closeable : closeables) {

            if (closeable != null) {

                try {

                    closeable.close();

                } catch (IOException ignored) {

                }

            }

        }

    }

}