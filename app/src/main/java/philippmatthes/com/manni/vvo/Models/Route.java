package philippmatthes.com.manni.vvo.Models;

import android.support.annotation.NonNull;


import com.android.volley.RequestQueue;
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
    @SerializedName("PriceLevel") @Getter @Setter private Integer priceLevel;
    @SerializedName("Price") @Getter @Setter private String price;
    @SerializedName("Duration") @Getter @Setter private Integer duration;
    @SerializedName("Interchanges") @Getter @Setter private Integer interchanges;
    @SerializedName("MotChain") @Getter @Setter private List<ModeElement> modeChain;
    @SerializedName("FareZoneOrigin") @Getter @Setter private Integer fareZoneOrigin;
    @SerializedName("FareZoneDestination") @Getter @Setter private Integer fareZoneDestination;
    @SerializedName("MapPdfId") @Getter @Setter private String mapPdfId;
    @SerializedName("RouteId") @Getter @Setter private Integer routeId;
    @SerializedName("PartialRoutes") @Getter @Setter private List<RoutePartial> partialRoutes;
    @SerializedName("MapData") @Getter @Setter private List<String> mapData;

    @Override
    public int compareTo(@NonNull Route o) {
        return o.getRouteId().compareTo(routeId);
    }

    @AllArgsConstructor
    public class ModeElement {
        @SerializedName("Name") @Getter @Setter private String name;
        @SerializedName("Type") @Getter @Setter private Mode mode;
        @SerializedName("Direction") @Getter @Setter private String direction;
        @SerializedName("Changes") @Getter @Setter private List<String> changes;
        @SerializedName("Diva") @Getter @Setter private Diva diva;
    }

    @AllArgsConstructor
    public class RoutePartial {
        @SerializedName("PartialRouteId") @Getter @Setter private Integer partialRouteId;
        @SerializedName("Duration") @Getter @Setter private Integer duration;
        @SerializedName("Mot") @Getter @Setter private ModeElement mode;
        @SerializedName("MapDataIndex") @Getter @Setter private Integer mapDataIndex;
        @SerializedName("Shift") @Getter @Setter private String shift;
        @SerializedName("RegularStops") @Getter @Setter private List<RouteStop> regularStops;
    }

    @AllArgsConstructor
    public class RouteStop {
        @SerializedName("ArrivalTime") @Getter @Setter private String arrivalTime;
        @SerializedName("DepartureTime") @Getter @Setter private String departureTime;
        @SerializedName("Place") @Getter @Setter private String place;
        @SerializedName("Name") @Getter @Setter private String name;
        @SerializedName("Type") @Getter @Setter private String type;
        @SerializedName("DataId") @Getter @Setter private String dataId;
        @SerializedName("Platform") @Getter @Setter private Platform platform;
        @SerializedName("Latitude") @Getter @Setter private Double wgsLatitude;
        @SerializedName("Longitude") @Getter @Setter private Double wgsLongitude;
        @SerializedName("MapPdfId") @Getter @Setter private String mapPdfId;
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
            Response.Listener<Result<RoutesResponse>> listener,
            RequestQueue queue
    ) {
        Map<String, Object> data = new HashMap<>();
        data.put("origin", originId);
        data.put("destination", destinationId);
        data.put("time", ISO8601.fromDate(time));
        data.put("isarrivaltime", dateIsArrival);
        data.put("shorttermchanges", allowShortTermChanges);
        data.put("mobilitySettings", "None");
        data.put("includeAlternativeStops", true);
        Map<String, Object> standardSettings = new HashMap<>();
        standardSettings.put("maxChanges", "Unlimited");
        standardSettings.put("walkingSpeed", "Normal");
        standardSettings.put("footpathToStop", 5);
        List<String> modeIdentifiers = Mode.getAll().stream().map(Mode::getRawValue).collect(Collectors.toList());
        standardSettings.put("mot", modeIdentifiers);
        data.put("standardSettings", standardSettings);
        Connection.post(Endpoint.route, data, listener, RoutesResponse.class, queue);
    }

    public static void find(
            String originId,
            String destinationId,
            Response.Listener<Result<RoutesResponse>> listener,
            RequestQueue queue
    ) {
        find(
                originId,
                destinationId,
                new Date(),
                false,
                true,
                listener,
                queue
        );
    }

    public static void findByName(
            String origin,
            String destination,
            Date time,
            Boolean dateIsArrival,
            Boolean allowShortTermChanges,
            Response.Listener<Result<RoutesResponse>> listener,
            RequestQueue queue
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
                                listener,
                                queue
                        );
                    }, queue
                );
            }, queue
        );
    }

    public static void findByName(
            String origin,
            String destination,
            Response.Listener<Result<RoutesResponse>> listener,
            RequestQueue queue
    ) {
        findByName(
                origin,
                destination,
                new Date(),
                false,
                true,
                listener,
                queue
        );
    }

}
