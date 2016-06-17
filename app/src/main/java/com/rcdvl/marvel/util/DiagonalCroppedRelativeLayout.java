package com.rcdvl.marvel.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by renan on 16/06/16.
 */
public class DiagonalCroppedRelativeLayout extends RelativeLayout {

    Path path = new Path();

    public DiagonalCroppedRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        float px = AndroidUtils.dpToPx(20f);

        path.lineTo(getWidth(), 0f);
        path.lineTo(getWidth(), getHeight());
        path.lineTo(0f, getHeight() - px);
        path.lineTo(0f, 0f);

        canvas.clipPath(path);
        super.dispatchDraw(canvas);
    }
}
