package com.ajie.loginsendmail.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * ip工具
 */
public class IpUtil {

    public static final char POINT = '.';

    private IpUtil() {

    }

    /**
     * 从字符串中提取ip（找到有4个"."并且每个点中间的字符数不超过3就认为是ip）
     *
     * @param str
     * @return
     */
    public static String pickupIp(String str) {
        //ajie     + pts/0        2021-02-10 22:25   .         27979 (14.121.215.219)
        if (StringUtils.isBlank(str)) {
            return "";
        }
        //开始游标
        int idx = -1;
        int pointCount = 0;//匹配中了点的个数
        int pointIdx = -1;//本次匹配中点的下标
        int i = 0;
        for (; i < str.length(); i++) {
            char c = str.charAt(i);
            if (-1 == idx) {
                if (Character.isDigit(c)) {
                    //匹配中了数字
                    idx = i;
                }
            } else if (POINT == c) {
                //是点号，判断数字段的长度是否超过3
                int offset;
                if (pointIdx == -1) {
                    offset = i - idx;
                } else {
                    offset = i - pointIdx;
                }
                if (offset > 4) {//网段最长3位，现在在"."的位置，所以是4
                    idx = -1;
                    pointCount = 0;
                } else {
                    pointCount++;
                    pointIdx = i;
                }
            } else {
                //前面已经匹配中了数字，但是现在遇到了不是数字的字符
                if (!Character.isDigit(c)) {
                    //看看是不是最后一个网段了
                    if (pointCount != 3) {
                        //不是最后一个网段，从头再来
                        idx = -1;
                        pointCount = 0;
                    } else {
                        //最后一个网段，看看前面的是不是数字，如果是数字，则结束，如果是"."则有问题，如：192.168.0.a
                        char preChar = str.charAt(i - 1);//都已经匹配了三个点了，不可能i还是0的，所以不用判断越界
                        if (Character.isDigit(preChar)) {
                            //皆大欢喜，结束了
                            break;
                        } else {
                            //从头再来
                            idx = -1;
                            pointCount = 0;
                        }
                    }
                }
            }
        }
        if (pointCount != 3) {
            return "";
        }
        //上面的循环并没有判断最后一个网段的长度，所以要加一下判断
        String ret = str.substring(idx, i);
        int lastPointIdx = ret.lastIndexOf(".");
        int offset = ret.length() - lastPointIdx - 1;
        if (offset > 3) {
            return "";
        }
        return ret;
    }

    public static void main(String[] args) {
        String str = pickupIp("ajie     + pts/0        2021-02-10 22:25   .         27979 (14.121.215.219)");
        System.out.println(str);
    }
}
