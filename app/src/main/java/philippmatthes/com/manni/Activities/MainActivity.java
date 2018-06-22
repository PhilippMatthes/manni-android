package philippmatthes.com.manni.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import philippmatthes.com.manni.Colors;
import philippmatthes.com.manni.R;
import philippmatthes.com.manni.RouteChangeManager;
import philippmatthes.com.manni.jVVO.Models.Stop;

public class MainActivity extends AppCompatActivity {

    private ListView stopListView;
    private ArrayAdapter<String> adapter;
    private List<String> stopNames;
    private RelativeLayout destinationSearchLayout;

    private SearchView startSearchView;
    private SearchView destinationSearchView;

    boolean destinationSearchViewIsOpen = true;

    private void moveToDeparturesOrRoutes() {
        if (destinationSearchViewIsOpen) {
            moveToRouteActivity(startSearchView.getQuery().toString(), destinationSearchView.getQuery().toString());
        } else {
            moveToDepartureActivity(startSearchView.getQuery().toString());
        }
    }

    private void moveToDepartureActivity(String stopName) {
        Intent intent = new Intent(getApplicationContext(), DeparturesActivity.class);
        IntentHandler.setStopName(stopName);
        startActivity(intent);
    }

    private void moveToRouteActivity(String from, String to) {
        Intent intent = new Intent(getApplicationContext(), RoutesActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("to", to);
        startActivity(intent);
    }

    private void configureContentView() {
        setContentView(R.layout.activity_main);
    }

    private void configureStopListView() {
        stopListView = (ListView) findViewById(R.id.stop_list_view);

        stopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                replaceActiveSearch(stopNames.get(i));
                if (destinationSearchViewIsOpen) switchSelectedSearchView();
            }
        });

        stopNames = new ArrayList<>();

        adapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                stopNames
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                view.setBackgroundColor(Colors.getColor(tv.getText().length()));
                return view;
            }
        };

        stopListView.setAdapter(adapter);
    }

    private void configureStartSearch() {
        startSearchView = (SearchView) findViewById(R.id.search_view_start);

        startSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                moveToDeparturesOrRoutes();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadStops(newText);
                return false;
            }
        });

        startSearchView.setIconified(false);

        RelativeLayout startSearchLayout = (RelativeLayout) findViewById(R.id.search_layout_start);
        startSearchLayout.bringToFront();
    }

    private void configureDestinationSearch() {
        destinationSearchLayout = (RelativeLayout)
                findViewById(R.id.search_layout_destination);

        destinationSearchView = (SearchView) findViewById(R.id.search_view_destination);

        destinationSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                moveToDepartureActivity(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadStops(newText);
                return false;
            }
        });

        ImageButton toggleDestinationSearchViewButton = (ImageButton)
                findViewById(R.id.toggle_destination_search_view_button);

        toggleDestinationSearchViewButton.setOnClickListener(view -> {
            setDestinationSearchViewOpen(!destinationSearchViewIsOpen);
        });

        ImageButton shuffleButton = (ImageButton) findViewById(R.id.shuffle_button);

        shuffleButton.setOnClickListener(view -> {
            shuffleStartAndDestination();
        });
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RequestQueue queue = Volley.newRequestQueue(this);

        RouteChangeManager.getShared().update(queue);

        configureContentView();
        configureStopListView();
        configureStartSearch();
        configureDestinationSearch();
        hideActionBar();
    }

    private void setDestinationSearchViewOpen(boolean open) {
        destinationSearchViewIsOpen = open;
        destinationSearchLayout.animate().translationY(open ? 0 : -startSearchView.getHeight());
        stopListView.animate().translationY(open ? 0 : -startSearchView.getHeight());
        if (!open) startSearchView.setIconified(false);
    }

    private void switchSelectedSearchView() {
        if (currentlyEditingSearchView() == startSearchView) {
            destinationSearchView.setIconified(false);
        } else {
            startSearchView.setIconified(false);
        }
    }

    private void loadStops(String query) {
        RequestQueue queue = Volley.newRequestQueue(this);

        Stop.find(query, queue, response -> {
            if (response.getResponse().isPresent()) {
                List<Stop> stops = response.getResponse().get().getStops();
                if (stops != null) {
                    stopNames = stops.stream()
                            .map(stop -> {
                                String title = stop.getName();
                                if (stop.getRegion() != null)
                                    title += " (" + stop.getRegion() + ")";
                                return title;
                            })
                            .collect(Collectors.toList());
                    adapter.clear();
                    adapter.addAll(stopNames);
                    adapter.notifyDataSetChanged();
                }
            } else {
                System.out.println(response.getError().get().getDescription());
            }
        });
    }

    private void shuffleStartAndDestination() {
        CharSequence currentStartSearchViewInput = startSearchView.getQuery();
        startSearchView.setQuery(destinationSearchView.getQuery(), false);
        destinationSearchView.setQuery(currentStartSearchViewInput, false);
    }

    private SearchView currentlyEditingSearchView() {
        if (!destinationSearchViewIsOpen) return startSearchView;
        return destinationSearchView.hasFocus() ? destinationSearchView : startSearchView;
    }

    private void replaceActiveSearch(CharSequence replacement) {
        SearchView searchView = currentlyEditingSearchView();
        searchView.setQuery(replacement, false);
    }

}
