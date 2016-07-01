package example.com.findmycar;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by 107926 on 7/1/16.
 */
public class Utilities
{
    public static int dpToPx(int dp, Context context)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
