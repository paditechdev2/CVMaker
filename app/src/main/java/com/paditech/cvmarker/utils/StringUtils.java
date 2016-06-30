package com.paditech.cvmarker.utils;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by USER on 10/6/2016.
 */
public class StringUtils {


    public static boolean isEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }



}