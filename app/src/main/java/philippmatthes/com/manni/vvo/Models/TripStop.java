package philippmatthes.com.manni.vvo.Models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import philippmatthes.com.manni.vvo.Connection;
import philippmatthes.com.manni.vvo.Tools.SAP;

public class TripStop implements Comparable<TripStop> {

    private String id;
    private String place;
    private String name;
    private Position position;
    private Optional<Platform> platform;
    private Date time;

    public TripStop(String id, String place, String name, Position position, Optional<Platform> platform, Date time) {
        this.id = id;
        this.place = place;
        this.name = name;
        this.position = position;
        this.platform = platform;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getPlace() {
        return place;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public Optional<Platform> getPlatform() {
        return platform;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public int compareTo(TripStop o) {
        return o.getId().compareTo(id);
    }

    public enum Position {
        Previous("Previous"),
        Current("Current"),
        Next("Next");

        private final String rawValue;

        Position(String s) {
            rawValue = s;
        }

        public String getRawValue() {
            return rawValue;
        }
    }

    public static Result<TripsResponse> get(
            String tripId,
            String stopId,
            Date time
    ) {
        Map<String, Object> data = new HashMap<>();
        data.put("tripid", tripId);
        data.put("stopid", stopId);
        data.put("time", SAP.fromDate(time));
        return Connection.post(Endpoint.trip, data);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
