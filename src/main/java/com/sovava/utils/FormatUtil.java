package com.sovava.utils;


import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>Title:FormatUtil </p>
 * <p>Description: 格式化字符串类</p>
 */
public class FormatUtil {

    /**
     * 将字符串中的多个空格，替换成一个
     *
     * @param str
     * @return
     */
    public static String replaceKg(String str) {
        Pattern p = Pattern.compile("\\s+");
        Matcher m = p.matcher(str);
        return m.replaceAll(" ");
    }


    /**
     * m转为g
     *
     * @param str
     * @return
     */
    public static double mToG(String str) {
        double result = 0;
        double mod = 1024;
        if (str.contains("M")) {
            int f = new BigDecimal(str.replace("M", "")).intValue();
            result = f / mod;
        } else if (str.contains("K")) {
            int f = new BigDecimal(str.replace("K", "")).intValue();
            result = (f / mod) / mod;
        } else if (str.contains("T")) {
            double f = new BigDecimal(str.replace("T", "")).doubleValue();
            result = f * 1024;
        } else if (str.contains("G")) {
            result = new BigDecimal(str.replace("G", "")).doubleValue();
        }
        return formatDouble(result, 2);
    }


    /**
     * 格式化double数据，截取小数点后数字
     *
     * @param str
     * @param num
     * @return
     */
    public static double formatDouble(double str, int num) {
        java.math.BigDecimal b = new java.math.BigDecimal(str);
        double myNum3 = b.setScale(num, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
        return myNum3;
    }


}
