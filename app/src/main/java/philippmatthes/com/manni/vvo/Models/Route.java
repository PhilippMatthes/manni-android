package philippmatthes.com.manni.vvo.Models;

import android.support.annotation.NonNull;


import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import philippmatthes.com.manni.vvo.Connection;
import philippmatthes.com.manni.vvo.DVBError;
import philippmatthes.com.manni.vvo.Endpoint;
import philippmatthes.com.manni.vvo.Result;
import philippmatthes.com.manni.vvo.Tools.ISO8601;
import philippmatthes.com.manni.vvo.WGSCoordinate;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class Route implements Comparable<Route> {
    @SerializedName("PriceLevel") @Getter @Setter private Optional<Integer> priceLevel;
    @SerializedName("Price") @Getter @Setter private Optional<String> price;
    @SerializedName("Duration") @Getter @Setter private Integer duration;
    @SerializedName("Interchanges") @Getter @Setter private Integer interchanges;
    @SerializedName("MotChain") @Getter @Setter private List<ModeElement> modeChain;
    @SerializedName("FareZoneOrigin") @Getter @Setter private Optional<Integer> fareZoneOrigin;
    @SerializedName("FareZoneDestination") @Getter @Setter private Optional<Integer> fareZoneDestination;
    @SerializedName("MapPdfId") @Getter @Setter private String mapPdfId;
    @SerializedName("RouteId") @Getter @Setter private Integer routeId;
    @SerializedName("PartialRoutes") @Getter @Setter private List<RoutePartial> partialRoutes;
    @SerializedName("MapData") @Getter @Setter private MapData[] mapData;

    @Override
    public int compareTo(@NonNull Route o) {
        return o.getRouteId().compareTo(routeId);
    }

    @AllArgsConstructor
    public class ModeElement {
        @SerializedName("Name") @Getter @Setter private Optional<String> name;
        @SerializedName("Type") @Getter @Setter private Optional<Mode> mode;
        @SerializedName("Direction") @Getter @Setter private Optional<String> direction;
        @SerializedName("Changes") @Getter @Setter private Optional<List<String>> changes;
        @SerializedName("Diva") @Getter @Setter private Optional<Diva> diva;
    }

    @AllArgsConstructor
    public class RoutePartial {
        @SerializedName("PartialRouteId") @Getter @Setter private Optional<Integer> partialRouteId;
        @SerializedName("Duration") @Getter @Setter private Optional<Integer> duration;
        @SerializedName("Mot") @Getter @Setter private ModeElement mode;
        @SerializedName("MapDataIndex") @Getter @Setter private Integer mapDataIndex;
        @SerializedName("Shift") @Getter @Setter private String shift;
        @SerializedName("RegularStops") @Getter @Setter private Optional<List<RouteStop>> regularStops;
    }

    @AllArgsConstructor
    public class RouteStop {
        @SerializedName("ArrivalTime") @Getter @Setter private Date arrivalTime;
        @SerializedName("DepartureTime") @Getter @Setter private Date departureTime;
        @SerializedName("Place") @Getter @Setter private String place;
        @SerializedName("Name") @Getter @Setter private String name;
        @SerializedName("Type") @Getter @Setter private String type;
        @SerializedName("DataId") @Getter @Setter private String dataId;
        @SerializedName("Platform") @Getter @Setter private Optional<Platform> platform;
        @SerializedName("Latitude") @Getter @Setter private Optional<Double> wgsLatitude;
        @SerializedName("Longitude") @Getter @Setter private Optional<Double> wgsLongitude;
        @SerializedName("MapPdfId") @Getter @Setter private Optional<String> mapPdfId;
    }

    @AllArgsConstructor
    public class MapData {
        @Getter @Setter private String mode;
        // TODO: WGSCoordinate Handling
    }

    @Override
    public int hashCode() {
        return routeId.hashCode();
    }

    public static void find(
            String originId,
            String destinationId,
            Date time,
            Boolean dateIsArrival,
            Boolean allowShortTermChanges,
            Response.Listener<Result<RoutesResponse>> listener
    ) {
        Map<String, String> data = new HashMap<>();
        data.put("origin", originId);
        data.put("destination", destinationId);
        data.put("time", ISO8601.fromDate(time));
        data.put("isarrivaltime", dateIsArrival.toString());
        data.put("shorttermchanges", allowShortTermChanges.toString());
        data.put("mobilitySettings", "None");
        data.put("includeAlternativeStops", "true");
        Map<String, String> standardSettings = new HashMap<>();
        standardSettings.put("maxChanges", "Unlimited");
        standardSettings.put("walkingSpeed", "Normal");
        standardSettings.put("footpathToStop", "5");
        List<String> modeIdentifiers = Mode.getAll().stream().map(Mode::getRawValue).collect(Collectors.toList());
        standardSettings.put("mot", new Gson().toJson(modeIdentifiers));
        data.put("standardSettings", new Gson().toJson(standardSettings));
        Connection.post(Endpoint.route, data, listener);
    }

    public static void find(
            String originId,
            String destinationId,
            Response.Listener<Result<RoutesResponse>> listener
    ) {
        find(
                originId,
                destinationId,
                new Date(),
                false,
                true,
                listener
        );
    }

    public static void findByName(
            String origin,
            String destination,
            Date time,
            Boolean dateIsArrival,
            Boolean allowShortTermChanges,
            Response.Listener<Result<RoutesResponse>> listener
    ) {

        Stop.find(origin,
            originResponse -> {
                if (!originResponse.getResponse().isPresent()) {
                    listener.onResponse(new Result<>(Optional.empty(), originResponse.getError()));
                    return;
                }
                FindResponse originFindResponse = originResponse.getResponse().get();
                if (originFindResponse.getStops().size() == 0) {
                    listener.onResponse(new Result<>(Optional.empty(), Optional.of(DVBError.response)));
                    return;
                }
                String originId = originFindResponse.getStops().get(0).getId();

                Stop.find(destination,
                    destinationResponse -> {
                        if (!destinationResponse.getResponse().isPresent()) {
                            listener.onResponse(new Result<>(Optional.empty(), destinationResponse.getError()));
                            return;
                        }
                        FindResponse destinationFindResponse = destinationResponse.getResponse().get();
                        if (destinationFindResponse.getStops().size() == 0) {
                            listener.onResponse(new Result<>(Optional.empty(), Optional.of(DVBError.response)));
                            return;
                        }
                        String destinationId = destinationFindResponse.getStops().get(0).getId();

                        find(
                                originId,
                                destinationId,
                                time,
                                dateIsArrival,
                                allowShortTermChanges,
                                listener
                        );
                    }
                );
            }
        );
    }

    public static void findByName(
            String origin,
            String destination,
            Response.Listener<Result<RoutesResponse>> listener
    ) {
        findByName(
                origin,
                destination,
                new Date(),
                false,
                true,
                listener
        );
    }

}
