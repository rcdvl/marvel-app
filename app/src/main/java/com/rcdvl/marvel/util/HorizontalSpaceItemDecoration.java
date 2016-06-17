package com.rcdvl.marvel.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

/**
 * Created by renan on 20/06/16.
 */
public class HorizontalSpaceItemDecoration extends ItemDecoration {
    private float horizontalSpaceWidth;

    public HorizontalSpaceItemDecoration(float spacing) {
        horizontalSpaceWidth = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            return;
        }

        outRect.left = (int)AndroidUtils.dpToPx(horizontalSpaceWidth);
    }
}
