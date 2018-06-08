package philippmatthes.com.manni.vvo.Models;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import philippmatthes.com.manni.vvo.Connection;
import philippmatthes.com.manni.vvo.DVBError;
import philippmatthes.com.manni.vvo.Endpoint;
import philippmatthes.com.manni.vvo.GKCoordinate;
import philippmatthes.com.manni.vvo.Result;
import philippmatthes.com.manni.vvo.WGSCoordinate;

@AllArgsConstructor
public class Stop implements Comparable<Stop> {

    // Deserialization with StopDeserializer
    @Getter @Setter private String id;
    @Getter @Setter private String name;
    @Getter @Setter private String region;
    @Getter @Setter private WGSCoordinate location;

    public static void find(
            String query,
            Response.Listener<Result<FindResponse>> listener,
            RequestQueue queue
    ) {
        Map<String, Object> data = new HashMap<>();
        data.put("limit", 0);
        data.put("query", query);
        data.put("stopsOnly", true);
        data.put("dvb", true);
        Connection.post(Endpoint.pointfinder, data, listener, FindResponse.class, queue);
    }

    public static void findNear(
            Double lat,
            Double lng,
            Response.Listener<Result<FindResponse>> listener,
            RequestQueue queue
    ) {
        WGSCoordinate coordinate = new WGSCoordinate(lat, lng);
        findNear(
                coordinate,
                listener,
                queue
        );
    }

    public static void findNear(
            WGSCoordinate coordinate,
            Response.Listener<Result<FindResponse>> listener,
            RequestQueue queue
    ) {
        Optional<GKCoordinate> gkCoordinate = coordinate.asGK();
        if (!gkCoordinate.isPresent()) {
            listener.onResponse(new Result<>(Optional.empty(), Optional.of(DVBError.coordinate)));
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("limit", 10);
        data.put("assignedStops", true);
        data.put("query", "coord:" + gkCoordinate.get().getX().intValue() + ":" + gkCoordinate.get().getY().intValue());
        Connection.post(Endpoint.pointfinder, data, listener, FindResponse.class, queue);
    }

    public void monitor(
            Date date,
            Departure.DateType dateType,
            List<Mode> modes,
            Boolean allowShorttermChanges,
            Response.Listener<Result<MonitorResponse>> listener,
            RequestQueue queue
    ) {
        Departure.monitor(id, date, dateType, modes, allowShorttermChanges, listener, queue);
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
