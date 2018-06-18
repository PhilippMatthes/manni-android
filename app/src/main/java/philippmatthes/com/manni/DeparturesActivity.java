package philippmatthes.com.manni;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import philippmatthes.com.manni.jVVO.Models.Departure;
import philippmatthes.com.manni.jVVO.Models.Route;
import philippmatthes.com.manni.jVVO.Models.Stop;

public class DeparturesActivity extends SpinnerActivity {

    String stopName;

    ListView departureListView;
    DepartureAdapter adapter;
    ArrayList<Departure> departures;
    SwipeRefreshLayout layout;

    private void setUp() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_departures);

        departureListView = (ListView) findViewById(R.id.stop_list_view);

        departures = new ArrayList<>();

        adapter = new DepartureAdapter(this, departures);

        departureListView.setAdapter(adapter);

        layout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        layout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadDepartures(stopName);
                    }
                }
        );

        setTitle(stopName);
    }

    private void loadDepartures(String stopName) {
        startAnimatingSpinner();

        RequestQueue queue = Volley.newRequestQueue(this);

        Departure.monitorByName(stopName, queue, response -> {
            if (response.getResponse().isPresent()) {
                List<Departure> responseDepartures = response.getResponse().get().getDepartures();
                if (responseDepartures != null) {
                    departures = new ArrayList<>();
                    departures.addAll(responseDepartures);
                    adapter.clear();
                    adapter.addAll(departures);
                    adapter.notifyDataSetChanged();
                    stopAnimatingSpinner();
                }
            } else {
                System.out.println(response.getError().get().getDescription());
            }

            if (layout.isRefreshing()) {
                layout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stopName = getIntent().getStringExtra("stopName");
        setUp();
        loadDepartures(stopName);
    }


}
