package cz.developer.library.callback;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by cz on 1/13/17.
 */

public interface OnDisplayComplete {
    void onComplete(String url, File file, Bitmap bitmap);
}
