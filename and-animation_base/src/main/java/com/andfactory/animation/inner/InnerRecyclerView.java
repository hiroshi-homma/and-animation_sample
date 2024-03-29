package com.andfactory.animation.inner;


import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Extended {@link androidx.recyclerview.widget.RecyclerView}
 * that works <b>only</b> with {@link InnerLayoutManager} and {@link InnerAdapter}.
 */
public class InnerRecyclerView extends RecyclerView {

    public InnerRecyclerView(Context context) {
        super(context);
    }

    public InnerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param adapter must be instance of TailAdapter class
     */
    @Override
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof InnerAdapter)) {
            throw new IllegalArgumentException("Adapter must be instance of InnerAdapter class");
        }
        super.setAdapter(adapter);
    }

    /**
     *
     * @param lm must be instance of InnerLayoutManager class
     */
    @Override
    public void setLayoutManager(LayoutManager lm) {
        if (!(lm instanceof InnerLayoutManager)) {
            throw new IllegalArgumentException("LayoutManager must be instance of InnerLayoutManager class");
        }
        super.setLayoutManager(lm);
    }

}
