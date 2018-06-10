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

import philippmatthes.com.manni.jVVO.Models.Stop;

public class MainActivity extends AppCompatActivity {

    ListView stopListView;
    ArrayAdapter<String> adapter;
    List<String> stopNames;

    private void setUp() {
        setContentView(R.layout.activity_main);

        stopListView = findViewById(R.id.stop_list_view);

        stopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DeparturesActivity.class);
                intent.putExtra("stopName", stopNames.get(i));
                startActivity(intent);
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
                TextView tv = view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                view.setBackgroundColor(Colors.getColor(tv.getText().length()));
                return view;
            }
        };

        stopListView.setAdapter(adapter);
    }

    private void loadStops(String query) {
        RequestQueue queue = Volley.newRequestQueue(this);

        Stop.find(query, queue, response -> {
            if (response.getResponse().isPresent()) {
                List<Stop> stops = response.getResponse().get().getStops();
                stopNames = stops.stream()
                        .map(stop -> {
                            String title = stop.getName();
                            if (stop.getRegion() != null) title += " (" + stop.getRegion() + ")";
                            return title;
                        })
                        .collect(Collectors.toList());
                adapter.clear();
                adapter.addAll(stopNames);
                adapter.notifyDataSetChanged();
            } else {
                System.out.println(response.getError().get().getDescription());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO: Jump to activity
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadStops(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
    }


}
