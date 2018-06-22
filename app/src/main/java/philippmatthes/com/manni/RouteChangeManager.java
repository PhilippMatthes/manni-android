package philippmatthes.com.manni;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import philippmatthes.com.manni.jVVO.Models.Departure;
import philippmatthes.com.manni.jVVO.Models.RouteChange;
import philippmatthes.com.manni.jVVO.Models.RouteChangeResponse;

public class RouteChangeManager {

    @Getter @Setter private static RouteChangeManager shared = new RouteChangeManager();
    @Getter @Setter private List<RouteChange> changes;
    @Getter @Setter private RequestQueue queue;

    private RouteChangeManager() {
        changes = new ArrayList<>();
    }

    public void update(RequestQueue queue) {
        RouteChange.get(queue, response -> {
            if (response.getResponse().isPresent()) {
                RouteChangeResponse result = response.getResponse().get();
                changes = result.getChanges();
            } else {
                System.out.println(response.getError().get().getDescription());
            }
        });
    }

    public String changesHTML(List<String> ids) {
        if (ids == null) return "";
        return changes.stream()
                .filter(routeChange -> ids.contains(routeChange.getId()))
                .map(routeChange -> routeChange.getHtmlDescription())
                .collect(Collectors.joining());
    }

    public String changes(List<String> ids) {
        if (ids == null) return "";
        return changes.stream()
                .filter(routeChange -> ids.contains(routeChange.getId()))
                .map(routeChange -> routeChange.getTitle())
                .collect(Collectors.joining(", "));
    }

}
