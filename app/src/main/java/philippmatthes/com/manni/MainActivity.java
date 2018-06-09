package philippmatthes.com.manni;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.stream.Collectors;

import philippmatthes.com.manni.jVVO.Models.Route;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);

        /*
        Departure.monitorByName("Tharandter Straße", queue, response -> {
            if (response.getResponse().isPresent()) {
                System.out.println(response.getResponse().get().getDepartures());
            } else {
                System.out.println(response.getError().get().getDescription());
            }
        });

        RouteChange.get(queue, response -> {
            System.out.println(response.getResponse().get().getChanges());
        });


        Stop.find("Tharandter Straße", queue, response -> {
            if (response.getResponse().isPresent()) {
                System.out.println(response.getResponse().get().getStops());
            } else {
                System.out.println(response.getError().get().getDescription());
            }
        });

        Route.find("33000155", "33000028", queue, response -> {
            if (response.getResponse().isPresent()) {
                for (Route route : response.getResponse().get().getRoutes()) {
                    System.out.println(route.getModeChain().stream().map(mode -> mode.getName()).collect(Collectors.toList()));
                }
            } else {
                System.out.println(response.getError().get().getDescription());
            }
        });

        Route.findByName("Tharandter Straße", "Hauptbahnhof", queue, response -> {
            if (response.getResponse().isPresent()) {
                for (Route route : response.getResponse().get().getRoutes()) {
                    System.out.println(route.getModeChain().stream().map(mode -> mode.getName()).collect(Collectors.toList()));
                }
            } else {
                System.out.println(response.getError().get().getDescription());
            }
        });

        RouteChange.get(queue, response -> {
            if (response.getResponse().isPresent()) {
                for (RouteChange change : response.getResponse().get().getChanges()) {
                    System.out.println(change.getTitle());
                }
            } else {
                System.out.println(response.getError().get().getDescription());
            }
        });

        Line.getByName("Hauptbahnhof", queue, response -> {
            if (response.getResponse().isPresent()) {
                System.out.println(response.getResponse().get().getLines());
            } else {
                System.out.println(response.getError().get().getDescription());
            }
        });
        */

        Route.findByName("Tharandter Straße", "Hauptbahnhof", queue, response -> {
            if (response.getResponse().isPresent()) {
                for (Route route : response.getResponse().get().getRoutes()) {
                    System.out.println(route.getModeChain().stream().map(mode -> mode.getName()).collect(Collectors.toList()));
                }
            } else {
                System.out.println(response.getError().get().getDescription());
            }
        });
    }


}

//["isarrival": false, "limit": 0, "stopid": "33000155", "time": "2018-06-08T18:53:16Z", "shorttermchanges": true, "mot": ["tram", "citybus", "bus", "metropolitan", "train", "lift", "ferry", "alita"]]
//