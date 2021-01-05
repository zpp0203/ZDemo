package com.zpp.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Created by Ric on 2017/3/5.
 */

public class StringUtils {

    public static final String VERSION_SEPERATOR = ".";

    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    private final static Pattern mobiler = Pattern
            .compile("^(13|14|15|17|18|19)\\d{9}$");

    private final static Pattern chineseName = Pattern.compile("^[a-zA-Z\\u4e00-\\u9fa5]{2,16}$");

    private final static Pattern password = Pattern.compile("^[a-zA-Z0-9~!@#$%^&*()_+-=`;:',.<>/\\\\|?\"\\{}\\]\\[]{6,20}$");

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str) || TextUtils.isEmpty(str.trim());
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断是不是一个合法的手机号码
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        if (mobile == null || mobile.trim().length() == 0)
            return false;
        return mobiler.matcher(mobile).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转长整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 对象转小数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0.0;
    }

    /**
     * 对象转浮点数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static float toFloat(String obj) {
        try {
            return Float.parseFloat(obj);
        } catch (Exception e) {
        }
        return 0.0f;
    }

    /**
     * 如果含小数位 输出小数位，否则去掉小数位
     *
     * @param price
     * @return
     */
    public static String getPrettyPrice(float price) {
        int intPrice = (int) price;
        if ((price - intPrice) == 0f) {
            return String.valueOf(intPrice);
        }
        return String.valueOf(price);
    }

    public static List<String> stringToList(String str, String seperator) {
        List<String> itemList = new ArrayList<String>();
        if (isEmpty(str)) {
            return itemList;
        }
        StringTokenizer st = new StringTokenizer(str, seperator);
        while (st.hasMoreTokens()) {
            itemList.add(st.nextToken());
        }

        return itemList;
    }

    /**
     * 去str中的空格
     *
     * @param str
     * @return
     */
    public static String getNoSpaceStr(String str) {
        if (str.contains(" ")) {
            str.replace(" ", "");
        }
        return str;
    }

    /**
     * 检测路径是否为正常的图片路径
     *
     * @param path
     * @return
     */
    public static boolean isNormalImagePath(String path) {
        String extension = path.substring(path.lastIndexOf('.'));
        boolean ret = extension != null && (extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".png"));
        return ret;
    }

    /**
     * 将String转换成InputStream
     * @param in
     * @return
     * @throws Exception
     */
    public static InputStream StringTOInputStream(String in) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("utf-8"));
        return is;
    }

    public static boolean checkPhoneNum(Context context, String phone) {
        if (StringUtils.isNotEmpty(phone.trim())) {
            if (isMobile(phone.trim())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 判断是不是一个合法密码
     *
     * @param context
     * @param psw
     * @return
     */
    public static boolean isPassword(Context context, EditText psw) {
        if (StringUtils.isNotEmpty(psw.getText().toString().trim())) {
            if (password.matcher(psw.getText().toString()).matches()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 检测验证码是否正确
     *
     * @return
     */
    public static boolean checkValidCode(Context context, EditText validCode) {
        if (!TextUtils.isEmpty(validCode.getText().toString().trim())) {
            if (validCode.getText().toString().trim().length() == 4) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * md5加密
     * @param text
     * @return
     */
    public static String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(text.getBytes("utf-8"));
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                String b = Integer.toHexString(0xFF & digest[i]);
                if (b.length() == 1)
                    buf.append('0');
                buf.append(b);
            }
            return buf.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }


    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint  比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
    /**
     * 十六进制转换字符串
     * @param hexStr Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr)
    {
        String mhex=hexStr.replaceAll(" +","").toUpperCase();
        String str = "0123456789ABCDEF";
        char[] hexs = mhex.toCharArray();
        byte[] bytes = new byte[mhex.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++)
        {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    public static void main(String[] a){
        System.out.print(str2HexStr("猪撒")+"="+hexStr2Str("E7 8C AA E6 92 92"));
    }
    /**
     * 字符串转换成十六进制字符串
     * @param str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str)
    {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.toUpperCase().getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * input 流转换为字符串
     *
     * @param is
     * @return
     */
    public static String streamToString(InputStream is) {
        String s = null;
        try {
            //格式转换
            Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A");
            if (scanner.hasNext()) {
                s = scanner.next();
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
