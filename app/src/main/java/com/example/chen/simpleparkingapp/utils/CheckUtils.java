package com.example.chen.simpleparkingapp.utils;

import android.text.TextUtils;
import android.widget.TextView;

public class CheckUtils {

    private static CheckUtils instances;
    private TextView textView;

    public static CheckUtils getInstances() {
        if (instances == null) {
            instances = new CheckUtils();
        }
        return instances;
    }

    /**
     * 验证手机格式
     */
    public boolean isMobile(String number) {
        String num = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            return number.matches(num);
        }
    }

    /**
     * 车牌号
     *
     * @param carnumber
     * @return
     */
    public boolean isCarnumberNO(String carnumber) {
     /*
         1.常规车牌号：仅允许以汉字开头，后面可录入六个字符，由大写英文字母和阿拉伯数字组成。如：粤B12345；
         2.武警车牌：允许前两位为大写英文字母，后面可录入五个或六个字符，由大写英文字母和阿拉伯数字组成，其中第三位可录汉字也可录大写英文字母及阿拉伯数字，第三位也可空，如：WJ警00081、WJ京1234J、WJ1234X。
         3.最后一个为汉字的车牌：允许以汉字开头，后面可录入六个字符，前五位字符，由大写英文字母和阿拉伯数字组成，而最后一个字符为汉字，汉字包括“挂”、“学”、“警”、“军”、“港”、“澳”。如：粤Z1234港。
        4.新军车牌：以两位为大写英文字母开头，后面以5位阿拉伯数字组成。如：BA12345。
       */
        String carnumRegex = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{0,1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";
        if (TextUtils.isEmpty(carnumber)) return false;
        else return carnumber.matches(carnumRegex);
    }

    public String getTwoNumber(int number) {
        if (number >= 10) {
            return number + "";
        } else {
            return "0" + number;
        }
    }
}
