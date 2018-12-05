package com.zandroid.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.MODIFY_PHONE_STATE;

/**
 * openWirelessSettings : 打开网络设置界面
 * isConnected : 判断网络是否连接
 * isAvailableByPing : 判断网络是否可用
 * getMobileDataEnabled : 判断移动数据是否打开
 * setMobileDataEnabled : 打开或关闭移动数据 需要系统程序权限
 * isMobileData : 判断网络是否是移动数据
 * is4G : 判断网络是否是 4G
 * getWifiEnabled : 判断wifi 是否打开
 * setWifiEnabled : 打开或关闭 wifi
 * isWifiConnected : 判断 wifi 是否连接状态
 * isWifiAvailable : 判断 wifi 数据是否可用
 * getNetworkOperatorName: 获取移动网络运营商名称
 * getNetworkType : 获取当前网络类型
 * getIPAddress : 获取 IP 地址
 * getDomainAddress : 获取域名 ip 地址
 */

public final class NetworkUtils {


    private NetworkUtils() {

        throw new UnsupportedOperationException("u can't instantiate me...");

    }


    public enum NetworkType {

        NETWORK_WIFI,

        NETWORK_4G,

        NETWORK_3G,

        NETWORK_2G,

        NETWORK_UNKNOWN,

        NETWORK_NO

    }


    /**
     * 打开网络设置页面
     * Open the settings of wireless.
     */
    public static void openWirelessSettings(Context context) {

        context.startActivity(
                new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );

    }


    /**
     * Return whether network is connected.
     * 判断网络是否连接
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: connected<br>{@code false}: disconnected
     */

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isConnected(Context context) {

        NetworkInfo info = getActiveNetworkInfo(context);

        return info != null && info.isConnected();

    }


    /**
     * Return whether network is available using ping.
     * 判断网络是否可用
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     * <p>
     * <p>The default ping ip: 223.5.5.5</p>
     *
     * @return {@code true}: yes<br>{@code false}: no
     */

    @RequiresPermission(INTERNET)

    public static boolean isAvailableByPing() {

        return isAvailableByPing(null);

    }


    /**
     * Return whether network is available using ping.
     * 判断移动数据是否打开
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param ip The ip address.
     * @return {@code true}: yes<br>{@code false}: no
     */
    @RequiresPermission(INTERNET)

    public static boolean isAvailableByPing(String ip) {

        if (ip == null || ip.length() <= 0) {

            ip = "223.5.5.5";// default ping ip

        }
        ShellUtils.CommandResult result = ShellUtils.execCmd(String.format("ping -c 1 %s", ip), false);

        boolean ret = result.result == 0;

        if (result.errorMsg != null) {

            Log.d("NetworkUtils", "isAvailableByPing() called" + result.errorMsg);

        }

        if (result.successMsg != null) {

            Log.d("NetworkUtils", "isAvailableByPing() called" + result.successMsg);

        }

        return ret;

    }


    /**
     * Return whether mobile data is enabled.
     * 打开或关闭移动数据
     *
     * @return {@code true}: enabled<br>{@code false}: disabled
     */

