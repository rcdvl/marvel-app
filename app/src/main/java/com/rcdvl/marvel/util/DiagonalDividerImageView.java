package com.rcdvl.marvel.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.rcdvl.marvel.R;

/**
 * Created by renan on 16/06/16.
 */
public class DiagonalDividerImageView extends ImageView {

    int lineColor;
    Path path = new Path();
    Paint borderPaint = new Paint();
    int borderColor = 0;

    public DiagonalDividerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DiagonalDividerImageView, 0, 0);

        try {
            lineColor = a.getColor(R.styleable.DiagonalDividerImageView_dividerColor, Color.GREEN);
        } finally {
            a.recycle();
        }

        borderPaint.setStrokeWidth(AndroidUtils.dpToPx(5f));
        borderPaint.setStyle(Paint.Style.STROKE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);

        Palette.from(bm).generate(new PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                borderColor = palette.getVibrantColor(0);
                invalidate();
            }
        });
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);

        if (drawable != null) {
            Palette.from(drawableToBitmap(drawable)).generate(new PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    borderColor = palette.getVibrantColor(0);
                    invalidate();
                }
            });
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float px = AndroidUtils.dpToPx(20f);

        path.lineTo(getWidth(), 0f);
        path.lineTo(getWidth(), getHeight());
        path.lineTo(0f, getHeight() - px);
        path.lineTo(0f, 0f);

        canvas.clipPath(path);
        super.onDraw(canvas);

        borderPaint.setColor(borderColor);
        canvas.drawLine(0f, getHeight() - px, getWidth(), getHeight(), borderPaint);
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
