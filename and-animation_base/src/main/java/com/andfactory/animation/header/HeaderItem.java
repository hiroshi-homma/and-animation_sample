package com.andfactory.animation.header;

import android.view.View;

import com.andfactory.animation.TailItem;
import com.andfactory.animation.inner.InnerRecyclerView;

public abstract class HeaderItem extends TailItem<InnerRecyclerView> {

    public HeaderItem(View itemView) {
        super(itemView);
    }

    abstract public View getHeader();

    abstract public View getHeaderAlphaView();

}
