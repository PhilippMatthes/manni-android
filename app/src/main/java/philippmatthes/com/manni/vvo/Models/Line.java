package philippmatthes.com.manni.vvo.Models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Line implements Comparable<Line>  {
    private String name;
    private Mode mode;
    private Optional<List<String>> changes;
    private List<Direction> directions;
    private Diva diva;

    public Line(
        String name,
        Mode mode,
        Optional<List<String>> changes,
        List<Direction> directions,
        Diva diva
    ) {
        this.name = name;
        this.mode = mode;
        this.changes = changes;
        this.directions = directions;
        this.diva = diva;
    }

    public String getName() {
        return name;
    }

    public Mode getMode() {
        return mode;
    }

    public Optional<List<String>> getChanges() {
        return changes;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public Diva getDiva() {
        return diva;
    }

    @Override
    public int compareTo(Line o) {
        return o.getName().compareTo(name);
    }

    public class Direction {
        private String name;
        private List<TimeTable> timetables;

        public Direction(String name, List<TimeTable> timetables) {
            this.name = name;
            this.timetables = timetables;
        }

        public String getName() {
            return name;
        }

        public List<TimeTable> getTimetables() {
            return timetables;
        }
    }

    public class TimeTable {
        public String id;
        public String name;

        public TimeTable(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }

    public static Result<LinesResponse> get(
        String forStopId
    ) {
        Map<String, Object> data = new HashMap<>();
        data.put("stopid", forStopId);
        return Connection.post(Endpoint.lines, data);
    }

    public static Result<LinesResponse> get(
        String forStopName
    ) {
        // TODO
    }

    @Override
    public int hashCode() {
        return name.hashCode() + mode.hashCode();
    }

}
