package philippmatthes.com.manni;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import philippmatthes.com.manni.jVVO.Models.Route;
import philippmatthes.com.manni.jVVO.Tools.Time;

public class ExpandableRoutesAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Route> routes;

    private RouteRecycleViewAdapter.ItemClickListener listener;

    private RouteRecycleViewAdapter adapter;

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    public void clear() {
        routes.clear();
        notifyDataSetChanged();
        notifyDataSetInvalidated();
    }

    public void addAll(List<Route> routes) {
        if (routes == null) return;
        this.routes.addAll(routes);
        notifyDataSetChanged();
        notifyDataSetInvalidated();
    }

    public ExpandableRoutesAdapter(
            Context context,
            List<Route> routes,
            RouteRecycleViewAdapter.ItemClickListener clickListener
    ) {
        this.context = context;
        this.routes = routes;
        this.listener = clickListener;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return routes.get(groupPosition).getPartialRoutes().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Route.RoutePartial child = (Route.RoutePartial) getChild(groupPosition, childPosition);
        boolean hasRegularStops = child.getRegularStops() == null ? false : child.getRegularStops().size() > 0;

        LayoutInflater inflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (!hasRegularStops) {
            convertView = inflater.inflate(R.layout.tight_cell, null);
            TextView textView = (TextView) convertView.findViewById(R.id.tight_cell_text_view);

            if (child.getMode().getName() == null && child.getDuration() == null) {
                textView.setText("Note the departure places and times.");
            } else if (child.getMode().getName() == null) {
                textView.setText("Site change may be necessary. (" + child.getDuration() + " min)");
            } else {
                textView.setText(child.getMode().getName() + " (" + child.getDuration() + " min)");
            }
            return convertView;
        }

        convertView = inflater.inflate(R.layout.route_cell, null);

        TextView startTextView = (TextView) convertView.findViewById(R.id.route_cell_start_text_view);
        TextView destinationTextView = (TextView) convertView.findViewById(R.id.route_cell_destination_text_view);
        TextView travelTextView = (TextView) convertView.findViewById(R.id.route_cell_travel_text_view);
        Button lineButton = (Button) convertView.findViewById(R.id.route_cell_travel_image_button);

        List<Route.RouteStop> routeStops = child.getRegularStops();
        Route.RouteStop startStop = routeStops.get(0);
        Route.RouteStop destinationStop = routeStops.get(routeStops.size() - 1);
        String modeDirection = child.getMode().getDirection() == null ? "" : child.getMode().getDirection();

        startTextView.setText(startStop.getName() + " - " + Time.formatDateNoDay(startStop.getDepartureTime()));
        destinationTextView.setText(destinationStop.getName() + " - " + Time.formatDateNoDay(destinationStop.getArrivalTime()));
        travelTextView.setText(modeDirection + " (" + child.getDuration() + " min)");
        lineButton.setText(child.getMode().getName());

        int color = Colors.color(child);
        convertView.setBackgroundColor(color);
        lineButton.setBackgroundColor(Colors.lighten(color, 0.2));

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return routes.get(groupPosition).getPartialRoutes().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return routes.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return routes.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Route route = (Route) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.route_header, null);
        }

        RecyclerView recyclerView = (RecyclerView) convertView
                .findViewById(R.id.route_header_lines_recycler_view);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandableListView expandableListView = (ExpandableListView) parent;
                if (!isExpanded) {
                    expandableListView.expandGroup(groupPosition);
                } else {
                    expandableListView.collapseGroup(groupPosition);
                }
            }
        });

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        adapter = new RouteRecycleViewAdapter(context, route.getPartialRoutes());
        adapter.setClickListener(listener);
        recyclerView.setAdapter(adapter);

        List<Route.RoutePartial> routePartials = route.getPartialRoutes();

        if (routePartials == null) return convertView;
        if (routePartials.size() < 1) return convertView;
        if (routePartials.get(0).getRegularStops() == null) return convertView;
        if (routePartials.get(routePartials.size() - 1).getRegularStops() == null) return convertView;
        if (routePartials.get(0).getRegularStops().size() < 1) return convertView;
        if (routePartials.get(routePartials.size() - 1).getRegularStops().size() < 1) return convertView;

        List<Route.RouteStop> startRouteStops = routePartials.get(0).getRegularStops();
        List<Route.RouteStop> destinationRouteStops = routePartials.get(routePartials.size() - 1).getRegularStops();

        Route.RouteStop startStop = startRouteStops.get(0);
        Route.RouteStop destinationStop = destinationRouteStops.get(destinationRouteStops.size() - 1);

        TextView textView = (TextView) convertView.findViewById(R.id.route_header_duration_text_view);
        textView.setText(Time.formatDateNoDay(startStop.getDepartureTime()) + " - " + Time.formatDateNoDay(destinationStop.getArrivalTime()));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
