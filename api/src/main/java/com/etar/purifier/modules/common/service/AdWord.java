package com.etar.purifier.modules.common.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * 字符随机生成类
 *
 * @author ASUS
 */
public class AdWord {
    /**
     * 密码类型枚举
     *
     * @author ASUS
     */
    public static enum TYPE {
        /**
         * 字符型
         */
        LETTER,
        /**
         * 大写字符型
         */
        CAPITAL,
        /**
         * 数字型
         */
        NUMBER,
        /**
         * 符号型
         */
        SIGN,
        /**
         * 大+小字符 型
         */
        LETTER_CAPITAL,
        /**
         * 小字符+数字 型
         */
        LETTER_NUMBER,
        /**
         * 大+小字符+数字 型
         */
        LETTER_CAPITAL_NUMBER,
        /**
         * 大+小字符+数字+符号 型
         */
        LETTER_CAPITAL_NUMBER_SIGN
    }

    private static String[] lowercase = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    private static String[] capital = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private static String[] number = {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    private static String[] sign = {
            "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "`", "-", "=",
            "{", "}", "|", ":", "\"", "<", ">", "?",
            "[", "]", "\\", ";", "'", ",", ".", "/"};
    private static String[] signS = {
            "~", " ", "@", "#", "￥", "%", "……", "&", "*", "（", "）", "——", "-", "+", "=",
            "{", "}", "|", "：", "”", "“","《", "》", "？", "；", "’", "‘", "，", "。",
            "【", "】", "、"};
    /**
     * 静态随机数
     */
    private static Random random = new Random();

    public static void main(String[] args) {
        System.out.println(AdWord.getRandom(random.nextInt(48), AdWord.TYPE.LETTER_CAPITAL_NUMBER_SIGN));
    }

    public static String getTitle() {

        int titleInt = AdWord.random.nextInt(16);
        if (titleInt == 0) {
            titleInt = 1;
        }
        return AdWord.getRandom(titleInt, TYPE.LETTER_CAPITAL_NUMBER_SIGN);


    }

    public static String getSolgan() {
        int solganInt = AdWord.random.nextInt(48);
        if (solganInt == 0) {
            solganInt = 1;
        }
        return AdWord.getRandom(solganInt, TYPE.LETTER_CAPITAL_NUMBER_SIGN);

    }

    /**
     * 获取随机组合码
     *
     * @param num  位数
     * @param type 类型
     * @type <br>字符型 LETTER,
     * <br>大写字符型 CAPITAL,
     * <br>数字型 NUMBER,
     * <br>符号型 SIGN,
     * <br>大+小字符型 LETTER_CAPITAL,
     * <br>小字符+数字 型 LETTER_NUMBER,
     * <br>大+小字符+数字 型 LETTER_CAPITAL_NUMBER,
     * <br>大+小字符+数字+符号 型 LETTER_CAPITAL_NUMBER_SIGN
     */
    public static String getRandom(int num, TYPE type) {
        ArrayList<String> temp = new ArrayList<String>();
        StringBuffer code = new StringBuffer();
        if (type == TYPE.LETTER) {
            temp.addAll(Arrays.asList(lowercase));
        } else if (type == TYPE.CAPITAL) {
            temp.addAll(Arrays.asList(capital));
        } else if (type == TYPE.NUMBER) {
            temp.addAll(Arrays.asList(number));
        } else if (type == TYPE.SIGN) {
            temp.addAll(Arrays.asList(sign));
        } else if (type == TYPE.LETTER_CAPITAL) {
            temp.addAll(Arrays.asList(lowercase));
            temp.addAll(Arrays.asList(capital));
        } else if (type == TYPE.LETTER_NUMBER) {
            temp.addAll(Arrays.asList(lowercase));
            temp.addAll(Arrays.asList(number));
        } else if (type == TYPE.LETTER_CAPITAL_NUMBER) {
            temp.addAll(Arrays.asList(lowercase));
            temp.addAll(Arrays.asList(capital));
            temp.addAll(Arrays.asList(number));
        } else if (type == TYPE.LETTER_CAPITAL_NUMBER_SIGN) {
            temp.addAll(Arrays.asList(lowercase));
            temp.addAll(Arrays.asList(capital));
            temp.addAll(Arrays.asList(number));
            temp.addAll(Arrays.asList(sign));
            temp.addAll(Arrays.asList(signS));
            temp.addAll(Arrays.asList(WordCode.getWords()));
        }
        for (int i = 0; i < num; i++) {
            int size = temp.size();
            code.append(temp.get(random.nextInt(size)));
        }
        return code.toString();
    }
}