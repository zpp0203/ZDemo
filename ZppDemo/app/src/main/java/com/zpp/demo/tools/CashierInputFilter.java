package com.zpp.demo.tools;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//数字过滤器
public class CashierInputFilter implements InputFilter {
    Pattern mPattern=Pattern.compile("([0-9]|\\.)*");

    //输入的最大数值
    private int MAX_VALUE = Integer.MAX_VALUE;
    private boolean isPointer=true;//是否是小数
    //小数点后的位数
    private static final int POINTER_LENGTH = 2;

    private static final String POINTER = ".";

    private static final String ZERO = "0";

    public CashierInputFilter() {
    }

    public CashierInputFilter(int MAX_VALUE) {
        this.MAX_VALUE = MAX_VALUE;
    }

    public CashierInputFilter(int MAX_VALUE, boolean isPointer) {
        this.MAX_VALUE = MAX_VALUE;
        this.isPointer = isPointer;
    }

    public void isPointer(boolean isPointer){
        this.isPointer=isPointer;
    }
    public void setMaxValue(int MAX_VALUE){
        this.MAX_VALUE=MAX_VALUE;
    }


    /**
     * @param source    新输入的字符串
     * @param start     新输入的字符串起始下标，一般为0
     * @param end       新输入的字符串终点下标，一般为source长度-1
     * @param dest      输入之前文本框内容
     * @param dstart    原内容起始坐标，一般为0
     * @param dend      原内容终点坐标，一般为dest长度-1
     * @return          输入内容
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceText = source.toString();
        String destText = dest.toString();

        //验证删除等按键
        if (TextUtils.isEmpty(sourceText)) {
            return "";
        }

        Matcher matcher = mPattern.matcher(source);
        if (!matcher.matches()) {
            return "";
        }
        if(isPointer) {
            //已经输入小数点的情况下，只能输入数字
            if (destText.contains(POINTER)) {

                if (POINTER.equals(source)) {  //只能输入一个小数点
                    return "";
                }

                //验证小数点精度，保证小数点后只能输入两位
                int index = destText.indexOf(POINTER);
                int length = dend - index;

                if (length > POINTER_LENGTH) {
                    return dest.subSequence(dstart, dend);
                }
            } else {
                //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点和0|| ZERO.equals(source)
                if ((POINTER.equals(source)) && TextUtils.isEmpty(destText)) {
                    return "";
                }
            }
        }else {
            if ((ZERO.equals(source) && TextUtils.isEmpty(destText)) || POINTER.equals(source)) {
                return "";
            }
        }
        //验证输入的大小
        double sumText = Double.parseDouble(destText + sourceText);
        if (sumText > MAX_VALUE) {
            return dest.subSequence(dstart, dend);
        }
        return dest.subSequence(dstart, dend) + sourceText;
    }

}
