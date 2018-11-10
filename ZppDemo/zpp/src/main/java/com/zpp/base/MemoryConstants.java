package com.zpp.base;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 墨 on 2018/6/11.
 * 内存常量
 */

public class MemoryConstants {
    public static final int BYTE = 1;

    public static final int KB   = 1024;

    public static final int MB   = 1048576;

    public static final int GB   = 1073741824;



    @IntDef({BYTE, KB, MB, GB})

    @Retention(RetentionPolicy.SOURCE)

    public @interface Unit {

    }

}
