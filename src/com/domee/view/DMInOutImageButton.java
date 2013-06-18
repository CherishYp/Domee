package com.domee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import com.domee.view.animation.DMInOutAnimation;

/**
 * Created by duyuan on 13-6-5.
 */
public class DMInOutImageButton extends ImageButton {

    private Animation animation;

    public DMInOutImageButton(Context context) {
        super(context);
    }

    public DMInOutImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DMInOutImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        if ((this.animation instanceof DMInOutAnimation)) {
            setVisibility(((DMInOutAnimation) this.animation).direction != DMInOutAnimation.Direction.OUT ? View.VISIBLE
                    : View.GONE);
        }
    }

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
        if ((this.animation instanceof DMInOutAnimation))
            setVisibility(View.VISIBLE);
    }

    @Override
    public void startAnimation(Animation animation) {
        super.startAnimation(animation);
        this.animation = animation;
        getRootView().postInvalidate();
    }
}
