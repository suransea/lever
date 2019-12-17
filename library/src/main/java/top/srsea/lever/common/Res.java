package top.srsea.lever.common;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import top.srsea.lever.Lever;

/**
 * Convenient access to resources.
 *
 * @author sea
 */
public class Res {

    /**
     * Returns a color associated with a particular resource ID and styled for
     * the current theme.
     *
     * @param id the desired resource identifier
     * @return color value
     */
    public static int color(@ColorRes int id) {
        return ContextCompat.getColor(Lever.getContext(), id);
    }

    /**
     * Retrieve a dimension for a particular resource ID.
     *
     * @param id the desired resource identifier
     * @return dimen value in px
     */
    public static float dimen(@DimenRes int id) {
        return Lever.getContext().getResources().getDimension(id);
    }

    /**
     * Returns the string value associated with a particular resource ID.
     *
     * @param id the desired resource identifier
     * @return the string data associated with the resource
     */
    public static String string(@StringRes int id) {
        return Lever.getContext().getResources().getString(id);
    }

    /**
     * Return a drawable object associated with a particular resource ID.
     *
     * @param id the desired resource identifier
     * @return an object that can be used to draw this resource.
     */
    public static Drawable drawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(Lever.getContext(), id);
    }

    /**
     * Returns a color state list associated with a particular resource ID and
     * styled for the current theme.
     *
     * @param id the desired resource identifier
     * @return a color state list.
     */
    public static ColorStateList colorStateList(@ColorRes int id) {
        return ContextCompat.getColorStateList(Lever.getContext(), id);
    }
}
