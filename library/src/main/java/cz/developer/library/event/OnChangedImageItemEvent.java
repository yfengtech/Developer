package cz.developer.library.event;

import cz.developer.library.model.ImageItem;

/**
 * Created by cz on 11/9/16.
 */

public class OnChangedImageItemEvent {
    public final ImageItem item;
    public final int index;

    public OnChangedImageItemEvent(ImageItem item,int index) {
        this.item = item;
        this.index=index;
    }
}
