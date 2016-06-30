package com.paditech.cvmarker.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;

/**
 * Created by USER on 16/6/2016.
 */
public class FontUtil {

    public static BaseFont arialFont() {
        BaseFont urName = null;
        try {
            urName = BaseFont.createFont("assets/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return urName;
    }

    public static BaseFont aachenbFont() {
        BaseFont urName = null;
        try {
            urName = BaseFont.createFont("assets/aachenb.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return urName;
    }

    public static BaseFont tahomaFont() {
        BaseFont urName = null;
        try {
            urName = BaseFont.createFont("assets/tahomabd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return urName;
    }

    public static BaseFont timesFont() {
        BaseFont urName = null;
        try {
            urName = BaseFont.createFont("assets/palab.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return urName;
    }

}
