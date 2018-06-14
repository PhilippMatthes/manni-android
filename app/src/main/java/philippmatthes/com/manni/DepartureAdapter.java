package philippmatthes.com.manni;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import philippmatthes.com.manni.jVVO.Models.Departure;
import philippmatthes.com.manni.jVVO.Models.Platform;
import philippmatthes.com.manni.jVVO.Tools.Time;

public class DepartureAdapter extends ArrayAdapter<Departure> {

    private final Context context;
    private final ArrayList<Departure> departures;

    public DepartureAdapter(Context context, ArrayList<Departure> departures) {

        super(context, R.layout.departure_row, departures);

        this.context = context;
        this.departures = departures;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView;
        if (parent != null) {
            rowView = inflater.inflate(R.layout.departure_row, parent, false);
        } else {
            return null;
        }

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

        /*
        List<String> routeChanges = departure.getRouteChanges();

        if (routeChanges != null) {
            for (String change : routeChanges) {
                if (moreInformation == "") {

                    moreInformation += change;
                } else {
                    moreInformation += ", " + change;
                }
            }
        }
        */

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
