package philippmatthes.com.manni.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import philippmatthes.com.manni.Colors;
import philippmatthes.com.manni.R;
import philippmatthes.com.manni.jVVO.Models.Route;

public class RouteRecycleViewAdapter extends RecyclerView.Adapter<RouteRecycleViewAdapter.ViewHolder> {

    private List<Route.RoutePartial> routePartials;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    // data is passed into the constructor
    RouteRecycleViewAdapter(Context context, List<Route.RoutePartial> routePartials) {
        this.inflater = LayoutInflater.from(context);
        this.routePartials = routePartials;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.route_cell_header_line_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Route.RoutePartial routePartial = routePartials.get(position);

        String text;
        int color;
        if (routePartial.getMode().getName() == null || routePartial.getDuration() == null) {
            text = "Info";
            color = Color.GRAY;
        } else {
            text = routePartial.getMode().getName() + " (" + routePartial.getDuration() + " min)";
            color = Colors.color(routePartial);
        }

        holder.lineButton.setText(text);

        holder.lineButton.setBackgroundColor(color);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return routePartials.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button lineButton;

        ViewHolder(View itemView) {
            super(itemView);
            lineButton = (Button) itemView.findViewById(R.id.route_cell_header_line_item_button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Route.RoutePartial getItem(int id) {
        return routePartials.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}