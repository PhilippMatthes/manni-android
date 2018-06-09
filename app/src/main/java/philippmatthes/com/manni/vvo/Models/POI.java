package philippmatthes.com.manni.vvo.Models;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import philippmatthes.com.manni.vvo.Connection;
import philippmatthes.com.manni.vvo.DVBError;
import philippmatthes.com.manni.vvo.Endpoint;
import philippmatthes.com.manni.vvo.GKCoordinate;
import philippmatthes.com.manni.vvo.Result;

@ToString
@AllArgsConstructor
public class POI {
    @Getter @Setter private String descriptionString;

    public enum Kind {
        @SerializedName("RentABike") rentABike ("RentABike"),
        @SerializedName("Stop") stop ("Stop"),
        @SerializedName("Poi") poi ("Poi"),
        @SerializedName("CarSharing") carSharing ("CarSharing"),
        @SerializedName("TicketMachine") ticketMachine ("TicketMachine"),
        @SerializedName("Platform") platform ("Platform"),
        @SerializedName("ParkAndRide") parkAndRide ("ParkAndRide");

        private final String rawValue;

        Kind(String s) {
            this.rawValue = s;
        }

        public String getValue() {
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

    @AllArgsConstructor
    public class CoordRect {
        @Getter @Setter private Coordinate northeast;
        @Getter @Setter private Coordinate southwest;
    }

    public static void find(
        List<Kind> types,
        CoordRect inRect,
        RequestQueue queue,
        Response.Listener<Result<POIResponse>> listener
    ) {
        Optional<GKCoordinate> sw = inRect.getSouthwest().asGK();
        Optional<GKCoordinate> ne = inRect.getNortheast().asGK();

        if (!sw.isPresent() || !ne.isPresent()) {
            listener.onResponse(new Result<>(Optional.empty(), Optional.of(DVBError.coordinate)));
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("swlat", sw.get().getX());
        data.put("swlng", sw.get().getY());
        data.put("nelat", ne.get().getX());
        data.put("nelng", ne.get().getY());
        data.put("showlines", true);
        data.put("pintypes", types.stream().map(Kind::getValue).collect(Collectors.toList()));
        Connection.post(Endpoint.poiSearch, data, listener, POIResponse.class, queue);
    }
}
