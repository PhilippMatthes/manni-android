package philippmatthes.com.manni.jVVO.Tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class Time {
    public static int minutesUntil(Date date) {
        if (date == null) { return 0; }

        long diff = date.getTime() - new Date().getTime();
        return (int) (diff / 60000);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        return dateFormat.format(date);
    }
}
