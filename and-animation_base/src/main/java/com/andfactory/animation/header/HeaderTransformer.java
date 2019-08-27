package com.andfactory.animation.header;

import android.view.View;

import com.andfactory.animation.TailItemTransformer;
import com.andfactory.animation.TailLayoutManager;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

public class HeaderTransformer implements TailLayoutManager.PageTransformer<HeaderItem> {

    private final TailItemTransformer mTransformer;

    public HeaderTransformer() {
        mTransformer = new TailItemTransformer();
    }

    @Override
    public void transformPage(@NonNull HeaderItem item, float scrollPosition) {
        final View header = item.getHeader();
        final TailItemTransformer.TransformParams params = mTransformer.getParamsForPosition(
                scrollPosition, header.getWidth(), header.getHeight());

        ViewCompat.setPivotX(header, params.pivotX);
        ViewCompat.setScaleX(header, params.scale);
        ViewCompat.setScaleY(header, params.scale);
        ViewCompat.setAlpha(header, scrollPosition < 0 ? params.alphaLeft : params.alphaRight);
        ViewCompat.setTranslationY(header, params.offsetY);

        ViewCompat.setAlpha(item.getHeaderAlphaView(), 1 - params.alphaChild);

        mTransformer.transformPage(item, scrollPosition);
    }

}