    public static boolean getMobileDataEnabled(Context context) {

        try {

            TelephonyManager tm =

                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (tm == null) return false;

            @SuppressLint("PrivateApi")

            Method getMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("getDataEnabled");

            if (null != getMobileDataEnabledMethod) {

                return (boolean) getMobileDataEnabledMethod.invoke(tm);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }


    /**
     * Set mobile data enabled.
     * <p>
     * <p>Must hold {@code android:sharedUserId="android.uid.system"},
     * <p>
     * {@code <uses-permission android:name="android.permission.MODIFY_PHONE_STATE" />}</p>
     *
     * @param enabled True to enabled, false otherwise.
     */

    @RequiresPermission(MODIFY_PHONE_STATE)

    public static void setMobileDataEnabled(Context context, final boolean enabled) {

        try {

            TelephonyManager tm =

                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (tm == null) return;

            Method setMobileDataEnabledMethod =

                    tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            if (null != setMobileDataEnabledMethod) {

                setMobileDataEnabledMethod.invoke(tm, enabled);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    /**
     * Return whether using mobile data.
     * <p>
     * <p>Must hold
     * <p>
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: yes<br>{@code false}: no
     */

    @RequiresPermission(ACCESS_NETWORK_STATE)

    public static boolean isMobileData(Context context) {

        NetworkInfo info = getActiveNetworkInfo(context);

        return null != info

                && info.isAvailable()

                && info.getType() == ConnectivityManager.TYPE_MOBILE;

    }


    /**
     * Return whether using 4G.
     * <p>
     * <p>Must hold
     * <p>
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: yes<br>{@code false}: no
     */

    @RequiresPermission(ACCESS_NETWORK_STATE)

    public static boolean is4G(Context context) {

        NetworkInfo info = getActiveNetworkInfo(context);

        return info != null

                && info.isAvailable()

                && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;

    }


    /**
     * Return whether wifi is enabled.
     * <p>
     * <p>Must hold
     * <p>
     * {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />}</p>
     *
     * @return {@code true}: enabled<br>{@code false}: disabled
     */

    @RequiresPermission(ACCESS_WIFI_STATE)

    public static boolean getWifiEnabled(Context context) {

        @SuppressLint("WifiManagerLeak")

        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        return manager != null && manager.isWifiEnabled();

    }


    /**
     * Set wifi enabled.
     * <p>
     * <p>Must hold
     * <p>
     * {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />}</p>
     *
     * @param enabled True to enabled, false otherwise.
     */

    @RequiresPermission(CHANGE_WIFI_STATE)

    public static void setWifiEnabled(Context context, final boolean enabled) {

        @SuppressLint("WifiManagerLeak")

        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (manager == null) return;

        if (enabled) {

            if (!manager.isWifiEnabled()) {

                manager.setWifiEnabled(true);

            }

        } else {

            if (manager.isWifiEnabled()) {

                manager.setWifiEnabled(false);

            }

        }

    }


    /**
     * Return whether wifi is connected.
     * <p>
     * <p>Must hold
     * <p>
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: connected<br>{@code false}: disconnected
     */

    @RequiresPermission(ACCESS_NETWORK_STATE)

    public static boolean isWifiConnected(Context context) {

        ConnectivityManager cm =

                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null

                && cm.getActiveNetworkInfo() != null

                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }


    /**
     * Return whether wifi is available.
     * <p>
     * <p>Must hold
     * <p>
     * {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
     * <p>
     * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @return {@code true}: available<br>{@code false}: unavailable
     */

    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})

    public static boolean isWifiAvailable(Context context) {

        return getWifiEnabled(context) && isAvailableByPing();

    }


    /**
     * Return the name of network operate.
     *
     * @return the name of network operate
     */

    public static String getNetworkOperatorName(Context context) {

        TelephonyManager tm =

                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        return tm != null ? tm.getNetworkOperatorName() : "";

    }


    private static final int NETWORK_TYPE_GSM = 16;

    private static final int NETWORK_TYPE_TD_SCDMA = 17;

    private static final int NETWORK_TYPE_IWLAN = 18;


    /**
     * Return type of network.
     * <p>
     * <p>Must hold
     * <p>
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return type of network
     * <p>
     * <ul>
     * <p>
     * <li>{@link NetworkUtils.NetworkType#NETWORK_WIFI   } </li>
     * <p>
     * <li>{@link NetworkUtils.NetworkType#NETWORK_4G     } </li>
     * <p>
     * <li>{@link NetworkUtils.NetworkType#NETWORK_3G     } </li>
     * <p>
     * <li>{@link NetworkUtils.NetworkType#NETWORK_2G     } </li>
     * <p>
     * <li>{@link NetworkUtils.NetworkType#NETWORK_UNKNOWN} </li>
     * <p>
     * <li>{@link NetworkUtils.NetworkType#NETWORK_NO     } </li>
     * <p>
     * </ul>
     */

    @RequiresPermission(ACCESS_NETWORK_STATE)

    public static NetworkType getNetworkType(Context context) {

        NetworkType netType = NetworkType.NETWORK_NO;

        NetworkInfo info = getActiveNetworkInfo(context);

        if (info != null && info.isAvailable()) {


            if (info.getType() == ConnectivityManager.TYPE_WIFI) {

                netType = NetworkType.NETWORK_WIFI;

            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {

                switch (info.getSubtype()) {


                    case NETWORK_TYPE_GSM:

                    case TelephonyManager.NETWORK_TYPE_GPRS:

                    case TelephonyManager.NETWORK_TYPE_CDMA:

                    case TelephonyManager.NETWORK_TYPE_EDGE:

                    case TelephonyManager.NETWORK_TYPE_1xRTT:

                    case TelephonyManager.NETWORK_TYPE_IDEN:

                        netType = NetworkType.NETWORK_2G;

                        break;


                    case NETWORK_TYPE_TD_SCDMA:

                    case TelephonyManager.NETWORK_TYPE_EVDO_A:

                    case TelephonyManager.NETWORK_TYPE_UMTS:

                    case TelephonyManager.NETWORK_TYPE_EVDO_0:

                    case TelephonyManager.NETWORK_TYPE_HSDPA:

                    case TelephonyManager.NETWORK_TYPE_HSUPA:

                    case TelephonyManager.NETWORK_TYPE_HSPA:

                    case TelephonyManager.NETWORK_TYPE_EVDO_B:

                    case TelephonyManager.NETWORK_TYPE_EHRPD:

                    case TelephonyManager.NETWORK_TYPE_HSPAP:

                        netType = NetworkType.NETWORK_3G;

                        break;


                    case NETWORK_TYPE_IWLAN:

                    case TelephonyManager.NETWORK_TYPE_LTE:

                        netType = NetworkType.NETWORK_4G;

                        break;

                    default:


                        String subtypeName = info.getSubtypeName();

                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")

                                || subtypeName.equalsIgnoreCase("WCDMA")

                                || subtypeName.equalsIgnoreCase("CDMA2000")) {

                            netType = NetworkType.NETWORK_3G;

                        } else {

                            netType = NetworkType.NETWORK_UNKNOWN;

                        }

                        break;

                }

            } else {

                netType = NetworkType.NETWORK_UNKNOWN;

            }

        }

        return netType;

    }


    @RequiresPermission(ACCESS_NETWORK_STATE)

    private static NetworkInfo getActiveNetworkInfo(Context context) {

        ConnectivityManager manager =

                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager == null) return null;

        return manager.getActiveNetworkInfo();

    }


    /**
     * Return the ip address.
     * <p>
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param useIPv4 True to use ipv4, false otherwise.
     * @return the ip address
     */

    @RequiresPermission(INTERNET)

    public static String getIPAddress(final boolean useIPv4) {

        try {

            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();

            while (nis.hasMoreElements()) {

                NetworkInterface ni = nis.nextElement();

                // To prevent phone of xiaomi return "10.0.2.15"

                if (!ni.isUp()) continue;

                Enumeration<InetAddress> addresses = ni.getInetAddresses();

                while (addresses.hasMoreElements()) {

                    InetAddress inetAddress = addresses.nextElement();

                    if (!inetAddress.isLoopbackAddress()) {

                        String hostAddress = inetAddress.getHostAddress();

                        boolean isIPv4 = hostAddress.indexOf(':') < 0;

                        if (useIPv4) {

                            if (isIPv4) return hostAddress;

                        } else {

                            if (!isIPv4) {

                                int index = hostAddress.indexOf('%');

                                return index < 0

                                        ? hostAddress.toUpperCase()

                                        : hostAddress.substring(0, index).toUpperCase();

                            }

                        }

                    }

                }

            }

        } catch (SocketException e) {

            e.printStackTrace();

        }

        return "";

    }


    /**
     * Return the domain address.
     * <p>
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param domain The name of domain.
     * @return the domain address
     */

    @RequiresPermission(INTERNET)

    public static String getDomainAddress(final String domain) {

        InetAddress inetAddress;

        try {

            inetAddress = InetAddress.getByName(domain);

            return inetAddress.getHostAddress();

        } catch (UnknownHostException e) {

            e.printStackTrace();

            return "";

        }

    }

}
