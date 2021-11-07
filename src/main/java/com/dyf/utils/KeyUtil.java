package com.dyf.utils;

import java.util.Random;

public class KeyUtil
{
    public static synchronized String genUniqueKey()
    {
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000; // 生成6位的随机数
        return System.currentTimeMillis() + String.valueOf(number);
    }

    //生成食物id
    public static synchronized String genUniqueFoodKey()
    {
        Random random = new Random();
        Integer number = random.nextInt(9000) + 1000; // 生成4位的随机数
        return System.currentTimeMillis() + String.valueOf(number);
    }

    //生成类别id
    public static synchronized String genUniqueCategoryKey()
    {
        Random random = new Random();
        Integer number = random.nextInt(9000) + 1000; // 生成4位的随机数
        return System.currentTimeMillis() + String.valueOf(number);
    }

    //生成banner id
    public static synchronized String genUniqueBannerKey()
    {
        Random random = new Random();
        Integer number = random.nextInt(90000) + 10000; // 生成5位的随机数
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
