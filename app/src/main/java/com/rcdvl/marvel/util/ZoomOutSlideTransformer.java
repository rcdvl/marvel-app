package com.rcdvl.marvel.util;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

/**
 * Created by renan on 04/07/16.
 */
public class ZoomOutSlideTransformer implements PageTransformer {
    public static float MIN_SCALE = 0.9f;
    public static float MIN_ALPHA = 0.6f;

    @Override
    public void transformPage(View page, float position) {
        if (page == null) {
            return;
        }

        float correctPosition = position - ((ViewPager)page.getParent()).getPaddingRight() / (float)page.getWidth();

        if (correctPosition <= -1 || correctPosition >= 1) {
            page.setAlpha(0f);
        } else {
            page.setAlpha(1f);
        }

        if (correctPosition >= -1 || correctPosition <= 1) {
            // Modify the default slide transition to shrink the page as well
            int height = page.getHeight();
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(correctPosition));
            float vertMargin = height * (1 - scaleFactor) / 2;
            float horzMargin = page.getWidth() * (1 - scaleFactor) / 2;

            // Center vertically
            page.setPivotY(0.5f * height);

            if (correctPosition < 0) {
                page.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                page.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            page.setAlpha((MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)));
        }
    }
}
