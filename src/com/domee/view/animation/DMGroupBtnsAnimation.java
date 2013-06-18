package com.domee.view.animation;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import com.domee.view.DMInOutImageButton;

/**
 * Created by duyuan on 13-6-5.
 */
public class DMGroupBtnsAnimation extends DMInOutAnimation {

    public static final int		DURATION	= 200;
    private static final int	xOffset		= 16;
    private static final int	yOffset		= -13;

    public DMGroupBtnsAnimation(Direction direction, long l, View view) {
        super(direction, l, new View[] { view });
    }

    public static void startAnimations(ViewGroup viewgroup, DMInOutAnimation.Direction direction) {
        switch (direction) {
            case IN:
                startAnimationsIn(viewgroup);
                break;
            case OUT:
                startAnimationsOut(viewgroup);
                break;
        }
    }

    private static void startAnimationsIn(ViewGroup viewgroup) {
        for (int i = 0; i < viewgroup.getChildCount(); i++) {
            if (viewgroup.getChildAt(i) instanceof DMInOutImageButton) {
                DMInOutImageButton inoutimagebutton = (DMInOutImageButton) viewgroup.getChildAt(i);
                DMGroupBtnsAnimation animation = new DMGroupBtnsAnimation(
                        DMInOutAnimation.Direction.IN, DURATION, inoutimagebutton);
                animation.setStartOffset((i * 100) / (-1 + viewgroup.getChildCount()));
                animation.setInterpolator(new OvershootInterpolator(2F));
                inoutimagebutton.startAnimation(animation);
            }
        }
    }

    private static void startAnimationsOut(ViewGroup viewgroup) {
        for (int i = 0; i < viewgroup.getChildCount(); i++) {
            if (viewgroup.getChildAt(i) instanceof DMInOutImageButton) {
                DMInOutImageButton inoutimagebutton = (DMInOutImageButton) viewgroup
                        .getChildAt(i);
                DMGroupBtnsAnimation animation = new DMGroupBtnsAnimation(
                        DMInOutAnimation.Direction.OUT, DURATION,
                        inoutimagebutton);
                animation.setStartOffset((100 * ((-1 + viewgroup
                        .getChildCount()) - i))
                        / (-1 + viewgroup.getChildCount()));
                animation.setInterpolator(new AnticipateInterpolator(2F));
                inoutimagebutton.startAnimation(animation);
            }
        }
    }

    @Override
    protected void addInAnimation(View[] aview) {
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) aview[0]
                .getLayoutParams();
        addAnimation(new TranslateAnimation(xOffset + -mlp.leftMargin, 0F,
                yOffset + mlp.bottomMargin, 0F));
    }

    @Override
    protected void addOutAnimation(View[] aview) {
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) aview[0]
                .getLayoutParams();
        addAnimation(new TranslateAnimation(0F, xOffset + -mlp.leftMargin, 0F,
                yOffset + mlp.bottomMargin));
    }
}
