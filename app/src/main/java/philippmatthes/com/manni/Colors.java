package philippmatthes.com.manni;

import android.graphics.Color;

public class Colors {

    private static final int[] colors = {
            Color.parseColor("#f44336"),
            Color.parseColor("#e91e63"),
            Color.parseColor("#9c27b0"),
            Color.parseColor("#673ab7"),
            Color.parseColor("#3f51b5"),
            Color.parseColor("#2196f3"),
            Color.parseColor("#03a9f4"),
            Color.parseColor("#00bcd4"),
            Color.parseColor("#009688"),
            Color.parseColor("#4caf50"),
            Color.parseColor("#8bc34a"),
            Color.parseColor("#ffc107"),
    };

    public static int getColor(int attribute) {
        return colors[attribute % colors.length];
    }

    public static int lighten(int color, double factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

}
