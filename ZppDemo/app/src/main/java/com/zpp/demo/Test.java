package com.zpp.demo;

import java.util.regex.Pattern;

public class Test {
    public static void main(String[] a){
        System.out.print(Pattern.compile(".").matcher("1234dsd").replaceAll("*"));
    }
}
