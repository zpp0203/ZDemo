package com.zandroid.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 墨 on 2018/6/12.
 * isPad  ：判断是否平板设备
 * isInstallApp : 判断 App 是否安装
 * installApp : 安装 App（支持 8.0）
 * installAppSilent : 静默安装 App
 * uninstallApp : 卸载 App
 * uninstallAppSilent : 静默卸载 App
 * isAppRoot : 判断 App 是否有 root 权限
 * launchApp : 打开 App
 * exitApp : 关闭应用
 * getAppPackageName : 获取 App 包名
 * getAppDetailsSettings: 获取 App 具体设置
 * getAppName : 获取 App 名称
 * getAppIcon : 获取 App 图标
 * getAppPath : 获取 App 路径
 * getAppVersionName : 获取 App 版本号
 * getAppVersionCode : 获取 App 版本码
 * isSystemApp : 判断 App 是否是系统应用
 * isAppDebug : 判断 App 是否是 Debug 版本
 * getAppSignature : 获取 App 签名
 * getAppSignatureSHA1 : 获取应用签名的的 SHA1 值
 * isAppForeground : 判断 App 是否处于前台
 * getForegroundApp : 获取前台应用包名
 * getAppInfo : 获取 App 信息
 * getAppsInfo : 获取所有已安装 App 信息
 * cleanAppData : 清除 App 所有数据
 * getAppMetaDataString :获取manifest文件的meta-data的值
 */

public final class AppUtils {

    private static AppUtils instance;
    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static AppUtils getAppUtils() {
        if (instance == null) {
            instance = new AppUtils();
        }
        return instance;
    }

    /**
     * 判断是否平板设备
     * @param context
     * @return true:平板,false:手机
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     * @param filePath The path of file.
     */
    public void installApp(Application app,final String filePath) {
        installApp(app,getFileByPath(filePath));
    }

    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     * @param file The file.
     */

    public void installApp(Application app,final File file) {
        if (!isFileExists(file)) return;
        app.startActivity(IntentUtils.getIntentUtils().getInstallAppIntent(app,file, true));
    }



    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     * @param filePath  The path of file.
     * @param authority Target APIs greater than 23 must hold the authority of a FileProvider
     *                  defined in a {@code <provider>} element in your app's manifest.
     */
    @Deprecated
    public void installApp(Application app,final String filePath, final String authority) {
        installApp(app,getFileByPath(filePath), authority);
    }


    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     * @param file      The file.
     * @param authority Target APIs greater than 23 must hold the authority of a FileProvider
     *                  defined in a {@code <provider>} element in your app's manifest.
     */
    @Deprecated
    public void installApp(Application app,final File file, final String authority) {
        if (!isFileExists(file)) return;
        app.startActivity(IntentUtils.getIntentUtils().getInstallAppIntent(app,file, authority, true));
    }



    /**

     * Install the app.

     * <p>Target APIs greater than 25 must hold

     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>

     *

     * @param activity    The activity.

     * @param filePath    The path of file.

     * @param requestCode If &gt;= 0, this code will be returned in

     *                    onActivityResult() when the activity exits.

     */

    public void installApp(final Activity activity,

                                  final String filePath,

                                  final int requestCode) {

        installApp(activity, getFileByPath(filePath), requestCode);

    }



    /**

     * Install the app.

     * <p>Target APIs greater than 25 must hold

     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>

     *

     * @param activity    The activity.

     * @param file        The file.

     * @param requestCode If &gt;= 0, this code will be returned in

     *                    onActivityResult() when the activity exits.

     */

    public void installApp(final Activity activity,

                                  final File file,

                                  final int requestCode) {

        if (!isFileExists(file)) return;

        activity.startActivityForResult(IntentUtils.getIntentUtils().getInstallAppIntent(activity.getApplication(),file), requestCode);

    }



    /**

     * Install the app.

     * <p>Target APIs greater than 25 must hold

     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>

     *

     * @param activity    The activity.

     * @param filePath    The path of file.

     * @param authority   Target APIs greater than 23 must hold the authority of a FileProvider

     *                    defined in a {@code <provider>} element in your app's manifest.

     * @param requestCode If &gt;= 0, this code will be returned in

     *                    onActivityResult() when the activity exits.

     */

