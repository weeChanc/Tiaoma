package com.example.tiaoma.utils;

import java.math.BigDecimal;

public class DigitUtils {
    public static boolean isDigit(CharSequence str){
        try{
            new BigDecimal(str.toString());
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
