package philippmatthes.com.manni.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import philippmatthes.com.manni.Activities.DeparturesActivity;
import philippmatthes.com.manni.Activities.RouteChangeActivity;
import philippmatthes.com.manni.Colors;
import philippmatthes.com.manni.R;
import philippmatthes.com.manni.RouteChangeManager;
import philippmatthes.com.manni.jVVO.Models.Departure;
import philippmatthes.com.manni.jVVO.Models.Platform;
import philippmatthes.com.manni.jVVO.Tools.Time;

public class DepartureAdapter extends ArrayAdapter<Departure> {

    private final ArrayList<Departure> departures;
    private final DeparturesActivity activity;

    public DepartureAdapter(DeparturesActivity activity, ArrayList<Departure> departures) {

        super(activity, R.layout.departure_row, departures);
        this.activity = activity;
        this.departures = departures;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView;
        rowView = inflater.inflate(R.layout.departure_row, parent, false);

        TextView lineLabel = (TextView) rowView.findViewById(R.id.lineLabel);
        TextView arrivalTimeLabel = (TextView) rowView.findViewById(R.id.arrivalTimeLabel);
        TextView clockLabel = (TextView) rowView.findViewById(R.id.clockLabel);
        TextView moreInformationLabel = (TextView) rowView.findViewById(R.id.moreInformationLabel);
        Button button = (Button) rowView.findViewById(R.id.lineNumberButton);

        Departure departure = departures.get(position);
        lineLabel.setText(departure.getDirection());
        arrivalTimeLabel.setText("Arrival in " + departure.getETA() + " min");
        if (departure.getRealTime() != null) {
            clockLabel.setText("Arrival: " + Time.formatDate(departure.getRealTime()));
        } else {
            clockLabel.setText("Scheduled Arrival: " + Time.formatDate(departure.getScheduledTime()));
        }

        String moreInformation = departure.getPlatform() != null ? "From Platform: " + departure.getPlatform().getName() : "";
        if (departure.getRouteChanges() != null && !departure.getRouteChanges().isEmpty()) {
            moreInformation += " Route changes: " + RouteChangeManager.getShared().changes(departure.getRouteChanges());
        }

        moreInformationLabel.setText(moreInformation);
        button.setText(departure.getLine());

        int color;
        try {
            color = Colors.getColor(Integer.valueOf(departure.getLine()));
        } catch (NumberFormatException e) {
            color = Colors.getColor(departure.getDirection().length());
        }

        button.setBackgroundColor(Colors.lighten(color, 0.2));

        rowView.setBackgroundColor(color);

        return rowView;
    }

}