    @Deprecated

    public void installApp(final Activity activity,

                                  final String filePath,

                                  final String authority,

                                  final int requestCode) {

        installApp(activity, getFileByPath(filePath), authority, requestCode);

    }



    /**

     * Install the app.

     * <p>Target APIs greater than 25 must hold

     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>

     *

     * @param activity    The activity.

     * @param file        The file.

     * @param authority   Target APIs greater than 23 must hold the authority of a FileProvider

     *                    defined in a {@code <provider>} element in your app's manifest.

     * @param requestCode If &gt;= 0, this code will be returned in

     *                    onActivityResult() when the activity exits.

     */

    @Deprecated

    public void installApp(final Activity activity,

                                  final File file,

                                  final String authority,

                                  final int requestCode) {

        if (!isFileExists(file)) return;

        activity.startActivityForResult(IntentUtils.getIntentUtils().getInstallAppIntent(activity.getApplication(),file, authority),

                requestCode);

    }



    /**

     * Install the app silently.

     * <p>Without root permission must hold

     * {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>

     *

     * @param filePath The path of file.

     * @return {@code true}: success<br>{@code false}: fail

     */

    public boolean installAppSilent(final String filePath) {

        return installAppSilent(getFileByPath(filePath), null);

    }



    /**

     * Install the app silently.

     * <p>Without root permission must hold

     * {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>

     *

     * @param file The file.

     * @return {@code true}: success<br>{@code false}: fail

     */

    public boolean installAppSilent(final File file) {

        return installAppSilent(file, null);

    }





    /**

     * Install the app silently.

     * <p>Without root permission must hold

     * {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>

     *

     * @param filePath The path of file.

     * @param params   The params of installation(e.g.,<code>-r</code>, <code>-s</code>).

     * @return {@code true}: success<br>{@code false}: fail

     */

    public boolean installAppSilent(final String filePath, final String params) {

        return installAppSilent(getFileByPath(filePath), params);

    }



    /**

     * Install the app silently.

     * <p>Without root permission must hold

     * {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>

     *

     * @param file   The file.

     * @param params The params of installation(e.g.,<code>-r</code>, <code>-s</code>).

     * @return {@code true}: success<br>{@code false}: fail

     */

    public boolean installAppSilent(final File file, final String params) {

        return installAppSilent(file, params, isDeviceRooted());

    }



    /**

     * Install the app silently.

     * <p>Without root permission must hold

     * {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>

     *

     * @param file     The file.

     * @param params   The params of installation(e.g.,<code>-r</code>, <code>-s</code>).

     * @param isRooted True to use root, false otherwise.

     * @return {@code true}: success<br>{@code false}: fail

     */

    public boolean installAppSilent(final File file,

                                           final String params,

                                           final boolean isRooted) {

        if (!isFileExists(file)) return false;

        String filePath = '"' + file.getAbsolutePath() + '"';

        String command = "LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm install " +

                (params == null ? "" : params + " ")

                + filePath;

        ShellUtils.CommandResult commandResult = ShellUtils.execCmd(command, isRooted);

        if (commandResult.successMsg != null

                && commandResult.successMsg.toLowerCase().contains("success")) {

            return true;

        } else {

            Log.e("AppUtils", "installAppSilent successMsg: " + commandResult.successMsg +

                    ", errorMsg: " + commandResult.errorMsg);

            return false;

        }

    }



    /**

     * Uninstall the app.

     *

     * @param packageName The name of the package.

     */

    public void uninstallApp(Application app,final String packageName) {

        if (isSpace(packageName)) return;

        app.startActivity(IntentUtils.getIntentUtils().getUninstallAppIntent(packageName, true));

    }



    /**

     * Uninstall the app.

     *

     * @param activity    The activity.

     * @param packageName The name of the package.

     * @param requestCode If &gt;= 0, this code will be returned in

     *                    onActivityResult() when the activity exits.

     */

    public void uninstallApp(final Activity activity,

                                    final String packageName,

                                    final int requestCode) {

        if (isSpace(packageName)) return;

        activity.startActivityForResult(

                IntentUtils.getIntentUtils().getUninstallAppIntent(packageName),

                requestCode

        );

    }



