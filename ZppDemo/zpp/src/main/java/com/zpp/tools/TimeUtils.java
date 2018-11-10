package com.zpp.tools;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/5/28/028.
 */

public class TimeUtils {
    public static int getAgeFromBirthTime(int year, int month, int day) {
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - year;
        int monthMinus = monthNow - month;
        int dayMinus = dayNow - day;

        int age = yearMinus;// 先大致赋值
        if (monthMinus == 0) {
            if (dayMinus == 0) {

            } else if (dayMinus > 0) {
                if (yearMinus == 0) {
                    age++;
                }
                return age;
            } else {
                age--;
            }
        } else if (monthMinus > 0) {
            if (yearMinus == 0) {
                age++;
            }
            return age;
        } else {
            age--;
        }
        return age;
    }

    public static int getAgeFromBirthTime(String str) {
        if (TextUtils.isEmpty(str))
            return 0;
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
        String sfstr = "";
        try {
            sfstr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] a = sfstr.split("-");
        return getAgeFromBirthTime(Integer.valueOf(a[0]), Integer.valueOf(a[1]), Integer.valueOf(a[2]));
    }

    public static String getConstellationX(String str) {
        if (TextUtils.isEmpty(str))
            return "";
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
        String sfstr = "";
        try {
            sfstr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getConstellation(sfstr);
    }

    public static String getConstellation(String birthTimeString) {
        String ret = null;
        String str[] = birthTimeString.trim().split("-");
        int month = Integer.parseInt(str[1]);
        int days = Integer.parseInt(str[2]);
        if ((month == 1 && days >= 20) || (month == 2 && days <= 18)) {
            ret = "水瓶座";
        } else if (month == 2 || month == 3 && days <= 20) {
            ret = "双鱼座";
        } else if (month == 3 || month == 4 && days <= 19) {
            ret = "白羊座";
        } else if (month == 4 || month == 5 && days <= 20) {
            ret = "金牛座";
        } else if (month == 5 || month == 6 && days <= 21) {
            ret = "双子座";
        } else if (month == 6 || month == 7 && days <= 22) {
            ret = "巨蟹座";
        } else if (month == 7 || month == 8 && days <= 22) {
            ret = "狮子座";
        } else if (month == 8 || month == 9 && days <= 22) {
            ret = "处女座";
        } else if (month == 9 || month == 10 && days <= 23) {
            ret = "天秤座";
        } else if (month == 10 || month == 11 && days <= 22) {
            ret = "天蝎座";
        } else if (month == 11 || month == 12 && days <= 21) {
            ret = "射手座";
        } else if (month == 12 || month == 1) {
            ret = "摩羯座";
        }
        return ret;
    }

    public static String timeInt2String(String timeString, int time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeString, Locale.getDefault());
        return simpleDateFormat.format(Long.valueOf(time + "000"));
    }

    //时间戳转换
    public static String timestamp2String(long timestamp) {
        Date currentTime = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String temp = format.format(currentTime).toString();
        return temp;
    }
    //时间戳转换
    public static String timestamp2String(long timestamp,String format) {
        Date currentTime = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(currentTime);
    }

    //时间戳转换
    public static Long String2Timestamp(String time,String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return Long.valueOf(0);
        } else {
            return date.getTime()/1000; // date类型转成long类型
        }
    }

    private Timer timer;

    //倒计时
    public void startCountDown(final long midTime) {
        if (midTime > 0) {
            final long[] time1 = {midTime};
            timer = new Timer();
            final long[] hh = new long[1];
            final long[] mm = new long[1];
            final long[] ss = new long[1];

            timer.schedule(new TimerTask() {
                String min;
                String sec;
                public void run() {
                    time1[0]--;
                    hh[0] = time1[0] / 60 / 60;
                    mm[0] = time1[0] / 60 % 60;
                    ss[0] = time1[0] % 60;
//                LLog.d("倒计时", "剩余:" + hh[0] + "小时" + mm[0] + "分钟" + ss[0] + "秒");
                    if (getTimeLisenter != null) {
                        min = mm[0] + "";
                        sec = ss[0] + "";
                        if (mm[0] < 10) {
                            min = "0" + mm[0];
                        }
                        if (ss[0] < 10) {
                            sec = "0" + ss[0];
                        }
                        getTimeLisenter.getTime(hh[0] + "时:" + min + "分:" + sec+"秒");
                        if ((hh[0] + ":" + min + ":" + sec).equals("0:00:00")) {
                            cancleCountDown();
                        }
                    }
                }
            }, 0, 1000);
        }
    }

    public void cancleCountDown() {
        if (timer != null) {
            timer.cancel();
            getTimeLisenter = null;
        }
    }

    public GetTimeLisenter getTimeLisenter;

    public interface GetTimeLisenter {

        void getTime(String time);
    }

    public void setGetTimeLisenter(GetTimeLisenter getTimeLisenter) {
        this.getTimeLisenter = getTimeLisenter;
    }
}
