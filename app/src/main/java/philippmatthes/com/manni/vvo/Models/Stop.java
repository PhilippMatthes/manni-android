package main.Models;

import java.util.*;

public class Stop implements Comparable<Stop> {

    private String id;
    private String name;
    private Optional<String> region;
    private Optional<WGSCoordinate> location;


    public Stop(String id, String name, Optional<String> region, Optional<WGSCoordinate> location) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getRegion() {
        return region;
    }

    public Optional<WGSCoordinate> getLocation() {
        return location;
    }

    public static Result<FindResponse> find(String query) {
        Map<String, Object> data = new HashMap<>();
        data.put("limit", 0);
        data.put("query", query);
        data.put("stopsOnly", true);
        data.put("dvb", true);
        return Connection.post(Endpoint.pointfinder, data);
    }

    public static Result<FindResponse> findNear(Double lat, Double lng) {
        WGSCoordinate coordinate = new WGSCoordinate(lat, lng);
        return findNear(coordinate);
    }

    public static Result<FindResponse> findNear(WGSCoordinate coordinate) {
        Optional<GKCoordinate> gkCoordinate = coordinate.asGK();
        if (!gkCoordinate.isPresent()) {
            return new Result<FindResponse>(DVBError.getCoordinate());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("limit", 10);
        data.put("assignedStops", true);
        data.put("query", "coord:" + (int) gkCoordinate.get().getX() + ":" + (int) gkCoordinate.get().getY());
        return Connection.post(Endpoint.pointfinder, data);
    }

    public Result<MonitorResponse> monitor(
            Date date,
            Departure.DateType dateType,
            List<Mode> modes,
            Boolean allowShorttermChanges
    ) {
        return Departure.monitor(id, date, dateType, modes, allowShorttermChanges);
    }


    @Override
    public int compareTo(Stop o) {
        return o.getId().compareTo(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
