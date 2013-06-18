package com.domee.view.animation;

import android.graphics.Path;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

/**
 * Created by duyuan on 13-6-5.
 */
public abstract class DMInOutAnimation extends AnimationSet {

    public Direction	direction;

    public enum Direction {
        IN, OUT;
    }

    public DMInOutAnimation(Direction direction, long l, View[] aview) {
        super(true);
        this.direction = direction;
        switch (this.direction) {
            case IN:
                addInAnimation(aview);
                break;
            case OUT:
                addOutAnimation(aview);

                break;
        }
        setDuration(l);
    }

    protected abstract void addInAnimation(View aview[]);

    protected abstract void addOutAnimation(View aview[]);
}
