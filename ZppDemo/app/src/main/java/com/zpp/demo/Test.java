package com.zpp.demo;

import android.util.Log;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] a){
//        String str = "SSID:foreasy;PSW:rdfffcc";
//        byte[] msg = str.getBytes();
//        byte[] resultMsg = new byte[msg.length + 2];
//        System.arraycopy(msg, 0, resultMsg, 0, msg.length);
//        resultMsg[resultMsg.length - 2] = 0x0d;
//        resultMsg[resultMsg.length - 1] = 0x0A;
//        System.out.print(str+"-"+Arrays.toString(resultMsg));
//        System.out.print(Pattern.compile(".").matcher("1234dsd").replaceAll("*"));

        System.out.print(min(new float[]{0,-30,-4,-10,-15,-40,-56,-35})+"--:");
    }

    public static float min(float[] num2){
        float min = num2[0];
        float len = num2.length;
        for (int i = 0; i < len; i++) {
            if(min>num2[i]){
                float t;
                t = num2[i];
                num2[i] = min;
                min = t;
            }
        }
        if(!((min%-20)==0)){
            min=(((float) Math.floor(min/-20)+1)*-20);
        }
        return min;
    }
}
