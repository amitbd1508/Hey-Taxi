package com.tesseract.taxisharing.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tesseract.taxisharing.R;
import com.tesseract.taxisharing.model.TripHistory;

import java.util.List;

/**
 * Created by hhson on 8/12/2016.
 */
public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.ViewHolder> {
    private List<TripHistory> tripHistories;
    private Context context;
    private int lastPosition = -1;

    public TripHistoryAdapter(List<TripHistory> tripHistories, Context context) {
        this.tripHistories = tripHistories;
        this.context = context;
    }

    @Override
    public TripHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip_history, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        setAnimation(holder.container, position);

        TripHistory item = tripHistories.get(position);

        holder.locationFrom.setText(item.getLocationFrom());
        holder.locationTo.setText(item.getLocationTo());
        holder.tripTime.setText(item.getTripTime());
        if (item.getShared().equals("no")) {
            holder.shared.setText("Personal Ride");
        } else {
            holder.shared.setText("Shared Ride");
            holder.person.setText("With " + item.getPerson() + "Persons");
        }

        holder.driverName.setText(item.getDriverName());
        holder.amount.setText("Amount: " + item.getAmount());

    }

    @Override
    public int getItemCount() {
        return tripHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView locationFrom;
        TextView locationTo;
        TextView tripTime;
        TextView shared;
        TextView driverName;
        TextView amount;
        TextView person;

        CardView container;

        public ViewHolder(View itemView) {
            super(itemView);

            locationFrom = (TextView) itemView.findViewById(R.id.tv_from_trip_history);
            locationTo = (TextView) itemView.findViewById(R.id.tv_to_trip_history);
            tripTime = (TextView) itemView.findViewById(R.id.tv_time_trip_history);
            shared = (TextView) itemView.findViewById(R.id.tv_isShared_trip_history);
            driverName = (TextView) itemView.findViewById(R.id.tv_driver_trip_history);
            amount = (TextView) itemView.findViewById(R.id.tv_amount_trip_history);
            person = (TextView) itemView.findViewById(R.id.tv_person_trip_history);
            container = (CardView) itemView.findViewById(R.id.container);

        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
