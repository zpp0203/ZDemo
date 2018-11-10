package com.zpp.tools;

import java.util.regex.Pattern;

/**
 * Created by 墨 on 2018/4/9.
 */

public class TextUtils {

    //获取String中的数字
    public static String getStringNum(String str) {

        String str2 = "";
        if (str != null && !"".equals(str.trim())) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    str2 += str.charAt(i);
                }
            }
        }
        return str2;
    }

    private final static String PHONE_PATTERN = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17([0,1,6,7,]))|(18[0-2,5-9]))\\d{8}$";

    public static boolean isPhone(String phone) {
        return Pattern.compile(PHONE_PATTERN).matcher(phone).matches();
    }
}
