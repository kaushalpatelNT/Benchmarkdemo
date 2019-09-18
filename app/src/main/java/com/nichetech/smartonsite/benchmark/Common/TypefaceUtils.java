package com.nichetech.smartonsite.benchmark.Common;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by nichetech on 5/12/16.
 */

public class TypefaceUtils {

    public static Typeface MyriadProRegular(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "font/MyriadPro-Regular.otf");
    }

    public static Typeface RalewayBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "font/Raleway-Bold_0.ttf");
    }

    public static Typeface RalewayMedium(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "font/Raleway-Medium_0.ttf");
    }

    public static Typeface RalewaySemiBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "font/Raleway-SemiBold_0.ttf");
    }


}
