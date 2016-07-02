package example.com.findmycar;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.renderscript.Sampler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by 107926 on 7/1/16.
 */
public class Utilities
{
    public static FastOutSlowInInterpolator fastOutSlowInInterpolator = new FastOutSlowInInterpolator();

    public static int dpToPx(int dp, Context context)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static ValueAnimator darkenThenLightenAnimation(Context c, final View view)
    {
        int colorFrom = ContextCompat.getColor(c, R.color.blue_500);
        int colorTo = ContextCompat.getColor(c, R.color.blue_700);
        final ValueAnimator darkenColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        darkenColorAnimation.setInterpolator(fastOutSlowInInterpolator);
        darkenColorAnimation.setDuration(500); // milliseconds

        darkenColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {

            @Override
            public void onAnimationUpdate(ValueAnimator animator)
            {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        final ValueAnimator lightenColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorTo, colorFrom);
        lightenColorAnimation.setInterpolator(fastOutSlowInInterpolator);
        lightenColorAnimation.setDuration(500);
        lightenColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {

            @Override
            public void onAnimationUpdate(ValueAnimator animator)
            {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });


        darkenColorAnimation.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {

            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                lightenColorAnimation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {

            }
        });
        return darkenColorAnimation;
    }
}
