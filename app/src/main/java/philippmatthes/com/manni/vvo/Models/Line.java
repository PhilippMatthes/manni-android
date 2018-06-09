package philippmatthes.com.manni.vvo.Models;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import philippmatthes.com.manni.vvo.Connection;
import philippmatthes.com.manni.vvo.DVBError;
import philippmatthes.com.manni.vvo.Endpoint;
import philippmatthes.com.manni.vvo.Result;

@ToString
@AllArgsConstructor
public class Line implements Comparable<Line>  {
    @SerializedName("Name") @Getter @Setter private String name;
    @SerializedName("Mot") @Getter @Setter private Mode mode;
    @SerializedName("Changes") @Getter @Setter private List<String> changes;
    @SerializedName("Directions") @Getter @Setter private List<Direction> directions;
    @SerializedName("Diva") @Getter @Setter private Diva diva;

    @Override
    public int compareTo(Line o) {
        return o.getName().compareTo(name);
    }

    @AllArgsConstructor
    public class Direction {
        @SerializedName("Name") @Getter @Setter private String name;
        @SerializedName("TimeTables") @Getter @Setter private List<TimeTable> timetables;
    }

    @AllArgsConstructor
    public class TimeTable {
        @SerializedName("Id") @Getter @Setter public String id;
        @SerializedName("Name") @Getter @Setter public String name;
    }

    public static void getById(
        String stopId,
        RequestQueue queue,
        Response.Listener<Result<LinesResponse>> listener
    ) {
        Map<String, Object> data = new HashMap<>();
        data.put("stopid", stopId);
        Connection.post(Endpoint.lines, data, listener, LinesResponse.class, queue);
    }

    public static void getByName(
        String stopName,
        RequestQueue queue,
        Response.Listener<Result<LinesResponse>> listener
    ) {
        Stop.find(
            stopName, queue,
            response -> {
                if (!response.getResponse().isPresent()) {
                    listener.onResponse(new Result<>(Optional.empty(), response.getError()));
                    return;
                }
                List<Stop> stops = response.getResponse().get().getStops();
                if (stops.size() == 0) {
                    listener.onResponse(new Result<>(Optional.empty(), Optional.of(DVBError.request)));
                    return;
                }
                Stop stop = stops.get(0);
                getById(stop.getId(), queue, listener);
            }
        );
    }

    @Override
    public int hashCode() {
        return name.hashCode() + mode.hashCode();
    }

}
