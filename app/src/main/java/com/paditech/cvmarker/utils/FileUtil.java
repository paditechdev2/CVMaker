package com.paditech.cvmarker.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.paditech.cvmarker.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


/**
 * Created by USER on 15/6/2016.
 */
public class FileUtil {

    public static Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/" + "image" + new Date().getTime() + ".jpeg");
        if(!file.canRead()) {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "image" + new Date().getTime() + ".jpeg");
        }
        Uri imgUri = Uri.fromFile(file);
        return imgUri;
    }

    public static String resizeImage(File imagePath) {
        Bitmap b= BitmapFactory.decodeFile(imagePath.getAbsolutePath());
        Bitmap main = null;

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(imagePath.getAbsolutePath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    main = rotateImage(b, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    main = rotateImage(b, 180);
                    break;
                // etc.
                default: main = b;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap out = Bitmap.createScaledBitmap(main, main.getWidth()/4, main.getHeight()/4, false);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(imagePath);
            out.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {}
        return imagePath.getAbsolutePath();
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        try {
            Bitmap retVal;
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
            return retVal;

        } catch (OutOfMemoryError e) {

        }
        return source;
    }

    public static Bitmap getBitmapFromFile(File file) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
        return bitmap;
    }

    public static String getFilePathFromUri(Context context, Uri contentURI) {
        String path = "";
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null) {
            path = contentURI.getPath();
        } else {
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
            }
            cursor.close();
        }

        return path;
    }

    public static String getPdfFile( String name) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/" + name ;
        File file = new File(path);
        if(!file.canRead()) {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).mkdir();
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/" + name;
        }
        return path;
    }

    public static Image getImageAvatar(Context context, String file) {
        File imgFile = new File(file);
        Bitmap bitmap;
        if(imgFile.exists()) {
            bitmap = getBitmapFromFile(new File(file));

        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.ic_user);
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        try {
            Image image = Image.getInstance(stream.toByteArray());
            return image;
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getInternalFilePath(Context context) {
        File file = new File(context.getFilesDir(), Constant.TEMPLATE_RESUME);
        return file.getPath();
    }

}
