package edu.whpu.matthew.animationmenu;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

/**
 * Created by matthew on 17-1-5.
 */

public class AnimationUtil{

    public static int count=0;

    public static void closeMenu(RelativeLayout rl,int offset) {
        for(int i=0;i<rl.getChildCount();i++){
            rl.getChildAt(i).setEnabled(false);
        }
        RotateAnimation animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 1f);
        animation.setDuration(400);
        animation.setFillAfter(true);
        animation.setStartOffset(offset);
        animation.setAnimationListener(new MyListener());
        rl.startAnimation(animation);
    }

    public static void openMenu(RelativeLayout rl,int offset) {
        for(int i=0;i<rl.getChildCount();i++){
            rl.getChildAt(i).setEnabled(true);
        }
        RotateAnimation animation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 1f);
        animation.setDuration(400);
        animation.setFillAfter(true);
        animation.setStartOffset(offset);
        animation.setAnimationListener(new MyListener());
        rl.startAnimation(animation);
    }

    public static class MyListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {
            count++;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            count--;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


}
