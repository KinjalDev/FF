package com.example.friendfield.Utils;

import android.view.View;

public class CubeOutTransformer extends ABaseTransformer {
    private int distanceMultiplier = 20;
    @Override
    protected void onTransform(View view, float position) {
        view.setCameraDistance((float) (view.getWidth() * this.distanceMultiplier));
        view.setPivotX(position < 0f ? view.getWidth() : 0f);
        view.setPivotY(view.getHeight() * 0.5f);
        view.setRotationY(90f * position);
    }

    @Override
    public boolean isPagingEnabled() {
        return true;
    }

}