    /**

     * Uninstall the app silently.

     * <p>Without root permission must hold

     * {@code <uses-permission android:name="android.permission.DELETE_PACKAGES" />}</p>

     *

     * @param packageName The name of the package.

     * @return {@code true}: success<br>{@code false}: fail

     */

    public boolean uninstallAppSilent(final String packageName) {

        return uninstallAppSilent(packageName, false);

    }



    /**

     * Uninstall the app silently.

     * <p>Without root permission must hold

     * {@code <uses-permission android:name="android.permission.DELETE_PACKAGES" />}</p>

     *

     * @param packageName The name of the package.

     * @param isKeepData  Is keep the data.

     * @return {@code true}: success<br>{@code false}: fail

     */

    public boolean uninstallAppSilent(final String packageName, final boolean isKeepData) {

        return uninstallAppSilent(packageName, isKeepData, isDeviceRooted());

    }



    /**

     * Uninstall the app silently.

     * <p>Without root permission must hold

     * {@code <uses-permission android:name="android.permission.DELETE_PACKAGES" />}</p>

     *

     * @param packageName The name of the package.

     * @param isKeepData  Is keep the data.

     * @param isRooted    True to use root, false otherwise.

     * @return {@code true}: success<br>{@code false}: fail

     */

    public boolean uninstallAppSilent(final String packageName,

                                             final boolean isKeepData,

                                             final boolean isRooted) {

        if (isSpace(packageName)) return false;

        String command = "LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm uninstall "

                + (isKeepData ? "-k " : "")

                + packageName;

        ShellUtils.CommandResult commandResult = ShellUtils.execCmd(command, isRooted);

        if (commandResult.successMsg != null

                && commandResult.successMsg.toLowerCase().contains("success")) {

            return true;

        } else {

            Log.e("AppUtils", "uninstallAppSilent successMsg: " + commandResult.successMsg +

                    ", errorMsg: " + commandResult.errorMsg);

            return false;

        }

    }



    /**

     * Return whether the app is installed.

     *

     * @param action   The Intent action, such as ACTION_VIEW.

     * @param category The desired category.

     * @return {@code true}: yes<br>{@code false}: no

     */

    public boolean isAppInstalled(Application app,@NonNull final String action,

                                         @NonNull final String category) {

        Intent intent = new Intent(action);

        intent.addCategory(category);

        PackageManager pm = app.getPackageManager();

        ResolveInfo info = pm.resolveActivity(intent, 0);

        return info != null;

    }



    /**

     * Return whether the app is installed.

     *

     * @param packageName The name of the package.

     * @return {@code true}: yes<br>{@code false}: no

     */

    public boolean isAppInstalled(Application app,@NonNull final String packageName) {

        return !isSpace(packageName) && IntentUtils.getIntentUtils().getLaunchAppIntent(app,packageName) != null;

    }



    /**

     * Return whether the application with root permission.

     *

     * @return {@code true}: yes<br>{@code false}: no

     */

    public boolean isAppRoot() {

        ShellUtils.CommandResult result = ShellUtils.execCmd("echo root", true);

        if (result.result == 0) return true;

        if (result.errorMsg != null) {

            Log.d("AppUtils", "isAppRoot() called" + result.errorMsg);

        }

        return false;

    }



    /**

     * Return whether it is a debug application.

     *

     * @return {@code true}: yes<br>{@code false}: no

     */

    public boolean isAppDebug(Application app) {

        return isAppDebug(app,app.getPackageName());

    }



    /**

     * Return whether it is a debug application.

     *

     * @param packageName The name of the package.

     * @return {@code true}: yes<br>{@code false}: no

     */

