package philippmatthes.com.manni;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import philippmatthes.com.manni.vvo.Models.Departure;
import philippmatthes.com.manni.vvo.Models.POI;
import philippmatthes.com.manni.vvo.Models.Route;
import philippmatthes.com.manni.vvo.Models.Stop;
import philippmatthes.com.manni.vvo.WGSCoordinate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);

        Departure.monitorByName("Tharandter Straße", queue, response -> {
            System.out.println(response.getResponse().get().getDepartures());
        });

        /*
        Stop.find("Tharandter Straße", response -> System.out.println(response.getResponse().get().getStops()), queue);

        Route.find("33000155", "33000028", response -> {
            for (Route route : response.getResponse().get().getRoutes()) {
                System.out.println(route.getModeChain().stream().map(mode -> mode.getName()).collect(Collectors.toList()));
            }
        }, queue);



        Route.findByName("Clara-Viebig-Straße", "Tharandter Straße", response -> {
            for (Route route : response.getResponse().get().getRoutes()) {
                System.out.println(route.getModeChain().stream().map(mode -> mode.getName()).collect(Collectors.toList()));
            }
        }, queue);
        */
    }


}

//["isarrival": false, "limit": 0, "stopid": "33000155", "time": "2018-06-08T18:53:16Z", "shorttermchanges": true, "mot": ["tram", "citybus", "bus", "metropolitan", "train", "lift", "ferry", "alita"]]
//