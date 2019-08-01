package com.etar.purifier.modules.common.service;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * 获取中文
 *
 * @author hzh
 * @version 1.0
 * @date 2019/3/18 11:07
 */
public class WordCode {

    /**
     * 获取单个中文字符
     *
     * @return
     */
    public static String getChinese() {
        Random random = new Random();
        String ctmp = "";
        String rbase[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
//生成第一位区码
        int r1 = random.nextInt(3) + 11;//生成11-14随机数
        String str_r1 = rbase[r1];
//生成第二位区码
        int r2;
        if (r1 == 13) {
            r2 = random.nextInt(10);
        } else {
            r2 = random.nextInt(16);
        }
        String str_r2 = rbase[r2];
//生成第一位位码
        int r3 = random.nextInt(6) + 10;
        String str_r3 = rbase[r3];
//生成第二位位码
        int r4;
        if (r3 == 10) {
            r4 = random.nextInt(14) + 2;
        } else {
            r4 = random.nextInt(16);
        }

        String str_r4 = rbase[r4];
//将生成的区码放入第一个元素，位码放入第二个元素
        byte a[] = new byte[2];
        a[0] = (byte) Integer.parseInt(str_r1 + str_r2, 16);//转换成16进制
        a[1] = (byte) Integer.parseInt(str_r3 + str_r4, 16);
        try {
            ctmp = new String(a, "GB2312");//根据字节生成汉字
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ctmp;
    }

    /**
     * 获取48个中文
     *
     * @return
     */
    static String[] getWords() {
        String[] strings = new String[48];
        Random random = new Random();
        int nextInt = random.nextInt(48);
        for (int i = 0; i < 48; i++) {
            strings[i] = getChinese();
        }
        System.out.println(JSONObject.toJSONString(strings));
        return strings;
    }

    public static void main(String[] args) {
        System.out.println(JSONObject.toJSONString(getWords()));
    }
}
