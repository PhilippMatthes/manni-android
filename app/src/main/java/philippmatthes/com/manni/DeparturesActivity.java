package philippmatthes.com.manni;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class DeparturesActivity extends AppCompatActivity {

    String stopName;

    ListView departureListView;
    ArrayAdapter<String> adapter;
    List<String> departureLabels;

    private void setUp() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_departures);

        departureListView = findViewById(R.id.stop_list_view);

        departureLabels = new ArrayList<>();

        adapter = new ArrayAdapter<String>(
                DeparturesActivity.this,
                android.R.layout.simple_list_item_1,
                departureLabels
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);

                TextView tv1 = view.findViewById(android.R.id.text1);
                tv1.setTextColor(Color.WHITE);

                view.setBackgroundColor(Colors.getColor(tv1.getText().length()));
                return view;
            }
        };

        departureListView.setAdapter(adapter);
    }

    private void loadDepartures(String stopName) {
        RequestQueue queue = Volley.newRequestQueue(this);

        Departure.monitorByName(stopName, queue, response -> {
            if (response.getResponse().isPresent()) {
                List<Departure> departures = response.getResponse().get().getDepartures();
                departureLabels = departures.stream().map(d -> {
                    return d.getLine() + " " + d.getDirection() + " in " + d.getETA() + " min";
                }).collect(Collectors.toList());
                adapter.clear();
                adapter.addAll(departureLabels);
                adapter.notifyDataSetChanged();
            } else {
                System.out.println(response.getError().get().getDescription());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        stopName = getIntent().getStringExtra("stopName");
        loadDepartures(stopName);

    }


}
