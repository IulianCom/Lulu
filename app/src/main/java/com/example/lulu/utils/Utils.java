package com.example.lulu.utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

public class Utils {
    public static String getFileName(Uri uri, Activity activity) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        Log.d("sasasasas", "getFileName: " + result);
        return result;
    }

    public static String withoutFileExtension(String fileName) {
        if(!fileName.contains(".")) {
            return fileName;
        }
        int cut = fileName.lastIndexOf('.');
        return fileName.substring(0, cut);
    }

    public static String getFileExtension(String fileName) {
        if(!fileName.contains(".")) {
            return "";
        }
        int cut = fileName.lastIndexOf('.');
        return fileName.substring(cut);
    }

    public static String getCurrentUserUuid() {
        return FirebaseHelper.mAuth.getUid();
    }
}
