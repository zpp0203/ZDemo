package com.zandroid.tools;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CashierInputFilter implements InputFilter {
    Pattern mPattern;

    //输入的最大金额
    private Long MAX_VALUE = Long.MAX_VALUE;
    //小数点后的位数
    private int POINTER_LENGTH = 1;

    private static final String POINTER = ".";

    private static final String ZERO = "0";

    private Context context;
    private String warnMsg;

    private Boolean firstZero=true;//第一个是否可以为0

    public CashierInputFilter(Long MAX_VALUE) {
        this.MAX_VALUE = MAX_VALUE;
        mPattern = Pattern.compile("([0-9]|\\.)*");
    }

    public CashierInputFilter(Context context, Long MAX_VALUE, String warnMsg) {
        this.MAX_VALUE = MAX_VALUE;
        this.context=context;
        this.warnMsg=warnMsg;
        mPattern = Pattern.compile("([0-9]|\\.)*");
    }

    public CashierInputFilter() {
        mPattern = Pattern.compile("([0-9]|\\.)*");
    }

    public void setPointerLength(int pointerLength) {
        POINTER_LENGTH = pointerLength;
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
        if (TextUtils.isEmpty(sourceText) || (!firstZero && destText.length() == 0 && sourceText.equals(ZERO))) {
            // delete
            if (!firstZero && source.length() == 0 && dend > dstart) {
                if (dend != dest.length() && destText.length() >= 2 && destText.endsWith(ZERO)) {
                    // 非尾端删除
                    // 通过添加已删除的内容达到不删除的效果
                    return dest.subSequence(dstart, dend);
                }
            }
            return "";
        }

        Matcher matcher = mPattern.matcher(sourceText);
        //已经输入小数点的情况下，只能输入数字
        if (destText.contains(POINTER)) {
            if (!matcher.matches()) {
                return "";
            } else {
                if (POINTER.equals(sourceText)) {  //只能输入一个小数点
                    return "";
                }
            }
            //验证小数点精度，保证小数点后只能输入两位
            int index = destText.indexOf(POINTER);
            int length = destText.length() - index;
            if (index < dend && length > POINTER_LENGTH) {
                return dest.subSequence(dstart, dend);
            }
        } else {
            //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点和0
            if (!matcher.matches()) {
                return "";
            } else {
                if ((POINTER.equals(sourceText)) && TextUtils.isEmpty(destText)) {//|| ZERO.equals(source)
                    return "";
                } else if (destText.length() == 1 && destText.equals(ZERO) && !POINTER.equals(sourceText)) {//首位输入0后只可输入小数点
                    return "";
                }
            }
        }
        //验证输入金额的大小
        String endStr=destText.substring(0,dstart)+sourceText+destText.substring(dstart);

        double sumText = new BigDecimal(endStr).doubleValue();//+ (sourceText.equals(POINTER)?(sourceText+"0"):sourceText)
        if (sumText > MAX_VALUE) {
            if (warnMsg!=null)
                ToastUtils.showToast(context, warnMsg);
            return dest.subSequence(dstart, dend);
        }

        return dest.subSequence(dstart, dend) + sourceText;
    }

    public void setFirstZero(Boolean firstZero) {
        this.firstZero = firstZero;
    }
}
