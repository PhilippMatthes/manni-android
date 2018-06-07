package philippmatthes.com.manni.vvo.Models;

public class RoutesResponse {
    private Route[] routes;
    private String sessionId;

    public RoutesResponse(Route[] routes, String sessionId) {
        this.routes = routes;
        this.sessionId = sessionId;
    }

    public Route[] getRoutes() {
        return routes;
    }

    public String getSessionId() {
        return sessionId;
    }
}
