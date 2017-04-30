package com.inf8405.polymtl.lab3.managers;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Utility class used to encode and decode images from the camera to something that can be saved to
 * firebase, in this case base64!
 * Carried over from lab 2
 */

public final class ImageManager {
    private static final String TAG = "ImageManager";
    private static final String noPicture = "No picture found";
    
    private ImageManager() {
        // Empty by design for a "Static class"
        throw new AssertionError();
    }
    
    public static String encodeImageToString(Bitmap image) {
        if (image == null) {
            return noPicture;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }
    
    public static Bitmap decodeImageFromString(String image) {
        if (image == null || image.equals(noPicture)) {
            return null;
        }
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
}
