package com.zpp.demo;

import android.util.Log;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] a){
        String str = "SSID:foreasy;PSW:rdfffcc";
        byte[] msg = str.getBytes();
        byte[] resultMsg = new byte[msg.length + 2];
        System.arraycopy(msg, 0, resultMsg, 0, msg.length);
        resultMsg[resultMsg.length - 2] = 0x0d;
        resultMsg[resultMsg.length - 1] = 0x0A;
        System.out.print(str+"-"+Arrays.toString(resultMsg));
//        System.out.print(Pattern.compile(".").matcher("1234dsd").replaceAll("*"));
    }
}
