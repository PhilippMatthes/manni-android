package main.Models;

import main.Tools.ISO8601;

import java.util.*;
import java.util.stream.Collectors;

public class Route implements Comparable {
    private Optional<Integer> priceLevel;
    private Optional<String> price;
    private Integer duration;
    private Integer interchanges;
    private ModeElement[] modeChain;
    private Optional<Integer> fareZoneOrigin;
    private Optional<Integer> fareZoneDestination;
    private String mapPdfId;
    private Integer routeId;
    private RoutePartial[] partialRoutes;
    private MapData[] mapData;

    public Route(
        Optional<Integer> priceLevel,
        Optional<String> price,
        Integer duration,
        Integer interchanges,
        ModeElement[] modeChain,
        Optional<Integer> fareZoneOrigin,
        Optional<Integer> fareZoneDestination,
        String mapPdfId, Integer routeId,
        RoutePartial[] partialRoutes,
        MapData[] mapData
    ) {
        this.priceLevel = priceLevel;
        this.price = price;
        this.duration = duration;
        this.interchanges = interchanges;
        this.modeChain = modeChain;
        this.fareZoneOrigin = fareZoneOrigin;
        this.fareZoneDestination = fareZoneDestination;
        this.mapPdfId = mapPdfId;
        this.routeId = routeId;
        this.partialRoutes = partialRoutes;
        this.mapData = mapData;
    }

    public Optional<Integer> getPriceLevel() {
        return priceLevel;
    }

    public Optional<String> getPrice() {
        return price;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getInterchanges() {
        return interchanges;
    }

    public ModeElement[] getModeChain() {
        return modeChain;
    }

    public Optional<Integer> getFareZoneOrigin() {
        return fareZoneOrigin;
    }

    public Optional<Integer> getFareZoneDestination() {
        return fareZoneDestination;
    }

    public String getMapPdfId() {
        return mapPdfId;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public RoutePartial[] getPartialRoutes() {
        return partialRoutes;
    }

    public MapData[] getMapData() {
        return mapData;
    }

    public class ModeElement {
        private Optional<String> name;
        private Optional<Mode> mode;
        private Optional<String> direction;
        private Optional<String[]> changes;
        private Optional<Diva> diva;

        public ModeElement(
            Optional<String> name,
            Optional<Mode> mode,
            Optional<String> direction,
            Optional<String[]> changes,
            Optional<Diva> diva
        ) {
            this.name = name;
            this.mode = mode;
            this.direction = direction;
            this.changes = changes;
            this.diva = diva;
        }

        public Optional<String> getName() {
            return name;
        }

        public Optional<Mode> getMode() {
            return mode;
        }

        public Optional<String> getDirection() {
            return direction;
        }

        public Optional<String[]> getChanges() {
            return changes;
        }

        public Optional<Diva> getDiva() {
            return diva;
        }
    }

    public class RoutePartial {
        private Optional<Integer> partialRouteId;
        private Optional<Integer> duration;
        private ModeElement mode;
        private Integer mapDataIndex;
        private String shift;
        private Optional<RouteStop[]> regularStops;

        public RoutePartial(
            Optional<Integer> partialRouteId,
            Optional<Integer> duration,
            ModeElement mode,
            Integer mapDataIndex,
            String shift,
            Optional<RouteStop[]> regularStops
        ) {
            this.partialRouteId = partialRouteId;
            this.duration = duration;
            this.mode = mode;
            this.mapDataIndex = mapDataIndex;
            this.shift = shift;
            this.regularStops = regularStops;
        }

        public Optional<Integer> getPartialRouteId() {
            return partialRouteId;
        }

        public Optional<Integer> getDuration() {
            return duration;
        }

        public ModeElement getMode() {
            return mode;
        }

        public Integer getMapDataIndex() {
            return mapDataIndex;
        }

        public String getShift() {
            return shift;
        }

        public Optional<RouteStop[]> getRegularStops() {
            return regularStops;
        }
    }

    public class RouteStop {
        private Date arrivalTime;
        private Date departureTime;
        private String place;
        private String name;
        private String type;
        private String dataId;
        private Optional<Platform> platform;
        private Optional<WGSCoordinate> coordinate;
        private Optional<String> mapPdfId;

        public RouteStop(
            Date arrivalTime,
            Date departureTime,
            String place,
            String name,
            String type,
            String dataId,
            Optional<Platform> platform,
            Optional<WGSCoordinate> coordinate,
            Optional<String> mapPdfId
        ) {
            this.arrivalTime = arrivalTime;
            this.departureTime = departureTime;
            this.place = place;
            this.name = name;
            this.type = type;
            this.dataId = dataId;
            this.platform = platform;
            this.coordinate = coordinate;
            this.mapPdfId = mapPdfId;
        }

        public Date getArrivalTime() {
            return arrivalTime;
        }

        public Date getDepartureTime() {
            return departureTime;
        }

        public String getPlace() {
            return place;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getDataId() {
            return dataId;
        }

        public Optional<Platform> getPlatform() {
            return platform;
        }

        public Optional<WGSCoordinate> getCoordinate() {
            return coordinate;
        }

        public Optional<String> getMapPdfId() {
            return mapPdfId;
        }
    }

    public class MapData {
        private String mode;
        private WGSCoordinate[] points;

        public MapData(String mode, WGSCoordinate[] points) {
            this.mode = mode;
            this.points = points;
        }

        public String getMode() {
            return mode;
        }

        public WGSCoordinate[] getPoints() {
            return points;
        }
    }

    @Override
    public int hashCode() {
        return routeId.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Route)) {
            return 0;
        } else {
            return ((Route) o).getRouteId().compareTo(routeId);
        }
    }

    public static Result<RoutesResponse> find(
            String originId,
            String destinationId,
            Date time,
            Boolean dateIsArrival,
            Boolean allowShortTermChanges
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
        Connection.post(Endpoint.route, data);
    }

    public static Result<RoutesResponse> find(
            String originId,
            String destinationId
    ) {
        return find(
                originId,
                destinationId,
                new Date(),
                false,
                true
        );
    }

    public static Result<RoutesResponse> findByName(
            String origin,
            String destination,
            Date time,
            Boolean dateIsArrival,
            Boolean allowShortTermChanges
    ) {

        Result<FindResponse> originResult = Stop.find(origin);
        if (!originResult.isSuccessful()) {
            return new Result<RoutesResponse>(originResult.getError());
        }
        FindResponse originResponse = originResult.getResponse();
        if (originResponse.getStops().length == 0) {
            return new Result<RoutesResponse>(DVBError.getResponse());
        }
        String originId = originResponse.getStops()[0].getId();

        Result<FindResponse> destinationResult = Stop.find(origin);
        if (!destinationResult.isSuccessful()) {
            return new Result<RoutesResponse>(destinationResult.getError());
        }
        FindResponse destinationResponse = destinationResult.getResponse();
        if (destinationResponse.getStops().length == 0) {
            return new Result<RoutesResponse>(DVBError.getResponse());
        }
        String destinationId = destinationResponse.getStops()[0].getId();
        return find(
                originId,
                destinationId,
                time,
                dateIsArrival,
                allowShortTermChanges
        );
    }

    public static Result<RoutesResponse> findByName(
            String origin,
            String destination
    ) {
        return findByName(
                origin,
                destination,
                new Date(),
                false,
                true
        );
    }

}
