package com.etar.purifier.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author hzh
 * @version 1.0
 * @date 2019/7/18 16:59
 */
public class NumUtil {

    public static Double get2Double(Integer size, Integer total) {
        DecimalFormat df = new DecimalFormat("0.00");
        final String format = df.format(((float) size / total) * 100);
        return Double.valueOf(format);
    }

    public static void main(String[] args) {
        System.out.println(get2Double(1, 3));
    }
}
