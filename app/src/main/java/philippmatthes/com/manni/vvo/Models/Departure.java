package philippmatthes.com.manni.vvo.Models;

import main.Tools.ISO8601;
import main.Tools.Time;

import java.util.*;
import java.util.stream.Collectors;

public class Departure implements Comparable<Departure> {
    private String id;
    private String line;
    private String direction;
    private Optional<Platform> platform;
    private Mode mode;
    private Optional<Date> realTime;
    private Date scheduledTime;
    private Optional<State> state;
    private Optional<List<String>> routeChanges;
    private Optional<Diva> diva;

    public Departure(
        String id,
        String line,
        String direction,
        Optional<Platform> platform,
        Mode mode,
        Optional<Date> realTime,
        Date scheduledTime,
        Optional<State> state,
        Optional<List<String>> routeChanges,
        Optional<Diva> diva
    ) {
        this.id = id;
        this.line = line;
        this.direction = direction;
        this.platform = platform;
        this.mode = mode;
        this.realTime = realTime;
        this.scheduledTime = scheduledTime;
        this.state = state;
        this.routeChanges = routeChanges;
        this.diva = diva;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public Mode getMode() {
        return mode;
    }

    public Optional<Date> getRealTime() {
        return realTime;
    }

    public Optional<Diva> getDiva() {
        return diva;
    }

    public Optional<List<String>> getRouteChanges() {
        return routeChanges;
    }

    public Optional<Platform> getPlatform() {
        return platform;
    }

    public Optional<State> getState() {
        return state;
    }

    public String getDirection() {
        return direction;
    }

    public String getId() {
        return id;
    }

    public String getLine() {
        return line;
    }

    public Integer getETA() {
        if(!realTime.isPresent()) {
            return getScheduledETA();
        } else {
            return Time.minutesUntil(realTime.get());
        }
    }

    public Integer getScheduledETA() {
        return Time.minutesUntil(scheduledTime);
    }

    public String fancyETA() {
        if (!realTime.isPresent()) {
            return getScheduledETA().toString();
        }

        Date time = realTime.get();
        Integer diff = Time.minutesUntil(time);
        if (diff < 0) {
            return getScheduledETA().toString()+diff.toString();
        } else if (diff == 0) {
            return getScheduledETA().toString();
        } else {
            return getScheduledETA().toString()+"+"+diff.toString();
        }
    }

    @Override
    public int compareTo(Departure o) {
        return o.getId().compareTo(id);
    }

    public enum State {
        onTime ("InTime"),
        delayed ("Delayed"),
        unknown ("Unknown");

        private final String rawValue;

        State(String s) {
            rawValue = s;
        }

        public String getRawValue() {
            return rawValue;
        }

        public boolean equalsState(String rawValue) {
            return this.rawValue.equals(rawValue);
        }

        public String toString() {
            return rawValue;
        }

        public State[] getAll() {
            return new State[]{State.onTime, State.delayed};
        }
    }

    public enum DateType {
        arrival,
        departure;

        public Boolean requestVal() {
            return this == arrival;
        }
    }

    public static Result<MonitorResponse> monitor(
         String stopWithId,
         Date date,
         DateType dateType,
         List<Mode> allowedModes,
         boolean allowShorttermChanges
    ) {
        Map<String, Object> data = new HashMap<>();
        data.put("stopid", stopWithId);
        data.put("time", ISO8601.fromDate(date));
        data.put("isarrival", dateType.requestVal());
        data.put("limit", 0);
        data.put("shorttermchanges", allowShorttermChanges);
        data.put("mot", allowedModes.stream().map(mode -> mode.getRawValue()).collect(Collectors.toList()));
        return Connection.post(Endpoint.departureMonitor, data);
    }

    public static Result<MonitorResponse> monitor(
        String stopWithId
    ) {
        return Departure.monitor(
            stopWithId,
            new Date(),
            DateType.arrival,
            Mode.allRequest,
            true
        );
    }

    public static Result<MonitorResponse> monitorByName(
        String stopWithName,
        Date date,
        DateType dateType,
        List<Mode> allowedModes,
        boolean allowShorttermChanges
    ) {
        // TODO
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
