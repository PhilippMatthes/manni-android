package philippmatthes.com.manni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import philippmatthes.com.manni.jVVO.Models.Route;

public class RoutesActivity extends SpinnerActivity implements RouteRecycleViewAdapter.ItemClickListener, ExpandableListView.OnGroupExpandListener {

    ExpandableRoutesAdapter adapter;
    ExpandableListView expandableListView;
    List<Route> routes;

    SwipeRefreshLayout layout;

    String from;
    String to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");

        configureContentView();
        configureBar();
        configureExpandableListView();

        loadRoute(from, to);
    }

    private void configureContentView() {

        layout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        layout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadRoute(from, to);
                    }
                }
        );
    }

    private void configureBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(from + " - " + to);
    }

    private void configureExpandableListView() {
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_routes_list_view);

        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            parent.expandGroup(groupPosition);
            return false;
        });

        routes = new ArrayList<>();

        adapter = new ExpandableRoutesAdapter(this, routes, this);

        expandableListView.setAdapter(adapter);
    }

    private void loadRoute(String from, String to) {
        startAnimatingSpinner();

        RequestQueue queue = Volley.newRequestQueue(this);

        Route.findByName(from, to, queue, response -> {
            if (response.getResponse().isPresent()) {
                routes = response.getResponse().get().getRoutes();
                adapter.clear();
                adapter.addAll(routes);
                stopAnimatingSpinner();
            } else {
                System.out.println(response.getError().get().getDescription());
            }

            if (layout.isRefreshing()) {
                layout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        System.out.println("Clicked");
    }

    @Override
    public void onGroupExpand(int i) {
        System.out.println("Expand");
    }
}