    public boolean isAppDebug(Application app,final String packageName) {

        if (isSpace(packageName)) return false;

        try {

            PackageManager pm = app.getPackageManager();

            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);

            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

            return false;

        }

    }



    /**

     * Return whether it is a system application.

     *

     * @return {@code true}: yes<br>{@code false}: no

     */

    public boolean isAppSystem(Application app) {

        return isAppSystem(app,app.getPackageName());

    }



    /**

     * Return whether it is a system application.

     *

     * @param packageName The name of the package.

     * @return {@code true}: yes<br>{@code false}: no

     */

    public boolean isAppSystem(Application app,final String packageName) {

        if (isSpace(packageName)) return false;

        try {

            PackageManager pm = app.getPackageManager();

            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);

            return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

            return false;

        }

    }






    /**

     * Return whether application is foreground.

     * <p>Target APIs greater than 21 must hold

     * {@code <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />}</p>

     *

     * @param packageName The name of the package.

     * @return {@code true}: yes<br>{@code false}: no

     */

    public boolean isAppForeground(Application application,@NonNull final String packageName) {

        return !isSpace(packageName) && packageName.equals(ProcessUtils.getForegroundProcessName(application));

    }



    /**

     * Launch the application.

     *

     * @param packageName The name of the package.

     */

    public void launchApp(Application app,final String packageName) {

        if (isSpace(packageName)) return;

        app.startActivity(IntentUtils.getIntentUtils().getLaunchAppIntent(app,packageName, true));

    }



    /**

     * Launch the application.

     *

     * @param activity    The activity.

     * @param packageName The name of the package.

     * @param requestCode If &gt;= 0, this code will be returned in

     *                    onActivityResult() when the activity exits.

     */

    public void launchApp(final Activity activity,

                                 final String packageName,

                                 final int requestCode) {

        if (isSpace(packageName)) return;

        activity.startActivityForResult(IntentUtils.getIntentUtils().getLaunchAppIntent(activity.getApplication(),packageName), requestCode);

    }



    /**

     * Relaunch the application.

     */

    public void relaunchApp(Application app) {

        PackageManager packageManager = app.getPackageManager();

        Intent intent = packageManager.getLaunchIntentForPackage(app.getPackageName());

        if (intent == null) return;

        ComponentName componentName = intent.getComponent();

        Intent mainIntent = Intent.makeRestartActivityTask(componentName);

        app.startActivity(mainIntent);

        System.exit(0);

    }



    /**

     * Launch the application's details settings.

     */

    public void launchAppDetailsSettings(Application app) {

        launchAppDetailsSettings(app,app.getPackageName());

    }



    /**

     * Launch the application's details settings.

     *

     * @param packageName The name of the package.

     */

    public void launchAppDetailsSettings(Application app,final String packageName) {

        if (isSpace(packageName)) return;

        app.startActivity(

                IntentUtils.getIntentUtils().getLaunchAppDetailsSettingsIntent(packageName, true)

        );

    }



    /**

     * Exit the application.

     */

    public void exitApp() {
        List<Activity> activityList = ActivityUtils.getActivityUtils().getAllActivity();
        for (int i = activityList.size() - 1; i >= 0; --i) {// remove from top
            Activity activity = activityList.get(i);
            // sActivityList remove the index activity at onActivityDestroyed
            activity.finish();
        }
        System.exit(0);
    }



    /**

     * Return the application's icon.

     *

     * @return the application's icon

     */

    public Drawable getAppIcon(Application app) {

        return getAppIcon(app,app.getPackageName());

    }



    /**

     * Return the application's icon.

     *

     * @param packageName The name of the package.

     * @return the application's icon

     */

    public Drawable getAppIcon(Application app,final String packageName) {

        if (isSpace(packageName)) return null;

        try {

            PackageManager pm = app.getPackageManager();

            PackageInfo pi = pm.getPackageInfo(packageName, 0);

            return pi == null ? null : pi.applicationInfo.loadIcon(pm);

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

            return null;

        }

    }



    /**

     * Return the application's package name.

     *

     * @return the application's package name

     */

    public String getAppPackageName(Application app) {

        return app.getPackageName();

    }



    /**

     * Return the application's name.

     *

     * @return the application's name

     */

    public String getAppName(Context app) {

        return getAppName(app,app.getPackageName());

    }



    /**

     * Return the application's name.

     *

     * @param packageName The name of the package.

     * @return the application's name

     */

    public String getAppName(Context app,final String packageName) {

        if (isSpace(packageName)) return "";

        try {

            PackageManager pm = app.getPackageManager();

            PackageInfo pi = pm.getPackageInfo(packageName, 0);

            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

            return "";

        }

    }



    /**

     * Return the application's path.

     *

     * @return the application's path

     */

    public String getAppPath(Application app) {

        return getAppPath(app,app.getPackageName());

    }



    /**

     * Return the application's path.

     *

     * @param packageName The name of the package.

     * @return the application's path

     */

    public String getAppPath(Application app,final String packageName) {

        if (isSpace(packageName)) return "";

        try {

            PackageManager pm = app.getPackageManager();

            PackageInfo pi = pm.getPackageInfo(packageName, 0);

            return pi == null ? null : pi.applicationInfo.sourceDir;

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

            return "";

        }

    }



    /**

     * Return the application's version name.

     *

     * @return the application's version name

     */

    public String getAppVersionName(Application app) {

        return getAppVersionName(app,app.getPackageName());

    }



    /**

     * Return the application's version name.

     *

     * @param packageName The name of the package.

     * @return the application's version name

     */

    public String getAppVersionName(Application app,final String packageName) {

        if (isSpace(packageName)) return "";

        try {

            PackageManager pm = app.getPackageManager();

            PackageInfo pi = pm.getPackageInfo(packageName, 0);

            return pi == null ? null : pi.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

            return "";

        }

    }



    /**

     * Return the application's version code.

     *

     * @return the application's version code

     */

    public int getAppVersionCode(Application app) {

        return getAppVersionCode(app,app.getPackageName());

    }



    /**

     * Return the application's version code.

     *

     * @param packageName The name of the package.

     * @return the application's version code

     */

    public int getAppVersionCode(Application app,final String packageName) {

        if (isSpace(packageName)) return -1;

        try {

            PackageManager pm = app.getPackageManager();

            PackageInfo pi = pm.getPackageInfo(packageName, 0);

            return pi == null ? -1 : pi.versionCode;

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

            return -1;

        }

    }



    /**

     * Return the application's signature.

     *

     * @return the application's signature

     */

    public Signature[] getAppSignature(Application app) {

        return getAppSignature(app,app.getPackageName());

    }



    /**

     * Return the application's signature.

     *

     * @param packageName The name of the package.

     * @return the application's signature

     */

    public Signature[] getAppSignature(Application app,final String packageName) {

        if (isSpace(packageName)) return null;

        try {

            PackageManager pm = app.getPackageManager();

            @SuppressLint("PackageManagerGetSignatures")

            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            return pi == null ? null : pi.signatures;

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

            return null;

        }

    }



    /**

     * Return the application's signature for SHA1 value.

     *

     * @return the application's signature for SHA1 value

     */

    public String getAppSignatureSHA1(Application app) {

        return getAppSignatureSHA1(app,app.getPackageName());

    }



    /**

     * Return the application's signature for SHA1 value.

     *

     * @param packageName The name of the package.

     * @return the application's signature for SHA1 value

     */

    public String getAppSignatureSHA1(Application app,final String packageName) {

        if (isSpace(packageName)) return "";

        Signature[] signature = getAppSignature(app,packageName);

        if (signature == null || signature.length <= 0) return "";

        return encryptSHA1ToString(signature[0].toByteArray()).

                replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");

    }



    /**

     * Return the application's information.

     * <ul>

     * <li>name of package</li>

     * <li>icon</li>

     * <li>name</li>

     * <li>path of package</li>

     * <li>version name</li>

     * <li>version code</li>

     * <li>is system</li>

     * </ul>

     *

     * @return the application's information

     */

    public AppInfo getAppInfo(Application app) {

        return getAppInfo(app,app.getPackageName());

    }



    /**

     * Return the application's information.

     * <ul>

     * <li>name of package</li>

     * <li>icon</li>

     * <li>name</li>

     * <li>path of package</li>

     * <li>version name</li>

     * <li>version code</li>

     * <li>is system</li>

     * </ul>

     *

     * @param packageName The name of the package.

     * @return 当前应用的 AppInfo

     */

    public AppInfo getAppInfo(Application app,final String packageName) {

        try {

            PackageManager pm = app.getPackageManager();

            PackageInfo pi = pm.getPackageInfo(packageName, 0);

            return getBean(pm, pi);

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

            return null;

        }

    }



    /**

     * Return the applications' information.

     *

     * @return the applications' information

     */

    public List<AppInfo> getAppsInfo(Application app) {

        List<AppInfo> list = new ArrayList<>();

        PackageManager pm = app.getPackageManager();

        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);

        for (PackageInfo pi : installedPackages) {

            AppInfo ai = getBean(pm, pi);

            if (ai == null) continue;

            list.add(ai);

        }

        return list;

    }



    private AppInfo getBean(final PackageManager pm, final PackageInfo pi) {

        if (pm == null || pi == null) return null;

        ApplicationInfo ai = pi.applicationInfo;

        String packageName = pi.packageName;

        String name = ai.loadLabel(pm).toString();

        Drawable icon = ai.loadIcon(pm);

        String packagePath = ai.sourceDir;

        String versionName = pi.versionName;

        int versionCode = pi.versionCode;

        boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;

        return new AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem);

    }



    private boolean isFileExists(final File file) {

        return file != null && file.exists();

    }



    private File getFileByPath(final String filePath) {

        return isSpace(filePath) ? null : new File(filePath);

    }



    private boolean isSpace(final String s) {

        if (s == null) return true;

        for (int i = 0, len = s.length(); i < len; ++i) {

            if (!Character.isWhitespace(s.charAt(i))) {

                return false;

            }

        }

        return true;

    }



    private boolean isDeviceRooted() {

        String su = "su";

        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",

                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};

        for (String location : locations) {

            if (new File(location + su).exists()) {

                return true;

            }

        }

        return false;

    }



    private final char HEX_DIGITS[] =

            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};



    private String encryptSHA1ToString(final byte[] data) {

        return bytes2HexString(encryptSHA1(data));

    }



    private byte[] encryptSHA1(final byte[] data) {

        if (data == null || data.length <= 0) return null;

        try {

            MessageDigest md = MessageDigest.getInstance("SHA1");

            md.update(data);

            return md.digest();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

            return null;

        }

    }



    private String bytes2HexString(final byte[] bytes) {

        if (bytes == null) return "";

        int len = bytes.length;

        if (len <= 0) return "";

        char[] ret = new char[len << 1];

        for (int i = 0, j = 0; i < len; i++) {

            ret[j++] = HEX_DIGITS[bytes[i] >>> 4 & 0x0f];

            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];

        }

        return new String(ret);

    }



    /**

     * The application's information.

     */

    public class AppInfo {



        private String   packageName;

        private String   name;

        private Drawable icon;

        private String   packagePath;

        private String   versionName;

        private int      versionCode;

        private boolean  isSystem;



        public Drawable getIcon() {

            return icon;

        }



        public void setIcon(final Drawable icon) {

            this.icon = icon;

        }



        public boolean isSystem() {

            return isSystem;

        }



        public void setSystem(final boolean isSystem) {

            this.isSystem = isSystem;

        }



        public String getPackageName() {

            return packageName;

        }



        public void setPackageName(final String packageName) {

            this.packageName = packageName;

        }



        public String getName() {

            return name;

        }



        public void setName(final String name) {

            this.name = name;

        }



        public String getPackagePath() {

            return packagePath;

        }



        public void setPackagePath(final String packagePath) {

            this.packagePath = packagePath;

        }



        public int getVersionCode() {

            return versionCode;

        }



        public void setVersionCode(final int versionCode) {

            this.versionCode = versionCode;

        }



        public String getVersionName() {

            return versionName;

        }



        public void setVersionName(final String versionName) {

            this.versionName = versionName;

        }



        public AppInfo(String packageName, String name, Drawable icon, String packagePath,

                       String versionName, int versionCode, boolean isSystem) {

            this.setName(name);

            this.setIcon(icon);

            this.setPackageName(packageName);

            this.setPackagePath(packagePath);

            this.setVersionName(versionName);

            this.setVersionCode(versionCode);

            this.setSystem(isSystem);

        }



        @Override

        public String toString() {

            return "pkg name: " + getPackageName() +

                    "\napp icon: " + getIcon() +

                    "\napp name: " + getName() +

                    "\napp path: " + getPackagePath() +

                    "\napp v name: " + getVersionName() +

                    "\napp v code: " + getVersionCode() +

                    "\nis system: " + isSystem();

        }

    }
    public static String getAppMetaDataString(Context context, String metaName) {
        try {
            //application标签下用getApplicationinfo，如果是activity下的用getActivityInfo
            String value = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData.getString(metaName, null);
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}