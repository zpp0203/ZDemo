package com.zpp.demo;

public class Test {
    public static void main(String[] args){
        int[] a=new int[]{1,2,2,3,3,3,4,5,6};
        removeDuplicates(a);
        for(int i=0;i<a.length;i++)
            System.out.print(a[i]+" ");
    }
    public static int removeDuplicates(int[] A) {
        if (A == null || A.length == 0) {
            return 0;
        }
        int size = 0;
        for (int i = 0; i < A.length; i++) {
            System.out.print(i+" "+size);
            if (A[i] != A[size]) {
                A[++size] = A[i];
            }
        }
        return size + 1;
    }
}
