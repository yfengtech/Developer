package cz.developer.library.ui;

import android.widget.ImageView;

import java.io.File;

import cz.developer.library.callback.OnDisplayComplete;

/**
 * Created by cz on 1/13/17.
 */
public interface ImageDisplayInterface {
    void onDisplay(ImageView imageView, String url, OnDisplayComplete displayComplete);

    File getImageFile(String url);
}
