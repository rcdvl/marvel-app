package com.rcdvl.marvel.util

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.graphics.Palette
import android.util.AttributeSet
import android.widget.ImageView
import com.rcdvl.marvel.R

/**
 * Created by renan on 3/18/16.
 */
class DiagonalDividerImageView : ImageView {

    val lineColor: Int
    var path: Path = Path()
    val borderPaint = Paint()
    var borderColor: Int = 0

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val a: TypedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.DiagonalDividerImageView,
                0, 0);

        try {
            lineColor = a.getColor(R.styleable.DiagonalDividerImageView_dividerColor, Color.GREEN);
        } finally {
            a.recycle();
        }

        borderPaint.strokeWidth = AndroidUtils.dpToPx(5f)
        borderPaint.style = Paint.Style.STROKE

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)

        Palette.from(bm).generate {
            borderColor = it.getVibrantColor(0)
            invalidate()
        }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)

        if (drawable != null) {
            Palette.from(drawableToBitmap(drawable)).generate {
                borderColor = it.getVibrantColor(0)
                invalidate()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        val px: Float = AndroidUtils.dpToPx(20f)

        path.lineTo(width.toFloat(), 0f)
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0f, height.toFloat() - px)
        path.lineTo(0f, 0f)

        canvas?.clipPath(path)
        super.onDraw(canvas)

        borderPaint.color = borderColor
        canvas?.drawLine(0f, height.toFloat() - px, width.toFloat(), height.toFloat(), borderPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    fun drawableToBitmap(drawable: Drawable): Bitmap {
        var bitmap: Bitmap;

        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable;
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap;
            }
        }

        if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888);
        }

        val canvas = Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.width, canvas.height);
        drawable.draw(canvas);
        return bitmap;
    }
}