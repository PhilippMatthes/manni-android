package philippmatthes.com.manni.vvo.Models;

import java.util.Date;
import java.util.List;

public class MonitorResponse {
    private final String stopName;
    private final String place;
    private final Date expirationTime;
    private final List<Departure> departures;

    public MonitorResponse(
            String stopName,
            String place,
            Date expirationTime,
            List<Departure> departures
    ) {
        this.stopName = stopName;
        this.place = place;
        this.expirationTime = expirationTime;
        this.departures = departures;
    }

    public String getPlace() {
        return place;
    }

    public String getStopName() {
        return stopName;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public List<Departure> getDepartures() {
        return departures;
    }
}
