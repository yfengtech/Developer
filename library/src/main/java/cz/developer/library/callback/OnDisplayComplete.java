package cz.developer.library.callback;

import android.graphics.Bitmap;

/**
 * Created by cz on 1/13/17.
 */

public interface OnDisplayComplete {
    void onComplete(String url,Bitmap bitmap);
}
