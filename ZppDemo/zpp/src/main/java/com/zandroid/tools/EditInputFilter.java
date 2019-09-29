package com.zandroid.tools;

import android.content.Context;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditInputFilter implements InputFilter {
    private Context context;
    private int mMax;

    Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    public EditInputFilter(Context context) {
        this.context=context;
    }

    public EditInputFilter(Context context, int mMax) {
        this.context=context;
        this.mMax = mMax;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        //过滤非选中输入，比如输入中文时，过滤掉搜索框里已经输入的拼音（小米手机出现的情况）
        SpannableString ss = new SpannableString(source);
        Object[] spans = ss.getSpans(0, ss.length(), Object.class);
        if(spans != null) {
            for(Object span : spans) {
                if(span instanceof UnderlineSpan) {
                    return "";
                }
            }
        }

        Matcher matcher = pattern.matcher(source);
        if(matcher.find()){
            ToastUtils.showToast(context,"不可输入表情");
            return "";
        }

        if(mMax>0) {
            int keep = mMax - (dest.length() - (dend - dstart));
            if (keep <= 0) {
                ToastUtils.showToast(context,"最大长度"+mMax);
                return "";
            } else if (keep >= end - start) {
                return null; // keep original
            } else {
                keep += start;
                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                    --keep;
                    if (keep == start) {
                        ToastUtils.showToast(context,"最大长度"+mMax);
                        return "";
                    }
                }
                return source.subSequence(start, keep);
            }
        }

        return null;
    }
}
