package main.Models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class POI {
    private String descriptionString;

    public POI(String descriptionString) {
        this.descriptionString = descriptionString;
    }

    public String getDescriptionString() {
        return descriptionString;
    }

    public enum Kind {
        rentABike ("RentABike"),
        stop ("Stop"),
        poi ("Poi"),
        carSharing ("CarSharing"),
        ticketMachine ("TicketMachine"),
        platform ("Platform"),
        parkAndRide ("ParkAndRide");

        private final String rawValue;

        Kind(String s) {
            this.rawValue = s;
        }

        public String getRawValue() {
            return rawValue;
        }

        public Kind[] getAll() {
            return new Kind[]{
                Kind.rentABike,
                Kind.stop,
                Kind.poi,
                Kind.carSharing,
                Kind.ticketMachine,
                Kind.platform,
                Kind.parkAndRide,
            };
        }
    }

    public class CoordRect {
        private Coordinate northeast;
        private Coordinate southwest;

        public CoordRect(Coordinate northeast, Coordinate southwest) {
            this.northeast = northeast;
            this.southwest = southwest;
        }

        public Coordinate getNortheast() {
            return northeast;
        }

        public Coordinate getSouthwest() {
            return southwest;
        }
    }

    public static Result<POIResponse> find(
        List<Kind> types,
        CoordRect inRect
    ) {
        Optional<GKCoordinate> sw = inRect.southwest.asGK();
        Optional<GKCoordinate> ne = inRect.northeast.asGK();

        if (!sw.isPresent() && !ne.isPresent()) {
            // TODO
        }
        Map<String, Object> data = new HashMap<>();
        data.put("swlat", sw.get().getX());
        data.put("swlng", sw.get().getY());
        data.put("nelat", ne.get().getX());
        data.put("nelng", ne.get().getY());
        data.put("showlines", true);
        data.put("pintypes", types.stream().map(Kind::getRawValue).collect(Collectors.toList()));
        return Connection.post(Endpoint.poiSearch, data);
    }
}
