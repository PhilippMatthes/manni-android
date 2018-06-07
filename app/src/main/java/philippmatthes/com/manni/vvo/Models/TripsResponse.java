package philippmatthes.com.manni.vvo.Models;

import java.util.Date;

public class TripsResponse {

    private final TripStop[] tripStops;
    private final Date expirationTime;

    public TripsResponse(TripStop[] tripStops, Date expirationTime) {

        this.tripStops = tripStops;
        this.expirationTime = expirationTime;
    }

    public TripStop[] getTripStops() {
        return tripStops;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

}
