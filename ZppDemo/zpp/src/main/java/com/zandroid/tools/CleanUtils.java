package com.zandroid.tools;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by 墨 on 2018/6/12.
 * cleanInternalCache : 清除内部缓存
 * cleanInternalFiles : 清除内部文件
 * cleanInternalDbs : 清除内部数据库
 * cleanInternalDbByName: 根据名称清除数据库
 * cleanInternalSP : 清除内部 SP
 * cleanExternalCache : 清除外部缓存
 * cleanCustomCache : 清除自定义目录下的文件
 */

public final class CleanUtils {



    private CleanUtils() {

        throw new UnsupportedOperationException("u can't instantiate me...");

    }



    /**

     * Clean the internal cache.

     * <p>directory: /data/data/package/cache</p>

     *

     * @return {@code true}: success<br>{@code false}: fail

     */

    public static boolean cleanInternalCache(Context context) {

        return deleteFilesInDir(context.getCacheDir());

    }



    /**

     * Clean the internal files.

     * <p>directory: /data/data/package/files</p>

     *

     * @return {@code true}: success<br>{@code false}: fail

     */

    public static boolean cleanInternalFiles(Context context) {

        return deleteFilesInDir(context.getFilesDir());

    }



    /**

     * Clean the internal databases.

     * <p>directory: /data/data/package/databases</p>

     *

     * @return {@code true}: success<br>{@code false}: fail

     */

    public static boolean cleanInternalDbs(Context context) {

        return deleteFilesInDir(new File(context.getFilesDir().getParent(), "databases"));

    }



    /**

     * Clean the internal database by name.

     * <p>directory: /data/data/package/databases/dbName</p>

     *

     * @param dbName The name of database.

     * @return {@code true}: success<br>{@code false}: fail

     */

    public static boolean cleanInternalDbByName(Context context,final String dbName) {

        return context.deleteDatabase(dbName);

    }



    /**

     * Clean the internal shared preferences.

     * <p>directory: /data/data/package/shared_prefs</p>

     *

     * @return {@code true}: success<br>{@code false}: fail

     */

    public static boolean cleanInternalSp(Context context) {

        return deleteFilesInDir(new File(context.getFilesDir().getParent(), "shared_prefs"));

    }



    /**

     * Clean the external cache.

     * <p>directory: /storage/emulated/0/android/data/package/cache</p>

     *

     * @return {@code true}: success<br>{@code false}: fail

     */

    public static boolean cleanExternalCache(Context context) {

        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())

                && deleteFilesInDir(context.getExternalCacheDir());

    }



    /**

     * Clean the custom directory.

     *

     * @param dirPath The path of directory.

     * @return {@code true}: success<br>{@code false}: fail

     */

    public static boolean cleanCustomDir(final String dirPath) {

        return deleteFilesInDir(dirPath);

    }



    /**

     * Clean the custom directory.

     *

     * @param dir The directory.

     * @return {@code true}: success<br>{@code false}: fail

     */

    public static boolean cleanCustomDir(final File dir) {

        return deleteFilesInDir(dir);

    }



    public static boolean deleteFilesInDir(final String dirPath) {

        return deleteFilesInDir(getFileByPath(dirPath));

    }



    private static boolean deleteFilesInDir(final File dir) {

        if (dir == null) return false;

        // dir doesn't exist then return true

        if (!dir.exists()) return true;

        // dir isn't a directory then return false

        if (!dir.isDirectory()) return false;

        File[] files = dir.listFiles();

        if (files != null && files.length != 0) {

            for (File file : files) {

                if (file.isFile()) {

                    if (!file.delete()) return false;

                } else if (file.isDirectory()) {

                    if (!deleteDir(file)) return false;

                }

            }

        }

        return true;

    }



    private static boolean deleteDir(final File dir) {

        if (dir == null) return false;

        // dir doesn't exist then return true

        if (!dir.exists()) return true;

        // dir isn't a directory then return false

        if (!dir.isDirectory()) return false;

        File[] files = dir.listFiles();

        if (files != null && files.length != 0) {

            for (File file : files) {

                if (file.isFile()) {

                    if (!file.delete()) return false;

                } else if (file.isDirectory()) {

                    if (!deleteDir(file)) return false;

                }

            }

        }

        return dir.delete();

    }



    private static File getFileByPath(final String filePath) {

        return isSpace(filePath) ? null : new File(filePath);

    }



    private static boolean isSpace(final String s) {

        if (s == null) return true;

        for (int i = 0, len = s.length(); i < len; ++i) {

            if (!Character.isWhitespace(s.charAt(i))) {

                return false;

            }

        }

        return true;

    }

}
