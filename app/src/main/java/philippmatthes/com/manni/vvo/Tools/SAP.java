package main.Tools;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class SAP {

    public static String fromDate(Date date) {
        long seconds = System.currentTimeMillis() / 1000l;
        return "/Date(" + seconds + "+0100)/";
    }

}
